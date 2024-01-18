package com.slackunderflow.slackunderflow.services.implementation;

import com.slackunderflow.slackunderflow.dtos.requests.AnswerRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.AnswerResponseDto;
import com.slackunderflow.slackunderflow.enums.BadgeEnum;
import com.slackunderflow.slackunderflow.errors.ModelNotFoundError;
import com.slackunderflow.slackunderflow.errors.UserNotFoundError;
import com.slackunderflow.slackunderflow.mappers.AnswerMapper;
import com.slackunderflow.slackunderflow.mappers.UserMapper;
import com.slackunderflow.slackunderflow.models.Answer;
import com.slackunderflow.slackunderflow.models.Question;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.repositories.AnswerRepository;
import com.slackunderflow.slackunderflow.repositories.QuestionRepository;
import com.slackunderflow.slackunderflow.repositories.UserEntityRepository;
import com.slackunderflow.slackunderflow.services.AnswerService;
import com.slackunderflow.slackunderflow.services.SuggestionService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnswerServiceImpl
        extends BodyEntityServiceImpl<Answer, AnswerResponseDto, AnswerRequestDto, AnswerRepository,
        AnswerMapper>
        implements AnswerService {


    private final QuestionRepository questionRepository;
    private final SuggestionService suggestionService;
    private final UserMapper userMapper;

    private static final int MAX_POINTS = 75;
    private static final int MAX_RANK = 3;
    private static final int MIN_RANK = 1;

    public AnswerServiceImpl(AnswerRepository modelRepository, AnswerMapper modelMapper, UserEntityRepository userEntityRepository, QuestionRepository questionRepository, SuggestionService suggestionService, UserMapper userMapper) {
        super(modelRepository, modelMapper, userEntityRepository);
        this.questionRepository = questionRepository;
        this.suggestionService = suggestionService;
        this.userMapper = userMapper;
    }


    @Override
    public List<AnswerResponseDto> resetRanksByQuestion(Long questionId) {
        UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var question = questionRepository
                .findById(questionId)
                .orElseThrow(() ->
                        new ModelNotFoundError("Question not found with id: ", questionId.toString()));

        if (!authUser.getUsername().equals(question.getUser().getUsername())) {
            throw new ModelNotFoundError("Question not found with id: ", questionId.toString());
        }

        var answers = modelRepository.findByQuestion(question);

        answers.forEach(answer -> {
            modifyAnswerRank(answer, 0);
        });

        return getAll();
    }

    @Override
    public AnswerResponseDto create(AnswerRequestDto req, String username) {
        UserEntity user = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundError("User not found with username: ", username));

        Answer answer = modelMapper.fromRequestToEntity(req, user);
        answer.setCreateTimestamp(LocalDateTime.now());
        answer.setUpdateTimestamp(LocalDateTime.now());
        answer.setRank(0);
        Answer savedModel = modelRepository.save(answer);
        return modelMapper.fromEntityToResponse(savedModel);
    }

    @Override
    public List<AnswerResponseDto> updateRank(Long id, Integer rank, String username) {
        var answer = modelRepository
                .findById(id)
                .orElseThrow(() ->
                        new ModelNotFoundError("Answer not found with id: ", id.toString()));

        var question = answer.getQuestion();

        var user = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundError("User not found with username: ", username));

        if (!question.getUser().getUsername().equals(user.getUsername())) {
            throw new ModelNotFoundError("Answer not found with id: ", id.toString());
        }

        if (rank >= MIN_RANK && rank <= MAX_RANK && !rank.equals(answer.getRank())) {
            var otherAnswer = modelRepository.findFirstByRankAndQuestion(rank, question);

            if (otherAnswer != null) {
                modifyAnswerRank(otherAnswer, 0);
                modelRepository.save(otherAnswer);
            }

            modifyAnswerRank(answer, rank);
        }

        //return getAll();
        return getAllByQuestion(question.getId());
    }


    @Override
    public List<AnswerResponseDto> getAllByQuestion(Long id) {
        var question = questionRepository
                .findById(id)
                .orElseThrow(() ->
                        new ModelNotFoundError("Question not found with id: ", id.toString()));

        return modelRepository.findByQuestion(question)
                .stream()
                .map(modelMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public boolean delete(Long id, String username) {
        var answer = modelRepository.findById(id)
                .orElseThrow(() -> new ModelNotFoundError("Answer not found with id: ", id.toString()));
        var user = answer.getUser();

        hasAuthority(user, username, id);

        if (!suggestionService.deleteByAnswer(answer)) {
            return false;
        }

        var pointChange = -getPointsFromRank(answer.getRank());
        updatePoints(user.getUsername(), pointChange);

        return modelRepository.customDeleteById(id) == 1;
    }

    @Override
    public boolean deleteByQuestion(Question question) {
        var answers = modelRepository.findByQuestion(question);

        AtomicReference<Integer> numberDeleted = new AtomicReference<>(0);
        answers.forEach(answer -> {
            var pointChange = -getPointsFromRank(answer.getRank());
            var user = answer.getUser();

            updatePoints(user.getUsername(), pointChange);

            if (suggestionService.deleteByAnswer(answer)) {
                numberDeleted.updateAndGet(v -> v + modelRepository.customDeleteById(answer.getId()));
            }
        });

        return numberDeleted.get() == answers.size();
    }

    private Integer getPointsFromRank(Integer rank) {
        return switch (rank) {
            case 1 -> 15;
            case 2 -> 10;
            case 3 -> 5;
            default -> 0;
        };
    }

    private void modifyAnswerRank(Answer answer, Integer newRank) {
        if ((newRank != 0 && newRank < MIN_RANK) || newRank > MAX_RANK) {
            throw new IllegalArgumentException("The new rank is outside the limits");
        }

        var pointChange = getPointsFromRank(newRank) - getPointsFromRank(answer.getRank());
        var user = answer.getUser();
        updatePoints(user.getUsername(), pointChange);

        answer.setRank(newRank);
        modelRepository.save(answer);
    }

    private void updatePoints(String username, Integer points) {
        var user = userEntityRepository
                .findByUsername(username).orElseThrow(() -> new UserNotFoundError("User not found with username: ", username));

        var newPoints = user.getPoints() + points;
        user.setPoints(newPoints);

        var badge = getBadgeFromPoints(newPoints);
        user.setBadge(badge);

        userEntityRepository.save(user);

        //userMapper.fromEntityToResponseDto(user, null);
    }

    private BadgeEnum getBadgeFromPoints(Integer points) {
        return switch (Math.min(points, MAX_POINTS) / 25) {
            case 1, 2 -> BadgeEnum.INTERMEDIATE;
            case 3 -> BadgeEnum.MASTER;
            default -> BadgeEnum.BEGINNER;
        };
    }
}

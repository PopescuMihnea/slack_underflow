package com.slackunderflow.slackunderflow.services.implementation;

import com.slackunderflow.slackunderflow.dtos.requests.QuestionRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.QuestionResponseDto;
import com.slackunderflow.slackunderflow.enums.TopicEnum;
import com.slackunderflow.slackunderflow.errors.ModelNotFoundError;
import com.slackunderflow.slackunderflow.errors.TopicNotFoundError;
import com.slackunderflow.slackunderflow.errors.UserNotFoundError;
import com.slackunderflow.slackunderflow.mappers.QuestionMapper;
import com.slackunderflow.slackunderflow.models.Question;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.repositories.QuestionRepository;
import com.slackunderflow.slackunderflow.repositories.TopicRepository;
import com.slackunderflow.slackunderflow.repositories.UserEntityRepository;
import com.slackunderflow.slackunderflow.services.AnswerService;
import com.slackunderflow.slackunderflow.services.QuestionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionServiceImpl
        extends BodyEntityServiceImpl<Question, QuestionResponseDto, QuestionRequestDto,
        QuestionRepository, QuestionMapper>
        implements QuestionService {

    private final TopicRepository topicRepository;
    private final AnswerService answerService;

    public QuestionServiceImpl(QuestionRepository modelRepository, QuestionMapper modelMapper, UserEntityRepository userEntityRepository, TopicRepository topicRepository, AnswerService answerService) {
        super(modelRepository, modelMapper, userEntityRepository);
        this.topicRepository = topicRepository;
        this.answerService = answerService;
    }


    @Override
    public List<QuestionResponseDto> getAllByTopics(List<TopicEnum> topics) {
        var topicEntities = topics.stream().map(
                topic -> topicRepository.findByTopic(topic).orElseThrow(() -> new TopicNotFoundError("Topic not found with type: ", topic))
        ).collect(Collectors.toSet());

        return modelRepository.findByTopicsIn(new HashSet<>(topicEntities))
                .stream()
                .map(modelMapper::fromEntityToResponse)
                .toList();
    }

    @Override
    public List<QuestionResponseDto> getAllByTitle(String title) {
        return modelRepository.findByTitleContaining(title)
                .stream()
                .map(modelMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public QuestionResponseDto modify(Long id, QuestionRequestDto questionRequestDto, String username) {

        var question = modelRepository.findById(id).orElseThrow(() -> new ModelNotFoundError("Question not found with id: ", id.toString()));
        var user = question.getUser();

        /*if (!user.getUsername().equals(username)) {
            throw new ModelNotFoundError("Question not found with id: ", id.toString());
        }*/

        hasAuthority(user, username, id);

        question.setBody(questionRequestDto.getBody());
        question.setTitle(questionRequestDto.getTitle());
        question.setUpdateTimestamp(LocalDateTime.now());
        question.setTopics(
                questionRequestDto.
                        getTopics()
                        .stream()
                        .map(topic ->
                                topicRepository
                                        .findByTopic(topic)
                                        .orElseThrow(() ->
                                                new TopicNotFoundError("Topic not found", topic)))
                        .collect(Collectors.toSet()));

        modelRepository.save(question);

        return modelMapper.fromEntityToResponse(question);
    }

    @Override
    public boolean delete(Long id, String username) {
        var question = modelRepository.findById(id).orElseThrow(() -> new ModelNotFoundError("Question not found with id: ", id.toString()));
        var user = question.getUser();

        hasAuthority(user, username, id);

        if (!answerService.deleteByQuestion(question)) {
            return false;
        }

        return modelRepository.customDeleteById(id) == 1;
    }

    @Override
    public boolean deleteByUser(UserEntity user) {
        var questions = modelRepository.findByUser(user);

        AtomicReference<Integer> numberDeleted = new AtomicReference<>(0);
        questions.forEach(question -> {
            if (answerService.deleteByQuestion(question)) {
                numberDeleted.updateAndGet(v -> v + modelRepository.customDeleteById(question.getId()));
            }
        });

        return numberDeleted.get() == questions.size();
    }
}

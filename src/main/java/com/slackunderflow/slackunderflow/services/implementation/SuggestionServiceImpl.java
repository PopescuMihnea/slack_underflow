package com.slackunderflow.slackunderflow.services.implementation;

import com.slackunderflow.slackunderflow.dtos.requests.SuggestionRequestDto;
import com.slackunderflow.slackunderflow.dtos.responses.SuggestionResponseDto;
import com.slackunderflow.slackunderflow.errors.ModelNotFoundError;
import com.slackunderflow.slackunderflow.errors.UserNotFoundError;
import com.slackunderflow.slackunderflow.mappers.SuggestionMapper;
import com.slackunderflow.slackunderflow.models.Answer;
import com.slackunderflow.slackunderflow.models.Suggestion;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.repositories.AnswerRepository;
import com.slackunderflow.slackunderflow.repositories.SuggestionRepository;
import com.slackunderflow.slackunderflow.repositories.UserEntityRepository;
import com.slackunderflow.slackunderflow.services.SuggestionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Transactional
public class SuggestionServiceImpl
        extends BodyEntityServiceImpl<Suggestion, SuggestionResponseDto, SuggestionRequestDto,
        SuggestionRepository, SuggestionMapper>
        implements SuggestionService {


    private final AnswerRepository answerRepository;

    public SuggestionServiceImpl(SuggestionRepository modelRepository, SuggestionMapper modelMapper, UserEntityRepository userEntityRepository, AnswerRepository answerRepository) {
        super(modelRepository, modelMapper, userEntityRepository);
        this.answerRepository = answerRepository;
    }


    @Override
    public List<SuggestionResponseDto> getAllByAnswer(Long id) {
        var answer = answerRepository
                .findById(id)
                .orElseThrow(() ->
                        new ModelNotFoundError("Answer not found with id: ", id.toString()));

        return modelRepository.findByAnswer(answer)
                .stream()
                .map(modelMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public boolean delete(Long id, String username) {
        var suggestion = modelRepository.findById(id)
                .orElseThrow(() -> new ModelNotFoundError("Suggestion not found with id: ", id.toString()));
        var user = suggestion.getUser();

        hasAuthority(user, username, id);

        return modelRepository.customDeleteById(id) == 1;
    }

    @Override
    public boolean deleteByAnswer(Answer answer) {
        var suggestions = modelRepository.findByAnswer(answer);
        AtomicReference<Integer> numberDeleted = new AtomicReference<>(0);
        suggestions.forEach(suggestion -> numberDeleted.updateAndGet(v -> v + modelRepository.customDeleteById(suggestion.getId())));

        return numberDeleted.get() == suggestions.size();
    }

    protected void delete(Long id) {
        modelRepository.customDeleteById(id);
    }
}

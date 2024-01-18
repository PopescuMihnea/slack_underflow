package com.slackunderflow.slackunderflow.repositories;

import com.slackunderflow.slackunderflow.models.Answer;
import com.slackunderflow.slackunderflow.models.Question;
import com.slackunderflow.slackunderflow.models.Suggestion;
import com.slackunderflow.slackunderflow.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SuggestionRepository extends BodyEntityRepository<Suggestion> {
    @Modifying
    @Query(value = "DELETE FROM Suggestion s where s.id = ?1")
    int customDeleteById(Long id);


    List<Suggestion> findByAnswer(Answer answer);

}

package com.slackunderflow.slackunderflow.repositories;

import com.slackunderflow.slackunderflow.models.BodyEntity;
import com.slackunderflow.slackunderflow.models.Question;
import com.slackunderflow.slackunderflow.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BodyEntityRepository<MODEL extends BodyEntity> extends JpaRepository<MODEL, Long> {
    List<MODEL> findByUser(UserEntity user);
}

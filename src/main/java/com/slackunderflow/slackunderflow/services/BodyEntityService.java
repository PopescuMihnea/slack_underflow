package com.slackunderflow.slackunderflow.services;

import com.slackunderflow.slackunderflow.dtos.requests.AnswerRequestDto;
import com.slackunderflow.slackunderflow.dtos.requests.BodyRequest;
import com.slackunderflow.slackunderflow.dtos.responses.AnswerResponseDto;
import com.slackunderflow.slackunderflow.dtos.responses.BodyResponse;
import com.slackunderflow.slackunderflow.mappers.BodyEntityMapper;
import com.slackunderflow.slackunderflow.models.BodyEntity;
import com.slackunderflow.slackunderflow.repositories.BodyEntityRepository;

import java.util.List;

public interface BodyEntityService<MODEL extends BodyEntity, RESP extends BodyResponse, REQ extends BodyRequest,
        R extends BodyEntityRepository<MODEL>,
        MAPPER extends BodyEntityMapper<MODEL, RESP, REQ>> {

    List<RESP> getAll();

    List<RESP> getAllByUser(String username);

    List<RESP> getAllByUser(Long id);

    RESP get(Long id);

    RESP create(REQ req, String username);

    RESP modify(Long id, REQ req, String username);

    boolean delete(Long id, String username);

}

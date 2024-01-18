package com.slackunderflow.slackunderflow.mappers;

import com.slackunderflow.slackunderflow.dtos.requests.BodyRequest;
import com.slackunderflow.slackunderflow.dtos.responses.BodyResponse;
import com.slackunderflow.slackunderflow.models.BodyEntity;
import com.slackunderflow.slackunderflow.models.UserEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;


@RequiredArgsConstructor
public abstract class BodyEntityMapper<MODEL extends BodyEntity, RESP extends BodyResponse, REQ extends BodyRequest> {

    protected final UserMapper userMapper;

    public abstract RESP fromEntityToResponse(MODEL model);

    public abstract MODEL fromRequestToEntity(REQ req, UserEntity user);


}

package com.slackunderflow.slackunderflow.services.implementation;

import com.slackunderflow.slackunderflow.dtos.requests.BodyRequest;
import com.slackunderflow.slackunderflow.dtos.responses.BodyResponse;
import com.slackunderflow.slackunderflow.errors.ModelNotFoundError;
import com.slackunderflow.slackunderflow.errors.UserNotFoundError;
import com.slackunderflow.slackunderflow.mappers.BodyEntityMapper;
import com.slackunderflow.slackunderflow.models.BodyEntity;
import com.slackunderflow.slackunderflow.models.Question;
import com.slackunderflow.slackunderflow.models.UserEntity;
import com.slackunderflow.slackunderflow.repositories.BodyEntityRepository;
import com.slackunderflow.slackunderflow.repositories.UserEntityRepository;
import com.slackunderflow.slackunderflow.services.BodyEntityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
public abstract class BodyEntityServiceImpl<MODEL extends BodyEntity, RESP extends BodyResponse, REQ extends BodyRequest,
        R extends BodyEntityRepository<MODEL>,
        MAPPER extends BodyEntityMapper<MODEL, RESP, REQ>> implements BodyEntityService<MODEL, RESP, REQ, R, MAPPER> {

    protected final R modelRepository;
    protected final MAPPER modelMapper;
    protected final UserEntityRepository userEntityRepository;

    @Override
    public List<RESP> getAll() {
        return modelRepository.findAll()
                .stream().map(modelMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RESP> getAllByUser(String username) {
        UserEntity user = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundError("User not found with username: ", username));

        return respByUser(user);

    }

    @Override
    public List<RESP> getAllByUser(Long id) {
        UserEntity user = userEntityRepository
                .findById(id)
                .orElseThrow(() ->
                        new UserNotFoundError("User not found with id: ", id.toString()));
        return respByUser(user);
    }

    @Override
    public RESP get(Long id) {
        MODEL model = modelRepository.findById(id)
                .orElseThrow(() ->
                        new ModelNotFoundError("Entity not found with id: ", id.toString()));

        return modelMapper.fromEntityToResponse(model);
    }

    @Override
    public RESP create(REQ req, String username) {
        UserEntity user = userEntityRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundError("User not found with username: ", username));

        MODEL model = modelMapper.fromRequestToEntity(req, user);
        model.setCreateTimestamp(LocalDateTime.now());
        model.setUpdateTimestamp(LocalDateTime.now());
        MODEL savedModel = modelRepository.save(model);
        return modelMapper.fromEntityToResponse(savedModel);
    }

    @Override
    public RESP modify(Long id, REQ req, String username) {

        MODEL model = modelRepository.findById(id)
                .orElseThrow(() ->
                        new ModelNotFoundError("Entity not found with id: ", id.toString()));
        UserEntity user = model.getUser();

        /*if (!user.getUsername().equals(username)) {
            throw new ModelNotFoundError("Entity not found with id: ", id.toString());
        }*/

        hasAuthority(user, username, id);

        model.setBody(req.getBody());
        model.setUpdateTimestamp(LocalDateTime.now());

        MODEL savedModel = modelRepository.save(model);

        return modelMapper.fromEntityToResponse(savedModel);
    }

    @Override
    public abstract boolean delete(Long id, String username);

    private List<RESP> respByUser(UserEntity user) {
        return modelRepository.findByUser(user)
                .stream().map(modelMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    public boolean isAdmin(UserEntity user) {
        return user.getAuthorities().stream().anyMatch(e -> e.getAuthority().equals("ADMIN"));
    }

    public void hasAuthority(UserEntity user, String username, Long id) {
        if (!user.getUsername().equals(username) || !isAdmin(user)) {
            throw new ModelNotFoundError("Entity not found with id: ", id.toString());
        }
    }


}

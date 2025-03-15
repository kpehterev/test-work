package com.test.work.mapper;

import com.test.work.entity.UserEntity;
import com.test.work.model.User;
import com.test.work.model.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserEntity toEntity(User user){
        return modelMapper.map(user, UserEntity.class);
    }

    public User toDto(UserEntity entity){
        return modelMapper.map(entity, User.class);
    }

    public UserEntity toEntity(UserCreateRequest createRequest) {
        return modelMapper.map(createRequest, UserEntity.class);
    }
}
package com.example.demo.mapper;

import com.example.demo.dto.User;
import com.example.demo.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );
    User userEntityToUser(UserEntity userEntity);
    UserEntity userToUserEntity(User user);



}

package com.levelup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.levelup.model.User;
import com.levelup.repository.UserRepo;
import com.levelup.dto.UserDto;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public List<User> getUsers(){
        return userRepo.findAll();
    }

    public UserDto createUser(UserDto dto) {
        User entity = UserDto.toEntity(dto);
        User saved = userRepo.save(entity);
        return UserDto.fromEntity(saved);
    }

}

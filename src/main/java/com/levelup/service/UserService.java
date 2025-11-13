package com.levelup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.levelup.dto.UserDto;
import com.levelup.model.User;
import com.levelup.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;



    public List<User> getUsers(){
        return userRepo.getAllUsers();
    }

    

}

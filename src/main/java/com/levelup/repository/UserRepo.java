package com.levelup.repository;

import com.levelup.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepo {

    private List<User> listUser = new ArrayList<>();

    public UserRepo(){
        listUser.add(new User(1L, "1111111-1","Juan", "Perez", "juan.perez@mail.com", "password123", "USER"));
        listUser.add(new User(2L, "1111111-1","Maria", "Lopez", "maria.lopez@mail.com", "password123", "USER"));
        listUser.add(new User(3L, "1111111-1","Carlos", "Ruiz", "carlos.ruiz@mail.com", "password123", "USER"));
        listUser.add(new User(4L, "1111111-1","Ana", "Torres", "ana.torres@mail.com", "password123", "USER"));
        listUser.add(new User(5L, "1111111-1","Pedro", "Gonzalez", "pedro.gonzalez@mail.com", "password123", "USER"));
    }

    public List<User> getAllUsers(){
        return listUser;
    }

}

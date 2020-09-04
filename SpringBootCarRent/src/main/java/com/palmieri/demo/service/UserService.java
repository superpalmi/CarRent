package com.palmieri.demo.service;

import com.palmieri.demo.entities.User;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    List<User> readByRole(String role);

    List<User> readByCity(String city);


    List<User> readAll();

    User readById(int id);
    User readByPhone(String phone);
    User readByUsername(String username);
    User readByEmail(String email);
    public boolean isUsernameAlreadyInUse(String username);
    public boolean isEmailAlreadyInUse(String email);
    public boolean exists(User User);



    void saveOrUpdate(User user);
    void create(User user);
    void update(User user);
    void delete(User user);


}

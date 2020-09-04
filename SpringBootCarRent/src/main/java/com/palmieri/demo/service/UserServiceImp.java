package com.palmieri.demo.service;

import com.palmieri.demo.dao.UserDao;
import com.palmieri.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;


@Service("userService")
@Transactional
public class UserServiceImp implements UserService {

    @Qualifier("userDao")
    @Autowired
    private UserDao userRepository;

    @Override
    @Transactional
    public boolean isUsernameAlreadyInUse(String username){

        boolean userInDb = true;
        if (userRepository.findByUserName(username) == null) userInDb = false;
        return userInDb;
    }

    @Override
    public boolean isEmailAlreadyInUse(String email) {
        boolean userInDb = true;
        if (userRepository.findByEmail(email) == null) userInDb = false;
        return userInDb;
    }


    @Override
    public List<User> readByRole(String role) {
        List<User> users = userRepository.findByRole(role);
        return users;
    }

    @Override
    public List<User> readByCity(String city) {
        List<User> users = userRepository.findByCity(city);
        return users;
    }

    @Override
    public List<User> readAll() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public User readById(int id) {
        User user = userRepository.findById(id);
        return user;
    }

    @Override
    public User readByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        return user;
    }

    @Override
    public User readByUsername(String username) {
        User user = userRepository.findByUserName(username);
        return user;
    }

    @Override
    public User readByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;

    }

    public boolean exists(User user){
        boolean exists=false;
        User u=readByUsername(user.getUserName());
        User e=readByEmail(user.getEmail());
        if(u!=null&&e!=null){
            return exists=true;
        }
        return exists;

    }
    @Override
    public void saveOrUpdate(User user){
        if (exists(user)){
            update(user);
        }else create(user);
    }

    @Override
    public void create(User user) {
        userRepository.save(user);


    }

    @Override
    public void update(User user) {
        userRepository.save(user);

    }

    @Override

    public void delete(User user) {
        userRepository.delete(user);

    }
}

package com.palmieri.demo.dao;

import com.palmieri.demo.entities.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository("userDao")
public interface UserDao extends PagingAndSortingRepository<User, String> {
    List<User> findAll();
    List<User> findByRole(String role);
    List<User> findByCity(String city);
    User findByUserName(String userName);
    User findByEmail(String email);
    User findByPhone(String phone);
    User findById(int id);






}

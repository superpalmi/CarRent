package com.palmieri.demo.validation;

import com.palmieri.demo.dao.UserDao;
import com.palmieri.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private UserDao userDao;


    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        boolean retVal=true;
        User user = userDao.findByEmail(email);
        if(user !=null){
            retVal=false;
        }
        return retVal;
    }
}

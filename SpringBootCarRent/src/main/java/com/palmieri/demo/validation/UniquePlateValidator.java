package com.palmieri.demo.validation;

import com.palmieri.demo.entities.Vehicle;
import com.palmieri.demo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniquePlateValidator implements ConstraintValidator<UniquePlate, String> {
    @Autowired
    private VehicleService vehicleService;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean retVal=true;
        Vehicle vehicle=vehicleService.readByPlate(s);
        if(vehicle !=null){
            retVal=false;
        }
        return retVal;
    }
}

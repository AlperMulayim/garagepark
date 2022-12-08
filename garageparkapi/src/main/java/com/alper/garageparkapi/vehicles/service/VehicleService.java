package com.alper.garageparkapi.vehicles.service;

import com.alper.garageparkapi.enums.VehicleType;
import com.alper.garageparkapi.vehicles.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VehicleService {
    @Autowired
    private Map<VehicleType, Integer>  vehicleSlots;

}

package com.alper.garageparkapi.config;

import com.alper.garageparkapi.enums.VehicleType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class APIConfiguration {

    @Bean
    public Map<VehicleType,Integer> vehicleSlots(){
        Map<VehicleType, Integer> vehicleSlots = new HashMap<>();
        vehicleSlots.put(VehicleType.CAR,1);
        vehicleSlots.put(VehicleType.JEEP,2);
        vehicleSlots.put(VehicleType.TRUCK,4);
        return vehicleSlots;
    }

    @Bean
    public int parkingCapacity(){
        return 10;
    }
}

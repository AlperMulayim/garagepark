package com.alper.garageparkapi.config;

import com.alper.garageparkapi.enums.VehicleType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class APIConfiguration {

    @Bean
    public Map<String,Integer> vehicleSlots(){
        Map<String, Integer> vehicleSlots = new HashMap<>();
        vehicleSlots.put(VehicleType.CAR.toString(),1);
        vehicleSlots.put(VehicleType.JEEP.toString(),2);
        vehicleSlots.put(VehicleType.TRUCK.toString(),4);
        return vehicleSlots;
    }

    @Bean
    public Integer parkingCapacity(){
        return 10;
    }
}

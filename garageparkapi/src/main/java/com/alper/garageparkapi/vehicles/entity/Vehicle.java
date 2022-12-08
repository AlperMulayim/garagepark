package com.alper.garageparkapi.vehicles.entity;

import com.alper.garageparkapi.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vehicle {
    private String plate;
    private String color;
    private VehicleType type;
}

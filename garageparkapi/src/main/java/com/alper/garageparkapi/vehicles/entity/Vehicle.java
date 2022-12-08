package com.alper.garageparkapi.vehicles.entity;

import com.alper.garageparkapi.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Vehicle {
    private String licensePlate;
    private String color;
    private VehicleType type;
}

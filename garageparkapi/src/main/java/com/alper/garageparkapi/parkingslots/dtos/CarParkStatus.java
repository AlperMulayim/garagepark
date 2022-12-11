package com.alper.garageparkapi.parkingslots.dtos;

import com.alper.garageparkapi.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarParkStatus {
    private List<ParkArea> parks;
    private Integer totalCarsInPark;
    private Integer totalAllocatedSlots;
    private Integer totalEmptySlots;
    private Integer capacity;
}

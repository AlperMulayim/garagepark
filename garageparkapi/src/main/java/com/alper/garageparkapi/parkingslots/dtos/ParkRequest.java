package com.alper.garageparkapi.parkingslots.dtos;

import com.alper.garageparkapi.vehicles.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkRequest {
    private Vehicle vehicle;
}

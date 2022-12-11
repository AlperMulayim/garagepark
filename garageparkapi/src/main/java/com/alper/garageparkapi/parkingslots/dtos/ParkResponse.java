package com.alper.garageparkapi.parkingslots.dtos;

import com.alper.garageparkapi.vehicles.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ParkResponse {
    private Vehicle vehicle;
}

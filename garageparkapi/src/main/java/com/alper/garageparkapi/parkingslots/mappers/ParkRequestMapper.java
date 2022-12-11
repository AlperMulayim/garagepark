package com.alper.garageparkapi.parkingslots.mappers;

import com.alper.garageparkapi.parkingslots.dtos.ParkRequest;
import com.alper.garageparkapi.parkingslots.dtos.ParkResponse;
import org.springframework.stereotype.Component;

@Component
public class ParkRequestMapper  implements  IEntityMapper<ParkRequest, ParkResponse> {
    @Override
    public ParkResponse toDTO(ParkRequest parkRequest) {
        return  ParkResponse.builder()
                .vehicle(parkRequest.getVehicle())
                .build();
    }

    @Override
    public ParkRequest toEntity(ParkResponse parkResponse) {
        return ParkRequest.builder()
                .vehicle(parkResponse.getVehicle())
                .build();
    }
}

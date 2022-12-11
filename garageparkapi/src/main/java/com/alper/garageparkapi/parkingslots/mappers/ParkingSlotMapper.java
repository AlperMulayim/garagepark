package com.alper.garageparkapi.parkingslots.mappers;

import com.alper.garageparkapi.parkingslots.dtos.ParkingSlotDto;
import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import org.springframework.stereotype.Component;

@Component
public class ParkingSlotMapper implements  IEntityMapper<ParkingSlot, ParkingSlotDto> {
    @Override
    public ParkingSlotDto toDTO(ParkingSlot parkingSlot) {
        return  ParkingSlotDto.builder()
                .slotNum(parkingSlot.getSlotNum())
                .status(parkingSlot.getStatus())
                .vehicle(parkingSlot.getVehicle())
                .build();
    }

    @Override
    public ParkingSlot toEntity(ParkingSlotDto parkingSlotDto) {
        return ParkingSlot.builder()
                .vehicle(parkingSlotDto.getVehicle())
                .status(parkingSlotDto.getStatus())
                .slotNum(parkingSlotDto.getSlotNum())
                .build();
    }
}

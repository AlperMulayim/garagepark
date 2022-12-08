package com.alper.garageparkapi.parkingslots.entity;

import com.alper.garageparkapi.parkingslots.enums.SlotStatus;
import com.alper.garageparkapi.vehicles.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingSlot {
    private Integer slotNum;
    private Vehicle vehicle;
    private SlotStatus status;
}

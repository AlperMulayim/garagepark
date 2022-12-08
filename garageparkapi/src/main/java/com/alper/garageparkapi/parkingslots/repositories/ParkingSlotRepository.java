package com.alper.garageparkapi.parkingslots.repositories;

import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import com.alper.garageparkapi.parkingslots.enums.SlotStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class ParkingSlotRepository {

    private List<ParkingSlot> parkingSlots;

    private  Integer parkingCapacity = 10;

    @Autowired
    public ParkingSlotRepository(){
        parkingSlots = new ArrayList<>();

        for (int i = 0; i < parkingCapacity; ++i){
            ParkingSlot parkingSlot =  ParkingSlot.builder()
                    .slotNum(i)
                    .status(SlotStatus.AVAILABLE)
                    .vehicle(null)
                    .build();
            parkingSlots.add(parkingSlot);
        }
    }

    public  List<ParkingSlot> getParkingSlots(){
        return parkingSlots;
    }

}

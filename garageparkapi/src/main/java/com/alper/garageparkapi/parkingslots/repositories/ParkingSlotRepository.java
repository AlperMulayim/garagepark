package com.alper.garageparkapi.parkingslots.repositories;

import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import com.alper.garageparkapi.parkingslots.enums.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSlotRepository {
    private List<ParkingSlot> parkingSlots;
    private  Integer capacity;
    public void initParkingRepository(Integer capacity){
        parkingSlots = new ArrayList<>();
        this.capacity = capacity;

        for (int i = 0; i < capacity; ++i){
            ParkingSlot parkingSlot =  ParkingSlot.builder()
                    .slotNum(i)
                    .status(SlotStatus.AVAILABLE)
                    .vehicle(null)
                    .build();
            parkingSlots.add(parkingSlot);
        }
    }

    public  Integer getParkingCapacity(){
        return capacity;
    }
    public  List<ParkingSlot> getParkingSlots(){
        return parkingSlots;
    }

    public Optional<ParkingSlot> findParkingSlot(int slotNum){
        Optional<ParkingSlot> slot = parkingSlots.stream()
                .filter(parkingSlot -> parkingSlot.getSlotNum() == slotNum)
                .findFirst();

        return slot;
    }

    public  ParkingSlot updateParkingSlot(ParkingSlot parkingSlot){
        parkingSlots.set(parkingSlot.getSlotNum(), parkingSlot);
        return  parkingSlots.get(parkingSlot.getSlotNum());
    }

    public List<ParkingSlot> findAvailableSlots(){
        return parkingSlots.stream()
                .filter(parkingSlot -> parkingSlot.getStatus().equals(SlotStatus.AVAILABLE))
                .collect(Collectors.toList());
    }

    public List<ParkingSlot> findAllocatedSlots(){
        return parkingSlots.stream()
                .filter(parkingSlot -> parkingSlot.getStatus().equals(SlotStatus.ALLOCATED))
                .collect(Collectors.toList());
    }

    public List<ParkingSlot> findVehicleSlots(String plate){
       return parkingSlots.stream()
                .filter(parkingSlot ->
                     parkingSlot.getVehicle() != null && parkingSlot.getVehicle().getLicensePlate().equals(plate))
               .collect(Collectors.toList());
    }
}

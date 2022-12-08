package com.alper.garageparkapi.parkingslots.services;

import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import com.alper.garageparkapi.parkingslots.repositories.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingService {

    @Autowired
    private ParkingSlotRepository repository;

    public List<ParkingSlot> getParkingSlots(){
        return repository.getParkingSlots();
    }

    public ParkingSlot findParkingSlot(int slot) throws Exception{
        Optional<ParkingSlot> parkingSlotOp = repository.findParkingSlot(slot);
        ParkingSlot parkingSlot = parkingSlotOp.orElseThrow(()-> new NullPointerException());
        return parkingSlot;
    }

    public ParkingSlot updateParkingSlot( ParkingSlot parkingSlot) throws  Exception{
        ParkingSlot slot = findParkingSlot(parkingSlot.getSlotNum());
        return repository.updateParkingSlot(parkingSlot);
    }
}

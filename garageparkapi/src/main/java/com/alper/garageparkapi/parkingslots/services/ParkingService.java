package com.alper.garageparkapi.parkingslots.services;

import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import com.alper.garageparkapi.parkingslots.repositories.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingService {

    @Autowired
    private ParkingSlotRepository repository;

    public List<ParkingSlot> getParkingSlots(){
        return repository.getParkingSlots();
    }
}

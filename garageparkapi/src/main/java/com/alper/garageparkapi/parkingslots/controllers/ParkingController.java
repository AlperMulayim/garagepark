package com.alper.garageparkapi.parkingslots.controllers;

import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import com.alper.garageparkapi.parkingslots.services.ParkingService;
import com.alper.garageparkapi.vehicles.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/park")
public class ParkingController {

    @Autowired
    private ParkingService service;

    @GetMapping
    public List<ParkingSlot> getSlots(){
        return service.getParkingSlots();
    }

    @PostMapping
    public ParkingSlot createParking(@RequestBody Vehicle vehicle){
        return service.createParking(vehicle);
    }
}

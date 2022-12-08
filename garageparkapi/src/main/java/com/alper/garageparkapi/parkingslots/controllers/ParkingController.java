package com.alper.garageparkapi.parkingslots.controllers;

import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import com.alper.garageparkapi.parkingslots.services.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

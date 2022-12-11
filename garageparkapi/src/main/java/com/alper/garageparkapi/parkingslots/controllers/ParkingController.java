package com.alper.garageparkapi.parkingslots.controllers;

import com.alper.garageparkapi.parkingslots.dtos.CarParkStatus;
import com.alper.garageparkapi.parkingslots.dtos.ParkRequest;
import com.alper.garageparkapi.parkingslots.dtos.ParkingSlotDto;
import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import com.alper.garageparkapi.parkingslots.services.ParkingService;
import com.alper.garageparkapi.vehicles.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/garage")
public class ParkingController {

    @Autowired
    private ParkingService service;

    @GetMapping("/status")
    public CarParkStatus getSlots(){
        return service.getCarParkStatus();
    }

    @PostMapping("/park")
    public List<ParkingSlotDto> createParking(@RequestBody ParkRequest parkRequest){
        return service.createParking(parkRequest.getVehicle());
    }

    @PostMapping("/leave")
    public List<ParkingSlotDto> leavePark(@RequestParam String plate){
        return service.leavePark(plate);
    }
}

package com.alper.garageparkapi.parkingslots.controllers;

import com.alper.garageparkapi.parkingslots.dtos.CarParkStatus;
import com.alper.garageparkapi.parkingslots.dtos.ParkRequest;
import com.alper.garageparkapi.parkingslots.dtos.ParkingSlotDto;
import com.alper.garageparkapi.parkingslots.services.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/garage")
public class ParkingController {

    @Autowired
    private ParkingService service;

    @GetMapping("/status")
    public CarParkStatus getStatus(){
        return service.getCarParkStatus();
    }

    @PostMapping("/park")
    public List<ParkingSlotDto> createParking(@RequestBody ParkRequest parkRequest){
        return service.createParking(parkRequest.getVehicle());
    }

    @PostMapping("/leave")
    public List<ParkingSlotDto> leavePark(@RequestParam(required = true, name = "plate") String plate){
        return service.leavePark(plate);
    }
}

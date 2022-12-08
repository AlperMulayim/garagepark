package com.alper.garageparkapi.vehicles.controller;

import com.alper.garageparkapi.vehicles.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @GetMapping()
    public String getVehicles(){
         return "ok";
    }
}

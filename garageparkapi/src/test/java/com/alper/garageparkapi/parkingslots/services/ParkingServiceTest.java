package com.alper.garageparkapi.parkingslots.services;

import com.alper.garageparkapi.enums.VehicleType;
import com.alper.garageparkapi.exceptions.NotFoundException;
import com.alper.garageparkapi.parkingslots.dtos.ParkingSlotDto;
import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import com.alper.garageparkapi.parkingslots.enums.SlotStatus;
import com.alper.garageparkapi.parkingslots.mappers.ParkingSlotMapper;
import com.alper.garageparkapi.parkingslots.repositories.ParkingSlotRepository;
import com.alper.garageparkapi.vehicles.entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingServiceTest {
    @Mock
    private ParkingSlotRepository repository;

    private Map<String,Integer> vehicleSlots;

    private List<ParkingSlot> initialParkingSlots;
    private Integer capacity;

    @Mock
    private ParkingSlotMapper parkingSlotMapper;
    private ParkingService parkingService;

    @BeforeEach
    void init(){
        repository = new ParkingSlotRepository();
        repository.initParkingRepository(10);

        initialParkingSlots = new ArrayList<>();
        capacity = 10;

        Vehicle vehicle1988 = Vehicle
                .builder()
                .color("BLACK")
                .type(VehicleType.CAR)
                .licensePlate("34-SO-1988")
                .build();

        ParkingSlot slot1988 = ParkingSlot
                .builder()
                .slotNum(0)
                .status(SlotStatus.ALLOCATED)
                .vehicle(vehicle1988)
                .build();

        ParkingSlot slot1988Closed= ParkingSlot
                .builder()
                .slotNum(1)
                .status(SlotStatus.CLOSED)
                .vehicle(vehicle1988)
                .build();

        initialParkingSlots.add(slot1988);
        initialParkingSlots.add(slot1988Closed);
        repository.updateParkingSlot(slot1988);
        repository.updateParkingSlot(slot1988Closed);

        for(int i = 2; i < capacity; ++i ){
            ParkingSlot slot= ParkingSlot
                    .builder()
                    .slotNum(i)
                    .status(SlotStatus.AVAILABLE)
                    .vehicle(vehicle1988)
                    .build();
            initialParkingSlots.add(slot);
            repository.updateParkingSlot(slot);
        }

        vehicleSlots = new HashMap<>();
        vehicleSlots.put("CAR",1);
        vehicleSlots.put("JEEP",2);
        vehicleSlots.put("TRUCK",4);

        parkingSlotMapper = new ParkingSlotMapper();

        parkingService = new ParkingService(vehicleSlots,repository,capacity,parkingSlotMapper);
    }


    @Test
    void whenGetParkingSlotsThenReturnsAllParkingSlotsInGarage(){
        List<ParkingSlotDto> expectedSlotDto = initialParkingSlots.stream()
                .map(s->parkingSlotMapper.toDTO(s)).collect(Collectors.toList());

        List<ParkingSlotDto> result = parkingService.getParkingSlots();

        assertEquals(expectedSlotDto,result);
    }

    @Test
    void whenFindParkingSlotWithSlot1WillReturnSlot(){
        ParkingSlot result = parkingService.findParkingSlot(1);
        ParkingSlot expected = initialParkingSlots.get(1);

        assertEquals(expected,result);
    }

    @Test
    void whenFindParkingSlotsWithOutOfRangeThrowsNotFoundException(){
        assertThrows(NotFoundException.class, ()-> parkingService.findParkingSlot(-1));
    }

   @Test
    void whenCreateParkingIfVehicleNullThrowsNotFoundException(){
       assertThrows(NotFoundException.class, ()-> parkingService.createParking(null));
   }

   @Test
    void whenCreataParkingWillReturnVehicleCapacityParkingSlotsAndClosedSlot(){
        Vehicle vehicle = Vehicle.builder()
                .type(VehicleType.TRUCK)
                .licensePlate("34-ABC-123")
                .color("BLUE")
                .build();

        List<ParkingSlotDto> slotDtos = parkingService.createParking(vehicle);
        assertEquals(5,slotDtos.size());
        assertEquals(SlotStatus.CLOSED,slotDtos.get(slotDtos.size()-1).getStatus());
   }

    @Test
    void whenCreataParkingWillReturnVehicleCapacityParkingSlotsAndClosedSlotCar(){
        Vehicle vehicle = Vehicle.builder()
                .type(VehicleType.CAR)
                .licensePlate("34-ABC-123")
                .color("BLUE")
                .build();

        List<ParkingSlotDto> slotDtos = parkingService.createParking(vehicle);
        assertEquals(2,slotDtos.size());
        assertEquals(SlotStatus.CLOSED,slotDtos.get(slotDtos.size()-1).getStatus());
    }

    @Test
    void whenCreataParkingWillReturnVehicleCapacityParkingSlotsAndClosedSlotJeep(){
        Vehicle vehicle = Vehicle.builder()
                .type(VehicleType.JEEP)
                .licensePlate("34-ABC-123")
                .color("BLUE")
                .build();

        List<ParkingSlotDto> slotDtos = parkingService.createParking(vehicle);
        assertEquals(3,slotDtos.size());
        assertEquals(SlotStatus.CLOSED,slotDtos.get(slotDtos.size()-1).getStatus());
    }

    @Test
    void whenLeaveParkRequestedIfSlotNotFoundForGivenPlateThrowsException(){
        assertThrows(NotFoundException.class,()-> parkingService.leavePark("34-ABC-123"));
    }

    @Test
    void whenLeaveParkRequestedReturnsSlotsForGivenPlate(){

        Vehicle vehicle = Vehicle.builder()
                .type(VehicleType.JEEP)
                .licensePlate("34-ABC-123")
                .color("BLUE")
                .build();
        parkingService.createParking(vehicle);
        List<ParkingSlotDto> slotDtos = parkingService.leavePark("34-ABC-123");
        assertEquals(slotDtos.size(), 3);
    }

}
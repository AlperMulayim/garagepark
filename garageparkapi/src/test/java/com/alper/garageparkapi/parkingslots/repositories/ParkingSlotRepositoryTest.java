package com.alper.garageparkapi.parkingslots.repositories;


import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import static org.junit.jupiter.api.Assertions.*;

import com.alper.garageparkapi.parkingslots.enums.SlotStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ParkingSlotRepositoryTest {

    private ParkingSlotRepository repository;

    @BeforeEach
    void init(){
        repository = new ParkingSlotRepository();
        repository.initParkingRepository(10);
    }

    @Test
    void whenRepositoryCreatedInitialCapacityWill10(){
        assertEquals(10,repository.getParkingCapacity());
    }

    @Test
    void whenRepositoryCreatedInitialAllSlotsWillAvailable(){
        List<ParkingSlot> slots = repository.getParkingSlots();

        List<ParkingSlot> expected = new ArrayList<>();

        for(int i = 0; i < repository.getParkingCapacity(); ++i){
            ParkingSlot parkingSlot =  ParkingSlot.builder()
                    .slotNum(i)
                    .status(SlotStatus.AVAILABLE)
                    .vehicle(null)
                    .build();
            expected.add(parkingSlot);
        }
        assertEquals(expected.size(),slots.size());
        assertEquals(expected,slots);
    }

    @Test
    void shouldParkingSlotId9WillFindInRepository(){
        Optional<ParkingSlot> slot = repository.findParkingSlot(9);

        ParkingSlot expected = ParkingSlot.builder()
                .slotNum(9)
                .status(SlotStatus.AVAILABLE)
                .vehicle(null)
                .build();

        assertEquals(expected,slot.get());
    }

    @Test
    void whenSlotNumberOutOfRangeParkingSlotOptionalWillEmpty(){
        Optional<ParkingSlot> slot = repository.findParkingSlot(1000);
        assertTrue(slot.isEmpty());
    }

    @Test
    void whenParkingSlotUpdatedSlotWillUpdatedOnList(){
        ParkingSlot expected = ParkingSlot.builder()
                .slotNum(9)
                .status(SlotStatus.AVAILABLE)
                .vehicle(null)
                .build();

        ParkingSlot updatedSlot = repository.updateParkingSlot(expected);
        assertEquals(expected,updatedSlot);
    }

    @Test
    void testFindAvailableSlots(){
        ParkingSlot expected = ParkingSlot.builder()
                .slotNum(9)
                .status(SlotStatus.ALLOCATED)
                .vehicle(null)
                .build();
        ParkingSlot expectedA = ParkingSlot.builder()
                .slotNum(9)
                .status(SlotStatus.AVAILABLE)
                .vehicle(null)
                .build();
        ParkingSlot expectedB = ParkingSlot.builder()
                .slotNum(9)
                .status(SlotStatus.AVAILABLE)
                .vehicle(null)
                .build();

        List<ParkingSlot> slots = new ArrayList<>();
        slots.add(expected);
        slots.add(expectedA);
        slots.add(expectedB);

        int size = 3;

        repository = new ParkingSlotRepository(slots,size);

        List<ParkingSlot> results = repository.findAvailableSlots();
        List<ParkingSlot> expectedList = new ArrayList<>();
        expectedList.add(expectedA);
        expectedList.add(expectedB);

        assertEquals(expectedList,results);

    }

    @Test
    void testFindAllocatedSlots(){
        ParkingSlot expected = ParkingSlot.builder()
                .slotNum(9)
                .status(SlotStatus.ALLOCATED)
                .vehicle(null)
                .build();
        ParkingSlot expectedA = ParkingSlot.builder()
                .slotNum(9)
                .status(SlotStatus.AVAILABLE)
                .vehicle(null)
                .build();
        ParkingSlot expectedB = ParkingSlot.builder()
                .slotNum(9)
                .status(SlotStatus.AVAILABLE)
                .vehicle(null)
                .build();

        List<ParkingSlot> slots = new ArrayList<>();
        slots.add(expected);
        slots.add(expectedA);
        slots.add(expectedB);

        int size = 3;

        repository = new ParkingSlotRepository(slots,size);

        List<ParkingSlot> results = repository.findAllocatedSlots();
        List<ParkingSlot> expectedList = new ArrayList<>();
        expectedList.add(expected);

        assertEquals(expectedList,results);

    }
}
package com.alper.garageparkapi.parkingslots.services;

import com.alper.garageparkapi.enums.VehicleType;
import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import com.alper.garageparkapi.parkingslots.enums.SlotStatus;
import com.alper.garageparkapi.parkingslots.repositories.ParkingSlotRepository;
import com.alper.garageparkapi.vehicles.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ParkingService {

    @Autowired
    private ParkingSlotRepository repository;

    @Autowired
    private Map<String, Integer> vehicleSlots;

    public List<ParkingSlot> getParkingSlots(){
        return repository.getParkingSlots();
    }

    public ParkingSlot findParkingSlot(int slot) throws Exception{
        Optional<ParkingSlot> parkingSlotOp = repository.findParkingSlot(slot);
        ParkingSlot parkingSlot = parkingSlotOp.orElseThrow(()-> new NullPointerException());
        return parkingSlot;
    }

    public ParkingSlot updateParkingSlot(ParkingSlot parkingSlot) throws  Exception{
        ParkingSlot slot = findParkingSlot(parkingSlot.getSlotNum());
        return repository.updateParkingSlot(parkingSlot);
    }

    public ParkingSlot createParking(Vehicle vehicle){

        boolean isAllowed = isParkingAreaAvailable(vehicle);
        return null;
    }

    private boolean isParkingAreaAvailable(Vehicle vehicle){
        int requestedParkArea = 3;

        List<ParkingSlot> availableSlots = repository.findAvailableSlots();

        //find next requested area is available or not
        //if available count
        //not available next slot check.

        for (ParkingSlot slot: availableSlots){
            List<ParkingSlot> allocatables = allocationAvailableForSlots(slot);
            if(allocatables.size() >= requestedParkArea){
                System.out.println("allocate ok");
                System.out.println(allocatables);
                updateParkArea(allocatables,vehicle);
                break;
            }
        }

        return  true;
    }

    private List<ParkingSlot> allocationAvailableForSlots(ParkingSlot slot){
        List<ParkingSlot> allSlots = repository.getParkingSlots();
        List<ParkingSlot> canAllocate = new ArrayList<>();

        if(slot.getSlotNum() + 3 < allSlots.size()) {
            for (int i = slot.getSlotNum(); i <= slot.getSlotNum() + 3; ++i) {
                if (allSlots.get(i).getStatus().equals(SlotStatus.AVAILABLE)) {
                    canAllocate.add(allSlots.get(i));
                }
            }
        }
        return canAllocate;
    }

    private void updateParkArea(List<ParkingSlot> selectedSlots, Vehicle vehicle){
        for (ParkingSlot slot: selectedSlots){
            ParkingSlot allocated =  allocateSlot(slot,vehicle);
            repository.updateParkingSlot(allocated);
        }
        ParkingSlot shouldClose =  selectedSlots.get(selectedSlots.size() -1);
        repository.updateParkingSlot(closeSlot(shouldClose,vehicle));
    }
    private ParkingSlot allocateSlot(ParkingSlot parkingSlot, Vehicle vehicle){
        parkingSlot.setVehicle(vehicle);
        parkingSlot.setStatus(SlotStatus.ALLOCATED);
        return parkingSlot;
    }
    private ParkingSlot closeSlot(ParkingSlot parkingSlot, Vehicle vehicle){
        parkingSlot.setVehicle(vehicle);
        parkingSlot.setStatus(SlotStatus.CLOSED);
        return parkingSlot;
    }
}











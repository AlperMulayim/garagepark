package com.alper.garageparkapi.parkingslots.services;

import com.alper.garageparkapi.enums.VehicleType;
import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import com.alper.garageparkapi.parkingslots.enums.SlotStatus;
import com.alper.garageparkapi.parkingslots.repositories.ParkingSlotRepository;
import com.alper.garageparkapi.vehicles.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ParkingService {


    @Autowired
    @Qualifier("vehicleslots")
    private Map<String, Integer> vehicleSlots;

    @Autowired
    private  ParkingSlotRepository repository;

    @Autowired
    @Qualifier("capacity")
    private Integer capacityBean;

    @PostConstruct
    public  void setRepository(){
        repository.initParkingRepository(capacityBean);
    }

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

    public List<ParkingSlot> createParking(Vehicle vehicle){
        return park(vehicle);
    }

    private List<ParkingSlot> park(Vehicle vehicle){
        List<ParkingSlot> parkedSlots = new ArrayList<>();
        System.out.println(vehicleSlots);
        int requestedParkArea = vehicleSlots.get(vehicle.getType().toString());

        List<ParkingSlot> availableSlots = repository.findAvailableSlots();

        //find next requested area is available or not
        //if available count
        //not available next slot check.

        for (ParkingSlot slot: availableSlots){
            List<ParkingSlot> allocatables = allocationAvailableForSlots(slot,  requestedParkArea);
            if(allocatables.size() >= requestedParkArea){
                System.out.println("allocate ok");
                System.out.println(allocatables);
                parkedSlots =  createParkArea(allocatables,vehicle);
                break;
            }
        }

        return parkedSlots;
    }

    private List<ParkingSlot> allocationAvailableForSlots(ParkingSlot slot, int requestedArea){
        List<ParkingSlot> allSlots = repository.getParkingSlots();
        List<ParkingSlot> canAllocate = new ArrayList<>();

        if(slot.getSlotNum() + requestedArea < allSlots.size()) {
            for (int i = slot.getSlotNum(); i <= slot.getSlotNum() + requestedArea; ++i) {
                if (allSlots.get(i).getStatus().equals(SlotStatus.AVAILABLE)) {
                    canAllocate.add(allSlots.get(i));
                }
            }
        }
        return canAllocate;
    }

    private List<ParkingSlot> createParkArea(List<ParkingSlot> selectedSlots, Vehicle vehicle){
        List<ParkingSlot>  areaParked = new ArrayList<>();

        for (int i = 0; i < selectedSlots.size() -1; ++i){
            ParkingSlot allocated =  allocateSlot(selectedSlots.get(i),vehicle);
            areaParked.add(repository.updateParkingSlot(allocated));
        }
        ParkingSlot shouldClose =  selectedSlots.get(selectedSlots.size() -1);
        repository.updateParkingSlot(closeSlot(shouldClose,vehicle));
        areaParked.add(shouldClose);
        return areaParked;
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
    public List<ParkingSlot> leavePark(String plate){
        List<ParkingSlot> parkedSlots  = repository.findVehicleSlots(plate);
        return parkedSlots.stream()
                .map(this::freeSlot)
               .collect(Collectors.toList());
    }
    private ParkingSlot freeSlot(ParkingSlot slot){
        slot.setVehicle(null);
        slot.setStatus(SlotStatus.AVAILABLE);
        repository.updateParkingSlot(slot);
        return  slot;
    }

}











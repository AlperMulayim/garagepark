package com.alper.garageparkapi.parkingslots.services;

import com.alper.garageparkapi.enums.VehicleType;
import com.alper.garageparkapi.exceptions.NotFoundException;
import com.alper.garageparkapi.exceptions.ParkSlotsNotAvailableException;
import com.alper.garageparkapi.parkingslots.dtos.CarParkStatus;
import com.alper.garageparkapi.parkingslots.dtos.ParkArea;
import com.alper.garageparkapi.parkingslots.dtos.ParkRequest;
import com.alper.garageparkapi.parkingslots.dtos.ParkingSlotDto;
import com.alper.garageparkapi.parkingslots.entity.ParkingSlot;
import com.alper.garageparkapi.parkingslots.enums.SlotStatus;
import com.alper.garageparkapi.parkingslots.mappers.ParkingSlotMapper;
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

    @Autowired
    private ParkingSlotMapper parkingSlotMapper;

    @PostConstruct
    public  void setRepository(){
        repository.initParkingRepository(capacityBean);
    }

    public List<ParkingSlotDto> getParkingSlots(){
        List<ParkingSlot> parkingSlots = repository.getParkingSlots();
        return parkingSlots.stream()
                .map(parkingSlot -> parkingSlotMapper.toDTO(parkingSlot))
                .collect(Collectors.toList());
    }

    public ParkingSlot findParkingSlot(int slot) throws Exception{
        Optional<ParkingSlot> parkingSlotOp = repository.findParkingSlot(slot);
        ParkingSlot parkingSlot = parkingSlotOp.orElseThrow(()-> new NullPointerException());
        return parkingSlot;
    }

    public ParkingSlotDto updateParkingSlot(ParkingSlot parkingSlot) throws  Exception{
        ParkingSlot slot = findParkingSlot(parkingSlot.getSlotNum());
        ParkingSlot updated = repository.updateParkingSlot(parkingSlot);
        return parkingSlotMapper.toDTO(updated);
    }

    public List<ParkingSlotDto> createParking(Vehicle vehicle){
        List<ParkingSlot> parked = park(vehicle);
        return parked.stream()
                .map(parkingSlot ->  parkingSlotMapper.toDTO(parkingSlot))
                .collect(Collectors.toList());
    }

    private List<ParkingSlot> park(Vehicle vehicle) throws ParkSlotsNotAvailableException {
        List<ParkingSlot> parkedSlots = new ArrayList<>();
        int requestedParkArea = vehicleSlots.get(vehicle.getType().toString());
        List<ParkingSlot> availableSlots = repository.findAvailableSlots();

        for (ParkingSlot slot: availableSlots){
            List<ParkingSlot> allocatables = allocationAvailableForSlots(slot,  requestedParkArea);
            if(allocatables.size() >= requestedParkArea){
                parkedSlots =  createParkArea(allocatables,vehicle);
                break;
            }
        }

        if(parkedSlots.isEmpty()){
            throw new ParkSlotsNotAvailableException("Requested Park Slots Not Available For Vehicle, Garage is FULL, Please Go Other Garages!");
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
    public List<ParkingSlotDto> leavePark(String plate) throws  NotFoundException{
        List<ParkingSlot> parkedSlots  = repository.findVehicleSlots(plate);

        if(parkedSlots.isEmpty()){
            throw  new NotFoundException("Parked Slot Not Found Given Plate, Control Plate.");
        }
        return parkedSlots.stream()
                .map(this::freeSlot)
                .map(parkingSlot -> parkingSlotMapper.toDTO(parkingSlot))
               .collect(Collectors.toList());
    }
    private ParkingSlot freeSlot(ParkingSlot slot){
        slot.setVehicle(null);
        slot.setStatus(SlotStatus.AVAILABLE);
        repository.updateParkingSlot(slot);
        return  slot;
    }

    public CarParkStatus  getCarParkStatus(){
        List<ParkingSlot> slots;
        List<ParkArea> parkAreas = new ArrayList<>();

        slots = repository.getParkingSlots().stream()
                .filter(slot-> slot.getVehicle() != null)
                .collect(Collectors.toList());

        Map<String,List<ParkingSlot>> groupedSlots = groupSlotsByPlate(slots);

        parkAreas = getParkedArea(groupedSlots);
        return parkStatus(parkAreas,slots.size(),capacityBean);
    }

    private List<ParkArea> getParkedArea(Map<String, List<ParkingSlot>> groupedSlots ){
        List<ParkArea> areas = new ArrayList<>();

        for(String  plate : groupedSlots.keySet()){
            List<ParkingSlot> carSlots = groupedSlots.get(plate);
            String slotInfo =  getSlotInfo(carSlots);
            ParkArea parkArea =  buildParkArea(carSlots.get(0).getVehicle(), slotInfo);
            areas.add(parkArea);
        }
        return areas;
    }

    private Map<String, List<ParkingSlot>> groupSlotsByPlate(List<ParkingSlot> slots){
        return slots.stream()
                .collect(Collectors.groupingBy(slot-> slot.getVehicle().getLicensePlate()));
    }
    private  String getSlotInfo(List<ParkingSlot> slots){
        return slots.stream()
                .map(slot-> slot.getSlotNum().toString())
                .collect(Collectors.joining("- "));
    }

    private ParkArea buildParkArea(Vehicle vehicle,String slotInfo){
       return ParkArea.builder()
                .carColor(vehicle.getColor())
                .plate(vehicle.getLicensePlate())
                .slotInfo(slotInfo)
                .build();
    }

    private CarParkStatus parkStatus(List<ParkArea> areas, Integer allocatedSlots, Integer capacity){
       return CarParkStatus.builder()
                .parks(areas)
                .totalCarsInPark(areas.size())
                .totalAllocatedSlots(allocatedSlots)
                .totalEmptySlots(capacity - allocatedSlots)
                .capacity(capacity)
                .build();
    }

}











package com.alper.garageparkapi.parkingslots.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkArea {
    private String plate;
    private String carColor;
    private String slotInfo;
}

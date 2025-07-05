package com.lift.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiftRequest {
    private int requestedFloor;
    private boolean isEmergency;
}

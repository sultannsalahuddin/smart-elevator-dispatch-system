package com.lift.controller;

import com.lift.model.Lift;
import com.lift.model.LiftRequest;
import com.lift.service.DispatchManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lift")
public class LiftControllerAPI {

    /**
     * API to call a lift to a requested floor.
     *
     * @param request JSON body with requestedFloor and isEmergency flag
     * @return Response message indicating assigned lift or failure
     */
    @PostMapping("/call")
    public ResponseEntity<String> callLift(@RequestBody LiftRequest request) {
        if (request.getRequestedFloor() < -3 || request.getRequestedFloor() > 16) {
            return ResponseEntity.badRequest().body("❌ Invalid floor number. Valid range: B3(-3) to 16.");
        }

        Lift assignedLift = DispatchManager.dispatchLift(request.getRequestedFloor(), request.isEmergency());

        if (assignedLift != null) {
            return ResponseEntity.ok("✅ Lift " + assignedLift.getId() + " assigned to floor " + request.getRequestedFloor());
        } else {
            return ResponseEntity.ok("⚠️ No available lift at the moment. Try again later.");
        }
    }

    /**
     * API to toggle maintenance mode for a specific lift by ID.
     *
     * @param liftId Lift identifier
     * @return Response message indicating current maintenance status
     */
    @PostMapping("/maintenance/{liftId}")
    public ResponseEntity<String> toggleMaintenance(@PathVariable String liftId) {
        boolean result = DispatchManager.toggleMaintenance(liftId);
        if (result) {
            return ResponseEntity.ok("✅ Maintenance mode toggled for Lift " + liftId);
        } else {
            return ResponseEntity.badRequest().body("❌ Lift with ID " + liftId + " not found.");
        }
    }
}

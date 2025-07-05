package com.lift;

import com.lift.model.Lift;
import com.lift.service.DispatchManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LiftServiceTests {

    @BeforeEach
    public void resetLifts() {
        DispatchManager.oddLifts.forEach(l -> {
            l.setInMaintenance(false);
            l.getDestinationQueue().clear();
            l.setCurrentLoad(0);
            l.setCurrentFloor(0);
            l.setDirection("IDLE");
        });
        DispatchManager.evenLifts.forEach(l -> {
            l.setInMaintenance(false);
            l.getDestinationQueue().clear();
            l.setCurrentLoad(0);
            l.setCurrentFloor(0);
            l.setDirection("IDLE");
        });
    }

    @Test
    public void testDispatchLiftToOddFloor() {
        Lift lift = DispatchManager.dispatchLift(5, false);
        assertNotNull(lift);
        assertEquals("odd", lift.getSection());
        assertTrue(lift.getDestinationQueue().contains(5));
    }

    @Test
    public void testDispatchLiftToEvenFloor() {
        Lift lift = DispatchManager.dispatchLift(8, false);
        assertNotNull(lift);
        assertEquals("even", lift.getSection());
        assertTrue(lift.getDestinationQueue().contains(8));
    }

    @Test
    public void testDispatchLiftToBasementFloor() {
        Lift lift = DispatchManager.dispatchLift(-2, false);
        assertNotNull(lift);
        // Basements can be served by either section, so section could be odd or even
        assertTrue(lift.getSection().equals("odd") || lift.getSection().equals("even"));
        assertTrue(lift.getDestinationQueue().contains(-2));
    }

    @Test
    public void testDispatchLiftWithEmergency() {
        // Mark all even lifts as in maintenance to force odd lifts to pick up emergency
        DispatchManager.evenLifts.forEach(l -> l.setInMaintenance(true));

        Lift lift = DispatchManager.dispatchLift(8, true);
        // Even though floor 8 is even, due to emergency, odd lifts might be chosen if closer
        assertNotNull(lift);
        assertTrue(lift.getDestinationQueue().contains(8));

        DispatchManager.evenLifts.forEach(l -> l.setInMaintenance(false));
    }

    @Test
    public void testNoAvailableLift() {
        // Put all lifts in maintenance mode
        DispatchManager.oddLifts.forEach(l -> l.setInMaintenance(true));
        DispatchManager.evenLifts.forEach(l -> l.setInMaintenance(true));

        Lift lift = DispatchManager.dispatchLift(5, false);
        assertNull(lift);

        // Reset
        DispatchManager.oddLifts.forEach(l -> l.setInMaintenance(false));
        DispatchManager.evenLifts.forEach(l -> l.setInMaintenance(false));
    }

    @Test
    public void testLiftMovement() {
        Lift lift = DispatchManager.oddLifts.get(0);

        // Ensure clean state
        lift.getDestinationQueue().clear();
        lift.setCurrentFloor(0);
        lift.setDirection("IDLE");

        lift.getDestinationQueue().offer(3);

        // move() calls
        lift.move(); // Floor 0 → 1
        lift.move(); // 1 → 2
        lift.move(); // 2 → 3

        System.out.println("Queue after 3 moves: " + lift.getDestinationQueue());

        // Now, destinationQueue should be empty
        assertTrue(lift.getDestinationQueue().isEmpty(), "Queue should be empty after arriving at destination");

        // One more move() to allow state to update to IDLE
        lift.move();

        assertEquals("IDLE", lift.getDirection(), "Direction should be IDLE after no pending destinations");
    }


}

package com.lift.service;

import com.lift.model.Lift;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DispatchManager {
    public static final int BASEMENT_3 = -3;
    public static final int TOP_FLOOR = 16;

    public static List<Lift> oddLifts = new ArrayList<>();
    public static List<Lift> evenLifts = new ArrayList<>();

    static {
        // Initialize 6 lifts per section starting at ground floor (0)
        for (int i = 1; i <= 6; i++) {
            oddLifts.add(new Lift("O" + i, "odd", 0));
            evenLifts.add(new Lift("E" + i, "even", 0));
        }
    }

    /**
     * Dispatch the best lift to the requested floor, considering emergency and lift state.
     * @param floor requested floor (-3 to 16)
     * @param isEmergency emergency priority flag
     * @return assigned Lift or null if none available
     */
    public static Lift dispatchLift(int floor, boolean isEmergency) {
        String section = (floor <= 0) ? "both" : (floor % 2 == 0 ? "even" : "odd");
        List<Lift> liftsToConsider = new ArrayList<>();

        if (section.equals("odd") || section.equals("both")) {
            liftsToConsider.addAll(oddLifts);
        }
        if (section.equals("even") || section.equals("both")) {
            liftsToConsider.addAll(evenLifts);
        }

        // Emergency fallback: if no available lift in section, check both
        if (isEmergency && liftsToConsider.stream()
                .allMatch(l -> l.isInMaintenance() || l.getCurrentLoad() >= l.getCapacity())) {
            liftsToConsider.clear();
            liftsToConsider.addAll(oddLifts);
            liftsToConsider.addAll(evenLifts);
        }

        // Filter available lifts
        List<Lift> availableLifts = liftsToConsider.stream()
                .filter(l -> !l.isInMaintenance() && l.getCurrentLoad() < l.getCapacity())
                .toList();

        if (availableLifts.isEmpty()) {
            return null;
        }

        // Find nearest lift
        Lift nearest = availableLifts.stream()
                .min(Comparator.comparingInt(l -> Math.abs(l.getCurrentFloor() - floor)))
                .orElse(null);

        if (nearest != null) {
            nearest.getDestinationQueue().offer(floor);
        }

        return nearest;
    }

    /**
     * Toggle maintenance mode on/off for a lift by ID.
     * @param liftId lift identifier
     * @return true if toggled successfully, false if lift not found
     */
    public static boolean toggleMaintenance(String liftId) {
        for (Lift lift : oddLifts) {
            if (lift.getId().equalsIgnoreCase(liftId)) {
                lift.setInMaintenance(!lift.isInMaintenance());
                return true;
            }
        }
        for (Lift lift : evenLifts) {
            if (lift.getId().equalsIgnoreCase(liftId)) {
                lift.setInMaintenance(!lift.isInMaintenance());
                return true;
            }
        }
        return false;
    }
}

package com.lift.service;

import com.lift.model.Lift;

public class SimulationEngine {

    private volatile boolean running = true;

    public void startSimulation() {
        Thread simThread = new Thread(() -> {
            while (running) {
                // Move all lifts each tick
                for (Lift lift : DispatchManager.oddLifts) lift.move();
                for (Lift lift : DispatchManager.evenLifts) lift.move();

                displayStatus();

                try {
                    Thread.sleep(1000); // 1 second per simulation tick
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        simThread.setDaemon(true);
        simThread.start();
    }

    public void stopSimulation() {
        running = false;
    }

    private void displayStatus() {
        System.out.println("\n--- Lift Status ---");
        for (Lift lift : DispatchManager.oddLifts) {
            System.out.printf("%s | Floor: %d | Dir: %s | Queue: %s | Maintenance: %b%n",
                    lift.getId(), lift.getCurrentFloor(), lift.getDirection(),
                    lift.getDestinationQueue(), lift.isInMaintenance());
        }
        for (Lift lift : DispatchManager.evenLifts) {
            System.out.printf("%s | Floor: %d | Dir: %s | Queue: %s | Maintenance: %b%n",
                    lift.getId(), lift.getCurrentFloor(), lift.getDirection(),
                    lift.getDestinationQueue(), lift.isInMaintenance());
        }
    }
}

package com.lift.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.Queue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lift {
    private String id;
    private String section; // "odd" or "even"
    private int currentFloor;
    private String direction = "IDLE";
    private boolean inMaintenance = false;
    private int capacity = 8;
    private int currentLoad = 0;
    private Queue<Integer> destinationQueue = new LinkedList<>();

    public Lift(String id, String section, int currentFloor) {
        this.id = id;
        this.section = section;
        this.currentFloor = currentFloor;
    }

    public void move() {
        if (destinationQueue.isEmpty()) {
            direction = "IDLE";
            return;
        }

        int target = destinationQueue.peek();
        if (currentFloor < target) {
            currentFloor++;
            direction = "UP";
        } else if (currentFloor > target) {
            currentFloor--;
            direction = "DOWN";
        }

        // Now check after moving, whether arrived
        if (currentFloor == target) {
            destinationQueue.poll();
            if (destinationQueue.isEmpty()) {
                direction = "IDLE";
            } else {
                int nextTarget = destinationQueue.peek();
                if (currentFloor < nextTarget) {
                    direction = "UP";
                } else if (currentFloor > nextTarget) {
                    direction = "DOWN";
                }
            }
        }
    }

}

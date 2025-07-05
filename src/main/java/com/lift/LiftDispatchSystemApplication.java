package com.lift;

import com.lift.model.LiftRequest;
import com.lift.service.DispatchManager;
import com.lift.service.SimulationEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LiftDispatchSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiftDispatchSystemApplication.class, args);

        SimulationEngine engine = new SimulationEngine();
        engine.startSimulation();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nEnter floor number to call lift (-3 to 16), or 'exit' to quit:");
                String input = scanner.nextLine();
                if ("exit".equalsIgnoreCase(input)) {
                    System.out.println("Exiting simulation.");
                    engine.stopSimulation();
                    break;
                }

                try {
                    int floor = Integer.parseInt(input);
                    if (floor < -3 || floor > 16) {
                        System.out.println("Invalid floor. Must be between -3 and 16.");
                        continue;
                    }

                    LiftRequest request = new LiftRequest();
                    request.setRequestedFloor(floor);
                    request.setEmergency(false);

                    var assignedLift = DispatchManager.dispatchLift(floor, false);

                    if (assignedLift != null) {
                        System.out.printf("Lift %s assigned to floor %d%n", assignedLift.getId(), floor);
                    } else {
                        System.out.println("No lift available at the moment.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid floor number or 'exit'.");
                }
            }
        }
    }
}

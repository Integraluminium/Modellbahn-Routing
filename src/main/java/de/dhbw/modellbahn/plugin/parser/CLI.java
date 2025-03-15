package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.port.moba.communication.SystemCalls;
import de.dhbw.modellbahn.domain.graph.Graph;

import java.util.Scanner;

public class CLI {
    private final Actions actions;
    private final Parser parser;

    public CLI(Graph graph, LocomotiveRepository locomotiveRepository, SystemCalls systemCalls) {
        actions = new Actions(graph, locomotiveRepository, systemCalls);
        parser = new Parser(graph, locomotiveRepository);
    }

    public void runCLI() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("Enter command (type 'help' for options): ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");

            switch (parts[0]) {
                case "help":
                    printHelp();
                    break;
                case "add":
                    if (parts.length < 2) {
                        System.out.println("Usage: add <loc_id>");
                        break;
                    }
                    actions.addLocomotiveToConsider(parser.parseLocId(parts[1]));
                    break;
                case "destination":
                    if (parts.length < 3) {
                        System.out.println("Usage: destination <loc_id> <destination>");
                        break;
                    }
                    actions.setDestination(parser.parseLocId(parts[1]), parser.parseGraphPoint(parts[2]));
                    break;
                case "facing":
                    if (parts.length < 3) {
                        System.out.println("Usage: facing <loc_id> <facing_direction>");
                        break;
                    }
                    actions.setFacingDirection(parser.parseLocId(parts[1]), parser.parseGraphPoint(parts[2]));
                    break;
                case "electrification":
                    if (parts.length < 2) {
                        System.out.println("Usage: electrification <true|false>");
                        break;
                    }
                    actions.setElectrificationConsideration(Boolean.parseBoolean(parts[1]));
                    break;
                case "height":
                    if (parts.length < 2) {
                        System.out.println("Usage: height <true|false>");
                        break;
                    }
                    actions.setHeightConsideration(Boolean.parseBoolean(parts[1]));
                    break;
                case "optimization":
                    if (parts.length < 3) {
                        System.out.println("Usage: optimization <loc_id> <optimization>");
                        break;
                    }
                    actions.setOptimization(parser.parseLocId(parts[1]), parser.parseOptimization(parts[2]));
                    break;
                case "drive":
                    actions.generateAndDriveRoute();
                    break;
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command. Type 'help' for options.");
                    break;
            }
        }

        System.out.println("Exiting...");
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("add <locomotive_name> - Add a locomotive to the route");
        System.out.println("destination <locomotive_name> <destination> - Set the destination for a locomotive");
        System.out.println("facing <locomotive_name> <facing_direction> - Set the facing direction for a locomotive");
        System.out.println("electrification <true|false> - Consider electrification in the route");
        System.out.println("height <true|false> - Consider height in the route");
        System.out.println("optimization <locomotive_name> <optimization> - Set the route optimization for a locomotive");
        System.out.println("drive - Drive the route");
        System.out.println("exit - Exit the program");
    }
}

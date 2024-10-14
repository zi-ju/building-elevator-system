package main;

import building.Building;
import building.BuildingInterface;
import java.util.Scanner;
import scanerzus.Request;

/**
 * The driver for the elevator system.
 * This class will create the elevator system and run it.
 * this is for testing the elevator system.
 * <p>
 * It provides a user interface to the elevator system.
 */
public class MainConsole {

  /**
   * The main method for the elevator system.
   * This method creates the elevator system and runs it.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    // the number of floors, the number of elevators, and the number of people.

    final int numFloors = 11;
    final int numElevators = 8;
    final int numPeople = 3;


    String[] introText = {
        "Welcome to the Elevator System!",
        "This system will simulate the operation of an elevator system.",
        "The system will be initialized with the following parameters:",
        "Number of floors: " + numFloors,
        "Number of elevators: " + numElevators,
        "Number of people: " + numPeople,
        "The system will then be run and the results will be displayed.",
        "",
        "Press enter to continue."
    };

    for (String line : introText) {
      System.out.println(line);
    }
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();

    // set up the building
    BuildingInterface building = new Building(numFloors, numElevators, numPeople);

    // print the status of the elevator system
    System.out.println(building.getElevatorSystemStatus());

    // print input guide
    String inputGuide = "Enter a command to interact with the elevator system.\n"
            + "step: step\n"
            + "request start end: request from start floor to end floor\n"
            + "run: run the elevator system\n"
            + "stop: stop the elevator system\n"
            + "q: quit\n";
    System.out.println(inputGuide);

    // get user input
    while (scanner.hasNext()) {
      String input = scanner.nextLine();
      switch (input) {
        case "step":
          building.step();
          System.out.println(building.getElevatorSystemStatus());
          break;
        case "run":
          building.startElevatorSystem();
          System.out.println(building.getElevatorSystemStatus());
          break;
        case "stop":
          building.stopElevatorSystem();
          System.out.println(building.getElevatorSystemStatus());
          break;
        case "q":
          System.exit(0);
          break;
        default:
          String[] request = input.split(" ");
          if (request.length == 3 && request[0].equals("request")) {
            try {
              int start = Integer.parseInt(request[1]);
              int end = Integer.parseInt(request[2]);
              building.addRequest(new Request(start, end));
              System.out.println(building.getElevatorSystemStatus());
            } catch (NumberFormatException e) {
              System.out.println("Invalid input. Please enter a number.");
            } catch (IllegalArgumentException e) {
              System.out.println(e.getMessage());
            }
          } else {
            System.out.println("Invalid input. Please try again.");
          }
          break;
      }

    }





  }


}
package main;

import building.Building;
import controller.BuildingController;
import controller.BuildingControllerImpl;
import view.BuildingView;
import view.BuildingViewImpl;

/**
 * The main class for the elevator simulation.
 */
public class Main {
  /**
   * The main method for the elevator simulation.
   *
   * @param args the command line arguments.
   */
  public static void main(String[] args) {
    // default building with 10 floors, 8 elevators, and elevator capacity of 20
    Building building = new Building(10, 8, 20);
    BuildingView view = new BuildingViewImpl("Elevator Simulation",
            building.getNumberOfFloors(),
            building.getNumberOfElevators());
    BuildingController controller = new BuildingControllerImpl(view, building);
  }
}

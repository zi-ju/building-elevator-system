package building;

import scanerzus.Request;

/**
 * This interface is used to represent a building.
 */
public interface BuildingInterface {

  /**
   * Returns the number of floors in the building.
   *
   * @return the number of floors in the building.
   */
  int getNumberOfFloors();

  /**
   * Returns the number of elevators in the building.
   *
   * @return the number of elevators in the building.
   */
  int getNumberOfElevators();

  /**
   * Returns the capacity of the elevators in the building.
   *
   * @return the capacity of the elevators in the building.
   */
  int getElevatorCapacity();

  /**
   * Returns the status of the elevator system.
   *
   * @return the status of the elevator system.
   */
  BuildingReport getElevatorSystemStatus();

  /**
   * Adds a request to the building.
   *
   * @param request the request to add.
   * @return true if the request was added, false otherwise.
   */
  boolean addRequest(Request request);

  /**
   * Starts the elevator system.
   *
   * @return true if the elevator system was started, false otherwise.
   */
  boolean startElevatorSystem();

  /**
   * Stops the elevator system.
   */
  void stopElevatorSystem();

  /**
   * Steps the elevator system.
   *
   */
  void step();
}

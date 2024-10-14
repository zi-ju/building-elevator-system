package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import elevator.ElevatorReport;
import java.util.ArrayList;
import java.util.List;
import scanerzus.Request;


/**
 * This class represents a building.
 */
public class Building implements BuildingInterface {
  private final int numberOfFloors;
  private final int numberOfElevators;
  private final int elevatorCapacity;
  private final List<Elevator> elevators;
  private final List<Request> upRequests;
  private final List<Request> downRequests;
  private ElevatorSystemStatus systemStatus;

  /**
   * The constructor for the building.
   *
   * @param numberOfFloors the number of floors in the building.
   * @param numberOfElevators the number of elevators in the building.
   * @param elevatorCapacity the capacity of the elevators in the building.
   */
  public Building(int numberOfFloors, int numberOfElevators, int elevatorCapacity)
          throws IllegalArgumentException {
    if (numberOfFloors <= 0 || numberOfElevators <= 0 || elevatorCapacity <= 0) {
      throw new IllegalArgumentException("The number of floors, elevators, "
              + "and elevator capacity must be greater than 0.");
    }
    if (numberOfFloors < 3 || numberOfFloors > 30) {
      throw new IllegalArgumentException("The number of floors must must be between 3 and 30.");
    }
    if (elevatorCapacity < 3 || elevatorCapacity > 20) {
      throw new IllegalArgumentException("The elevator capacity must be be between 3 and 20.");
    }
    this.numberOfFloors = numberOfFloors;
    this.numberOfElevators = numberOfElevators;
    this.elevatorCapacity = elevatorCapacity;
    this.elevators = new ArrayList<>();
    for (int i = 0; i < numberOfElevators; i++) {
      this.elevators.add(new Elevator(this.numberOfFloors, this.elevatorCapacity));
    }
    this.upRequests = new ArrayList<>();
    this.downRequests = new ArrayList<>();
    this.systemStatus = ElevatorSystemStatus.outOfService;
  }

  /**
   * Returns the number of floors in the building.
   *
   * @return the number of floors in the building.
   */
  @Override
  public int getNumberOfFloors() {
    return this.numberOfFloors;
  }

  /**
   * Returns the number of elevators in the building.
   *
   * @return the number of elevators in the building.
   */
  @Override
  public int getNumberOfElevators() {
    return this.numberOfElevators;
  }

  /**
   * Returns the capacity of the elevators in the building.
   *
   * @return the capacity of the elevators in the building.
   */
  @Override
  public int getElevatorCapacity() {
    return this.elevatorCapacity;
  }

  /**
   * Returns the status of the elevator system.
   *
   * @return the status of the elevator system.
   */
  @Override
  public BuildingReport getElevatorSystemStatus() {
    ElevatorReport[] elevatorReports = new ElevatorReport[this.getNumberOfElevators()];
    for (int i = 0; i < this.getNumberOfElevators(); i++) {
      elevatorReports[i] = this.elevators.get(i).getElevatorStatus();
    }
    return new BuildingReport(this.numberOfFloors, this.numberOfElevators, this.elevatorCapacity,
            elevatorReports, this.upRequests, this.downRequests, this.systemStatus);
  }

  /**
   * Adds a request to the building.
   *
   * @param request the request to add.
   * @return true if the request was added, false otherwise.
   * @throws IllegalStateException if elevator system status is not running.
   * @throws IllegalArgumentException if the request floor is not valid.
   * @throws IllegalArgumentException if the start floor is the same as the end floor.
   * @throws IllegalArgumentException if the request floor is null
   */
  @Override
  public boolean addRequest(Request request)
          throws IllegalArgumentException, IllegalStateException {
    if (request == null) {
      throw new IllegalArgumentException("The request cannot be null.");
    }
    if (this.systemStatus == ElevatorSystemStatus.outOfService
            || this.systemStatus == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException("The elevator system is not running.");
    }
    if (request.getStartFloor() < 0 || request.getStartFloor() > this.numberOfFloors - 1
            || request.getEndFloor() < 0 || request.getEndFloor() > this.numberOfFloors - 1) {
      throw new IllegalArgumentException("The request floor is not valid.");
    }
    if (request.getStartFloor() == request.getEndFloor()) {
      throw new IllegalArgumentException("The start floor and end floor must be different.");
    }
    if (request.getStartFloor() < request.getEndFloor()) {
      this.upRequests.add(request);
    } else {
      this.downRequests.add(request);
    }
    return true;
  }

  /**
   * Starts the elevator system.
   *
   * @return true if the elevator system was started, false otherwise.
   * @throws IllegalStateException if the elevator system is stopping.
   * @throws IllegalStateException if the elevator system is already running.
   */
  @Override
  public boolean startElevatorSystem() {
    if (this.systemStatus == ElevatorSystemStatus.running) {
      throw new IllegalStateException("The elevator system is already running.");
    }
    if (this.systemStatus == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException("The elevator system is stopping.");
    }
    this.systemStatus = ElevatorSystemStatus.running;
    for (Elevator elevator : this.elevators) {
      elevator.start();
    }
    return true;
  }

  /**
   * Stops the elevator system.
   *
   * @throws IllegalStateException if the elevator system is stopping.
   */
  @Override
  public void stopElevatorSystem() {
    if (this.systemStatus == ElevatorSystemStatus.stopping
            || this.systemStatus == ElevatorSystemStatus.outOfService) {
      throw new IllegalStateException("The elevator system is already stopping.");
    }
    this.systemStatus = ElevatorSystemStatus.stopping;
    this.upRequests.clear();
    this.downRequests.clear();
    for (Elevator elevator : this.elevators) {
      elevator.takeOutOfService();
    }
  }

  /**
   * Steps the elevator system.
   */
  @Override
  public void step() {
    // if the system is out of service, do nothing
    if (this.systemStatus == ElevatorSystemStatus.outOfService) {
      return;
    }
    // if the system is stopping, check if all elevators are on the ground floor
    // if yes, set the system status to out of service
    if (this.systemStatus == ElevatorSystemStatus.stopping) {
      boolean allElevatorsOnGroundFloor = true;
      for (Elevator elevator : this.elevators) {
        if (elevator.getCurrentFloor() != 0) {
          allElevatorsOnGroundFloor = false;
          break;
        }
      }
      if (allElevatorsOnGroundFloor) {
        this.systemStatus = ElevatorSystemStatus.outOfService;
      }
    }
    // if the system is running, distribute requests to elevators and step each elevator
    if (this.systemStatus == ElevatorSystemStatus.running) {
      this.distributeRequestToElevator();
    }
    // step each elevator
    for (Elevator elevator : this.elevators) {
      elevator.step();
    }
  }

  /**
   * Distributes requests to elevators.
   */
  private void distributeRequestToElevator() {
    // if there are no requests, do nothing
    if (this.upRequests.isEmpty() && this.downRequests.isEmpty()) {
      return;
    }
    // distribute requests to elevators
    for (Elevator elevator : this.elevators) {
      if (elevator.isTakingRequests()) {
        // for elevator waiting at ground floor, give it requests going up
        if (elevator.getCurrentFloor() == 0) {
          if (!this.upRequests.isEmpty()) {
            if (this.upRequests.size() > elevator.getMaxOccupancy()) {
              elevator.processRequests(this.upRequests.subList(0, elevator.getMaxOccupancy()));
              this.upRequests.subList(0, elevator.getMaxOccupancy()).clear();
            } else {
              elevator.processRequests(this.upRequests);
              this.upRequests.clear();
            }
          }
        } else if (elevator.getCurrentFloor() == this.numberOfFloors - 1) {
          // for elevator waiting at top floor, give it requests going down
          if (!this.downRequests.isEmpty()) {
            if (this.downRequests.size() > elevator.getMaxOccupancy()) {
              elevator.processRequests(this.downRequests.subList(0, elevator.getMaxOccupancy()));
              this.downRequests.subList(0, elevator.getMaxOccupancy()).clear();
            } else {
              elevator.processRequests(this.downRequests);
              this.downRequests.clear();
            }
          }
        }
      }
    }
  }
}



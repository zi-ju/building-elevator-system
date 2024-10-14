package controller;

import building.Building;
import elevator.ElevatorReport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import scanerzus.Request;
import view.BuildingView;
import view.BuildingViewImpl;



/**
 * The controller class for the building.
 */
public class BuildingControllerImpl implements ActionListener, BuildingController {
  private BuildingView view;
  private Building model;
  private BuildingControllerImpl controller;

  /**
   * Constructor for the building controller.
   *
   * @param view the building view
   * @param building the building model
   */
  public BuildingControllerImpl(BuildingView view, Building building) {
    this.view = view;
    this.model = building;
    controller = this;
    this.view.setListeners(this);
  }

  /**
   * Converts a list of requests to a string.
   *
   * @param requests the list of requests
   * @return the string representation of the requests
   */
  private String requestsToString(List<Request> requests) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < requests.size(); i++) {
      sb.append(requests.get(i).toString());
      if (i < requests.size() - 1) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }

  @Override
  public void updateView() {
    // update building status label
    this.view.updateBuildingStatus(
            this.model.getElevatorSystemStatus().getSystemStatus().toString());

    // update requests lists
    List<Request> upRequests = this.model.getElevatorSystemStatus().getUpRequests();
    List<Request> downRequests = this.model.getElevatorSystemStatus().getDownRequests();
    this.view.updateRequestsLists(requestsToString(upRequests), requestsToString(downRequests));

    // update elevator table
    ElevatorReport[] elevatorReports = this.model.getElevatorSystemStatus().getElevatorReports();
    for (int i = 0; i < elevatorReports.length; i++) {
      this.view.updateElevator(elevatorReports[i], i);
    }

    // update error message, if no error, set to default
    this.view.displayErrorMessage("");
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("Start Building")) {
      try {
        this.model.startElevatorSystem();
        controller.updateView();
      } catch (IllegalStateException ex) {
        this.view.displayErrorMessage(ex.getMessage());
      }
    } else if (e.getActionCommand().equals("Stop Building")) {
      try {
        this.model.stopElevatorSystem();
        controller.updateView();
      } catch (IllegalStateException ex) {
        this.view.displayErrorMessage(ex.getMessage());
      }
    } else if (e.getActionCommand().equals("Step Building")) {
      try {
        this.model.step();
        controller.updateView();
      } catch (IllegalStateException ex) {
        this.view.displayErrorMessage(ex.getMessage());
      }
    } else if (e.getActionCommand().equals("Submit Request")) {
      try {
        int fromFloor = Integer.parseInt(this.view.getFromFloor());
        int toFloor = Integer.parseInt(this.view.getToFloor());
        this.view.setTextFieldDefault();
        Request request = new Request(fromFloor, toFloor);
        this.model.addRequest(request);
        controller.updateView();
      } catch (NumberFormatException ex) {
        this.view.displayErrorMessage("Invalid input. Please enter a number.");
      } catch (IllegalArgumentException | IllegalStateException ex) {
        this.view.displayErrorMessage(ex.getMessage());
      }
    } else if (e.getActionCommand().equals("Change Building")) {
      try {
        int floorNumber = Integer.parseInt(this.view.getBuildingFloorNumber());
        int elevatorNumber = Integer.parseInt(this.view.getBuildingElevatorNumber());
        int elevatorCapacity = Integer.parseInt(this.view.getBuildingElevatorCapacity());
        this.model = new Building(floorNumber, elevatorNumber, elevatorCapacity);
        this.view = new BuildingViewImpl("Elevator Simulation", floorNumber, elevatorNumber);
        controller = new BuildingControllerImpl(this.view, this.model);
      } catch (NumberFormatException ex) {
        this.view.displayErrorMessage("Invalid input. Please enter a number.");
      } catch (IllegalArgumentException | IllegalStateException ex) {
        this.view.displayErrorMessage(ex.getMessage());
      }
    }
  }
}

package view;

import elevator.ElevatorReport;
import java.awt.event.ActionListener;

/**
 * This interface represents the view for the building.
 */
public interface BuildingView {

  /**
   * Sets the listeners for the building view.
   *
   * @param clicks the action listener for the building view.
   */
  void setListeners(ActionListener clicks);

  /**
   * Displays an error message.
   *
   * @param message the error message to display.
   */
  void displayErrorMessage(String message);

  /**
   * Updates the building status.
   *
   * @param status the status of the building.
   */
  void updateBuildingStatus(String status);

  /**
   * Updates the requests lists.
   *
   * @param upRequests the list of up requests.
   * @param downRequests the list of down requests.
   */
  void updateRequestsLists(String upRequests, String downRequests);

  /**
   * Updates the elevator.
   *
   * @param report the report of the elevator.
   * @param elevatorId the id number of the elevator.
   */
  void updateElevator(ElevatorReport report, int elevatorId);

  /**
   * Updates the elevator in the building table.
   *
   * @param floor the floor of the building.
   * @param elevatorId the id number of the elevator.
   * @param status the status of the elevator.
   */
  void updateElevatorInBuildingTable(int floor, int elevatorId, String status);

  /**
   * Gets the From floor.
   *
   * @return the From floor.
   */
  String getFromFloor();

  /**
   * Gets the To floor.
   *
   * @return the To floor.
   */
  String getToFloor();

  /**
   * Gets the number of building floor.
   *
   * @return the number of building floor.
   */
  String getBuildingFloorNumber();

  /**
   * Gets the number of building elevator.
   *
   * @return the number of building elevator.
   */
  String getBuildingElevatorNumber();

  /**
   * Gets the building elevator capacity.
   *
   * @return the building elevator capacity.
   */
  String getBuildingElevatorCapacity();

  /**
   * Sets the text field to default.
   */
  void setTextFieldDefault();

}

package view;

import elevator.ElevatorReport;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * This class represents the view for the building.
 */
public class BuildingViewImpl implements BuildingView {
  private final int numberOfFloors;
  private final int numberOfElevators;
  private final JFrame frame;
  private final JTable table;
  private final JLabel statusLabel;
  private final JLabel errorMessageLabel;
  private final JLabel upRequestLabel;
  private final JLabel downRequestLabel;
  private final JTextField fromFloorField;
  private final JTextField toFloorField;
  private final JTextField buildingFloorNumberField;
  private final JTextField buildingElevatorNumberField;
  private final JTextField buildingElevatorCapacityField;
  private final JButton startButton;
  private final JButton stopButton;
  private final JButton stepButton;
  private final JButton submitButton;
  private final JButton changeBuildingButton;

  /**
   * The constructor for the building view.
   *
   * @param title the title of the building view.
   * @param numberOfFloors the number of floors in the building.
   * @param numberOfElevators the number of elevators in the building.
   */
  public BuildingViewImpl(String title, int numberOfFloors, int numberOfElevators) {
    this.numberOfFloors = numberOfFloors;
    this.numberOfElevators = numberOfElevators;

    frame = new JFrame(title);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    statusLabel = new JLabel("Out of service", SwingConstants.CENTER);
    frame.add(statusLabel, BorderLayout.NORTH);

    // Table for the building
    String[][] data = new String[this.numberOfFloors][this.numberOfElevators + 1];
    for (int i = 0; i < this.numberOfFloors; i++) {
      data[i][0] = "Floor " + (this.numberOfFloors - 1 - i);
    }
    String[] columnNames = new String[this.numberOfElevators + 1];
    columnNames[0] = "";
    for (int i = 1; i <= numberOfElevators; i++) {
      columnNames[i] = "Elevator " + i;
    }
    table = new JTable(data, columnNames);
    table.setGridColor(Color.BLACK);
    table.setRowHeight(30);
    JScrollPane sp = new JScrollPane(table);
    frame.add(sp, BorderLayout.CENTER);

    // Buttons for the building
    startButton = new JButton("Start");
    startButton.setActionCommand("Start Building");
    stopButton = new JButton("Stop");
    stopButton.setActionCommand("Stop Building");
    stepButton = new JButton("Step");
    stepButton.setActionCommand("Step Building");

    // Request list display
    JPanel requestDisplayPanel = new JPanel(new BorderLayout());
    upRequestLabel = new JLabel("Up Requests: None");
    downRequestLabel = new JLabel("Down Requests: None");
    requestDisplayPanel.add(upRequestLabel, BorderLayout.NORTH);
    requestDisplayPanel.add(downRequestLabel, BorderLayout.SOUTH);

    // Control panel
    errorMessageLabel = new JLabel("");
    errorMessageLabel.setForeground(Color.RED);
    JPanel controlPanel = new JPanel();
    controlPanel.add(startButton);
    controlPanel.add(stopButton);
    controlPanel.add(stepButton);
    JPanel userPanel = new JPanel(new BorderLayout());
    userPanel.add(requestDisplayPanel, BorderLayout.NORTH);
    userPanel.add(errorMessageLabel, BorderLayout.CENTER);
    userPanel.add(controlPanel, BorderLayout.SOUTH);

    // Request
    fromFloorField = new JTextField(2);
    toFloorField = new JTextField(2);
    submitButton = new JButton("Submit");
    submitButton.setActionCommand("Submit Request");
    controlPanel.add(new JLabel("Request From:"));
    controlPanel.add(fromFloorField);
    controlPanel.add(new JLabel("To:"));
    controlPanel.add(toFloorField);
    controlPanel.add(submitButton);

    frame.add(userPanel, BorderLayout.SOUTH);

    // Change building
    JPanel changeBuildingPanel = new JPanel();
    changeBuildingPanel.setLayout(new GridLayout(20, 2));
    buildingFloorNumberField = new JTextField(2);
    buildingElevatorNumberField = new JTextField(2);
    buildingElevatorCapacityField = new JTextField(2);
    changeBuildingButton = new JButton("Change Building");
    changeBuildingButton.setActionCommand("Change Building");
    changeBuildingPanel.add(new JLabel("Floor number: "));
    changeBuildingPanel.add(buildingFloorNumberField);
    changeBuildingPanel.add(new JLabel("Elevator number: "));
    changeBuildingPanel.add(buildingElevatorNumberField);
    changeBuildingPanel.add(new JLabel("Elevator capacity: "));
    changeBuildingPanel.add(buildingElevatorCapacityField);
    changeBuildingPanel.add(changeBuildingButton);
    frame.add(changeBuildingPanel, BorderLayout.EAST);

    frame.setSize(1200, 600);
  }

  @Override
  public void setListeners(ActionListener clicks) {
    this.startButton.addActionListener(clicks);
    this.stopButton.addActionListener(clicks);
    this.stepButton.addActionListener(clicks);
    this.submitButton.addActionListener(clicks);
    this.changeBuildingButton.addActionListener(clicks);
  }

  @Override
  public void displayErrorMessage(String message) {
    this.errorMessageLabel.setText(message);
  }

  @Override
  public void updateBuildingStatus(String status) {
    this.statusLabel.setText(status);
  }

  @Override
  public void updateRequestsLists(String upRequests, String downRequests) {
    this.upRequestLabel.setText("Up Requests: " + upRequests);
    this.downRequestLabel.setText("Down Requests: " + downRequests);
  }

  @Override
  public void updateElevator(ElevatorReport report, int elevatorId) {
    StringBuilder status = new StringBuilder();
    status.append(report.getDirection().toString());
    if (report.getFloorRequests() != null) {
      for (int i = 0; i < report.getFloorRequests().length; i++) {
        if (report.getFloorRequests()[i]) {
          status.append(i).append(" ");
        }
      }
    }
    if (!report.isDoorClosed()) {
      status.append("\n" + "Door Open ");
      status.append(report.getDoorOpenTimer());
    }
    if (report.getEndWaitTimer() > 0) {
      status.append("\n" + "Wait: ").append(report.getEndWaitTimer());
    }
    if (report.isOutOfService()) {
      status.setLength(0);
      status.append("Out of Service");
    }
    int currentFloor = report.getCurrentFloor();
    updateElevatorInBuildingTable(currentFloor, elevatorId, status.toString());
  }

  @Override
  public void updateElevatorInBuildingTable(int floor, int elevatorId, String status) {
    for (int i = 0; i < this.numberOfFloors; i++) {
      if (i == (this.numberOfFloors - 1 - floor)) {
        table.setValueAt(status, i, elevatorId + 1);
      } else {
        table.setValueAt("", i, elevatorId + 1);
      }
    }
  }

  @Override
  public String getFromFloor() {
    return fromFloorField.getText();
  }

  @Override
  public String getToFloor() {
    return toFloorField.getText();
  }

  @Override
  public String getBuildingFloorNumber() {
    return buildingFloorNumberField.getText();
  }

  @Override
  public String getBuildingElevatorNumber() {
    return buildingElevatorNumberField.getText();
  }

  @Override
  public String getBuildingElevatorCapacity() {
    return buildingElevatorCapacityField.getText();
  }

  @Override
  public void setTextFieldDefault() {
    fromFloorField.setText("");
    toFloorField.setText("");
  }


}

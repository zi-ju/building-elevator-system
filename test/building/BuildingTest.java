package building;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import building.enums.Direction;
import building.enums.ElevatorSystemStatus;
import elevator.ElevatorReport;
import org.junit.Before;
import org.junit.Test;
import scanerzus.Request;

/**
 * Test the Building class.
 */
public class BuildingTest {
  private Building building1;
  private Building building2;
  private Building building3;

  /**
   * Set up the building objects.
   */
  @Before
  public void setUp() {
    building1 = new Building(11, 1, 3);
    building2 = new Building(5, 3, 3);
    building3 = new Building(5, 1, 3);
  }

  /**
   * Test the Building constructor.
   */
  @Test
  public void testBuildingConstructor() {
    Building building = new Building(11, 8, 3);
    assertEquals(11, building.getNumberOfFloors());
    assertEquals(8, building.getNumberOfElevators());
    assertEquals(3, building.getElevatorCapacity());
  }

  /**
   * Test the Building constructor throws exception with floors less than 3.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBuildingConstructorFloorsLessThan3() {
    new Building(1, 8, 3);
  }

  /**
   * Test the Building constructor throws exception,
   * with floors greater than 30.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBuildingConstructorFloorsGreaterThan30() {
    new Building(31, 8, 3);
  }

  /**
   * Test the Building constructor throws exception,
   * with negative floors.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBuildingConstructorFloorsNegative() {
    new Building(-1, 8, 3);
  }

  /**
   * Test the Building constructor throws exception,
   * with elevator capacity less than 3.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBuildingConstructorElevatorCapacityLessThan3() {
    new Building(11, 8, 2);
  }

  /**
   * Test the Building constructor throws exception,
   * with elevator capacity greater than 20.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBuildingConstructorElevatorCapacityGreaterThan20() {
    new Building(11, 8, 21);
  }

  /**
   * Test the Building constructor throws exception,
   * with negative elevator capacity.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBuildingConstructorElevatorCapacityNegative() {
    new Building(11, 8, -1);
  }

  /**
   * Test the Building constructor throws exception,
   * with negative elevators.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBuildingConstructorElevatorsNegative() {
    new Building(11, -1, 3);
  }

  /**
   * Test the getNumberOfFloors method.
   */
  @Test
  public void testGetNumberOfFloors() {
    assertEquals(11, building1.getNumberOfFloors());
  }

  /**
   * Test the getNumberOfElevators method.
   */
  @Test
  public void testGetNumberOfElevators() {
    assertEquals(1, building1.getNumberOfElevators());
  }

  /**
   * Test the getElevatorCapacity method.
   */
  @Test
  public void testGetElevatorCapacity() {
    assertEquals(3, building1.getElevatorCapacity());
  }

  /**
   * Test the getElevatorSystemStatus method when elevator system is out of service.
   */
  @Test
  public void testGetElevatorSystemStatusOutOfService() {
    assertEquals(ElevatorSystemStatus.outOfService,
            building1.getElevatorSystemStatus().getSystemStatus());
    assertEquals(0, building1.getElevatorSystemStatus().getUpRequests().size());
    assertEquals(0, building1.getElevatorSystemStatus().getDownRequests().size());
    assertEquals(1, building1.getElevatorSystemStatus().getNumElevators());
    assertEquals(1, building1.getElevatorSystemStatus().getElevatorReports().length);
  }


  /**
   * Test the getElevatorSystemStatus method when elevator system is running.
   */
  @Test
  public void testGetElevatorSystemStatusRunning() {
    building1.startElevatorSystem();
    assertEquals(ElevatorSystemStatus.running,
            building1.getElevatorSystemStatus().getSystemStatus());
    assertEquals(0, building1.getElevatorSystemStatus().getUpRequests().size());
    assertEquals(0, building1.getElevatorSystemStatus().getDownRequests().size());
    assertEquals(1, building1.getElevatorSystemStatus().getNumElevators());
    assertEquals(1, building1.getElevatorSystemStatus().getElevatorReports().length);
  }

  /**
   * Test the getElevatorSystemStatus method when elevator system is stopping.
   */
  @Test
  public void testGetElevatorSystemStatusStopping() {
    building1.startElevatorSystem();
    building1.stopElevatorSystem();
    assertEquals(ElevatorSystemStatus.stopping,
            building1.getElevatorSystemStatus().getSystemStatus());
    assertEquals(0, building1.getElevatorSystemStatus().getUpRequests().size());
    assertEquals(0, building1.getElevatorSystemStatus().getDownRequests().size());
    assertEquals(1, building1.getElevatorSystemStatus().getNumElevators());
    assertEquals(1, building1.getElevatorSystemStatus().getElevatorReports().length);
  }


  /**
   * Test the addRequest method throws exception,
   * if elevator system status is not running.
   */
  @Test(expected = IllegalStateException.class)
  public void testAddRequestNotRunning() {
    building1.addRequest(new Request(1, 2));
  }

  /**
   * Test the addRequest method throws exception,
   * if request is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestNullRequest() {
    building1.startElevatorSystem();
    building1.addRequest(null);
  }

  /**
   * Test the addRequest method throws exception,
   * if request start floor is less than 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestStartFloorLessThan0() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(-1, 2));
  }

  /**
   * Test the addRequest method throws exception,
   * if request start floor is greater than number of floors - 1.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestStartFloorGreaterThanFloors() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(11, 2));
  }

  /**
   * Test the addRequest method throws exception,
   * if request end floor is less than 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestEndFloorLessThan0() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(1, -1));
  }

  /**
   * Test the addRequest method throws exception,
   * if request end floor is greater than number of floors - 1.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestEndFloorGreaterThanFloors() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(1, 11));
  }

  /**
   * Test the addRequest method throws exception,
   * if request start floor is the same as end floor.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestStartFloorEqualsEndFloor() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(1, 1));
  }

  /**
   * Test the addRequest method,
   * with go up request.
   */
  @Test
  public void testAddRequestGoUp() {
    building1.startElevatorSystem();
    assertTrue(building1.addRequest(new Request(1, 2)));
    assertEquals(1, building1.getElevatorSystemStatus().getUpRequests().size());
    assertEquals(0, building1.getElevatorSystemStatus().getDownRequests().size());
  }

  /**
   * Test the addRequest method,
   * with go down request.
   */
  @Test
  public void testAddRequestGoDown() {
    building1.startElevatorSystem();
    assertTrue(building1.addRequest(new Request(2, 1)));
    assertEquals(0, building1.getElevatorSystemStatus().getUpRequests().size());
    assertEquals(1, building1.getElevatorSystemStatus().getDownRequests().size());
  }

  /**
   * Test the addRequest method,
   * with multiple requests.
   */
  @Test
  public void testAddRequestMultiple() {
    building1.startElevatorSystem();
    assertTrue(building1.addRequest(new Request(1, 2)));
    assertTrue(building1.addRequest(new Request(2, 1)));
    assertTrue(building1.addRequest(new Request(3, 7)));
    assertTrue(building1.addRequest(new Request(4, 8)));
    assertTrue(building1.addRequest(new Request(7, 2)));
    assertEquals(3, building1.getElevatorSystemStatus().getUpRequests().size());
    assertEquals("1->2", building1.getElevatorSystemStatus().getUpRequests().get(0).toString());
    assertEquals("3->7", building1.getElevatorSystemStatus().getUpRequests().get(1).toString());
    assertEquals("4->8", building1.getElevatorSystemStatus().getUpRequests().get(2).toString());
    assertEquals(2, building1.getElevatorSystemStatus().getDownRequests().size());
    assertEquals("2->1", building1.getElevatorSystemStatus().getDownRequests().get(0).toString());
    assertEquals("7->2", building1.getElevatorSystemStatus().getDownRequests().get(1).toString());
  }

  /**
   * Test the startElevatorSystem method throws exception,
   * if elevator system status is already running.
   */
  @Test(expected = IllegalStateException.class)
  public void testStartElevatorSystemAlreadyRunning() {
    building1.startElevatorSystem();
    building1.startElevatorSystem();
  }

  /**
   * Test the startElevatorSystem method throws exception,
   * if elevator system status is stopping.
   */
  @Test(expected = IllegalStateException.class)
  public void testStartElevatorSystemStopping() {
    building1.startElevatorSystem();
    building1.stopElevatorSystem();
    building1.startElevatorSystem();
  }

  /**
   * Test the startElevatorSystem method.
   */
  @Test
  public void testStartElevatorSystem() {
    assertTrue(building1.startElevatorSystem());
    assertEquals(ElevatorSystemStatus.running,
            building1.getElevatorSystemStatus().getSystemStatus());
    BuildingReport buildingReport = building1.getElevatorSystemStatus();
    ElevatorReport elevatorReport = buildingReport.getElevatorReports()[0];
    assertFalse(elevatorReport.isOutOfService());
  }

  /**
   * Test the stopElevatorSystem method throws exception,
   * if elevator system status is stopping.
   */
  @Test(expected = IllegalStateException.class)
  public void testStopElevatorSystemStopping() {
    building1.startElevatorSystem();
    building1.stopElevatorSystem();
    building1.stopElevatorSystem();
  }

  /**
   * Test the stopElevatorSystem method throws exception,
   * if elevator system status is out of service.
   */
  @Test(expected = IllegalStateException.class)
  public void testStopElevatorSystemOutOfService() {
    building1.stopElevatorSystem();
  }

  /**
   * Test the stopElevatorSystem method.
   */
  @Test
  public void testStopElevatorSystem() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(1, 2));
    building1.addRequest(new Request(2, 3));
    building1.addRequest(new Request(2, 1));
    building1.step();
    building1.step();
    building1.stopElevatorSystem();
    building1.step();
    building1.step();
    assertEquals(ElevatorSystemStatus.stopping,
            building1.getElevatorSystemStatus().getSystemStatus());
    assertEquals(0, building1.getElevatorSystemStatus().getUpRequests().size());
    assertEquals(0, building1.getElevatorSystemStatus().getDownRequests().size());
    BuildingReport buildingReport = building1.getElevatorSystemStatus();
    ElevatorReport elevatorReport = buildingReport.getElevatorReports()[0];
    assertTrue(elevatorReport.isOutOfService());
    assertFalse(elevatorReport.isDoorClosed());
  }

  /**
   * Helper function to get a specific elevator report.
   */
  private ElevatorReport getElevatorReportHelper(Building building, int elevatorIndex) {
    BuildingReport buildingReport = building.getElevatorSystemStatus();
    return buildingReport.getElevatorReports()[elevatorIndex];
  }

  /**
   * Test the stepElevatorSystem method,
   * when elevator system is running.
   */
  @Test
  public void testStepSystemRunning() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(1, 2));
    building1.addRequest(new Request(2, 3));
    // step 1: go up
    building1.step();
    assertEquals(1, getElevatorReportHelper(building1, 0).getCurrentFloor());
    assertEquals(Direction.UP, getElevatorReportHelper(building1, 0).getDirection());
    // step 2: door open
    building1.step();
    assertEquals(1, getElevatorReportHelper(building1, 0).getCurrentFloor());
    assertFalse(getElevatorReportHelper(building1, 0).isDoorClosed());
    // step 3: door open
    building1.step();
    assertEquals(1, getElevatorReportHelper(building1, 0).getCurrentFloor());
    assertFalse(getElevatorReportHelper(building1, 0).isDoorClosed());
    // step 4: door open
    building1.step();
    assertEquals(1, getElevatorReportHelper(building1, 0).getCurrentFloor());
    assertFalse(getElevatorReportHelper(building1, 0).isDoorClosed());
    // step 5: door close
    building1.step();
    assertEquals(1, getElevatorReportHelper(building1, 0).getCurrentFloor());
    assertTrue(getElevatorReportHelper(building1, 0).isDoorClosed());
    // step 6: go up
    building1.step();
    assertEquals(2, getElevatorReportHelper(building1, 0).getCurrentFloor());
    assertEquals(Direction.UP, getElevatorReportHelper(building1, 0).getDirection());
  }

  /**
   * Test the stepElevatorSystem method,
   * when elevator system is out of service.
   */
  @Test
  public void testStepSystemOutOfService() {
    building1.step();
    assertEquals(0, getElevatorReportHelper(building1, 0).getCurrentFloor());
    assertTrue(getElevatorReportHelper(building1, 0).isOutOfService());
  }

  /**
   * Test the stepElevatorSystem method,
   * when elevator system is stopping.
   */
  @Test
  public void testStepSystemStopping() {
    building1.startElevatorSystem();
    building1.addRequest(new Request(1, 2));
    building1.addRequest(new Request(2, 3));
    // step 1: go up to floor 1
    building1.step();
    // step 2: door open
    building1.step();
    // step 3: door open
    building1.step();
    // step 4: door open
    building1.step();
    // step 5: door close
    building1.step();
    // step 6: go up to floor 2
    building1.step();
    // step 7: door open
    building1.step();
    // step 8: door open
    building1.step();
    // step 9: door open
    building1.step();
    // step 10: door close
    building1.step();
    // stop the system
    building1.stopElevatorSystem();
    // step 11: go down to floor 1
    building1.step();
    // step 12: go down to floor 0
    building1.step();
    // step 13: door open
    building1.step();
    assertEquals(0, getElevatorReportHelper(building1, 0).getCurrentFloor());
    assertEquals(Direction.STOPPED, getElevatorReportHelper(building1, 0).getDirection());
    assertFalse(getElevatorReportHelper(building1, 0).isDoorClosed());
    assertEquals(ElevatorSystemStatus.outOfService,
            building1.getElevatorSystemStatus().getSystemStatus());
  }

  /**
   * Test the request allocation.
   * One is taking up request, another is taking down request.
   */
  @Test
  public void testRequestAllocationUpAndDown() {
    building2.startElevatorSystem();
    building2.addRequest(new Request(1, 2));
    building2.addRequest(new Request(3, 4));
    building2.addRequest(new Request(4, 2));
    building2.addRequest(new Request(3, 1));
    building2.step();
    building2.step();
    assertFalse(getElevatorReportHelper(building2, 0).isDoorClosed());
    assertTrue(getElevatorReportHelper(building2, 1).isDoorClosed());
    assertTrue(getElevatorReportHelper(building2, 2).isDoorClosed());
    building2.step();
    building2.step();
    building2.step();
    building2.step();
    building2.step();
    assertFalse(getElevatorReportHelper(building2, 0).isDoorClosed());
    assertTrue(getElevatorReportHelper(building2, 1).isDoorClosed());
    assertTrue(getElevatorReportHelper(building2, 2).isDoorClosed());
    building2.step();
    building2.step();
    building2.step();
    building2.step();
    building2.step();
    assertFalse(getElevatorReportHelper(building2, 0).isDoorClosed());
    assertFalse(getElevatorReportHelper(building2, 1).isDoorClosed());
    assertTrue(getElevatorReportHelper(building2, 2).isDoorClosed());
  }

  /**
   * Test the request allocation when reaching max capacity.
   */
  @Test
  public void testRequestAllocationMaxCapacity() {
    building2.startElevatorSystem();
    building2.addRequest(new Request(1, 2));
    building2.addRequest(new Request(1, 3));
    building2.addRequest(new Request(2, 3));
    building2.addRequest(new Request(3, 4));
    building2.step();
    building2.step();
    assertFalse(getElevatorReportHelper(building2, 0).isDoorClosed());
    assertTrue(getElevatorReportHelper(building2, 1).isDoorClosed());
    building2.step();
    building2.step();
    assertFalse(getElevatorReportHelper(building2, 0).isDoorClosed());
    assertFalse(getElevatorReportHelper(building2, 1).isDoorClosed());
  }

  /**
   * Test one elevator handles one request.
   */
  @Test
  public void testOneElevatorOneRequest() {
    building3.startElevatorSystem();
    building3.addRequest(new Request(1, 2));
    building3.step();
    building3.step();
    assertFalse(getElevatorReportHelper(building3, 0).isDoorClosed());
    building3.step();
    building3.step();
    building3.step();
    assertTrue(getElevatorReportHelper(building3, 0).isDoorClosed());
  }

}
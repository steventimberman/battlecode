package playerbbbbb;
import battlecode.common.*;
import java.util.ArrayList;

/**
* For static methods that miners will use.
* Each should (most likely) take an rc as input
**/

public class MinerRobot {
  static Helpers helper;
  static RobotController rc;
  static Direction[] directions;
  static RobotType[] spawnedByMiner;
  static MapLocation HQMapLoc, currentLocation;
  static MapLocation refineryLocation;
  static WalkieTalkie walkie;
  static ArrayList<ArrayList<Integer>> toDo;
  static Direction dirToHQ;

  static boolean vaporatorIsMade;

  public MinerRobot(Helpers help) throws GameActionException {
    helper = help;
    vaporatorIsMade = false;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
    walkie = new WalkieTalkie(helper);
    toDo = new ArrayList<ArrayList<Integer>>();
    currentLocation = rc.getLocation();
    dirToHQ = Direction.NORTH;
  }

  public void runMiner() throws GameActionException {

    searchForTasks();

    tryRefineAlways();

    if (rc.getSoupCarrying()==RobotType.MINER.soupLimit) {
        goToRefinery();
    } else {
        for (Direction dir : directions)
            if (helper.tryMine(dir))
                System.out.println("I mined!");
        helper.tryMove(helper.randomDirection());
    }
  }

  public void searchForTasks() throws GameActionException {
    // Read new messages for tasks. If it isnt completed, move task to toDo.
    ArrayList<ArrayList<Integer>> newMessages = walkie.readBlockchain();
    System.out.print("newMessages: ");
    System.out.println(newMessages);

    for (int i=0; i < newMessages.size(); i++){
        ArrayList<Integer> newTasks = newMessages.get(i);
        int taskType = newTasks.get(0);
        int xLoc = newTasks.get(1);
        int yLoc =  newTasks.get(2);
        MapLocation senderLocation = new MapLocation(xLoc, yLoc);
        doTask(true, taskType, senderLocation);
    }

    System.out.print("toDo: ");
    System.out.println(toDo);
    // Search toDo for tasks. If it is completed, remove.
    for (int i=0; i < toDo.size(); i++){
        ArrayList<Integer> tasksToDo = toDo.get(i);
        int taskTypeToDo = tasksToDo.get(0);
        int xLocToDo = tasksToDo.get(1);
        int yLocToDo = tasksToDo.get(2);
        MapLocation senderLocationTodo = new MapLocation(xLocToDo, yLocToDo);
        if (doTask(false, taskTypeToDo, senderLocationTodo)){
            toDo.remove(i);
            i--;
        }
    }

  }

  public boolean doTask(boolean newTask, int taskType, MapLocation senderLoc) throws GameActionException {
    boolean success = false;
    if ( ((taskType == 0) && tryMakeBuilding(RobotType.VAPORATOR))
        || ((taskType == 2) && tryMakeBuilding(RobotType.REFINERY))
        || ((taskType == 4) && tryMakeBuilding(RobotType.DESIGN_SCHOOL))
        )
    {

        success = true;
        System.out.print("success: ");
        System.out.println(success);
    }
    else if (taskType == 3) {
        refineryLocation = senderLoc;
        success = true;
        System.out.println("The new Map Location is:");
        System.out.println(refineryLocation);
    }

    if ( (success==false) && newTask){
        ArrayList<Integer> newToDo = new ArrayList<Integer>();
        newToDo.add(taskType);
        newToDo.add(senderLoc.x);
        newToDo.add(senderLoc.y);
        toDo.add(newToDo);
    }

    return success;

  }

  public void findHQInit() throws GameActionException {
    RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
    for (RobotInfo robot : nearbyRobots)
        if (robot.type.equals(RobotType.HQ)) {
            HQMapLoc = robot.location;
            refineryLocation = robot.location;
            System.out.println("I've found HQ!");
        }

  }


  public void tryRefineAlways() throws GameActionException {
    MapLocation currentLocation = rc.getLocation();
    Direction dirToHQ = currentLocation.directionTo(refineryLocation);
    if (helper.tryRefine(dirToHQ))
        System.out.println("I refined soup! " + rc.getTeamSoup());
  }

  public void goToRefinery() throws GameActionException {
    System.out.println("GOING BACKKK!!");
    updatePosition();
    if (helper.tryMove(dirToHQ) == false) {
        if (helper.tryRefine(dirToHQ)==false){
          helper.tryMove(helper.randomDirection());
      }
    }
  }

  public boolean tryMakeBuilding(RobotType robotTypeToBuild) throws GameActionException{
    for (Direction dir : directions){
        if (helper.tryBuild(robotTypeToBuild, dir)){
            System.out.println("MADE BUILDING");
            return true;
        }
    }
    return false;
  }

  public void updatePosition() throws GameActionException {
    currentLocation = rc.getLocation();
    dirToHQ = currentLocation.directionTo(HQMapLoc);
  }

}

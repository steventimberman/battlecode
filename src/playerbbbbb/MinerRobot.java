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
  static MapLocation HQMapLoc;
  static MapLocation refineryLocation;
  static WalkieTalkie walkie;
  static ArrayList<ArrayList<Integer>> toDo;

  static boolean vaporatorIsMade;

  public MinerRobot(Helpers help) throws GameActionException {
    helper = help;
    vaporatorIsMade = false;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
    walkie = new WalkieTalkie(helper);
    toDo = new ArrayList<ArrayList<Integer>>();
  }

  public void runMiner() throws GameActionException {

    searchForTasks();

    tryRefineAlways();

    if (rc.getSoupCarrying()==RobotType.MINER.soupLimit) {
        if (refineryLocation != null)
            goToRefinery();
        else
            helper.tryMove(helper.randomDirection());
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
    for (int i=0; i < newMessages.size(); i++){
        ArrayList<Integer> newTasks = newMessages.get(i);
        int taskType = newTasks.get(0);
        int xLoc = newTasks.get(1);
        int yLoc =  newTasks.get(2);
        MapLocation senderLocation = new MapLocation(xLoc, yLoc);
        doTask(true, taskType, senderLocation);
    }

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
    if (taskType == 0){
        success = tryMakeBuilding(RobotType.VAPORATOR);
    } else if (taskType == 2) {
        success = tryMakeBuilding(RobotType.REFINERY);
    } else if (taskType == 3) {
        refineryLocation = senderLoc;
        success = true;
    }

    if (!success && newTask){
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
        if (robot.type == RobotType.HQ) {
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
    MapLocation currentLocation = rc.getLocation();
    Direction dirToHQ = currentLocation.directionTo(refineryLocation);
    System.out.println("GOING BACKKK!!");
    while (helper.tryMove(dirToHQ)) {
        currentLocation = rc.getLocation();
        dirToHQ = currentLocation.directionTo(refineryLocation);
        break;
    }
    helper.tryRefine(dirToHQ);
  }

  public boolean tryMakeBuilding(RobotType robotTypeToBuild) throws GameActionException{
    for (Direction dir : directions)
        if (helper.tryBuild(robotTypeToBuild, dir)){
            System.out.println("MADE VAPORATOR");
            return true;
        }
    return false;
  }

}

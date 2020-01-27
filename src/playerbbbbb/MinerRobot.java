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
  static WalkieTalkie walkie;
  static ArrayList<Integer> toDo;

  static boolean vaporatorIsMade;

  public MinerRobot(Helpers help) throws GameActionException {
    helper = help;
    vaporatorIsMade = false;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
    walkie = new WalkieTalkie(helper);
    toDo = new ArrayList<Integer>();
  }

  public void runMiner(int turnCount) throws GameActionException {

    searchForTasks();

    findHQ();

    if (rc.getSoupCarrying()==RobotType.MINER.soupLimit) {
        if (HQMapLoc != null)
            goToHQ();
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
    ArrayList<Integer> newMessages = walkie.readBlockchain();
    for (int i=0; i < newMessages.size(); i++){
        int taskType = newMessages.get(i);
        doTask(true, taskType);
    }

    // Search toDo for tasks. If it is completed, remove.
    for (int i=0; i < toDo.size(); i++){
        int taskType = toDo.get(i);
        if (doTask(false, taskType)){
            toDo.remove(i);
            i--;
        }
    }

  }

  public boolean doTask(boolean newTask, int taskType) throws GameActionException {
    boolean success = false;
    if (taskType == 0){
        success = tryMakeVaporator();
    }

    if (!success && newTask)
        toDo.add(taskType);

    return success;
  }

  public void findHQ() throws GameActionException {
    if (HQMapLoc == null){
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
        for (RobotInfo robot : nearbyRobots)
            if (robot.type == RobotType.HQ) {
                HQMapLoc = robot.location;
                System.out.println("I've found HQ!");
            }
    }else{
        MapLocation currentLocation = rc.getLocation();
        Direction dirToHQ = currentLocation.directionTo(HQMapLoc);
        if (helper.tryRefine(dirToHQ))
            System.out.println("I refined soup! " + rc.getTeamSoup());
    }

  }

  public void goToHQ() throws GameActionException {
    MapLocation currentLocation = rc.getLocation();
    Direction dirToHQ = currentLocation.directionTo(HQMapLoc);
    System.out.println("GOING BACKKK!!");
    while (helper.tryMove(dirToHQ)) {
        currentLocation = rc.getLocation();
        dirToHQ = currentLocation.directionTo(HQMapLoc);
        break;
    }
    helper.tryRefine(dirToHQ);
  }

  public boolean tryMakeVaporator() throws GameActionException{
    for (Direction dir : directions)
        if (helper.tryBuild(RobotType.VAPORATOR, dir)){
            System.out.println("MADE VAPORATOR");
            return true;
        }
    return false;
  }

}

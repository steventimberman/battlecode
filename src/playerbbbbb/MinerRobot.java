package playerbbbbb;
import battlecode.common.*;

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

  public MinerRobot(Helpers help) {
    helper = help;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
  }

  public void runMiner(int turnCount) throws GameActionException {
    helper.tryBlockchain(turnCount);
    if (HQMapLoc == null){
        findHQ();
    } else {
        MapLocation currentLocation = rc.getLocation();
        Direction dirToHQ = currentLocation.directionTo(HQMapLoc);
        if (helper.tryRefine(dirToHQ))
            System.out.println("I refined soup! " + rc.getTeamSoup());
    }


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

  public void findHQ() throws GameActionException {
    RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
    for (RobotInfo robot : nearbyRobots)
        if (robot.type == RobotType.HQ) {
            HQMapLoc = robot.location;
            System.out.println("I've found HQ!");
        }

  }

  public void goToHQ() throws GameActionException {
    MapLocation currentLocation = rc.getLocation();
    Direction dirToHQ = currentLocation.directionTo(HQMapLoc);
    System.out.println("GOING BACKKK!!");
    helper.tryMove(dirToHQ);
    while (helper.tryMove(dirToHQ)) {
        currentLocation = rc.getLocation();
        dirToHQ = currentLocation.directionTo(HQMapLoc);
        break;
    }
  }

}

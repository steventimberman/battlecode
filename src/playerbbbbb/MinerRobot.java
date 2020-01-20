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
  static WalkieTalkie walkie;

  static boolean vaporatorIsMade;

  public MinerRobot(Helpers help) {
    helper = help;
    vaporatorIsMade = false;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
    walkie = new WalkieTalkie(helper);
  }

  public void runMiner(int turnCount) throws GameActionException {
    helper.tryBlockchain(turnCount);

    if (!vaporatorIsMade){
        boolean received = walkie.receiveVaporatorMade();
        if (received){
            vaporatorIsMade = true;
        }
    }

    if (!vaporatorIsMade){
        vaporatorProcess();
    }

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

  public void vaporatorProcess() throws GameActionException{
    int soupTotal = rc.getTeamSoup();
    if (soupTotal > 500){
        boolean makeVaporator = walkie.receiveMakeVaporator();
        System.out.println("Made it to vapor");
        if (makeVaporator)
            if (tryMakeVaporator()){
                vaporatorIsMade = true;
                walkie.sendVaporatorMade();
            }
    }
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

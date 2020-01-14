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

  public MinerRobot(Helpers help) {
    helper = help;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
  }

  public static void sayHi() {
    System.out.println("I'm a miner robot!");
  }

  public void runMiner(int turnCount) throws GameActionException{
    helper.tryBlockchain(turnCount);
    RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
    // for (RobotInfo robot : nearbyRobots)
    //     if (robot.type == RobotType.HQ)
    //         HQMapLoc = robot.location;


    // tryBuild(randomSpawnedByMiner(), randomDirection());

    for (Direction dir : directions)
        if (helper.tryMine(dir))
            System.out.println("I mined!");
    // for (Direction dir : directions)
    //     tryBuild(randomSpawnedByMiner(), dir);
    for (Direction dir : directions)
        if (helper.tryRefine(dir))
            System.out.println("I refined soup! " + rc.getTeamSoup());
    helper.tryMove(helper.randomDirection());
  }

}

package playerbbbbb;
import battlecode.common.*;

/**
* For static methods that HQ will use.
* Each should (most likely) take an rc as input
**/

public class HQRobot {

  static Helpers helper;
  static RobotController rc;
  static Direction[] directions;
  static RobotType[] spawnedByMiner;
  static MapLocation HQMapLoc;

  public HQRobot(Helpers help) {
    helper = help;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
  }

  public void runHQ(int turnCount) throws GameActionException {
    if (turnCount < 250 || turnCount%45 == 0)
        for (Direction dir : directions)
            helper.tryBuild(RobotType.MINER, dir);
  }


}

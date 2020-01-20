package playerbbbbb;
import battlecode.common.*;

public class NetGunRobot {
  static Helpers helper;
  static RobotController rc;
  static Direction[] directions;
  static MapLocation HQMapLoc;
  static WalkieTalkie walkie;

  public NetGunRobot(Helpers help) {
        helper = help;
        rc = helper.rc;
        directions = helper.directions;
        walkie = new WalkieTalkie(helper);
    }

  public void tryShooting() throws GameActionException{
    int enemyID = 0;
    Team enemy = rc.getTeam().opponent();
    //-1 uses full vision and enemy makes sure all robots sense are on the other team
    RobotInfo[] nearbyRobots = rc.senseNearbyRobots(-1,enemy);
    for (RobotInfo robot: nearbyRobots){
      //If a delivery drone is found in the list of enemy robots shoot it
        if (robot.type == RobotType.DELIVERY_DRONE){
            enemyID = robot.ID;
            if (rc.canShootUnit(enemyID))
              rc.shootUnit(enemyID);
        }
    }

  }

}

package playerbbbbb;
import battlecode.common.*;

public class NetGunRobot {
  static Helpers helper;
  static RobotController rc;
  static Direction[] directions;
  static MapLocation HQMapLoc;

  public NetGunRobot(Helpers help) {
        helper = help;
        rc = helper.rc;
        directions = helper.directions;
        walkie = new WalkieTalkie(helper);
    }

  public void tryShooting() throws GameActionException{
  	boolean enemyFound = false;
  	int enemyID = 0;
    Team enemy = rc.getTeam().opponent();
    RobotInfo[] nearbyRobots = rc.senseNearbyRobots(-1,enemy);
    for (RobotInfo robot: nearbyRobots){
        if (robot.type == RobotType.DELIVERY_DRONE){
            enemyID = robot.ID;
            if rc.canShootUnit(int enemyID){
    			rc.shootUnit(int enemyID);
    		}
        }
    }

  }

}
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
  	int enemyID = 0;
    Team enemy = rc.getTeam().opponent();
    //-1 sets the search radius to the max for that robot type
    RobotInfo[] nearbyRobots = rc.senseNearbyRobots(-1,enemy);
    //This list should have only robots on the enemy team
    for (RobotInfo robot: nearbyRobots){
        if (robot.type == RobotType.DELIVERY_DRONE){
        	//Stores the id of the delivery drone to shoot and tries to shoot it
            enemyID = robot.ID;
            if rc.canShootUnit(int enemyID){
    			rc.shootUnit(int enemyID);
    		}
        }
    }

  }

}
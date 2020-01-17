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
    static int minerRobotID = -1;
    static WalkieTalkie walkie;


    public HQRobot(Helpers help) {
        helper = help;
        rc = helper.rc;
        directions = helper.directions;
        spawnedByMiner = helper.spawnedByMiner;
        walkie = new WalkieTalkie(helper);
    }

    public void runHQ(int turnCount) throws GameActionException {
        if ((turnCount < 50) && (turnCount > 45) && (minerRobotID == -1))
            findMiner();
        if (turnCount < 230 || turnCount%50 == 0)
            for (Direction dir : directions)
                helper.tryBuild(RobotType.MINER, dir);
        if (turnCount < 430 && turnCount > 420)
            walkie.sendMakeVaporator(minerRobotID);
    }

    public void findMiner() throws GameActionException{
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
        for (RobotInfo robot: nearbyRobots){
            if (robot.type == RobotType.MINER){
                minerRobotID = robot.ID;
                break;
            }

        }
    }


}

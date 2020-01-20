package playerbbbbb;
import battlecode.common.*;
import java.util.ArrayList;

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
    static ArrayList minerRobotIDs = new ArrayList<Integer>();;
    static WalkieTalkie walkie;


    public HQRobot(Helpers help) {
        helper = help;
        rc = helper.rc;
        directions = helper.directions;
        spawnedByMiner = helper.spawnedByMiner;
        walkie = new WalkieTalkie(helper);

    }

    public void runHQ(int turnCount) throws GameActionException {
        if (turnCount < 230 || turnCount%50 == 0)
            for (Direction dir : directions){
                makeNewMiner(dir);
            }
        if (turnCount < 430 && turnCount > 420)
            walkie.sendMakeVaporator((int) minerRobotIDs.get(0));
    }

    public void makeNewMiner(Direction dir) throws GameActionException{
        if (helper.tryBuild(RobotType.MINER, dir)){
            MapLocation newRobotLocation = rc.adjacentLocation(dir);
            RobotInfo newRobot = rc.senseRobotAtLocation(newRobotLocation);
            minerRobotIDs.add(newRobot.ID);
        }
    }

}

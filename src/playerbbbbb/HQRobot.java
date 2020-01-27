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
    static ArrayList<Integer> minerRobotIDs = new ArrayList<Integer>();
    static WalkieTalkie walkie;
    static boolean vaporatorMessageSent;


    public HQRobot(Helpers help) throws GameActionException {
        helper = help;
        rc = helper.rc;
        directions = helper.directions;
        spawnedByMiner = helper.spawnedByMiner;
        walkie = new WalkieTalkie(helper);
        vaporatorMessageSent = false;

    }

    public void runHQ(int turnCount) throws GameActionException {
        if (minerRobotIDs.size() < 25)
            for (Direction dir : directions){
                makeNewMiner(dir);
            }
        int soupTotal = rc.getTeamSoup();
        if (soupTotal > 500 && !vaporatorMessageSent && tryMakeVaporator(3)){
            vaporatorMessageSent = true;
        }
    }

    public void makeNewMiner(Direction dir) throws GameActionException {
        if (helper.tryBuild(RobotType.MINER, dir)){
            MapLocation newRobotLocation = rc.adjacentLocation(dir);
            RobotInfo newRobot = rc.senseRobotAtLocation(newRobotLocation);
            minerRobotIDs.add(newRobot.ID);
            System.out.println("ADDED NEW ROBOT");
            System.out.println(minerRobotIDs);
        }
    }

    public boolean tryMakeVaporator (int cost) throws GameActionException {
        int minerToSendTo = (int) minerRobotIDs.get(1);
        int[] message = walkie.makeMessage(0, minerToSendTo, -1);
        boolean sent = walkie.trySendMessage(message, cost);
        return sent;

    }

}

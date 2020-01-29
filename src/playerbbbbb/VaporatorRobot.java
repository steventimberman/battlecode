package playerbbbbb;
import battlecode.common.*;
import java.util.ArrayList;

/**
* For static methods that a Vaporator building will use.
**/

public class VaporatorRobot {

    static Helpers helper;
    static RobotController rc;
    static Direction[] directions;
    static RobotType[] spawnedByMiner;
    static MapLocation HQMapLoc;
    static ArrayList<Integer> minerRobotIDs = new ArrayList<Integer>();
    static WalkieTalkie walkie;
    static boolean vaporatorMessageSent;


    public VaporatorRobot(Helpers help) throws GameActionException {
        helper = help;
        rc = helper.rc;
        directions = helper.directions;
        walkie = new WalkieTalkie(helper);
    }

    public void runVaporator() throws GameActionException {

    }

    public boolean sayHello() throws GameActionException {
        int[] message = walkie.makeMessage(1, -1, 1, -1);
        boolean sent = walkie.trySendMessage(message, 10);
        return sent;
    }
}

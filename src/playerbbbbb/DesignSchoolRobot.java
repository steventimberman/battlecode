package playerbbbbb;
import battlecode.common.*;
import java.util.ArrayList;

/**
* For static methods that a DesignSchool building will use.
**/

public class DesignSchoolRobot {

    static Helpers helper;
    static RobotController rc;
    static Direction[] directions;
    static RobotType[] spawnedByMiner;
    static MapLocation HQMapLoc;
    static ArrayList<Integer> minerRobotIDs = new ArrayList<Integer>();
    static WalkieTalkie walkie;
    static boolean makeLandscaper;


    public DesignSchoolRobot(Helpers help) throws GameActionException {
        helper = help;
        rc = helper.rc;
        directions = helper.directions;
        walkie = new WalkieTalkie(helper);
        makeLandscaper = true;
    }

    public void runDesignSchool() throws GameActionException {
        if (makeLandscaper){
            for (Direction dir : directions){
                if (helper.tryBuild(RobotType.LANDSCAPER, dir)){
                    makeLandscaper = false;
                }
            }
        }
    }

    public boolean sayHelloDesignSchool() throws GameActionException {
        int[] message = walkie.makeMessage(5, -1, 1, -1);
        boolean sent = walkie.trySendMessage(message, 10);
        return sent;
    }
}

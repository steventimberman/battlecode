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
    static int numLandscapers;
    static boolean alertLandscaper, alertLandscaperWait;


    public DesignSchoolRobot(Helpers help) throws GameActionException {
        helper = help;
        rc = helper.rc;
        directions = helper.directions;
        walkie = new WalkieTalkie(helper);
        numLandscapers = 0;
        alertLandscaperWait = alertLandscaper = false;

    }

    public void runDesignSchool() throws GameActionException {
        int soupTotal = rc.getTeamSoup();
        if (alertLandscaperWait){
            alertLandscaperWait = false;
            alertLandscaper = true;
        }
        if (alertLandscaper){
            tellLandscaper(numLandscapers);
            alertLandscaper = false;
        }
        if ((soupTotal > 155) && (numLandscapers < 3)){
            for (Direction dir : directions){
                if (helper.tryBuild(RobotType.LANDSCAPER, dir)){
                    numLandscapers++;
                    alertLandscaperWait = true;
                }
            }
        }
    }

    public boolean sayHelloDesignSchool() throws GameActionException {
        int[] message = walkie.makeMessage(5, -1, 1, -1);
        boolean sent = walkie.trySendMessage(message, 10);
        return sent;
    }

    public void tellLandscaper(int numOfScapers) throws GameActionException {
        System.out.println("Sending!");
        int[] message = walkie.makeMessage(7+numOfScapers, -1, 2, -1);
        walkie.trySendMessage(message, 5);
    }
}

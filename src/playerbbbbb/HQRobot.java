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
    static boolean sendVaporatorMessage, sendRefineryMessage, sendDesignSchoolMessage;


    public HQRobot(Helpers help) throws GameActionException {
        helper = help;
        rc = helper.rc;
        directions = helper.directions;
        spawnedByMiner = helper.spawnedByMiner;
        walkie = new WalkieTalkie(helper);
        sendVaporatorMessage = true;
        sendRefineryMessage = sendDesignSchoolMessage = false;

    }

    public void runHQ() throws GameActionException {
        searchForInfo();
        if (minerRobotIDs.size() < 25)
            for (Direction dir : directions){
                makeNewMiner(dir);
            }
        int soupTotal = rc.getTeamSoup();
        if (sendVaporatorMessage && soupTotal > 520 && tryMakeBuilding(5, RobotType.VAPORATOR)){
            System.out.println("MAKE A VAPORATOR! AHHH");
            sendVaporatorMessage = false;
        }

        if (sendRefineryMessage && soupTotal > 225 && tryMakeBuilding(10, RobotType.REFINERY)){
            System.out.println("MAKE A REFINE! AHHH");
            sendRefineryMessage = false;
        }
    }

    public void searchForInfo() throws GameActionException {
        // Read new messages for info.
        ArrayList<ArrayList<Integer>> newMessages = walkie.readBlockchain();
        for (int i=0; i < newMessages.size(); i++){
            int infoType = newMessages.get(i).get(0);
            if (infoType == 1)
                sendRefineryMessage = true;
            else if (infoType == 3)
                sendDesignSchoolMessage = true;
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

    public boolean tryMakeBuilding (int cost, RobotType buildingType) throws GameActionException {
        int messageType;
        switch (buildingType){
            case VAPORATOR: messageType = 0;   break;
            case REFINERY: messageType = 2;   break;
            default: messageType = -1;

        }
        System.out.println(messageType);
        int minerToSendTo = (int) minerRobotIDs.get(1);
        int[] message = walkie.makeMessage(messageType, minerToSendTo, -1, -1);
        boolean sent = walkie.trySendMessage(message, cost);
        return sent;
    }
}

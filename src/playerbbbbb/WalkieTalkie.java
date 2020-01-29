package playerbbbbb;
import battlecode.common.*;
import java.util.ArrayList;

public class WalkieTalkie {

  static final int teamSecretKey = 494949;
  static RobotController rc;
  static Helpers helper;
  static Direction[] directions;
  static RobotType[] spawnedByMiner;
  static int myRobotType;
  static int myID;

  static final String[] messasgeType = {
    "Make Vaporator",
    "Vaporator Was Made",
    "Make Refinery",
    "Refinery was made",
    "Make Design School",
    "Design School was made"
  };

  static final RobotType[] robotTypes = {
    RobotType.MINER, RobotType.HQ,
    RobotType.LANDSCAPER, RobotType.DESIGN_SCHOOL
  };


  public WalkieTalkie(Helpers help) throws GameActionException {
    helper = help;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
    setRobotType();
    System.out.println("My robot type is");
    System.out.println(myRobotType);
    myID = rc.getID();
  }



  public static void setRobotType() throws GameActionException  {
    for (int i=0; i < robotTypes.length; i++){
      if (rc.getType() == robotTypes[i]){
        myRobotType = i;
        break;
      }
    }
  }

  public static ArrayList<ArrayList<Integer>> readBlockchain() throws GameActionException {
    ArrayList<ArrayList<Integer>> allMessages = new ArrayList<ArrayList<Integer>>();
    int currentRound = rc.getRoundNum();
    Transaction[] currentBlock = rc.getBlock(currentRound - 1);
    for (Transaction trans : currentBlock){
      int[] message = trans.getMessage();
      if (message[0] == teamSecretKey && ((message[2] == myID) || (message[3] == myRobotType) || (message[4] == myRobotType))) {
        ArrayList<Integer> newMessage = new ArrayList<Integer>();
        newMessage.add(message[1]);
        newMessage.add(message[5]);
        newMessage.add(message[6]);
        allMessages.add(newMessage);
      }
    }
    return allMessages;
  }

  public static int[] makeMessage(
                                  int typeOfMessage, int targetID, int targetRobotType, int targetRobotTypeOther
                                  ) throws GameActionException {
    int[] message = new int[7];
    message[0] = teamSecretKey;
    message[1] = typeOfMessage;
    message[2] = targetID;
    message[3] = targetRobotType;
    message[4] = targetRobotTypeOther;
    message[5] = rc.getLocation().x;
    message[6] = rc.getLocation().y;
    return message;
  }

  // private static int[] findMessage(int typeOfMessage) throws GameActionException{

  // }

  public static boolean trySendMessage(int[] message, int cost) throws GameActionException {
    if (rc.canSubmitTransaction(message, cost)){
      rc.submitTransaction(message, cost);
      System.out.print("MESSAGE SENT");
      return true;
    } return false;
  }


}


package playerbbbbb;
import battlecode.common.*;
import java.util.ArrayList;

public class WalkieTalkie {

  static final int teamSecretKey = 494949;
  static final String[] messasgeType = {"Make Vaporator", "Vaporator Made"};
  static final RobotType[] robotTypes = {RobotType.MINER, RobotType.HQ, RobotType.LANDSCAPER, RobotType.DESIGN_SCHOOL};
  static RobotController rc;
  static Helpers helper;
  static Direction[] directions;
  static RobotType[] spawnedByMiner;
  static int myRobotType = -1;
  static int myID;


  public WalkieTalkie(Helpers help) throws GameActionException {
    helper = help;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
    setRobotType();
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

  public static ArrayList<Integer> readBlockchain() throws GameActionException {
    ArrayList<Integer> allMessages = new ArrayList<Integer>();
    int currentRound = rc.getRoundNum();
    Transaction[] currentBlock = rc.getBlock(currentRound - 1);
    for (Transaction trans : currentBlock){
      int[] message = trans.getMessage();
      if (message[0] == teamSecretKey && (message[2] == myID || message[3] == myRobotType)){
        allMessages.add(message[1]);
      }
    }
    return allMessages;
  }

  public static int[] makeMessage(int typeOfMessage, int targetID, int targetRobotType) throws GameActionException {
    int[] message = new int[7];
    message[0] = teamSecretKey;
    message[1] = typeOfMessage;
    message[2] = targetID;
    message[3] = targetRobotType;
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

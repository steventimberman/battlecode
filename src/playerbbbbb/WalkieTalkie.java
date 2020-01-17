package playerbbbbb;
import battlecode.common.*;

public class WalkieTalkie {

  static final int teamSecretKey = 494949;
  static final String[] messasgeType = {"Make Vaporator"};
  static RobotController rc;
  static Helpers helper;
  static Direction[] directions;
  static RobotType[] spawnedByMiner;


  public WalkieTalkie(Helpers help) {
    helper = help;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
  }


  private static int[] genericMessage(int typeOfMessage) throws GameActionException{
    int[] message = new int[7];
    message[0] = teamSecretKey;
    message[1] = typeOfMessage;
    return message;
  }

  public static void sendMessage(int[] message, int cost) throws GameActionException{
    if (rc.canSubmitTransaction(message, cost)){
      rc.submitTransaction(message, cost);
      System.out.print("MESSAGE SENT");
    }
  }

  public static void sendMakeVaporator(int idNumber) throws GameActionException{
    int[] message = genericMessage(0);
    message[2] = idNumber;
    sendMessage(message, 3);
  }

  public static boolean receiveMakeVaporator() throws GameActionException{
    int currentRound = rc.getRoundNum();
    for (Transaction trans : rc.getBlock(currentRound - 1)){
      int[] message = trans.getMessage();
      if (message[0] == teamSecretKey && message[1] == 0 && message[2] == rc.getID()){
        System.out.println("MESSAGE RECEIVED");
        return true;
      }
    }
    System.out.println("DIDNT GET IT");
    return false;
  }


}

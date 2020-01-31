package playerbbbbb;
import battlecode.common.*;
import java.util.ArrayList;

/**
* For static methods that landscapers will use.
* Each should (most likely) take an rc as input
**/

public class LandscaperRobot {
  static Helpers helper;
  static RobotController rc;
  static Direction[] directions;
  static RobotType[] spawnedByMiner;
  static MapLocation hqMapLoc, currentLocation;
  static WalkieTalkie walkie;
  static Direction dirToHQ;
  static ArrayList<MapLocation> nearHQ;
  static ArrayList<MapLocation> fourSides;
  static ArrayList<MapLocation> smallSquare;
  static int nextTile;
  static boolean justDug, alert1, alert2, alert2sender;
  static boolean startDigging;

  public LandscaperRobot(Helpers help) throws GameActionException {
    helper = help;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
    walkie = new WalkieTalkie(helper);
    nearHQ = new ArrayList<MapLocation>();
    smallSquare = new ArrayList<MapLocation>();
    nextTile = 0;
    currentLocation = rc.getLocation();
    dirToHQ = Direction.NORTH;
    justDug = false;
    alert1 = alert2 = alert2sender = false;

  }

  public void runLandscaper() throws GameActionException {
    if (alert2==false){
      lookForAlerts();
      getToHQSquare();
    }

    if (alert2){
      updateSquarePosition();

      returnDirt();
    }

  }

  public void lookForAlerts() throws GameActionException{
    ArrayList<ArrayList<Integer>> newMessages = walkie.readBlockchain();
    for (int i=0; i < newMessages.size(); i++){
        System.out.println("LOOKINGG");
        ArrayList<Integer> message = newMessages.get(i);
        int task = message.get(0);
        System.out.println(message);
        if (alert1==false && task==8){
          System.out.println("I got my task!");
          nextTile = 0;
          alert1=true;
        }
        else if (alert1==false && task == 9){
          alert1 = true;
          nextTile = 3;
          System.out.println("I got my task!");
        }else if (alert1==false && task == 10){
          nextTile = 6;
          alert1=true;
          System.out.println("I got my task!");
          alert2sender = true;
        }else if (alert1==true && task == 11){
          System.out.println("I got my second task!");
          alert2 = true;
        }

    }
  }

  public void returnDirt() throws GameActionException {
    System.out.println("Returning Dirt!");

    System.out.println("Lots of it! Dirt!");
    if (currentLocation.equals(getNextLocationSquare())){
      System.out.println("BAHH");
      putDirtDownSmallSquare();
    } else {
      System.out.println("BLAHH!");
      getToHQSquare();
    }

  }

  public void getToHQ() throws GameActionException {
    if (helper.tryMove(dirToHQ)==false && (currentLocation.equals(getLastLocationSquare())==false)) {
      helper.tryMove(helper.randomDirection());
    }

    updatePosition();
  }

  public void getToHQSquare() throws GameActionException {
    if (alert2sender && alert2 == false && currentLocation.equals(getNextLocationSquare())){
      int[] message = walkie.makeMessage(11, -1, 2, -1);
      walkie.trySendMessage(message, 5);
      alert2=true;
    }
    if (alert2==true || currentLocation.equals(getNextLocationSquare())==false ){
      if (helper.tryMove(dirToHQ)==false && (currentLocation.equals(getLastLocationSquare())==false)) {
        helper.tryMove(helper.randomDirection());
      }
      updateSquarePosition();
    }
  }

  public void putDirtDown() throws GameActionException {
    updatePosition();
    if (justDug) {
      if ( (currentLocation.y+2 == hqMapLoc.y) &&  (helper.tryDepositDirt(Direction.NORTH))
          || (currentLocation.y-2 == hqMapLoc.y) && (helper.tryDepositDirt(Direction.SOUTH))
          || (currentLocation.x+2 == hqMapLoc.x) && (helper.tryDepositDirt(Direction.EAST))
          || (currentLocation.x-2 == hqMapLoc.x) && (helper.tryDepositDirt(Direction.WEST))
          )
        {
          nextTile ++;
          justDug = (justDug==false);
        }
      }

      else {
            if ((currentLocation.y+2 == hqMapLoc.y) && helper.tryDig(Direction.SOUTH)
              || (currentLocation.y-2 == hqMapLoc.y) && helper.tryDig(Direction.NORTH)
              || (currentLocation.x+2 == hqMapLoc.x) && helper.tryDig(Direction.WEST)
              || (currentLocation.x-2 == hqMapLoc.x) && helper.tryDig(Direction.EAST)
              )
            {
              if (rc.isLocationOccupied(getNextLocationSquare())==false){
                justDug = (justDug==false);
              }
            }

      }

  }

  public void findHQLocation() throws GameActionException {
    ArrayList<Integer> messageWithLocation;
    messageWithLocation = walkie.findMessages(1,2).get(0);
    hqMapLoc = new MapLocation(messageWithLocation.get(5), messageWithLocation.get(6));
    storeNextToHQ();
  }

  public void updatePosition() throws GameActionException {
    currentLocation = rc.getLocation();
    dirToHQ = currentLocation.directionTo(nearHQ.get(nextTile%16));
  }

  public void updateSquarePosition() throws GameActionException {
    currentLocation = rc.getLocation();
    dirToHQ = currentLocation.directionTo(smallSquare.get(nextTile%8));
  }

  public MapLocation getNextLocation() throws GameActionException {
    int currentNextTile = nextTile % 16;
    return nearHQ.get(currentNextTile);
  }

  public MapLocation getNextLocationSquare() throws GameActionException {
    int currentNextTile = nextTile % 8;
    return smallSquare.get(currentNextTile);
  }

  public MapLocation getLastLocation() throws GameActionException {
    int currentNextTile = (nextTile+15) % 16;
    return nearHQ.get(currentNextTile);
  }

  public MapLocation getLastLocationSquare() throws GameActionException {
    int currentNextTile = (nextTile+7) % 8;
    return smallSquare.get(currentNextTile);
  }


  public void storeNearHQ() throws GameActionException {
    MapLocation nextToStore;
    for (int side = 0; side <= 3; side++){
      for (int i = -1; i <= 1; i++) {
        if (side == 0)
          nextToStore = awayFromHQ(i, 2);
        else if (side == 1)
          nextToStore = awayFromHQ(2,-i);
        else if (side == 2)
          nextToStore = awayFromHQ(-i,-2);
        else
          nextToStore = awayFromHQ(-2,i);

        nearHQ.add(nextToStore);
        if (i==0){
          nearHQ.add(nextToStore);
          fourSides.add(nextToStore);
        }
      }
    }

  }
  public void storeNextToHQ() throws GameActionException {
    MapLocation nextToStore;
    for (int side = 0; side <= 3; side++){
      for (int i = -1; i <= 0; i++) {
        if (side == 0)
          nextToStore = awayFromHQ(i, 1);
        else if (side == 1)
          nextToStore = awayFromHQ(1,-i);
        else if (side == 2)
          nextToStore = awayFromHQ(-i,-1);
        else
          nextToStore = awayFromHQ(-1,i);

        smallSquare.add(nextToStore);
      }
    }
  }

   public void putDirtDownSmallSquare() throws GameActionException {
    updateSquarePosition();
    if (justDug) {
      if ( (currentLocation.equals(awayFromHQ(0,1))) &&  (helper.tryDepositDirt(Direction.WEST))
          || (currentLocation.equals(awayFromHQ(1,1))) &&  (helper.tryDepositDirt(Direction.WEST))
          || (currentLocation.equals(awayFromHQ(1,0))) &&  (helper.tryDepositDirt(Direction.NORTH))
          || (currentLocation.equals(awayFromHQ(1,-1))) &&  (helper.tryDepositDirt(Direction.NORTH))
          || (currentLocation.equals(awayFromHQ(0,-1))) &&  (helper.tryDepositDirt(Direction.EAST))
          || (currentLocation.equals(awayFromHQ(-1,-1))) &&  (helper.tryDepositDirt(Direction.EAST))
          || (currentLocation.equals(awayFromHQ(-1,0))) &&  (helper.tryDepositDirt(Direction.SOUTH))
          || (currentLocation.equals(awayFromHQ(-1,1))) &&  (helper.tryDepositDirt(Direction.SOUTH))
                   )
        {
          nextTile ++;
          justDug = (justDug==false);
        }
      }

      else {
            Direction actualDirToHQ = currentLocation.directionTo(hqMapLoc);
            MapLocation whereToDig = currentLocation.subtract(actualDirToHQ);
            if (helper.tryDig(currentLocation.directionTo(whereToDig))){
            //   if (rc.isLocationOccupied(getNextLocationSquare())==false){
                if (rc.getDirtCarrying() >= 6){
                  justDug = (justDug==false);
                }
              // }
            }

      }

  }



  public MapLocation awayFromHQ(int dx, int dy) throws GameActionException {
    MapLocation loc = new MapLocation(hqMapLoc.x + dx, hqMapLoc.y+dy);
    return loc;
  }




}

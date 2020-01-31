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
  static int nextTile;
  static boolean justDug;

  public LandscaperRobot(Helpers help) throws GameActionException {
    helper = help;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
    walkie = new WalkieTalkie(helper);
    nearHQ = new ArrayList<MapLocation>();
    nextTile = 0;
    currentLocation = rc.getLocation();
    dirToHQ = Direction.NORTH;
    justDug = false;
  }

  public void runLandscaper() throws GameActionException {
    updatePosition();

    returnDirt();

  }

  public void returnDirt() throws GameActionException {
    System.out.println("Returning Dirt!");

    System.out.println("Lots of it! Dirt!");
    if (currentLocation.equals(getNextLocation())){
      System.out.println("BAHH");
      putDirtDown();
    } else {
      System.out.println("BLAHH!");
      getToHQ();
    }

  }

  public void getToHQ() throws GameActionException {
    if (helper.tryMove(dirToHQ)==false && (currentLocation.equals(getLastLocation())==false)) {
      helper.tryMove(helper.randomDirection());
    }
    updatePosition();
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
              justDug = (justDug==false);
            }

      }

  }

  public void findHQLocation() throws GameActionException {
    ArrayList<Integer> messageWithLocation;
    messageWithLocation = walkie.findMessages(1,2).get(0);
    hqMapLoc = new MapLocation(messageWithLocation.get(5), messageWithLocation.get(6));
    storeNearHQ();
  }

  public void updatePosition() throws GameActionException {
    currentLocation = rc.getLocation();
    dirToHQ = currentLocation.directionTo(nearHQ.get(nextTile%16));
  }

  public MapLocation getNextLocation() throws GameActionException {
    int currentNextTile = nextTile % 16;
    return nearHQ.get(currentNextTile);
  }

  public MapLocation getLastLocation() throws GameActionException {
    int currentNextTile = (nextTile+15) % 16;
    return nearHQ.get(currentNextTile);
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
        if (i==0)
          nearHQ.add(nextToStore);
      }
    }
  }

  public MapLocation awayFromHQ(int dx, int dy) throws GameActionException {
    MapLocation loc = new MapLocation(hqMapLoc.x + dx, hqMapLoc.y+dy);
    return loc;
  }




}

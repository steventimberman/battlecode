package playerbbbbb;
import battlecode.common.*;

/**
* For static methods that landscapers will use.
* Each should (most likely) take an rc as input
**/

public class LandscaperRobot {
  static Helpers helper;
  static RobotController rc;
  static Direction[] directions;
  static RobotType[] spawnedByMiner;
  static MapLocation HQMapLoc;
  static WalkieTalkie walkie;


  public LandscaperRobot(Helpers help) throws GameActionException {
    helper = help;
    rc = helper.rc;
    directions = helper.directions;
    spawnedByMiner = helper.spawnedByMiner;
    walkie = new WalkieTalkie(helper);
  }

  public void runLandscaper(int turnCount) throws GameActionException {
    helper.tryBlockchain(turnCount);

    if (HQMapLoc != null){

      } else {
        findHQ();
      }
    }

    public void findHQ(){
      int i = 1;
    }

    public void getToHQ(){
      int i = 1;
    }

    public void depositAroundHQ(){
      MapLocation currentLocation = rc.getLocation();
      Direction dirToHQ = currentLocation.directionTo(HQMapLoc);
      switch(dirToHQ){
        case NORTH: {
          break;
        }
        case NORTHWEST: {
          break;
        }
        case WEST: {
          break;
        }
        case SOUTHWEST: {
          break;
        }
        case SOUTH: {
          break;
        }
        case SOUTHEAST: {
          break;
        }
        case EAST: {
          break;
        }
        case NORTHEAST: {
          break;
        }
      }

    }


}

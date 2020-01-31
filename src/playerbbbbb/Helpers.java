package playerbbbbb;
import battlecode.common.*;

public class Helpers {

  static RobotController rc;
  static Direction[] directions;
  static RobotType[] spawnedByMiner;

  public Helpers(RobotController controller, Direction[] directs, RobotType[] robTypes) {
    rc = controller;
    directions = directs;
    spawnedByMiner = robTypes;

  }

  /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    public Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    public Direction randomDirectionExcept(Direction notThisDirection) {
        Direction nextDirection;
        while (true){
            nextDirection = directions[(int) (Math.random() * directions.length)];
            if (nextDirection.equals(notThisDirection) == false){
                break;
            }
        }
        return nextDirection;
    }


    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    public boolean tryMove(Direction dir) throws GameActionException {
        // System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
        if (rc.canMove(dir)) {
            MapLocation nextToMe = rc.adjacentLocation(dir);
            boolean isFlooded = rc.senseFlooding(nextToMe);
            if (isFlooded==false){
                rc.move(dir);
                return true;
            }

        } return false;
    }

    /**
     * Attempts to build a given robot in a given direction.
     *
     * @param type The type of the robot to build
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    public boolean tryBuild(RobotType type, Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canBuildRobot(type, dir)) {
            rc.buildRobot(type, dir);
            return true;
        } else{
            return false;
        }
    }

    /**
     * Attempts to mine soup in a given direction.
     *
     * @param dir The intended direction of mining
     * @return true if a move was performed
     * @throws GameActionException
     */
    public boolean tryMine(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canMineSoup(dir)) {
            rc.mineSoup(dir);
            return true;
        } else return false;
    }

    public boolean tryDig(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canDigDirt(dir)) {
            rc.digDirt(dir);
            return true;
        } else return false;
    }

    /**
     * Attempts to refine soup in a given direction.
     *
     * @param dir The intended direction of refining
     * @return true if a move was performed
     * @throws GameActionException
     */
    public boolean tryRefine(Direction dir) throws GameActionException {
        if (rc.isReady() && rc.canDepositSoup(dir)) {
            rc.depositSoup(dir, rc.getSoupCarrying());
            return true;
        } else return false;
    }

    public boolean tryDepositDirt(Direction dir) throws GameActionException {
      if (rc.isReady() && rc.canDepositDirt(dir)){
        rc.depositDirt(dir);
        return true;
      } return false;
    }


    public void tryBlockchain(int turnCount) throws GameActionException {
        if (turnCount < 3) {
            int[] message = new int[7];
            for (int i = 0; i < 7; i++) {
                message[i] = 123;
            }
            if (rc.canSubmitTransaction(message, 10))
                rc.submitTransaction(message, 10);
        }
        // System.out.println(rc.getRoundMessages(turnCount-1));
    }

}

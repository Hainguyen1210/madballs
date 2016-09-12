package madballs.AI;

import madballs.Ball;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.item.Item;
import madballs.item.WeaponItem;
import madballs.map.Map;
import madballs.moveBehaviour.RotateBehaviour;
import madballs.moveBehaviour.StraightMove;
import madballs.wearables.Shield;
import madballs.wearables.Weapon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.regex.Matcher;

/**
 * Created by caval on 30/08/2016.
 */
public class MoveStrategy extends Strategy {
    private StraightMove straightMove;
    private int[] myNode;
    private LinkedList<int[]> objectivePath = new LinkedList<>();
    private LinkedList<int[]> tempPath = new LinkedList<>();
    private long objectiveChangeTime = 0;
    private final int PATH_LENGTH_LIMIT = 20;
    private GameObject objective;
    private double objectiveValue = -1;
    private Random random = new Random();
    private Map map;
    private boolean isCenterReached = false;
    private boolean shouldFindObjective = false;
    private ArrayList<ArrayList<LinkedList<int[]>>> pathsToNodes = new ArrayList<>();

    public void setCenterReached(boolean centerReached) {
        isCenterReached = centerReached;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public MoveStrategy(BotPlayer bot) {
        super(bot);
        setImportance(6);
    }

    @Override
    public void prepare() {
        clearPathsToNodes();

        // save the Ball movement and coordinates

        straightMove = (StraightMove) getBot().getBall().getMoveBehaviour();

        int myColumn = (int) (getBot().getBall().getTranslateX() / map.getColumnWidth());
        int myRow = (int) (getBot().getBall().getTranslateY() / map.getRowHeight());
        if (myColumn == map.getNumColumns() / 2 && myRow == map.getNumRows() / 2){
//            System.out.println("reached center");
            isCenterReached = true;
        }
        myNode = new int[] {myRow, myColumn};

        // decide whether the Ball should find a new objective
        if (!shouldFindObjective) shouldFindObjective = (getBot().getLastThoughtTime() - objectiveChangeTime) / 1000000000 > (3 + random.nextInt(10));
        if (shouldFindObjective){
            objective = null;
            objectivePath.clear();
            objectiveValue = -1;
        }

    }

    @Override
    public void consider(GameObject obj) {
        if (shouldFindObjective){
            double value = 0;

            /* *********** consider whether the obj is valuable to be the objective *********** */
            if (obj instanceof Item){
                value = 18;
                if (obj instanceof WeaponItem){
                    if (((WeaponItem)obj).getWeapon() instanceof Shield) return;
                    if (getBot().getBall().getWeapon().getAmmo() > 15){
                        double damageDiff = (((WeaponItem)obj).getWeapon().getDamage() - getBot().getBall().getWeapon().getDamage());
                        if (damageDiff < 0) return;
                    }
                    value *= ((WeaponItem)obj).getWeapon().getDamage() / 5;
                }
            }
            else if (obj instanceof Ball){
                Ball ball = (Ball) obj;
                double distance = Math.sqrt(Math.pow(ball.getTranslateY() - getBot().getBall().getTranslateY(), 2) + Math.pow(ball.getTranslateX() - getBot().getBall().getTranslateX(), 2));
                if (distance < getBot().getBall().getWeapon().getRange()) return;
                if (ball.getPlayer().getTeamNum() != getBot().getTeamNum()){
                    value = (100 - obj.getHpValue()) / 5;
                    double damageDiff = (ball.getWeapon().getDamage() - getBot().getBall().getWeapon().getDamage());
                    if (damageDiff > 0) return;
                    value -= damageDiff / 10;
                }
            }

            /* ************ check the path to the objective ********* */
            if (value != 0){
                int targetColumn = (int)(obj.getTranslateX() / map.getColumnWidth());
                int targetRow = (int)(obj.getTranslateY() / map.getRowHeight());
                findPath(targetRow, targetColumn);
                if (tempPath.size() != 0){
                    value /= (tempPath.size() / 4);
                    if (value > objectiveValue){
                        // update the objective
                        objective = obj;
                        objectiveValue = value;
                        objectiveChangeTime = getBot().getLastThoughtTime();
                        objectivePath = new LinkedList<>(tempPath);
                    }
                }
            }
        }

    }

    /**
     * clear all the previously found paths
     */
    private void clearPathsToNodes(){
        pathsToNodes.clear();
        for (int i = 0; i < map.getNumRows(); i++){
            ArrayList<LinkedList<int[]>> arrayList = new ArrayList<>();
            for (int j = 0; j < map.getNumColumns(); j ++){
                arrayList.add(new LinkedList<>());
            }
            pathsToNodes.add(arrayList);
        }
    }

    @Override
    public void act() {
        if (objective == null && shouldFindObjective){
            if (isCenterReached){
                // randomly move
                straightMove.setVelocityX(straightMove.getSpeed() * (random.nextInt(3) - 1));
                straightMove.setVelocityY(straightMove.getSpeed() * (random.nextInt(3) - 1));
            }
            else{
                // move to the center of the map, since there are often many items there
                tempPath.clear();
                objectiveValue = -1;
                findPath(map.getNumRows() / 2, map.getNumColumns() / 2);
                objectivePath = new LinkedList<>(tempPath);
            }
            objectiveChangeTime = getBot().getLastThoughtTime();
        }
        // move to the objective
        if (objectivePath.size() != 0) {
            shouldFindObjective = false;

            int[] nextNode = objectivePath.getFirst();
            double myX = getBot().getBall().getTranslateX();
            double myY = getBot().getBall().getTranslateY();

            double nodeX = myNode[1] * map.getColumnWidth() + 25;
            double nodeY = myNode[0] * map.getRowHeight() + 25;


            boolean shouldTargetObjective = false;
            if (objective != null){
                int[] targetNode = objectivePath.getLast();
                // reset the objective if the current one is dead or reached
                if (myNode[0] == targetNode[0] && myNode[1] == targetNode[1]){
                    shouldTargetObjective = true;
                    if (objective.isDead()) shouldFindObjective = true;
                }
            }

            // remove the moved node from the path
            if (objectivePath.size() > 1 && myNode[0] == nextNode[0] && myNode[1] == nextNode[1]){
                objectivePath.removeFirst();
                nextNode = objectivePath.getFirst();
            }

            // if it is the last node, then move to the objective itself
            double targetX, targetY;
            if (shouldTargetObjective) {
                targetX = objective.getTranslateX();
                targetY = objective.getTranslateY();
            }
            else {
                targetX = nextNode[1] * map.getColumnWidth() + 25;
                targetY = nextNode[0] * map.getRowHeight() + 25;
            }

            double myDirection = Math.atan2(targetY - myY, targetX - myX);
            double nodeDirection = Math.atan2(targetY - nodeY, targetX - nodeX);

            if (Math.abs(myDirection - nodeDirection) > 0.1){
                myDirection = Math.atan2(nodeY- myY, nodeX - myX);
            }

            straightMove.setNewDirection(myDirection);
            ((RotateBehaviour)getBot().getBall().getWeapon().getMoveBehaviour()).setNewDirection(myDirection + Math.PI);
        }
    }

    @Override
    public void updateImportance() {
        if (objectiveValue == -1){
            setImportance(6);
        }
        setImportance(getImportance() * objectiveValue);
    }

    /**
     * find the path to get to a node
     * @param targetRow
     * @param targetColumn
     */
    private void findPath(int targetRow, int targetColumn){
        tempPath.clear();
        if (!map.getMAP_ARRAY()[targetRow][targetColumn].equals("x") && !map.getMAP_ARRAY()[targetRow][targetColumn].equals("+")){
            LinkedList<int[]> foundPath = pathsToNodes.get(targetRow).get(targetColumn);
            if (foundPath.size() != 0){
                tempPath = new LinkedList<>(foundPath);
            }
            else {
                boolean noPathFound = true;
                for (int i = 0; i < pathsToNodes.size(); i++){
                    for (int j = 0; j < pathsToNodes.get(i).size(); j++){
                        foundPath = pathsToNodes.get(i).get(j);
                        if (foundPath.size() > 0){
                            noPathFound = false;
                            findPath(targetRow, targetColumn, i, j, new LinkedList<>(foundPath), -1);
                        }
                    }
                }
                if (noPathFound){
                    findPath(targetRow, targetColumn, myNode[0], myNode[1], new LinkedList<>(), -1);
                }
            }
        }
    }

    /**
     * the recursive method to find the shortest path to a node
     * @param targetRow
     * @param targetColumn
     * @param currentRow
     * @param currentColumn
     * @param path
     * @param lastDirection
     */
    private void findPath(int targetRow, int targetColumn, int currentRow, int currentColumn, LinkedList<int[]> path, int lastDirection){
        if (path.size() < PATH_LENGTH_LIMIT || (targetColumn == map.getNumColumns() / 2 && targetRow == map.getNumRows() /2)){
            int[][] adjacentNodes = new int[][] {
                    new int[] {currentRow, currentColumn - 1},
                    new int[] {currentRow + 1, currentColumn},
                    new int[] {currentRow, currentColumn + 1},
                    new int[] {currentRow - 1, currentColumn}
            };

            for (int direction = 0; direction < adjacentNodes.length; direction++){
                if (direction == lastDirection) continue;
                int[] adjacentNode = adjacentNodes[direction];
                if (adjacentNode[0] == myNode[0] && adjacentNode[1] == myNode[1]) continue;
                if (adjacentNode[0] < 0 || adjacentNode[0] >= map.getNumRows() || adjacentNode[1] < 0 || adjacentNode[1] >= map.getNumColumns()){
                    continue;
                }

                if (!map.getMAP_ARRAY()[adjacentNode[0]][adjacentNode[1]].equals("x") && !map.getMAP_ARRAY()[adjacentNode[0]][adjacentNode[1]].equals("+")){

                    LinkedList<int[]> foundPath = pathsToNodes.get(adjacentNode[0]).get(adjacentNode[1]);
                    if (foundPath.size() == 0 || path.size() + 1 < foundPath.size()){
                        LinkedList<int[]> newPath = new LinkedList<>(path);
                        newPath.add(adjacentNode);
                        pathsToNodes.get(adjacentNode[0]).remove(adjacentNode[1]);
                        pathsToNodes.get(adjacentNode[0]).add(adjacentNode[1], newPath);

                        if (adjacentNode[0] == targetRow && adjacentNode[1] == targetColumn){
                            if (tempPath.size() == 0 || path.size() + 1 < tempPath.size()){
                                tempPath = new LinkedList<>(newPath);
                                return;
                            }
                        }
                        else {
                            findPath(targetRow, targetColumn, adjacentNode[0], adjacentNode[1], newPath, (direction + 2) % 4);
                        }
                    }
                }

            }
        }
    }
}

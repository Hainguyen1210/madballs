package madballs.AI;

import madballs.Ball;
import madballs.GameObject;
import madballs.MadBalls;
import madballs.item.Item;
import madballs.map.Map;
import madballs.moveBehaviour.RotateBehaviour;
import madballs.moveBehaviour.StraightMove;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

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

    public MoveStrategy(BotPlayer bot, Map map) {
        super(bot);
        setImportance(6);
        this.map = map;
    }

    @Override
    public void prepare() {
        pathsToNodes.clear();
        for (int i = 0; i < map.getNumRows(); i++){
            ArrayList<LinkedList<int[]>> arrayList = new ArrayList<>();
            for (int j = 0; j < map.getNumColumns(); j ++){
                arrayList.add(new LinkedList<>());
            }
            pathsToNodes.add(arrayList);
        }

        objective = null;

        straightMove = (StraightMove) getBot().getBall().getMoveBehaviour();

        int myColumn = (int) (getBot().getBall().getTranslateX() / map.getColumnWidth());
        int myRow = (int) (getBot().getBall().getTranslateY() / map.getRowHeight());
        if (myColumn == map.getNumColumns() / 2 && myRow == map.getNumRows() / 2){
//            System.out.println("reached center");
            isCenterReached = true;
        }
        myNode = new int[] {myRow, myColumn};

        shouldFindObjective = (getBot().getLastThoughtTime() - objectiveChangeTime) / 1000000000 > (5 + random.nextInt(4));
        if (shouldFindObjective){
            objectivePath.clear();
            objectiveValue = -1;
        }

    }

    @Override
    public void consider(GameObject obj) {
        if (shouldFindObjective){
            double value = 0;
            if (obj instanceof Item){
                value = 15;
            }
            else if (obj instanceof Ball){
                if (((Ball)obj).getPlayer().getTeamNum() != getBot().getTeamNum()){
                    value = (100 - obj.getHpValue()) / 5;
                }
            }
            if (value != 0){
                findPath(obj);
                if (tempPath.size() != 0){
                    value /= (tempPath.size() / 4);
                    if (value > objectiveValue){
                        objective = obj;
                        objectiveValue = value;
                        objectiveChangeTime = getBot().getLastThoughtTime();
                        objectivePath = tempPath;
                    }
                }
            }
        }

    }

    @Override
    public void act() {
        if (objective == null && shouldFindObjective){
            if (isCenterReached){
                straightMove.setVelocityX(straightMove.getSpeed() * (random.nextInt(3) - 1));
                straightMove.setVelocityY(straightMove.getSpeed() * (random.nextInt(3) - 1));
            }
            else{
                tempPath.clear();
                objectiveValue = -1;
                findPath(map.getNumRows() / 2, map.getNumColumns() / 2, myNode[0], myNode[1], new LinkedList<>(), -1);
                objectivePath = tempPath;
            }
            objectiveChangeTime = getBot().getLastThoughtTime();
        }
        if (objectivePath.size() != 0) {
//            System.out.println("go");
            int[] nextNode = objectivePath.getFirst();
            double myX = getBot().getBall().getTranslateX();
            double myY = getBot().getBall().getTranslateY();

            double nodeX = myNode[1] * map.getColumnWidth() + 25;
            double nodeY = myNode[0] * map.getRowHeight() + 25;

            boolean shouldTargetObjective = false;
            if (objectivePath.size() == 1 && objective != null){
                shouldTargetObjective = true;
            }
            if (objectivePath.size() > 1 && myNode[0] == nextNode[0] && myNode[1] == nextNode[1]){
                objectivePath.removeFirst();
                nextNode = objectivePath.getFirst();
            }

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
            ((RotateBehaviour)getBot().getBall().getWeapon().getMoveBehaviour()).setNewDirection(myDirection);
        }
    }

    @Override
    public void updateImportance() {
        if (objectiveValue == -1){
            setImportance(6);
        }
        setImportance(getImportance() * objectiveValue);
    }

    private void findPath(GameObject obj){
        tempPath.clear();
        int targetColumn = (int)(obj.getTranslateX() / map.getColumnWidth());
        int targetRow = (int)(obj.getTranslateY() / map.getRowHeight());
        if (!map.getMAP_ARRAY()[targetColumn][targetRow].equals("x") && !map.getMAP_ARRAY()[targetColumn][targetRow].equals("+")){
            findPath(targetRow, targetColumn, myNode[0], myNode[1], new LinkedList<>(), -1);
        }
    }

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
                if (adjacentNode[0] < 0 || adjacentNode[0] >= map.getNumRows() || adjacentNode[1] < 0 || adjacentNode[1] >= map.getNumColumns()){
                    continue;
                }
//                for (String[] strings: map.getMAP_ARRAY()){
//                    for (String string: strings){
//                        System.out.print(string);
//                    }
//                    System.out.println();
//                }
                if (!map.getMAP_ARRAY()[adjacentNode[0]][adjacentNode[1]].equals("x") && !map.getMAP_ARRAY()[adjacentNode[0]][adjacentNode[1]].equals("+")){
//                    if (adjacentNode[0] == 4 && adjacentNode[1] == 23) {
//                        System.out.println("available node");
//                    }
                    LinkedList<int[]> foundPath = pathsToNodes.get(adjacentNode[0]).get(adjacentNode[1]);
                    if (foundPath.size() == 0 || path.size() + 1 < foundPath.size()){
//                        if (adjacentNode[0] == 4 && adjacentNode[1] == 23) {
//                            System.out.println("new path node");
//                        }
                        LinkedList<int[]> newPath = new LinkedList<>(path);
                        newPath.add(adjacentNode);
                        pathsToNodes.get(adjacentNode[0]).remove(adjacentNode[1]);
                        pathsToNodes.get(adjacentNode[0]).add(adjacentNode[1], newPath);
                        if (adjacentNode[0] == targetRow && adjacentNode[1] == targetColumn){
                            if (tempPath.size() == 0 || path.size() + 1 < tempPath.size()){
                                tempPath = newPath;
//                                System.out.println("new path");
//                                for (int[] step : tempPath){
//                                    System.out.print(step[0] + " " + step[1] + ", ");
//                                }
//                                System.out.println();
//                                System.out.println(myNode[0]);
//                                System.out.println(myNode[1]);
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

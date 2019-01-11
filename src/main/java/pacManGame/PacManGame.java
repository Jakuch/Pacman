package pacManGame;

import ghosts.Ghost;
import movement.Direction;
import movement.GhostMovementField;
import movement.Point;

import java.util.*;

public abstract class PacManGame {
    private PacMan pacMan;
    private Direction direction;
    private Walls walls;
    private Food food;
    private Point oldPosition;
    private Point newPosition;
    private Score score;
    private Ghost blinky;
    private Ghost pinky;
    private boolean isRunning;

    /**
     * Constructor creating the map
     * @param walls input
     * @param food input
     */
    public PacManGame(Walls walls, Food food) {
        this.walls = walls;
        this.food = food;
    }

    /**
     * Method that starts the game
     */
    private void gameStart() {
        this.score = new Score();
        this.pacMan = new PacMan(new Point(13, 16));
        this.oldPosition = pacMan.getPacmanPosition();
        this.direction = Direction.UP;
        ghostsInitialization();
    }

    /**
     * Method starting ghosts movement
     */
    public void ghostsInitialization(){
        blinkyStart();
        pinkyStart();
    }

    /**
     * Ghost number 1
     */
    private void blinkyStart(){
        blinky = new Ghost(new Point(13, 10)) {
            @Override
            public void chase() {
                Thread chaseThread = new Thread(() -> {
                    while (true) {
                        ghostMove(blinky);

                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                chaseThread.setDaemon(true);
                chaseThread.start();
            }
        };
    }

    /**
     * Ghost number 2
     */
    private void pinkyStart(){
        pinky = new Ghost(new Point(13, 8)) {
            @Override
            public void chase() {
                Thread chaseThread = new Thread(() -> {
                    while (true) {
                        ghostMove(pinky);

                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                chaseThread.setDaemon(true);
                chaseThread.start();
            }
        };
    }

    /**
     * Method that runs the game
     */
    public void gameRun() {
        gameStart();
        int speed = 250;
        isRunning = true;
        blinky.chase();
        pinky.chase();

        while (isRunning) {
            move();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Pacman movement and interactions
     */
    private void move() {
        switch (direction) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
        }
        eat();
        onMove();
    }

    /**
     * Pacman move up
     */
    private void moveUp() {
        newPosition = new Point(pacMan.getPacmanPosition().getX(), pacMan.getPacmanPosition().getY() - 1);
        if (checkIfWallIsNotOnTheWay()) {
            pacMan.setPacmanPosition(newPosition);
        }
    }

    /**
     * Pacman move down
     */
    private void moveDown() {
        newPosition = new Point(pacMan.getPacmanPosition().getX(), pacMan.getPacmanPosition().getY() + 1);
        if (checkIfWallIsNotOnTheWay()) {
            pacMan.setPacmanPosition(newPosition);
        }
    }

    /**
     * Pacman move left
     */
    private void moveLeft() {
        newPosition = new Point(pacMan.getPacmanPosition().getX() - 1, pacMan.getPacmanPosition().getY());
        if (checkIfWallIsNotOnTheWay()) {
            if (pacMan.getPacmanPosition().equals(new Point(0, 10))) {
                newPosition = new Point(26, 10);
            }
            pacMan.setPacmanPosition(newPosition);
        }
    }

    /**
     * Pacman move right
     */
    private void moveRight() {
        newPosition = new Point(pacMan.getPacmanPosition().getX() + 1, pacMan.getPacmanPosition().getY());
        if (checkIfWallIsNotOnTheWay()) {
            if (pacMan.getPacmanPosition().equals(new Point(26, 10))) {
                newPosition = new Point(0, 10);
            }
            pacMan.setPacmanPosition(newPosition);
        }
    }

    /**
     * Abstract method implemented in the controller, allowing visualisation
     */
    public abstract void onMove();

    /**
     * Method removing possibility to pass through walls
     * @return {true} if there are no walls on the way
     */
    private boolean checkIfWallIsNotOnTheWay() {
        return walls.getPoints().stream()
                .noneMatch(point -> point.equals(newPosition));
    }

    /**
     * Eating method, visualisation and points
     */
    private void eat() {
        if (checkSmallFood()) {
            food.getSmallFood().remove(newPosition);
            addPoints(10);
        }
        if (checkBigFood()) {
            food.getBigFood().remove(newPosition);
            addPoints(50);
        }
    }

    /**
     * Adding points after eating food
     * @param points int
     */
    private void addPoints(int points) {
        score.addPoints(points);
    }

    /**
     * Checks if player position matches big food position
     * @return {true} if positions are matched
     */
    private boolean checkBigFood() {
        return food.getBigFood().stream()
                .anyMatch(point -> point.equals(newPosition));
    }
    /**
     * Checks if player position matches small food position
     * @return {true} if positions are matched
     */
    private boolean checkSmallFood() {
        return food.getSmallFood().stream()
                .anyMatch(point -> point.equals(newPosition));
    }

    /**
     * Method used to calculate best ghost route
     * @param point input
     * @return {int} distance from ghost to pacman in X + Y direction
     */
    private int calculatePacmanPosition(Point point) {
        Point pacMan = getPacMan().getPacmanPosition();
        return Math.abs(pacMan.getX() - point.getX()) + Math.abs(pacMan.getY() - point.getY());
    }

    /**
     * Method used to decide which route ghost should take
     * @param ghost input
     */
    private void ghostPathChoice(Ghost ghost) {
        int x = ghost.getGhostPosition().getX();
        int y = ghost.getGhostPosition().getY();
        List<Point> ghostSurroundings = Arrays.asList(
                new Point(x + 1, y),
                new Point(x - 1, y),
                new Point(x, y + 1),
                new Point(x, y - 1)
        );

        ghostSurroundings.stream()
                .filter(point -> walls.getPoints().stream().noneMatch(point::equals))
                .forEach(point -> ghost.getGhostMovementFieldList().add(new GhostMovementField(point, calculatePacmanPosition(point))));
    }


    /**
     * Ghost movement method
     * @param ghost input
     */
    private void ghostMove(Ghost ghost) {
        ghostPathChoice(ghost);
        Point newGhostPosition = ghost.getGhostMovementFieldList().stream()
                .filter(ghostMovementField -> ghostMovementField.getScoreValue() == bestFieldToMove(ghost.getGhostMovementFieldList()))
                .map(GhostMovementField::getFieldPoint)
                .findAny()
                .orElse(ghost.getGhostPosition());
        ghost.setGhostPosition(newGhostPosition);
        ghost.getGhostMovementFieldList().removeAll(ghost.getGhostMovementFieldList());
    }

    /**
     * Method calculating best path to pacman
     * @param ghostMovementFields list of ghost fields to move to
     * @return {int} score value of the fastest route to pacman
     */
    private int bestFieldToMove(List<GhostMovementField> ghostMovementFields) {
        return ghostMovementFields.stream()
                .mapToInt(GhostMovementField::getScoreValue)
                .min()
                .getAsInt();

    }

    private void ghostMoveToOwnMapPart(Ghost ghost,  Point point){

    }

    public PacMan getPacMan() {
        return pacMan;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Walls getWalls() {
        return walls;
    }

    public Food getFood() {
        return food;
    }

    public Score getScore() {
        return score;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Ghost getBlinky() {
        return blinky;
    }

    public Ghost getPinky() {
        return pinky;
    }
}

package mainAndControl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import movement.Direction;
import movement.Point;
import pacManGame.Food;
import pacManGame.PacManGame;
import pacManGame.Walls;

import java.io.IOException;

public class Controller {
    @FXML
    private Canvas canvas;
    @FXML
    private HBox scoreBoard;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label timeLabel;
    private final static int POINT_SIZE = 20;
    private GraphicsContext graphicsContext;
    private PacManGame pacManGame;

    public void initialize() throws IOException {
        graphicsContext = canvas.getGraphicsContext2D();
        Walls walls = new Walls("Level1.txt");
        Food food = new Food("Level1.txt");
        styleScoreBoard();
        pacManGame = new PacManGame(walls, food) {
            @Override
            public void onMove() {
                draw();
                updateScore();
            }
        };

        Thread game = new Thread(() -> pacManGame.gameRun());
        game.setDaemon(true);
        game.start();
    }

    private void styleScoreBoard() {
        scoreBoard.setStyle("-fx-background-color: black");
    }

    private void generateWalls(Point point) {
        graphicsContext.fillRect(point.getX() * POINT_SIZE, point.getY() * POINT_SIZE, POINT_SIZE, POINT_SIZE);
    }

    private void generateFood(Point point) {
        if (checkIfFoodIsSmall(point)) {
            graphicsContext.fillOval((point.getX() * POINT_SIZE) + 7, (point.getY() * POINT_SIZE) + 7, 6, 6);
        } else {
            graphicsContext.fillOval((point.getX() * POINT_SIZE) + 3, (point.getY() * POINT_SIZE) + 3, 14, 14);
        }
    }

    private boolean checkIfFoodIsSmall(Point point) {
        return pacManGame.getFood().getSmallFood().stream().anyMatch(point1 -> point1.equals(point));
    }

    private void draw() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphicsContext.setFill(Color.DARKBLUE);
        pacManGame.getWalls().getPoints().forEach(this::generateWalls);
        graphicsContext.setFill(Color.LIGHTBLUE);
        drawPoint(pacManGame.getWalls().getGate());
        graphicsContext.setFill(Color.WHITE);
        pacManGame.getFood().getSmallFood().forEach(this::generateFood);
        pacManGame.getFood().getBigFood().forEach(this::generateFood);
        graphicsContext.setFill(Color.YELLOW);
        drawEntity(pacManGame.getPacMan().getPacmanPosition());
        graphicsContext.setFill(Color.RED);
        drawEntity(pacManGame.getBlinky().getGhostPosition());
        graphicsContext.setFill(Color.PINK);
        drawEntity(pacManGame.getPinky().getGhostPosition());
    }

    private void drawEntity(Point pacmanPosition) {
        graphicsContext.fillOval(pacmanPosition.getX() * POINT_SIZE, pacmanPosition.getY() * POINT_SIZE, POINT_SIZE, POINT_SIZE);
    }

    private void drawPoint(Point point) {
        graphicsContext.fillRect(point.getX() * POINT_SIZE, point.getY() * POINT_SIZE, POINT_SIZE, POINT_SIZE);
    }

    public void keyboardMovement() {
        canvas.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    moveUp();
                    break;
                case S:
                    moveDown();
                    break;
                case A:
                    moveLeft();
                    break;
                case D:
                    moveRight();
                    break;
            }
        });
    }

    private void updateScore() {
        Platform.runLater(() -> scoreLabel.setText("" + pacManGame.getScore().getValue()));
    }

    public void moveUp() {
        pacManGame.setDirection(Direction.UP);
    }

    public void moveDown() {
        pacManGame.setDirection(Direction.DOWN);
    }

    public void moveLeft() {
        pacManGame.setDirection(Direction.LEFT);
    }

    public void moveRight() {
        pacManGame.setDirection(Direction.RIGHT);
    }
}

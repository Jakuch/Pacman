package pacManGame;

import movement.Point;

public class PacMan {
    private Point pacmanPosition;

    public PacMan(Point pacmanPosition) {
        this.pacmanPosition = pacmanPosition;
    }

    public void setPacmanPosition(Point pacmanPosition) {
        this.pacmanPosition = pacmanPosition;
    }

    public Point getPacmanPosition() {
        return pacmanPosition;
    }
}

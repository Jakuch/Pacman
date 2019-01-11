package movement;

public class GhostMovementField {
    private Point fieldPoint;
    private int scoreValue;

    public GhostMovementField(Point fieldPoint, int scoreValue) {
        this.fieldPoint = fieldPoint;
        this.scoreValue = scoreValue;
    }

    public Point getFieldPoint() {
        return fieldPoint;
    }

    public int getScoreValue() {
        return scoreValue;
    }
}

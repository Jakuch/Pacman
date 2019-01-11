package ghosts;

import movement.GhostMovementField;
import movement.Point;

import java.util.LinkedList;
import java.util.List;

public abstract class Ghost {
    private Point ghostPosition;
    private List<GhostMovementField> ghostMovementFieldList;

    public Ghost(Point ghostPosition) {
        this.ghostPosition = ghostPosition;
        this.ghostMovementFieldList = new LinkedList<>();
    }

    public Point getGhostPosition() {
        return ghostPosition;
    }

    public void setGhostPosition(Point ghostPosition) {
        this.ghostPosition = ghostPosition;
    }

    public List<GhostMovementField> getGhostMovementFieldList() {
        return ghostMovementFieldList;
    }

    public void setGhostMovementFieldList(List<GhostMovementField> ghostMovementFieldList) {
        this.ghostMovementFieldList = ghostMovementFieldList;
    }

    public abstract void chase();
}

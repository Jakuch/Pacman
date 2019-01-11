package pacManGame;

public class Score {
    private int value;

    public Score() {
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void addPoints(int points){
        value += points;
    }
}

package pacManGame;

import movement.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Walls {
    private List<Point> points;
    private Point gate;

    public Walls(String fileName) throws IOException {
        points = new ArrayList<>();
        Path path = Paths.get(fileName);
        List<String> fileLines = Files.readAllLines(path);
        createPointsFromFile(fileLines);
    }

    public List<Point> getPoints() {
        return points;
    }

    public Point getGate() {
        return gate;
    }

    private void createPointsFromFile(List<String> fileLines) {
        int x = fileLines.size();
        for (int i = 0; i < x; i++) {
            int y = fileLines.get(i).length();
            String fileLine = fileLines.get(i);
            for (int j = 0; j < y; j++) {
                switch (fileLine.charAt(j)) {
                    case '+':
                        points.add(new Point(j, i));
                        break;
                    case 'G':
                        gate = new Point(j, i);
                        break;
                    case ' ':
                    case 'o':
                    case 'B':
                    case 'P':
                        break;
                }
            }
        }
    }
}

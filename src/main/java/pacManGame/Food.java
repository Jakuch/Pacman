package pacManGame;

import movement.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Food {
    private List<Point> smallFood;
    private List<Point> bigFood;

    public Food(String fileName) throws IOException {
        smallFood = new ArrayList<>();
        bigFood = new ArrayList<>();
        Path path = Paths.get(fileName);
        List<String> fileLines = Files.readAllLines(path);
        createFoodFromFile(fileLines);
    }

    private void createFoodFromFile(List<String> fileLines) {
        int x = fileLines.size();
        for (int i = 0; i < x; i++) {
            int y = fileLines.get(i).length();
            String fileLine = fileLines.get(i);
            for (int j = 0; j < y; j++) {
                switch (fileLine.charAt(j)) {
                    case ' ':
                        smallFood.add(new Point(j, i));
                        break;
                    case 'B':
                        bigFood.add(new Point(j, i));
                        break;
                    case '+':
                    case 'G':
                    case 'o':
                    case 'P':
                        break;
                }
            }
        }
    }

    public List<Point> getSmallFood() {
        return smallFood;
    }

    public List<Point> getBigFood() {
        return bigFood;
    }
}

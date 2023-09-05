import java.awt.*;
import java.util.ArrayList;

public class Snake {
    int score = 0;
    static int partSize = 10;
    static double speedMultiplier = 1;
    ArrayList<Point> bodyParts = new ArrayList<>();

    public Snake(int x, int y){
        bodyParts.add(new Point(x, y));
    }

    public void addBodyPart(){
        bodyParts.add(new Point(bodyParts.get(bodyParts.size()-1).x, bodyParts.get(bodyParts.size()-1).y));

        if(speedMultiplier == .5)
            score++;
        else if (speedMultiplier == 1)
            score+=2;
        else
            score+=3;
    }

    public String getMapSize(){
        if(partSize==20)
            return "Small";
        else if(partSize == 10)
            return "Normal";
        else
            return "Big";
    }

    public String getDifficulty(){
        if(speedMultiplier == .5)
            return "Easy";
        else if(speedMultiplier == 1)
            return "Normal";
        else
            return "Hard";
    }
    public static void setPartSize(int partSize) {
        Snake.partSize = partSize;
    }
    public ArrayList<Point> getBodyParts() {
        return bodyParts;
    }
    public int getSize(){
        return bodyParts.size();
    }

}


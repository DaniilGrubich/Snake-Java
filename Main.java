import javax.swing.*;
import javax.swing.plaf.synth.SynthDesktopIconUI;
import java.awt.*;


public class Main {

    static GameArea gameArea;

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        try {
            gameArea = new GameArea();
        }catch (Exception e){
            if(!e.equals("java.util.ConcurrentModificationException")) {
                JOptionPane.showMessageDialog(null, "Please, restart the game");
            }
        }
        runGame();

    }

    public static void runGame(){

    }
}


class myDimension extends Dimension {
    myDimension(int w, int h) {
        super(w, h);
    }
    @Override
    public String toString() {
        return (int) getWidth() + "x" + (int) getHeight();
    }
}


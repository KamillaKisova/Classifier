import java.awt.*;
import java.util.ArrayList;

public class MyCanvas extends Canvas {

    static final int PLUS = 500;
    private static final int R = 0;
    private static final int G = 1;
    private static final int B = 2;
    private static final int P = 3;
    private static final int COLOUR = 0;
    private static final int X = 2;
    private static final int Y = 3;
    private ArrayList<int[]> positions;

    public MyCanvas(ArrayList<int[]> positions){
        setBackground(Color.WHITE);
        setSize(1000, 1000);
        this.positions = positions;
    }

    public void drawPoint(Graphics g, int[] pos){
        if(pos[COLOUR] == R)
            g.setColor(Color.RED);
        else if(pos[COLOUR] == G)
            g.setColor(Color.GREEN);
        else if(pos[COLOUR] == B)
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.PINK);
        g.fillOval(pos[X]/10-3+PLUS, pos[Y]/10-3+PLUS, 6, 6);
    }

    public void paint(Graphics g){
        for(int[] pos : positions){
            drawPoint(g, pos);
        }
    }
}

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MyFrame extends Frame {

    public MyFrame(ArrayList<int[]> positions){
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setName("Classifier");
        add(new MyCanvas(positions));
        setSize(1000,1000);
        setLayout(null);
        setVisible(true);
    }

}

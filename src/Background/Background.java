package Background;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial") 
public class Background extends JPanel {
    final static int xSize=1638;
    final static int ySize=920;
    Toolkit kit = Toolkit.getDefaultToolkit(); 
    Image background = kit.getImage("Resources/DabbingBackground.png"); 
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(background, 0, 0, xSize, ySize,this); 
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Sample Frame");
        Background background= new Background();
        frame.add(background);
        frame.setSize(xSize, ySize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        while (true) {
            background.repaint();
            Thread.sleep(15);
        }
    }
}
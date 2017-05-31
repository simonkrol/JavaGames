package BasicAnimation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Game extends JPanel {
    int x = 0;
    int y = 0;
    int xIncrement=1;
    int yIncrement=1;
    final static int xSize=300;
    final static int ySize=400;
    final static int ballSize=30;
    private void moveBall() {
    	x = x +xIncrement;
        y = y + yIncrement;
    	if(x==xSize-2*ballSize || x==0){
    		xIncrement=xIncrement*(-1);
    	}
    	if(y==ySize-ballSize*2.2 || y==0){
    		yIncrement=yIncrement*(-1);
    	}
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillOval(x, y, ballSize, ballSize);
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Sample Frame");
        Game game = new Game();
        frame.add(game);
        frame.setSize(xSize, ySize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true) {
            game.moveBall();
            game.repaint();
            Thread.sleep(15);
        }
    }
}
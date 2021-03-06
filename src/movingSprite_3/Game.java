package movingSprite_3;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


@SuppressWarnings("serial")

public class Game extends JPanel {
	final static double time=0.030;
	Background currBack;
	List<Character> character_entities=new ArrayList<Character>();
	
	private static JPanel mainPanel;
	private static Direction direction=new Direction();
	
	public Game(int x, int y, String dest){
		currBack=new Background(x,y,dest, 5);
	}
    static int xSize=1200;
    static int ySize=600;
    static Game game;
    static int pixDist;
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        xSize=getWidth();//Resize the given image
        ySize=getHeight();
        g2d.drawImage(game.currBack.background, 0, 0, xSize, ySize,this);
        for (Character temp : character_entities){
        	if(temp.mainChar)
        	{
        		if(direction.right && !direction.left)
        		{
        			temp.xLoc+=(xSize*time*temp.speedMod/xSize)/currBack.length;
        		}
        		else if(direction.left && !direction.right)
        		{
        			temp.xLoc-=(xSize*time*temp.speedMod/xSize)/currBack.length;
        		}
        		temp.setY(direction.jump);
        	}
        	g2d.drawImage(temp.sprite, (int)(temp.xLoc*xSize), (int)(temp.yLoc*ySize),(int)(temp.xSize*xSize), (int)(temp.ySize*ySize), this);
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    }
    public void addChar(double x, double y,double xS, double yS, String sprite, boolean main){
    	Character newChar= new Character(x,y,xS, yS,sprite, main, time);
    	newChar.setSpeed(1.6);
    	character_entities.add(newChar);
    }

    

	public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Game Frame");
        game=new Game(xSize,ySize, "Resources/DabbingBackground.png");
        game.addChar(0.2, 0.6, 0.05, 0.20, "Resources/MinecraftSprite.png", true);
        frame.add(KeyInputPanel());//Add Key Reception
        frame.add(game);
        frame.setSize(xSize, ySize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        while (true) {
            game.repaint();
            Thread.sleep((int)(time*1000));
        }
    }
	static JPanel KeyInputPanel(){
		mainPanel=new JPanel();
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("UP"), "jump");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released UP"), "jumpR");
		mainPanel.getActionMap().put("jump", new MoveAction("jump"));
		mainPanel.getActionMap().put("jumpR", new MoveAction("jumpR"));
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released RIGHT"), "rightR");
		mainPanel.getActionMap().put("right", new MoveAction("right"));
		mainPanel.getActionMap().put("rightR", new MoveAction("rightR"));
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released LEFT"), "leftR");
		mainPanel.getActionMap().put("left", new MoveAction("left"));
		mainPanel.getActionMap().put("leftR", new MoveAction("leftR"));
		return mainPanel;
		
	}
	static class MoveAction extends AbstractAction
    {
		String action;
		public MoveAction(String command){
			action=command;
		}
        public void actionPerformed( ActionEvent tf )
        {
        	switch(action)
        	{
        		case"jump":direction.jump=true;break;
        		case"jumpR":direction.jump=false;break;
        		case"right":direction.right=true;break;
        		case"rightR":direction.right=false;break;
        		case"left":direction.left=true;break;
        		case"leftR":direction.left=false;break;
        	}
            
        } // end method actionPerformed()
        
    } // end class EnterAction
	static class Direction
	{
		boolean right;
		boolean left;
		boolean jump;
		public Direction()
		{
			right=false;
			left=false;
			jump=false;
		}
			
	}
}
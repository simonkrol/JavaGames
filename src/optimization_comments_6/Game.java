package optimization_comments_6;

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
	MapGen map;
	List<Character> character_entities=new ArrayList<Character>();
	
	private static JPanel mainPanel;
	private static Direction direction=new Direction();
	
	public Game(){
		map =new MapGen();
	}
    static int xSize;
    static int ySize;
    static Game game;
    static int pixDist;
    static double movementL, movementR;
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        xSize=getWidth();//Resize the given image
        ySize=getHeight();
        map.draw(g2d,xSize, ySize, this);
        //g2d.drawImage(game.currBack.background, 0, 0, xSize, ySize,this);
        for (Character temp : character_entities)
        {
        	temp.checkYCollision(map,(int)(temp.xLoc*map.blocksWide));
        	movementL=(xSize*time*temp.speedMod/xSize)/(map.blocksWide/4);
        	movementR=movementL;
        	if(temp.xLoc<=0)
        	{
        		movementL=0;
        	}
        	if(temp.xLoc+temp.xSize>=0.99)
        	{
        		movementR=0;
        	}
        	switch(temp.checkXCollision(map, (int)(temp.xLoc*map.blocksWide)))
        	{
        	case"left":movementL=0;break;
        	case"right":movementR=0;break;
        	}
       		if(direction.right && !direction.left)
        	{
        		temp.xLoc+=movementR;
        	}
        	else if(direction.left && !direction.right)
        	{
        		temp.xLoc-=movementL;
        	}
        	temp.setY(direction.jump);
        	
        	g2d.drawImage(temp.sprite, (int)(temp.xLoc*xSize), (int)(temp.yLoc*ySize),(int)(temp.xSize*xSize), (int)(temp.ySize*ySize), this);
        }
        /*for (Platform temp : currBack.platforms)
        {
           g2d.drawImage(currBack.platformImage, (int)(temp.xPos*xSize), (int)(temp.yPos*ySize),(int)(temp.xSize*xSize), (int)(temp.ySize*ySize), this);
        }*/
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    }
    public void addChar(double x, double y,String sprite){
    	Character newChar= new Character(x,y,0.7/game.map.blocksWide, 2.8/game.map.blocksWide,sprite, time);
    	character_entities.add(newChar);
    }

    

	public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Game Frame");
        frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
        game=new Game();
        game.addChar(0.15, 0,"Resources/MinecraftSprite.png");
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
		mainPanel.getActionMap().put("jump", new Action("jump"));
		mainPanel.getActionMap().put("jumpR", new Action("jumpR"));
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "right");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released RIGHT"), "rightR");
		mainPanel.getActionMap().put("right", new Action("right"));
		mainPanel.getActionMap().put("rightR", new Action("rightR"));
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "left");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released LEFT"), "leftR");
		mainPanel.getActionMap().put("left", new Action("left"));
		mainPanel.getActionMap().put("leftR", new Action("leftR"));
		return mainPanel;
		
	}
	static class Action extends AbstractAction
    {
		String action;
		public Action(String command){
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
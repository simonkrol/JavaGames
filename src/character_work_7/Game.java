package character_work_7;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


@SuppressWarnings("serial")

public class Game extends JPanel {
	final static double time=0.030;//The time between cycles, 1.0/time gives an average fps
	MapGen map;
	static List<Character> character_entities=new ArrayList<Character>();
	int currMain=0;
	static Character mainChar;
	static Character tempChar;
	private static JPanel mainPanel;
	
	
	public Game(){
		map =new MapGen();
	}
    static int xSize;
    static int ySize;
    static Game game;
    BufferedImage sprite;
   
    
    
    
    public void paint(Graphics g) {
        super.paint(g);
        
        xSize=getWidth();
        ySize=getHeight();
        mainChar=character_entities.get(currMain);
        Graphics2D g2d = (Graphics2D) g;
        map.draw(g2d,xSize, ySize, this);//Draws the clouds and grass/dirt
    	boolean changeY;
        boolean changeX;
        for(Character temp:character_entities)
        {
        	changeY=temp.setY(map);//Deal with the y axis
            changeX=temp.setX(map);
            if(changeX && temp.direction.right)temp.right=true;
            else if(changeX)temp.right=false;
        	if(changeY&& temp.jumped)
        	{
        		if(temp.justjumped)
        		{
	        		temp.animationIndex=0;
	        		temp.spriteIndex=0;
	        	}
	        	else if(temp.animationIndex!=2)
	        	{
	        		temp.spriteIndex=0;
	        		temp.animationIndex=2;
	        		temp.repeat=false;
	        	}
        		temp.spriteIndex++;
	        }
	        else if(changeX)
	        {
	        	if(temp.animationIndex!=1)
	        	{
	        		temp.spriteIndex=0;
	        		temp.animationIndex=1;
	        		temp.repeat=true;
	        	}
	        	temp.spriteIndex++;
	        }
	        else
	        {
	        	temp.animationIndex=0;
	        	temp.spriteIndex=0;
	        }
        	sprite=temp.getAnimationSprite();
        	g2d.drawImage(sprite,(int)(temp.xLoc*xSize), (int)(temp.yLoc*ySize),(int)(temp.xSize*xSize*1.3), (int)(temp.ySize*ySize), this);
        }
        
       
     
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,//Idk why these are here, not sure what they do
           // RenderingHints.VALUE_ANTIALIAS_ON);
    }

    

	public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Game Frame");
        frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH ); //Maximize the frame
        game=new Game();
        mainChar=new Character(0.3,0.15,0.7/game.map.blocksWide,2.8/game.map.blocksWide, time, "Blue");
        character_entities.add(mainChar);
        mainChar=new Character(0,0.15,0.7/game.map.blocksWide, 2.8/game.map.blocksWide, time,"Orange");
        character_entities.add(mainChar);
        frame.add(KeyInputPanel());//Add Key Reception
        frame.add(game);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        while (true) {
            game.repaint();//Repaint the game every time*1000 milliseconds
            Thread.sleep((int)(time*1000));
        }
    }
	/*
	 * Allow for the user to input keys.
	 */
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
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "space");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released SPACE"), "spaceR");
		mainPanel.getActionMap().put("space", new Action("space"));
		mainPanel.getActionMap().put("spaceR", new Action("spaceR"));
		return mainPanel;
		
	}
	/*
	 * Perform an action based on the given command
	 */
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
        		case"jump":mainChar.direction.jump=true;break;
        		case"jumpR":mainChar.direction.jump=false;break;
        		case"right":mainChar.direction.right=true;break;
        		case"rightR":mainChar.direction.right=false;break;
        		case"left":mainChar.direction.left=true;break;
        		case"leftR":mainChar.direction.left=false;break;
        		case"space":mainChar.direction.superJump=true;break;
        		case"spaceR":mainChar.direction.superJump=false;break;
        	}
            
        } // end method actionPerformed()
        
    } // end class EnterAction
	
	/*
	 * Direction class, used to determine which way to move the character
	 */
	static class Direction
	{
		boolean right;
		boolean left;
		boolean jump;
		boolean superJump;
		public Direction()
		{
			right=false;
			left=false;
			jump=false;
			superJump=false;
		}
			
	}
}
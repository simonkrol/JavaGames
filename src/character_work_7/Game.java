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
	//List<Character> character_entities=new ArrayList<Character>();
	static Character character;
	private static JPanel mainPanel;
	private static Direction direction=new Direction();
	public int facing=1;
	
	public Game(){
		map =new MapGen();
	}
    static int xSize;
    static int ySize;
    static Game game;
    static int pixDist;
    static double movementL, movementR;
    int movement_index=0;
    int super_index=0;
    BufferedImage sprite;
    
    static int last=0;
    
    
    
    public void paint(Graphics g) {
        super.paint(g);
        
        xSize=getWidth();//Resize the given image
        ySize=getHeight();
        
        Graphics2D g2d = (Graphics2D) g;
        map.draw(g2d,xSize, ySize, this);
        //g2d.drawImage(game.currBack.background, 0, 0, xSize, ySize,this);
        
        character.checkYCollision(map,(int)(character.xLoc*map.blocksWide));
        movementL=(xSize*time*character.speedMod/xSize)/(map.blocksWide/4);
        
        movementR=movementL;

        if(character.onGround)super_index=0;
        if(character.xLoc<=0)movementL=0;//Make sure the character is within the boundaries
        if(character.xLoc+character.xSize>=1)movementR=0;
        
        switch(character.checkXCollision(map, (int)(character.xLoc*map.blocksWide)))//Prevent movement in the direction of collision
        {
        	case"left":movementL=0;break;
        	case"right":movementR=0;break;
        }
       	if(direction.right && !direction.left&&character.onGround)
       	{
       		last=1;
       		movement_index++;
       		character.xLoc+=movementR;//Move
       		facing=1;
       	}
        else if(direction.left && !direction.right&&character.onGround)
        {
        	last=2;
        	movement_index++;
        	character.xLoc-=movementL;
        	facing=0;
        }
        else if(!character.onGround)
        {
        	
        	if(super_index!=0 || (direction.superJump&&direction.jump))super_index++;
        	if(character.jumped){
	        	if(last==1)
	        	{
	        		if(direction.right)character.xLoc+=movementR*0.17;
	        		if(direction.left)character.xLoc-=movementR*0.5;
	        		character.xLoc+=movementR;
	        	}
	        	else if(last==2)
	        	{
	        		if(direction.left)character.xLoc-=movementL*0.17;
	        		if(direction.right)character.xLoc+=movementL*0.5;
	        		character.xLoc-=movementL;
	        	}
	        	else
	        	{
	        		if(direction.left)character.xLoc-=movementL*0.4;
	        		if(direction.right)character.xLoc+=movementL*0.4;
	        		
	        	}
	        	movement_index=0;
        	}
        	else
        	{
        		if(direction.right)character.xLoc+=movementR*0.7;
        		if(direction.left)character.xLoc-=movementR*0.7;
        	}
        }
        else
        {
        	last=0;
        	movement_index=0;
        }
        
        character.setY(direction.jump, direction.superJump);//Deal with the y axis
        if(character.onGround||super_index==0)sprite=Character.getSprite(facing,(movement_index%32+7)/8);
        else sprite=Character.getSprite(facing, super_index/5+5);
        g2d.drawImage(sprite,(int)(character.xLoc*xSize), (int)(character.yLoc*ySize),(int)(character.xSize*xSize*1.3), (int)(character.ySize*ySize), this);
        //Redraw the character in their new position
     
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,//Idk why these are here, not sure what they do
            RenderingHints.VALUE_ANTIALIAS_ON);
    }

    

	public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Game Frame");
        frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH ); //Maximize the frame
        game=new Game();
        character = new Character(0,0.15,0.7/game.map.blocksWide, 2.8/game.map.blocksWide, time);
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
        		case"jump":direction.jump=true;break;
        		case"jumpR":direction.jump=false;break;
        		case"right":direction.right=true;break;
        		case"rightR":direction.right=false;break;
        		case"left":direction.left=true;break;
        		case"leftR":direction.left=false;break;
        		case"space":direction.superJump=true;break;
        		case"spaceR":direction.superJump=false;break;
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
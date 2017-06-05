package character_work_7;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
	static Character secondaryChar;
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
        Graphics2D g2d = (Graphics2D) g;
        if(MapGen.mapPainter==null)
        {
        	MapGen.setPainter(this);
        	Character.setPainter(this);
        }
        //g2d.drawImage(clouds, 0, 0, xSize, ySize, this);
        map.draw(xSize, ySize, g2d);//Draws the clouds and grass/dirt
        for(Character temp:character_entities)
        {
        	temp.draw(g2d,xSize,ySize,map, this);
        }
        
       
     
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,//Idk why these are here, not sure what they do
           // RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    public static Character addChar(String colour, String ability)
    {
    	tempChar=new Character(ThreadLocalRandom.current().nextInt(0, 11)/10.0, 0.3,0.7/game.map.blocksWide,2.8/game.map.blocksWide, time, colour, ability);
    	character_entities.add(tempChar);
    	return tempChar;
    }
	public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Game Frame");
        frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH ); //Maximize the frame
        game=new Game();
        mainChar=addChar("Red", "lightningbolt");
        //secondaryChar=addChar("Red", "lightningbolt");
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
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released DOWN"), "downR");
		mainPanel.getActionMap().put("down", new Action("down"));
		mainPanel.getActionMap().put("downR", new Action("downR"));
		
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
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("W"), "w");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released W"), "wR");
		mainPanel.getActionMap().put("w", new Action("w"));
		mainPanel.getActionMap().put("wR", new Action("wR"));
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("A"), "a");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released A"), "aR");
		mainPanel.getActionMap().put("a", new Action("a"));
		mainPanel.getActionMap().put("aR", new Action("aR"));
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("D"), "d");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released D"), "dR");
		mainPanel.getActionMap().put("d", new Action("d"));
		mainPanel.getActionMap().put("dR", new Action("dR"));
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("S"), "s");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released S"), "sR");
		mainPanel.getActionMap().put("s", new Action("s"));
		mainPanel.getActionMap().put("sR", new Action("sR"));
		
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("SHIFT"), "shift");
		mainPanel.getInputMap().put(KeyStroke.getKeyStroke("released SHIFT"), "shiftR");
		mainPanel.getActionMap().put("shift", new Action("shift"));
		mainPanel.getActionMap().put("shiftR", new Action("shiftR"));
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
        		case"down":mainChar.direction.ability=true;break;
        		case"downR":mainChar.direction.ability=false;break;
        		case"space":mainChar.direction.ability=true;break;
        		case"spaceR":mainChar.direction.ability=false;break;
        		
        		case"w":secondaryChar.direction.jump=true;break;
        		case"wR":secondaryChar.direction.jump=false;break;
        		case"d":secondaryChar.direction.right=true;break;
        		case"dR":secondaryChar.direction.right=false;break;
        		case"a":secondaryChar.direction.left=true;break;
        		case"aR":secondaryChar.direction.left=false;break;
        		case"s":secondaryChar.direction.ability=true;break;
        		case"sR":secondaryChar.direction.ability=false;break;
        		
        		case"shiftR":tempChar=mainChar;mainChar=secondaryChar;secondaryChar=tempChar;mainChar.direction.reset();secondaryChar.direction.reset();break;
        		
        	}
            
        } // end method actionPerformed()
        
    } // end class EnterAction
}
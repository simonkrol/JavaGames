package character_work_7;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;



public class Character {
	Toolkit kit = Toolkit.getDefaultToolkit(); //Used to gather images 
	public Direction direction=new Direction();
	BufferedImage spriteSheet;
	/*
	 * Store the left most point of feet(x and y) of the first sprite
	 * Store left and right distance
	 * Store up and down distance
	 * Store x distance to left most point of feet of next sprite
	 */
	static Sprite base;
	static Painter charPainter;
	List<Sprite> sprites=new ArrayList<Sprite>();
	static int[][] spriteVals={{80,9,17,30,76,1,10,69},{58,99,17,22,80,4,10}};
	//Distance to middle in X, distance in y, left side width, right side width, distance to new sprite, number of sprites, lapses per change, height
	static int[][] spriteValues={{63,9,0,46,69,1,1,0},{40,99,80,40,69,4,11,0},{99,179,64,46,79,2,14,0},{615,189,65,46,166,4,9,0},{494,90,135,127,69,4,11,85}};//For each animation(the first number), store the initial x jump, the y jump, the x between each sprite, the x size and the y size
	double xSize,ySize,yVel=0,yAcc=-9.81,xVel=0, time,speedMod=1, xLoc, yLoc; //Class variables
	boolean onGround=false;
	boolean jumped=false;
	boolean justJumped=false;
	int spriteIndex=0;
	int animationIndex=0;
	boolean repeat=true;
	boolean right=true;
	boolean abilAn=false;
	Ability ability;
	public Character(double x,double y,double xS, double yS, double t, String colour, String power)
	{
		xLoc=x;		//x and y Loc are the location on the screen, x and y Size are the size of the character in each direction
		yLoc=y;
		xSize=xS;
		ySize=yS;
		ability=new Ability(30,power);
		spriteSheet=loadSprite("Resources/Mage/"+colour+"Complete.png");
		time=t;//Time is the time between frames
		base=new Sprite(67,30,78,4,42,69,0,0,1,10, true, spriteSheet);
		sprites.add(base);
		Sprite.setTime(time);
		sprites.add(new Sprite(40,30,167,0,48,68,0,80,4,0.18,true,spriteSheet));
		sprites.add(new Sprite(99,28,257,0,45,78,0,65,2,0.18,true,spriteSheet));
		sprites.add(new Sprite(615,38,310,0,44,118,49,67,4,0.12,false,spriteSheet));
		sprites.add(new Sprite(494,29,159,0,127,69,0,135,4,0.12, false, spriteSheet));
		sprites.add(new Sprite(252,34,434,53,84,61,0,146,11,0.09, false, spriteSheet));
	}
	public static void setPainter( ImageObserver IO)
	{
		charPainter=new Painter(IO);
	}
	public static BufferedImage loadSprite(String dest) {

        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(new File(dest));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sprite;
    }
	public void jump(double multi)
	{
		if(onGround)
		{
			justJumped=true;
			yVel=8.022*multi;
			yAcc=-25.01;
			onGround=false;
			jumped=true;
		}

	}
	public boolean setY(MapGen map)
	{
		justJumped=false;
		int current = (int) (xLoc*map.blocksWide);
		checkYCollision(map,current);
		if(direction.jump)jump(1);
		if(!onGround)	//Calculate actual changes in motion and position	
		{
			yLoc+=(yVel*time*ySize/2);//Convert from m/s to pixels per 0.015 seconds
			yVel+=yAcc*time;//Reduce current velocity
			return true;
		}
		return false;
		
	}
	public boolean setX(MapGen map)
	{
		int current = (int) (xLoc*map.blocksWide);
		String collision=checkXCollision(map,current);
		xVel=(time*speedMod)/(map.blocksWide/2.5);
		if(xLoc<0)
		{
			if(direction.left)xVel=0;
		}
		else if(xLoc+xSize>1)if(direction.right)xVel=0;
		switch(collision)
		{
			case "right":if(direction.right)xVel=0;break;
			case "left":if(direction.left)xVel=0;break;
		}
		if(direction.left && !direction.right)xLoc-=xVel;
		else if(direction.right && !direction.left)xLoc+=xVel;
		else return false;
		return true;
	}
	public void setGround(){	//Move character to the ground
		jumped=false;
		onGround=true;
		yVel=0;
		//yAcc=0;
	}
	/*
	 * Check if a collision occurs with the bottom of the character's feet or the top of their head
	 * The map tells us where each platform is and current indicates which vertical strip the character is currently in
	 */
	public void checkYCollision(MapGen map,int current)
	{
		if(current<=1)current=1;
		else if(current>=map.blocksWide)current=map.blocksWide-2;
		Platform temp;
		boolean collide=false;
		for(int i=-1; i<2;i++)
		{
			temp=map.platforms.get(current+i);//Get the platforms and check for collisions
			if(temp.xPos+temp.xSize-xLoc>0.2/map.blocksWide && xLoc+xSize-temp.xPos>0.2/map.blocksWide)
			{
				//System.out.println(yLoc);
				//System.out.println(temp.yPos+temp.ySize);
				if(yLoc<temp.yPos+temp.ySize)
				{
					if(yLoc+0.1/map.blocksTall<temp.yPos+temp.ySize && yLoc-temp.yPos-temp.ySize>-0.8/map.blocksTall)yLoc+=0.005;
					if(yVel<0)
					{
						setGround();
					}
					collide=true;
				}
			}
			
			
		}
		if(!collide)//If no collisions has occurred, the character is permitted to fall
		{
			onGround=false;
		}
	}
	/*
	 * Check if a collision occurs with the side of the character's body
	 * The map tells us where each platform is and current indicates which vertical strip the character is currently in
	 */
	public String checkXCollision(MapGen map, int current)
	{
		if(current<=1)current=1;
		else if(current>=map.blocksWide)current=map.blocksWide-2;
		Platform temp;
		for(int i=-1; i<2;i++)
		{
			temp=map.platforms.get(i+current);
			if(temp.yPos+temp.ySize-yLoc>0.2/map.blocksWide && yLoc+ySize-temp.yPos>0.2/map.blocksWide)//Probably create a variable for this so we dont have to keep calculating it
			{
				
				if(xLoc<=temp.xPos+temp.xSize && xLoc+xSize>=temp.xPos)
				{
					if(temp.xPos+temp.xSize-xLoc<0.02)//Determine which side collided and return the response 
					{
						return "left";
						
					}
					else if(xLoc+xSize-temp.xPos<0.02)
					{
						return "right";
					}
					
					
				}
				
			}
		}
		return "null";
	}

	
	
	public BufferedImage Animate(MapGen map)
	{
		if(direction.ability)abilities();
		setY(map);
        boolean changeX=setX(map);
        if(changeX && direction.right)right=true;//Find out which way the character moved
        else if(changeX)right=false;
        
        if(ability.active)
        {
        	
        	if(sprites.get(animationIndex).pastFinal(spriteIndex))ability.active=false;
        	spriteIndex++;
        }
        else if(!onGround&&jumped)
        {
        	if(animationIndex!=2)
        	{
        		animationIndex=2;
        		spriteIndex=0;
        	}
        	spriteIndex++;
        }
        else if(changeX)
        {
        	if(animationIndex!=1)
        	{
        		spriteIndex=0;
        		animationIndex=1;
        	}
        	spriteIndex++;
        	
        }
        else animationIndex=0;
    	return sprites.get(animationIndex).getSprite(spriteIndex, right );
	}
	public void abilities()
	{
		if(direction.ability)
		{
			switch(ability.name)
			{
				case"superjump":if(onGround)
				{
					ability.active=true;
					jump(2.0);
					animationIndex=ability.animationIndex;
		        	spriteIndex=0;
				}break;
				default:
				{
					if(!ability.active)
					{
						animationIndex=ability.animationIndex;
						spriteIndex=0;
						ability.active=true;
					}
				}
			}
		}
	}
	public void draw(Graphics2D g2d, int xScreen, int yScreen, MapGen map, ImageObserver newThis)
	{
		

		BufferedImage image=Animate(map);
		Sprite temp=sprites.get(animationIndex);
		double xVal= (xLoc-(temp.left)*xSize/base.right);
		if(animationIndex==4)
		{
			if(right)xVal-=(0.18/map.blocksWide);
			else xVal+=(0.18/map.blocksWide);
		}
		if(!right)xVal+=(temp.left+temp.x2-temp.right)*xSize/base.right;
		xVal+=(0.15/map.blocksWide);
		//else xVal+=((temp.right-temp.x2)*xSize/base.right);
		
    	double yVal=yLoc-(temp.down*ySize/base.up);
    	double wid=(xSize*(double)(temp.left+temp.right)/(double)(base.left+base.right));
    	double hei=(ySize*(double)(temp.up+temp.down)/(double)(base.up+base.down));
    	charPainter.paint(image, (int)(2+xVal*xScreen), (int)(yVal*yScreen), (int)(xScreen*wid),(int)( hei*yScreen), yScreen, g2d);
    	//g2d.drawImage(image,xVal,(int)(yScreen*yVal),(int)(xScreen*wid),(int)(yScreen*hei), newThis);
	}
	
   
}
class Direction
{
	boolean right;
	boolean left;
	boolean jump;
	boolean ability;
	public Direction()
	{
		right=false;
		left=false;
		jump=false;
		ability=false;
	}
	
	public void reset()
	{
		right=false;
		left=false;
		jump=false;
		ability=false;
	}
		
}

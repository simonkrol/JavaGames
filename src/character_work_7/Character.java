package character_work_7;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import character_work_7.Game.Direction;

public class Character {
	Toolkit kit = Toolkit.getDefaultToolkit(); //Used to gather images 
	public Direction direction=new Direction();
	BufferedImage spriteSheet;
	static int[][] spriteValues={{63,9,0,46,69,1,1,0},{40,99,80,40,69,4,5,0},{99,179,64,46,79,2,7,0},{615,189,65,46,166,4,5,0},{494,90,135,127,69,4,5,85}};//For each animation(the first number), store the initial x jump, the y jump, the x between each sprite, the x size and the y size
	double xSize,ySize,yVel=0,yAcc=-9.81,xVel=0, time,speedMod=1, xLoc, yLoc; //Class variables
	boolean onGround=false;
	boolean jumped=false;
	boolean justjumped=false;
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
			justjumped=true;
			yVel=8.322*multi;
			yAcc=-28.01;
			onGround=false;
			jumped=true;
		}

	}
	public boolean setY(MapGen map)
	{
		justjumped=false;
		int current = (int) (xLoc*map.blocksWide);
		checkYCollision(map,current);
		if(direction.jump)jump(1);
		if(!onGround)	//Calculate actual changes in motion and position	
		{
			yLoc-=(yVel*time*ySize/2);//Convert from m/s to pixels per 0.015 seconds
			yVel+=yAcc*time;//Reduce current velocity
			return true;
		}
		return false;
		
	}
	public boolean setX(MapGen map)
	{
		int current = (int) (xLoc*map.blocksWide);
		String collision=checkXCollision(map,current);
		xVel=(time*speedMod)/(map.blocksWide/4);
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
				if(yLoc+ySize>=temp.yPos)
				{
					if(yLoc+ySize-temp.yPos>0.1/map.blocksTall && yLoc+ySize-temp.yPos<0.8/map.blocksTall)yLoc-=0.005;
					if(yVel<0)
					{
						yVel=0;
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
	public BufferedImage getAnimationSprite()
	{
		int temp[]=spriteValues[animationIndex];
		int tempIndex=spriteIndex/temp[6];
		if(!repeat&&tempIndex>temp[5])spriteIndex=temp[5]-1;
		else tempIndex=tempIndex%temp[5];
		//tempIndex=3;
		if(right)return spriteSheet.getSubimage(temp[0]+temp[2]*tempIndex, temp[1], temp[3], temp[4]);
		else return spriteSheet.getSubimage(1147-temp[0]-temp[3]-temp[2]*tempIndex, temp[1]+622, temp[3], temp[4]);
	}
	
	public BufferedImage Animate(MapGen map)
	{
		if(direction.ability)abilities();
		
		boolean changeY=setY(map);
        boolean changeX=setX(map);
        if(changeX && direction.right)right=true;
        else if(changeX)right=false;
        if((direction.ability&&ability.active) || abilAn)
        {
        	if(!abilAn)
        	{
        		animationIndex=ability.animationIndex;
        		spriteIndex=0;
        		abilAn=true;
        	}
        	else
        	{
        		spriteIndex++;
        	}
        	if(spriteIndex>spriteValues[animationIndex][5]*spriteValues[animationIndex][6])
        	{
        		abilAn=false;
        		ability.active=false;
        	}
        }
        else if(changeY&& jumped)
    	{
    		if(justjumped)
    		{
        		animationIndex=0;
        		spriteIndex=0;
        	}
        	else if(animationIndex!=2)
        	{
        		spriteIndex=0;
        		animationIndex=2;
        		repeat=false;
        	}
    		spriteIndex++;
        }
        else if(changeX)
        {
        	if(animationIndex!=1)
        	{
        		spriteIndex=0;
        		animationIndex=1;
        		repeat=true;
        	}
        	spriteIndex++;
        }
        else
        {
        	animationIndex=0;
        	spriteIndex=0;
        }
    	return getAnimationSprite();
	}
	public void abilities()
	{
		if(direction.ability)
		{
			switch(ability.name)
			{
				case"superjump":if(onGround)ability.active=true;jump(2.0);break;
				case"lightningbolt":ability.active=true;break;
			}
		}
	}
   
}

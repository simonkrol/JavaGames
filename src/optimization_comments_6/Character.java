package optimization_comments_6;

import java.awt.Image;
import java.awt.Toolkit;

public class Character {
	Toolkit kit = Toolkit.getDefaultToolkit(); //Used to gather images 
	Image sprite;//Soon to be array of images for accurate movement
	double xSize,ySize,yVel=0,yAcc=-9.81, time,speedMod=1, xLoc, yLoc; //Class variables
	boolean mainChar, onGround=false;
	public Character(double x,double y,double xS, double yS,String dest, double t){
		xLoc=x;		//x and y Loc are the location on the screen, x and y Size are the size of the character in each direction
		yLoc=y;
		xSize=xS;
		ySize=yS;
		sprite=kit.getImage(dest);
		time=t;//Time is the time between frames
	}
	public void setY(boolean pressed)
	{
		if(onGround && pressed)		//Only jump if on the ground and holding up
		{
			yVel=8.322;
			yAcc=-28.01;
			onGround=false;
		}
		if(!onGround)	//Calculate actual changes in motion and position	
		{
			yLoc-=(yVel*time*ySize/2);//Convert from m/s to pixels per 0.015 seconds
			yVel+=yAcc*time;//Reduce current velocity
		}
		
	}
	public void setGround(){	//Move character to the ground
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
					if(yVel<0)yVel=0;
					setGround();
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
   
}

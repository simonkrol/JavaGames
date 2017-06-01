package generation_5;

import java.awt.Image;
import java.awt.Toolkit;

public class Character {
	Toolkit kit = Toolkit.getDefaultToolkit(); 
	Image sprite;
	double xSize,ySize,yVel=0,yAcc=-9.81, time,speedMod=1, xLoc, yLoc;
	boolean mainChar, onGround=false;
	public Character(double x,double y,double xS, double yS,String dest, double t){
		xLoc=x;
		yLoc=y;
		xSize=xS;
		ySize=yS;
		sprite=kit.getImage(dest);
		time=t;
	}
	public void setY(boolean pressed)
	{
		if(onGround && pressed)
		{
			yVel=5.422;
			yAcc=-9.81;
			onGround=false;
		}
		if(yVel !=0 || yAcc!=0)
		{
			yLoc-=(yVel*time*ySize/2);//Convert from m/s to pixels per 0.015 seconds
			yVel+=yAcc*time;//Reduce current velocity
		}
		
	}
	public void setGround(){
		onGround=true;
		yVel=0;
		//yAcc=0;
	}
	
	public void checkYCollision(MapGen map)
	{
		for (Platform temp : map.platforms)
		{
			if(temp.xPos+temp.xSize-xLoc>0.2/map.blocksWide && xLoc+xSize-temp.xPos>0.2/map.blocksWide)
			{
				if(yLoc+ySize>=temp.yPos)
				{
					if(yLoc+ySize-temp.yPos>0.1/map.blocksTall && yLoc+ySize-temp.yPos<0.8/map.blocksTall)yLoc-=0.005;
					if(yVel<0)yVel=0;
					setGround();
				}
			}
			
			
		}
		if(yVel!=0)
		{
			onGround=false;
		}
	}
	public String checkXCollision(MapGen map)
	{
		for(Platform temp: map.platforms)//Checks for a collision on the side of the block, should change to only check those near the player
		{
			if(temp.yPos+temp.ySize-yLoc>0.05/map.blocksWide && yLoc+ySize-temp.yPos>0.05/map.blocksWide)//Probably create a variable for this so we dont have to keep calculating it
			{
				
				if(xLoc<=temp.xPos+temp.xSize && xLoc+xSize>=temp.xPos)
				{
					if(temp.xPos+temp.xSize-xLoc<0.02)
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
	public void setSpeed(double newSpeed)
	{
		speedMod=newSpeed;
	}
   
}

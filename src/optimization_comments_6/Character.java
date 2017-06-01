package optimization_comments_6;

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
			yVel=8.322;
			yAcc=-28.01;
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
	
	public void checkYCollision(MapGen map,int lower)
	{
		Platform temp;
		for(int i=-1; i<2;i++)
		{
			temp=map.platforms.get(lower+i);
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
	public String checkXCollision(MapGen map, int lower)
	{
		Platform temp;
		for(int i=-1; i<2;i++)
		{
			temp=map.platforms.get(i+lower);
			if(temp.yPos+temp.ySize-yLoc>0.2/map.blocksWide && yLoc+ySize-temp.yPos>0.2/map.blocksWide)//Probably create a variable for this so we dont have to keep calculating it
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

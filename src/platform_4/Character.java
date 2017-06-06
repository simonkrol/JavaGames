package platform_4;

import java.awt.Image;
import java.awt.Toolkit;

public class Character {
	Toolkit kit = Toolkit.getDefaultToolkit(); 
	Image sprite;
	double xSize,ySize,yVel,yAcc, time,speedMod=1, xLoc, yLoc;
	boolean mainChar, onGround=true;
	final double finishLine=0.9;
	public Character(double x,double y,double xS, double yS,String dest, boolean main, double t){
		xLoc=x;
		yLoc=y;
		xSize=xS;
		ySize=yS;
		sprite=kit.getImage(dest);
		mainChar=main;
		time=t;
	}
	public void setY(boolean pressed)
	{
		if(onGround && pressed)
		{
			yVel=12;//5.422;
			yAcc=-9.81;
			onGround=false;
		}
		if(yVel !=0 || yAcc!=0)
		{
			yLoc-=(yVel*time*ySize/2);//Convert from m/s to pixels per 0.015 seconds
			yVel+=yAcc*1.5*time;//Reduce current velocity
		}
		if(yLoc>0.6)setGround();
	}
	public void setGround(){
		onGround=true;
		yVel=0;
		yAcc=0;
	}
	
	public void checkYCollision(Background background)
	{
		for (Platform temp : Background.platforms)
		{
			if(xLoc<=temp.xPos+temp.xSize && xLoc+xSize>=temp.xPos)
			{
				if(yLoc<=temp.yPos+temp.ySize&&yLoc>=temp.yPos)
				{
					if(yVel>0)
					{
						yVel=0;
					}
				}
				if(yLoc+ySize<=temp.yPos+temp.ySize&&yLoc+ySize>=temp.yPos)
				{
					if(!(temp.xPos+temp.xSize-xLoc<0.02||xLoc+xSize-temp.xPos<0.02))
					{
						if(yVel<0)
						{
							System.out.println("Test");//Fix this!
							yVel=0;
							//yAcc=0;
							onGround=true;
						}
				
					}
				}
				
				
			}
			
		}
	}
	public String checkXCollision(Background background)
	{
		if(xLoc>finishLine)
		{
			System.out.println("Congrats, you passed the finish line");
			return "finish";
		}
		for(Platform temp: Background.platforms)
		{
			if(yLoc<=temp.yPos+temp.ySize && yLoc+ySize>=temp.yPos)
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

package character_work_7;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Character {
	Toolkit kit = Toolkit.getDefaultToolkit(); //Used to gather images 
	final static int TILE_SIZE=69;
	static BufferedImage spriteSheet=loadSprite();
	double xSize,ySize,yVel=0,yAcc=-9.81, time,speedMod=1, xLoc, yLoc; //Class variables
	boolean onGround=false;
	boolean jumped=false;
	public Character(double x,double y,double xS, double yS, double t)
	{
		xLoc=x;		//x and y Loc are the location on the screen, x and y Size are the size of the character in each direction
		yLoc=y;
		xSize=xS;
		ySize=yS;
		String dest;
		/*for(int i=0; i<10;i++)
		{
			dest="Resources/Mage/Walk"+(Integer.toString(i))+".png";
			System.out.println(dest);
			sprites[i]=kit.getImage(dest);
		}*/
		/*sprites[0]=kit.getImage("Resources/Mage/Walk0.png");
		sprites[1]=kit.getImage("Resources/Mage/Walk1.png");
		sprites[2]=kit.getImage("Resources/Mage/Walk2.png");
		sprites[3]=kit.getImage("Resources/Mage/Walk3.png");
		sprites[4]=kit.getImage("Resources/Mage/Walk4.png");
		sprites[5]=kit.getImage("Resources/Mage/Walk5.png");
		sprites[6]=kit.getImage("Resources/Mage/Walk6.png");
		sprites[7]=kit.getImage("Resources/Mage/Walk7.png");
		sprites[8]=kit.getImage("Resources/Mage/Walk8.png");
		sprites[9]=kit.getImage("Resources/Mage/Walk9.png");*/

		time=t;//Time is the time between frames
	}
	
	public static BufferedImage loadSprite() {

        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(new File("Resources/Mage/BlueWalkJump.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sprite;
    }
	public void setY(boolean pressed, boolean Super)
	{
		int multi=1;
		if(Super)multi=2;
		if(onGround && pressed)		//Only jump if on the ground and holding up
		{
			yVel=8.322*multi;
			yAcc=-28.01*multi;
			onGround=false;
			jumped=true;
		}
		if(!onGround)	//Calculate actual changes in motion and position	
		{
			yLoc-=(yVel*time*ySize/2);//Convert from m/s to pixels per 0.015 seconds
			yVel+=yAcc*time;//Reduce current velocity
		}
		
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
		if(current<=1)current=1;
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
	public static BufferedImage getSprite(int xGrid,int yGrid) {
		if(yGrid>7)yGrid=7;
        if (spriteSheet == null) {
            spriteSheet = loadSprite();
        }
        return spriteSheet.getSubimage(37+xGrid*252, 94*yGrid, 46, TILE_SIZE);
    }
   
}

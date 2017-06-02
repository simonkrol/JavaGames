package optimization_comments_6;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MapGen
{

	Toolkit kit = Toolkit.getDefaultToolkit(); //Class variables
	Image clouds, dirt;
	Image[] grass=new Image[3]; 
	int blocksWide, blocksTall;
	int[] grassy;//Used to indicate at what y value the grass is at each x 
	int[] grassT;//Determine which grass sprite
	List<Platform> platforms; //Arraylist of all grass blocks to allow for collision checking
	
	
	public MapGen()//Collect images, create Platform list, create grassy array and generate a random integer as the map wideness
	{
		blocksWide= ThreadLocalRandom.current().nextInt(32, 64 + 1);
		clouds=kit.getImage("Resources/SkyBackground.jpg");
		dirt=kit.getImage("Resources/Dirt.png");
		grass[0]=kit.getImage("Resources/Grass.png");
		grass[1]=kit.getImage("Resources/Grass1.png");
		grass[2]=kit.getImage("Resources/Grass2.png");
		platforms=new ArrayList<Platform>();
		grassy=new int[blocksWide];
		grassT=new int[blocksWide];
		generateTerrain();
		
		
		
	}
	/*
	 * Generates the terrain for the map.
	 */
	public void generateTerrain()
	{
		int rand;
		int starting=(int)(blocksWide*0.45);//Choose the first grass block
		blocksTall=(int)(blocksWide*0.6);
		for(int i=0;i<blocksWide;i++)
		{
			//Determine whether the next block will go up a level, down a level or stay the same (55% chance of staying the same)
			rand=ThreadLocalRandom.current().nextInt(0,3);	
			if(rand!=0)
			{
				rand=ThreadLocalRandom.current().nextInt(-1,2);
			}
			if(starting+rand<blocksTall-2 && starting+rand>4)	//Make sure new position isn't too close to the top or bottom
			{
				starting+=rand;
			}
			grassy[i]=starting;
			grassT[i]=ThreadLocalRandom.current().nextInt(0,3);
			//Generate a platform to allow for collision detection
			platforms.add(new Platform((double)i/blocksWide,(double)starting/blocksTall,1.0/blocksWide,1.0/blocksWide));
		}
		platforms.add(new Platform(0.0,0.0,0.0,0.0));
	}
	/*
	 * Draws all the components of the terrain on the canvas
	 */
	public void draw(Graphics2D g2d, int xSize, int ySize, ImageObserver newThis)
	{
		int grassY; //The y value of the current grass block
		int size=xSize/blocksWide; //How big to make each block
		g2d.drawImage(clouds, 0, 0, xSize, ySize, newThis);//Draw the initial background
		for(int i=0;i<blocksWide;i++)
		{
			grassY=grassy[i];
			g2d.drawImage(grass[grassT[i]], i*xSize/blocksWide,grassY*ySize/blocksTall, size+2, size, newThis);
			//Draw the grass and fill in all the dirt blocks below it, then break and move to the next block
			g2d.drawImage(dirt, i*xSize/blocksWide,(grassY+1)*ySize/blocksTall, size+2, 20*size, newThis);
			
			
		}
	}

}

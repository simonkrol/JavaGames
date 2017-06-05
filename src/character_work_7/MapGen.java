package character_work_7;

import java.awt.Graphics;
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
	static Painter mapPainter=null;
	
	
	public MapGen()//Collect images, create Platform list, create grassy array and generate a random integer as the map wideness
	{
		//blocksWide= ThreadLocalRandom.current().nextInt(32, 16 + 1);
		blocksWide=37;
		System.out.println(blocksWide);
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
	public static void setPainter( ImageObserver IO)
	{
		mapPainter=new Painter(IO);
	}
	/*
	 * Generates the terrain for the map.
	 */
	public void generateTerrain()
	{
		int rand;
		int starting=(int)(blocksWide*0.25);//Choose the first grass block
		blocksTall=(int)(blocksWide*0.6);
		for(int i=0;i<blocksWide;i++)
		{
			//Determine whether the next block will go up a level, down a level or stay the same (55% chance of staying the same)
			rand=ThreadLocalRandom.current().nextInt(0,3);	
			if(rand!=0)
			{
				rand=ThreadLocalRandom.current().nextInt(-2,3);
			}
			rand=1;
			if(starting+rand<blocksTall-2 && starting+rand>4)	//Make sure new position isn't too close to the top or bottom
			{
				starting+=rand;
			}
			else starting-=rand;
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
	public void draw(int xSize, int ySize, Graphics2D g2)
	{
		int grassY; //The y value of the current grass block
		mapPainter.paint(clouds, 0, 0, xSize, ySize,ySize,g2 );//Draw the initial background
		for(int i=0;i<blocksWide;i++)
		{
			grassY=grassy[i];
			mapPainter.paint(grass[grassT[i]], (xSize/blocksWide*i), grassY*ySize/blocksTall, xSize/blocksWide, ySize/blocksTall, ySize, g2);
			
			mapPainter.paint(dirt, (xSize/blocksWide*i), 0, xSize/blocksWide, grassY*ySize/blocksTall, ySize, g2);
			//mapPainter.paint(grass[0], xSize*i/blocksWide, grassY*ySize/blocksTall, xSize/blocksWide, ySize/blocksTall, ySize, g2);
			//mapPainter.paint(grass[grassT[i]], i*xSize/blocksWide,grassY*ySize/blocksTall, xSize/blocksWide, ySize/blocksTall,ySize, g2 );
			//Draw the grass and fill in all the dirt blocks below it, then break and move to the next block
			//mapPainter.paint(dirt, i*xSize/blocksWide,0, xSize/blocksWide, grassy[i]*ySize/blocksTall, ySize, g2);
			
			
		}
	}

}

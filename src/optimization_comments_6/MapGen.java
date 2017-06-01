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

	Toolkit kit = Toolkit.getDefaultToolkit(); 
	Image clouds, dirt, grass;
	int blocksWide, blocksTall;
	int[] grassy;
	List<Platform> platforms;
	
	
	public MapGen()
	{
		blocksWide= ThreadLocalRandom.current().nextInt(32, 64 + 1);
		clouds=kit.getImage("Resources/SkyBackground.jpg");
		dirt=kit.getImage("Resources/Dirt.png");
		grass=kit.getImage("Resources/Grass.png");
		generateTerrain();
		
		
		
	}
	public void generateTerrain()
	{
		int rand;
		platforms=new ArrayList<Platform>();
		int starting=(int)(blocksWide*0.45);
		blocksTall=(int)(blocksWide*0.6);
		grassy=new int[blocksWide];
		for(int i=0;i<blocksWide;i++)
		{
			rand=ThreadLocalRandom.current().nextInt(0,3);
			if(rand!=0)
			{
				rand=ThreadLocalRandom.current().nextInt(-1,2);
			}
			if(starting+rand<blocksTall-3 && starting+rand>3)
			{
				starting+=rand;
			}
			grassy[i]=starting;
			platforms.add(new Platform((double)i/(double)blocksWide,(double)starting/(double)blocksTall,1.0/(double)blocksWide,1.0/(double)blocksWide));
		}		
	}
	public void draw(Graphics2D g2d, int xSize, int ySize, ImageObserver newThis)
	{
		int grassY;
		int size=xSize/blocksWide;
		g2d.drawImage(clouds, 0, 0, xSize, ySize, newThis);
		for(int i=0;i<blocksWide;i++)
		{
			grassY=grassy[i];
			g2d.drawImage(grass, i*xSize/blocksWide,grassY*ySize/blocksTall, size+2, size, newThis);
			//Draw the grass and fill in all the dirt blocks below it, then break and move to the next block
			g2d.drawImage(dirt, i*xSize/blocksWide,(grassY+1)*ySize/blocksTall, size+2, 20*size, newThis);
			
			
		}
	}

}

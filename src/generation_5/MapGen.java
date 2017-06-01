package generation_5;

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
	boolean[][] grassy;
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
		grassy=new boolean[blocksWide][blocksTall];
		for(int i=0;i<blocksWide;i++)
		{
			rand=ThreadLocalRandom.current().nextInt(-1,2);
			if(starting+rand<blocksTall-3 && starting+rand>3)
			{
				starting+=rand;
			}
			grassy[i][starting]=true;
			platforms.add(new Platform((double)i/(double)blocksWide,(double)starting/(double)blocksTall,1.0/(double)blocksWide,1.0/(double)blocksWide));
		}		
	}
	public void draw(Graphics2D g2d, int xSize, int ySize, ImageObserver newThis)
	{
		boolean grassed;
		g2d.drawImage(clouds, 0, 0, xSize, ySize, newThis);
		for(int i=0;i<blocksWide;i++)
		{
			grassed=false;
			for (int j=0; j<blocksTall;j++)
			{
				if(grassed)
				{
					g2d.drawImage(dirt, i*xSize/blocksWide,j*ySize/blocksTall, xSize/blocksWide+2, 2*ySize/blocksWide, newThis);
				}
				else if(grassy[i][j])
				{
					g2d.drawImage(grass, i*xSize/blocksWide,j*ySize/blocksTall, xSize/blocksWide+2, xSize/blocksWide, newThis);
					grassed=true;
				}
			}
		}
	}

}

package movingSprite_3;

import java.awt.Image;
import java.awt.Toolkit;

public class Character {
	Toolkit kit = Toolkit.getDefaultToolkit(); 
	Image sprite;
	int xLoc;
	int yLoc;
	int xSize;
	int ySize;
	boolean mainChar=false;
	public Character(int x,int y,int xS, int yS,String dest, boolean main){
		xLoc=x;
		yLoc=y;
		xSize=xS;
		ySize=yS;
		sprite=kit.getImage(dest);
		mainChar=main;
	}
	public void setMain(boolean newVal){
		mainChar=newVal;
	}
   
}

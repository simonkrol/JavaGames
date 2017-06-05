package character_work_7;

import java.awt.image.BufferedImage;

public class Sprite {
	int x;
	int x2;
	int y;
	int left;
	int right;
	int up;
	int down;
	int toNext;
	int num;
	double seconds; //Seconds per sprite change
	boolean repeat;
	int temp;
	static double time;
	BufferedImage spriteSheet;
	
	
	public Sprite(int tx,int tx2,int ty,int tl,int tr,int tu,int td,int ttn, int number,double sec,boolean rep,BufferedImage sprites)
	{
		x=tx;
		x2=tx2;
		y=ty;
		left=tl;
		right=tr;
		up=tu;
		down=td;
		toNext=ttn;
		num=number;
		seconds=sec;
		repeat=rep;
		spriteSheet=sprites;
	}
	public BufferedImage getSprite(int index, boolean facing)
	{
		temp=(int)(index*(time/seconds));
		if(repeat)temp=temp%num;
		else if(temp>=num)temp=num-1;
		if(facing)return spriteSheet.getSubimage(x-left+toNext*temp,y-up,left+right, up+down);
		else return spriteSheet.getSubimage(1878 -x-right-toNext*temp, y+622-up, left+right, up+down);
	}
	
	public static void setTime(double t)
	{
		time=t;
	}
	public boolean pastFinal(int spIndex)
	{
		return spIndex>seconds*num/time;
	}


}

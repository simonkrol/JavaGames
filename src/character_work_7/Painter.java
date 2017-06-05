package character_work_7;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
public class Painter {

	ImageObserver imageOb;
	public Painter(ImageObserver IO)
	{
		imageOb=IO;
	}
	public void paint(BufferedImage toPrint,int x, int y, int width, int height, int mapWidth, int mapHeight, Graphics2D g2d)
	{
		g2d.drawImage(toPrint, x, mapHeight-(y+height), width, height, imageOb);
	}
	public void paint(Image toPrint,int x, int y, int width, int height, int mapHeight, Graphics2D g2d)
	{
		g2d.drawImage(toPrint, x, mapHeight-(y+height), width, height, imageOb);
	}
}

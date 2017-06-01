package platform_4;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial") 
public class Background extends JPanel 
{
	int xSize,ySize, length;
	Toolkit kit = Toolkit.getDefaultToolkit(); 
	Image background;
	static List<Platform> platforms=new ArrayList<Platform>();
	Image platformImage;
	public Background(int xS, int yS, String dest, int len, String plat)
	{
		xSize=xS;
		ySize=yS;
		background=kit.getImage(dest);
		platformImage=kit.getImage(plat);
		length=len;
	}
	public void addPlatform(double x,double y, double xS, double yS)
	{
		platforms.add(new Platform(x,y,xS,yS));
	}
   
}
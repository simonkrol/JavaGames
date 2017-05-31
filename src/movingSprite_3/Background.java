package movingSprite_3;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

@SuppressWarnings("serial") 
public class Background extends JPanel {
	int xSize,ySize;
	Toolkit kit = Toolkit.getDefaultToolkit(); 
	Image background;
	public Background(int xS, int yS, String dest){
		xSize=xS;
		ySize=yS;
		background=kit.getImage(dest);
	}
   
}
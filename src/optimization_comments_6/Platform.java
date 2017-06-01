package optimization_comments_6;

/*
 * Platform object, used to allow the user to walk and jump around
 * Stores the x and y positions of the platform as well as their size in the x and y direction
 */
public class Platform {
	double xPos, yPos, xSize, ySize;
	public Platform(double x,double y, double xS, double yS)
	{
		xPos=x;
		yPos=y;
		xSize=xS;
		ySize=yS;
	}

}

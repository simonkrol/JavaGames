package character_work_7;

public class Ability {
	int cooldown;
	String name;
	int animationIndex;
	boolean active=false;
	
	public Ability(int c, String n)
	{
		cooldown=c;
		name=n;
		switch(name)
		{
			case "superjump":animationIndex=3;break;
			case "lightningbolt":animationIndex=4;break;
			case "frost":animationIndex=5;break;
			default:animationIndex=5;break;
		}
	}
}

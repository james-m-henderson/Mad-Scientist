import java.util.Properties;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

public class Town extends Place {

	private Image mapImage;
	
	private int Anger = 0;
	private int Fear = 0;
	
	private int AngerStepTime = 10000;
	private int AngerStepTime_c = 0;
	private int FearStepTime = 10000;
	private int FearStepTime_c = 0;
	private int GatherStepTime_c = 0;
	private int GatherStepTime = 10000;
	
	private static final int UI_OFFSET = 200;
	
	private static int IMAGE = 0, X = 1, Y = 2;
	
	private int x, y;
	
	// TODO: Move this into the config file
	private static final String themeDir = "./res/themes/";
	private static final int MONEY_PER_UNIT = 100;
	
	public Town(GameCore mCore) throws SlickException
	{
		super("Town", mCore);
		Properties mConfig = mCore.getConfiguration();
		Properties mTheme = ConfigReader.readConfig(themeDir + mConfig.getProperty("theme"));
		String[] townAttributes = mTheme.getProperty("village").split(",");
		x = Integer.parseInt(townAttributes[X]);
		y = Integer.parseInt(townAttributes[Y]);
		mapImage = new Image(themeDir + mTheme.getProperty("folder") + "/" + townAttributes[IMAGE]);
		getCore().setValue("Money", 0);
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta)
	throws SlickException {
		super.update(container, game, delta);
		
		AngerStepTime_c += delta;
		if ( AngerStepTime_c >= AngerStepTime )
		{
			AngerStepTime_c = 0;
			Anger += getUsers().size();
			// TODO: Social Minions will subtract Anger
		}
		
		FearStepTime_c += delta;
		if ( FearStepTime_c >= FearStepTime )
		{
			FearStepTime_c = 0;
			Fear += getCore().getMinions().size();
			// TODO: Total Minion count effects Fear, probably not the best metric...
		}
		
		GatherStepTime_c += delta;
		if ( GatherStepTime_c >= GatherStepTime )
		{
			GatherStepTime_c = 0;
			getCore().setValue("Money", getCore().getValue("Money") + (MONEY_PER_UNIT * getUsers().size()) );
			if ( getUsers().size() > 0 )
				Log.debug("Recieved Money from robbing and extorting the townspeople, Current Money Available: " + getCore().getValue("Money"));
		}
		
	}
	
	public int getAnger() {
		return Anger;
	}

	public void setAnger(int anger) {
		Anger = anger;
	}

	public int getFear() {
		return Fear;
	}

	public void setFear(int fear) {
		Fear = fear;
	}

	@Override
	public void addUser(Minion mUnit)
	{
		// Hide the Minion from the main screen, and remove them from the gameCore list
		super.addUser(mUnit);
		getCore().removeMinion(mUnit);
		mUnit.setOnScreen(false);
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {
		arg2.drawImage(mapImage, x, y+UI_OFFSET);
	}

	@Override
	public Image getImage() {
		return mapImage;
	}

	@Override
	public Coord getPosition() {
		return new Coord(x, y);
	}
	
}

package radon;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {
    
    static AppGameContainer appgc;
    public static final String NAME = "Project Radon";
    public static final int WORLD = 0;
    public static int width = 800;
    public static int height = 600;
    
    public Game(String name) {
        super(name);
    }
    
    public static void main(String[] args) {
        try {
            appgc = new AppGameContainer(new Game(NAME));
            width = appgc.getScreenWidth();
            height = appgc.getScreenHeight();
            appgc.setDisplayMode(width, height, true);
            appgc.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new World(WORLD));
    }
}

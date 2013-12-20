package radon;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {
    
    static AppGameContainer appgc;
    public static final String NAME = "Project Radon";
    public static final int WORLD = 0;
    public static final int MENU = 1;
    public static int width = 800;
    public static int height = 600;
    public static Options o = new Options("Radon");
    
    public Game(String name) {
        super(name);
        
    }
    
    public static void main(String[] args) {
        
        try {
            appgc = new AppGameContainer(new Game(NAME));
            width = appgc.getScreenWidth();
            height = appgc.getScreenHeight();
            appgc.setDisplayMode(width, height, true);
            appgc.setShowFPS(false);
            appgc.start();
            
        } catch (SlickException e) {
            e.printStackTrace();
        }
        
    }
    
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new World(WORLD, this));
        addState(new Menu(MENU, this));
        
    }
    
    public static Image scaleImage(Image image, int scale) {
        if (image != null) {
            image.setFilter(Image.FILTER_NEAREST);
            return image.getScaledCopy(scale);
        }
        return image;
    }
}

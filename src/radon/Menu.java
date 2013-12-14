package radon;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Input;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by James Ward (epic_jdog)
 * <p/>
 * Date: 13/12/13
 * Time: 9:05 PM
 */
public class Menu extends BasicGameState {
    float accumulator = 0.0F;
    public static long tickCount = 0;
    private Options o = Game.o;

    private static StateBasedGame radon;
    public static float partialTicks;
    public static float[] timespeeds = {50F, 100F / 3F, 50F / 3F, 50F / 6F, 50F / 9F};
    public static float timestep = 50F / 3F; // 1/60 second
    Player p = new Player(100, 100);

    public static List<Entity> entities = new ArrayList<Entity>();

    public static Random rand = new Random();

    public Menu(int play, StateBasedGame radon)throws SlickException {
        this.radon = radon;
    }

    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();

        if (input.isKeyPressed(Input.KEY_4)) {
            radon.enterState(0);
        }
        if (delta > 25) delta = 25;
        accumulator += delta;

        while (accumulator >= timestep) {
            if (container.hasFocus()) {
                tick(container, game, delta);
            }
            accumulator -= timestep;
        }

        partialTicks = accumulator / timestep;

    if (input.isKeyPressed(Input.KEY_F11)) {
        o.setFullscreen(true);
    }


    if (input.isKeyPressed(Input.KEY_ESCAPE) && Game.appgc.isFullscreen()) {
        o.setFullscreen(false);
    }


    if (input.isKeyPressed(Input.KEY_V)) {
        o.toggleVisibility();
    }
    }

    public void tick(GameContainer container, StateBasedGame game, int delta) throws SlickException {

        tickCount++;

    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {


    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, Game.width, Game.height);


        g.setColor(new Color(60, 50, 0));
        Font.draw("Its a menu!", 20, 20, 10, g);
        g.setColor(new Color(150, 200, 52));
        Font.draw("LOLOL", 100, 100, 5, g);
        g.setColor(new Color(0, 0, 0));
        Font.draw("Raytrace Visibility: ", 100, 200, 5, g);
        Font.draw("Fullscreen Mode: ", 100, 300, 5, g);
        Font.draw("Timescale: ", 100, 400, 5, g);
        Font.draw("Key Config: ", 100, 500, 5, g);

        boolean fs = o.getFullscreen();
        boolean vis = o.getVisibility();
        int ts = o.getTimescale();

        String stringvis = "On";
        if (vis){
            stringvis = "On";
            g.setColor(new Color(0, 0 ,255));
        }
        else stringvis = "Off";{
            g.setColor(new Color(255,0 ,0));
        }
        Font.draw(stringvis, 800, 200, 5, g);

        String stringfs = "Off";
        if (fs){
            stringfs = "On";
            g.setColor(new Color(0, 0 ,255));
        }
        else stringfs = "Off";{
            g.setColor(new Color(255,0 ,0));
        }
        Font.draw(stringfs, 800, 300, 5, g);

        g.setColor(Color.blue);
        String stringts = "Normal";
        switch (ts){
            case 0: {
                stringts = "Quite Slowed Indeed";
            break;
            }
            case 1: {
                stringts = "Slightly Sluggish";
            break;
            }
            case 2: {
                stringts = "Normal";
                break;
            }
            case 3: {
                stringts = "Quick as a fox";
                break;
            }
            case 4: {
                stringts = "Quick as a quicker fox";
            break;
            }
            case 5: {
                stringts = "Quick as an epic organ";
            break;
            }
            case 6: {
                stringts = "f";
                break;
            }
        }


        Font.draw(stringts, 800, 400, 5, g);
        g.setColor(Color.lightGray);
        Font.draw("Default", 800, 500, 5, g);




    }


    @Override
    public int getID() {
        return Game.MENU;
    }


}

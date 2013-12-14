package radon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class World extends BasicGameState {

    float accumulator = 0.0F;
    private StateBasedGame radon;
    private Options o = Game.o;
    public static long tickCount = 0;
    public static float partialTicks;
    public static float[] timespeeds = {50F, 100F / 3F, 50F / 3F, 50F / 6F, 50F / 9F};

    public static float timestep = 50F / 3F; // 1/60 second
    Player p = new Player(100, 100);

    public static List<Entity> entities = new ArrayList<Entity>();

    public static Random rand = new Random();

    public World(int play, StateBasedGame radon) {
        this.radon = radon;
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();

        if (input.isKeyPressed(Input.KEY_F11)) {
            o.setFullscreen(true);
        }

        if (input.isKeyPressed(Input.KEY_4)) {
            radon.enterState(1);
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE) && Game.appgc.isFullscreen()) {
            o.setFullscreen(false);
        }

        if (input.isKeyPressed(Input.KEY_COMMA) && o.getTimescale() != 0) {
            o.slowTime(1);
            timestep = timespeeds[o.getTimescale()];
        }
        if (input.isKeyPressed(Input.KEY_PERIOD) && o.getTimescale() != timespeeds.length - 1) {
            o.speedTime(1);
            timestep = timespeeds[o.getTimescale()];
        }

        if (input.isKeyPressed(Input.KEY_V)) {
           o.toggleVisibility();
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
    }

    public void tick(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        tickCount++;

        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);

            for (int j = 0; j < entities.size(); j++) {
                Entity e2 = entities.get(j);
                if (e2 != e && e.intersects(e2) && e.isCollidable() && e2.isCollidable()) {
                    Collision.doCollision(e, e2);
                }
            }

            e.update(container, game, delta);
            Collision.doEdgeCollision(e);

            if (e.removed) {
                entities.remove(i--);
            }

        }
        Visibility.load(entities);
        Visibility.setLightLocation(p.x, p.y);
        Visibility.sweep();
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        add(p);

        Character c = new Character(100, 200, 242, 224, 42, 20, 30, true);
        add(c);
        int R = 42;
        int G = 47;
        int B = 159;

        while (entities.size() < 200) {
            GenericCuboid e = new GenericCuboid(rand.nextFloat() * Game.width, rand.nextFloat() * Game.height, R, G, B,
                    rand.nextFloat() * 50 + 5, rand.nextFloat() * 50 + 5, rand.nextInt(4) == 0 ? true : false);
            e.invMass = 0;
            e.col = new Color(42, 47, 159);
            boolean add = true;
            for (Entity LOL : entities) {
                if (e.intersects(LOL)) add = false;
            }
            if (add) add(e);
        }

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, Game.width, Game.height);

        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);

            e.render(container, game, g);
        }

        g.setColor(new Color(0, 0, 0));
        Font.draw("Time speed: " + 50F / 3F / timestep, 20, 20, 2, g);
        Font.draw("FPS: " + Game.appgc.getFPS(), 20, 35, 2, g);

        if (o.getVisibility()) {
            for (int i = 0; i < Visibility.output.size(); i += 2) {
                Vector2f p1 = Visibility.output.get(i);
                Vector2f p2 = Visibility.output.get(i + 1);
                Vector2f p3 = new Vector2f(Visibility.output.get(Visibility.output.size() - 1).x, Visibility.output.get(Visibility.output
                        .size() - 1).y);
                if (i > 0) {
                    p3 = Visibility.output.get(i - 1);
                }
                g.setColor(new Color(0, 0, 0));
                g.setLineWidth(1);
                g.drawLine(p3.x, p3.y, p1.x, p1.y);
                g.setColor(Visibility.outputColours.get(i / 2).brighter(1.5F));
                g.setLineWidth(2);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

    }

    public static void add(Entity entity) {
        entity.removed = false;
        entities.add(entity);
        entity.init();
    }

    public static List<Entity> getEntities() {
        return entities;
    }

    @Override
    public int getID() {
        return Game.WORLD;
    }

}

package radon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class World extends BasicGameState {
    
    float accumulator = 0.0F;
    public static long tickCount = 0;
    public static float partialTicks;
    public static float[] timespeeds = { 50F, 100F / 3F, 50F / 3F, 50F / 6F, 50F / 9F };
    public static int selectedTime = 2;
    public static float timestep = 50F / 3F; // 1/60 second
    
    public static List<Entity> entities = new ArrayList<Entity>();
    
    public static Random rand = new Random();
    
    public World(int play) {}
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();
        
        if (input.isKeyPressed(Input.KEY_F11)) {
            Game.appgc.setFullscreen(true);
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE) && Game.appgc.isFullscreen()) {
            Game.appgc.setFullscreen(false);
        }
        
        if (input.isKeyPressed(Input.KEY_COMMA) && selectedTime != 0) {
            selectedTime--;
            timestep = timespeeds[selectedTime];
        }
        if (input.isKeyPressed(Input.KEY_PERIOD) && selectedTime != timespeeds.length - 1) {
            selectedTime++;
            timestep = timespeeds[selectedTime];
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
    }
    
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        Player p = new Player(100, 100);
        add(p);
        
        Character c = new Character(100, 200, 242, 224, 42, 20, 30, true);
        add(c);
        /*int R = 42;
        int G = 47;
        int B = 159;
        for (int i = 0; i < 150; i++) {
            
            boolean physics = false;
            GenericCuboid e = new GenericCuboid(rand.nextFloat() * Game.width, rand.nextFloat() * Game.height, R, G, B,
                    rand.nextFloat() * 50 + 5, rand.nextFloat() * 50 + 5, physics);
            
            e.invMass = 0;
            add(e);
        }
        
        for (int i = 0; i < 50; i++) {
            
            boolean physics = true;
            GenericCuboid e = new GenericCuboid(rand.nextFloat() * Game.width, rand.nextFloat() * Game.height, R, G, B,
                    rand.nextFloat() * 50 + 5, rand.nextFloat() * 50 + 5, physics);
            
            e.invMass = 0;
            add(e);
        }*/
        
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, Game.width, Game.height);
        
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            
            e.render(container, game, g);
        }
        System.out.println(timestep);
        
        g.setColor(new Color(0, 0, 0));
        Font.draw("Time speed: " + 50F / 3F / timestep + "x", 20, 20, 2, g);
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

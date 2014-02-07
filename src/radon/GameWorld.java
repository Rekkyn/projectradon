package radon;

import java.util.*;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameWorld extends BasicGameState {
    
    float accumulator = 0.0F;
    private StateBasedGame radon;
    private Options o = Game.o;
    public static long tickCount = 0;
    public static float partialTicks;
    public static float[] timespeeds = { 50F, 100F / 3F, 50F / 3F, 50F / 6F, 50F / 9F };
    
    public static float timestep = 50F / 3F; // 1/60 second
    Player p = new Player(0, 40);
    
    public static List<Entity> entities = new ArrayList<Entity>();
    
    public static Random rand = new Random();
    
    public static boolean gunFocus = true;
    public static Vec2 gravity = new Vec2(0F, -9.8F);
    public static Vector2f gravity1, gravity2;
    
    public static World physicsWorld = new World(GameWorld.gravity);
    
    public GameWorld(int play, StateBasedGame radon) {
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
            Options.slowTime(1);
            timestep = timespeeds[o.getTimescale()];
        }
        if (input.isKeyPressed(Input.KEY_PERIOD) && o.getTimescale() != timespeeds.length - 1) {
            Options.speedTime(1);
            timestep = timespeeds[o.getTimescale()];
        }
        
        if (input.isKeyPressed(Input.KEY_V)) {
            o.toggleVisibility();
        }
        
        if (input.isKeyDown(Input.KEY_G)) {
            gunFocus = false;
            if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                gravity1 = new Vector2f(input.getMouseX(), input.getMouseY());
            }
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                gravity2 = new Vector2f(input.getMouseX(), input.getMouseY());
                if (gravity2.distance(gravity1) < 10) {
                    gravity2.set(gravity1);
                } else if (gravity2.distance(gravity1) > 95 && gravity2.distance(gravity1) < 105) {
                    Vector2f temp = gravity1.copy();
                    Vector2f toAdd = new Vector2f(100, 0);
                    toAdd.setTheta(gravity2.sub(gravity1.copy()).getTheta());
                    temp.add(toAdd);
                    gravity2.set(temp);
                }
                for (int i = 0; i < 4; i++) {
                    Vector2f temp = gravity1.copy();
                    Vector2f toAdd = new Vector2f(100, 0);
                    toAdd.setTheta(i * 90);
                    temp.add(toAdd);
                    if (gravity2.distance(temp) < 10) gravity2.set(temp);
                }
            }
            if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && gravity1 != null) {
                gravity = new Vec2((gravity2.x - gravity1.x) * 0.098F, (gravity2.y - gravity1.y) * 0.098F);
                physicsWorld.setGravity(gravity);
                gravity1 = null;
                gravity2 = null;
            }
        } else {
            gunFocus = true;
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
        Input input = container.getInput();
        tickCount++;
        
        if (input.isKeyDown(Input.KEY_RIGHT)) {
            Camera.x += 4 / Camera.zoom;
        }
        if (input.isKeyDown(Input.KEY_LEFT)) {
            Camera.x -= 4 / Camera.zoom;
        }
        if (input.isKeyDown(Input.KEY_UP)) {
            Camera.y += 4 / Camera.zoom;
        }
        if (input.isKeyDown(Input.KEY_DOWN)) {
            Camera.y -= 4 / Camera.zoom;
        }
        if (input.isKeyDown(Input.KEY_EQUALS)) {
            Camera.zoom *= 1.01;
        }
        if (input.isKeyDown(Input.KEY_MINUS)) {
            Camera.zoom *= 0.99;
        }
        Camera.update();
        
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            
            e.update(container, game, delta);
            
            if (e.removed) {
                entities.remove(i--);
            }
        }
        
        physicsWorld.step(1F / 60, 40, 20);
        
        Visibility.load(entities);
        Visibility.setLightLocation(p.x, p.y);
        Visibility.sweep();
    }
    
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        PlayerContactListener contlist = new PlayerContactListener();
        physicsWorld.setContactListener(contlist);
        
        add(p);
        
        Wall w = new Wall(0, -15, 242, 224, 42, Game.width / 20, 0.5F);
        add(w);
        
        while (entities.size() < 150) {
            DynamicBox db = new DynamicBox(rand.nextFloat() * 80 - 40, rand.nextFloat() * 80 - 40, 57, 90, 200, rand.nextFloat() * 5 + 1,
                    rand.nextFloat() * 5 + 1, true);
            boolean add = true;
            for (Entity LOL : entities) {
                if (db.intersects(LOL)) add = false;
            }
            if (add) add(db);
        }
        
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        Input input = container.getInput();
        
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, Game.width, Game.height);
        
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            
            e.prerender(g);
            e.render(container, game, g);
            e.postrender(g);
        }
        
        g.setColor(new Color(0, 0, 0));
        Font.draw("Time speed: " + 50F / 3F / timestep, 20, 20, 2, g);
        Font.draw("FPS: " + Game.appgc.getFPS(), 20, 35, 2, g);
        
        g.pushTransform();
        g.scale(20, 20);
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
        g.popTransform();
        if (input.isKeyDown(Input.KEY_G)) {
            g.setLineWidth(2);
            if (gravity1 != null) {
                g.drawOval(gravity1.x - 100, gravity1.y - 100, 200, 200);
                g.drawOval(gravity1.x - 5, gravity1.y - 5, 10, 10);
                
                g.drawOval(gravity1.x - 105, gravity1.y - 5, 10, 10);
                g.drawOval(gravity1.x + 95, gravity1.y - 5, 10, 10);
                g.drawOval(gravity1.x - 5, gravity1.y - 105, 10, 10);
                g.drawOval(gravity1.x - 5, gravity1.y + 95, 10, 10);
            }
            if (gravity2 != null) g.fillOval(gravity2.x - 5, gravity2.y - 5, 10, 10);
            if (gravity1 != null && gravity2 != null) {
                g.drawLine(gravity1.x, gravity1.y, gravity2.x, gravity2.y);
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
    
    public static Vec2 mousePos(GameContainer container) {
        Input input = container.getInput();
        return new Vec2(Camera.x - Game.width / 2 / Camera.zoom + input.getMouseX() / Camera.zoom, Camera.y + Game.height / 2 / Camera.zoom
                - input.getMouseY() / Camera.zoom);
    }
    
    @Override
    public int getID() {
        return Game.WORLD;
    }
    
    public class Filtering {
        public static final int GROUND = 1;
        public static final int PLAYER = 2;
        public static final int BULLET = 4;
    }
}

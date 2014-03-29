package radon;

import java.util.Random;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Entity {
    
    public float x, y;
    public float angle;
    public float prevX, prevY;
    public Vec2 velocity;
    public Vector2f prevVelocity;
    public boolean removed;
    public long ticksExisted = 0;
    public float width, height;
    public Color col = new Color(0, 0, 0);
    Input input;
    Random rand = new Random();
    public boolean gravity = false;
    public float restitution;
    /** 0 = not on wall, 1 = left wall, 2 = right wall */
    public byte onWall;
    
    public Fixture fixture;
    public Body body;
    public BodyType type;
    public BodyDef def;
    
    public GameWorld world;
    public static World physicsWorld;
    
    public Entity(float x, float y, BodyType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    public void remove() {
        removed = true;
    }
    
    public boolean intersects(Entity e) {
        if (e.x - e.width / 2 > x + width / 2 || x - width / 2 > e.x + e.width / 2) return false;
        if (e.y - e.height / 2 > y + height / 2 || y - height / 2 > e.y + e.height / 2) return false;
        return true;
    }
    
    public boolean isCollidable() {
        return true;
    }
    
    public void init() {
        physicsWorld = GameWorld.physicsWorld;
        def = new BodyDef();
        def.position.set(x, y);
        def.angle = (float) Math.toRadians(angle);
        def.type = type;
        body = GameWorld.physicsWorld.createBody(def);
        if (!gravity) {
            body.setGravityScale(0);
        }
    }
    
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        ticksExisted++;
        GameState state = game.getCurrentState();
        if (!(state instanceof GameWorld)) return;
        world = (GameWorld) game.getCurrentState();
        
        prevX = x;
        prevY = y;
        x = body.getPosition().x;
        y = body.getPosition().y;
        angle = body.getAngle();
        velocity = body.getLinearVelocity();
    }
    
    public void prerender(Graphics g) {
        g.pushTransform();
        g.setAntiAlias(true);
        g.scale(Camera.zoom, Camera.zoom);
        g.translate(GameWorld.partialTicks * (x - prevX) - Camera.x + Game.width / Camera.zoom / 2, GameWorld.partialTicks * (prevY - y)
                + Camera.y + Game.height / Camera.zoom / 2);
    }
    
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(col);
        g.rotate(x, -y, (float) (-angle * 180 / Math.PI));
    }
    
    public void postrender(Graphics g) {
        g.popTransform();
    }
    
    public void onHit() {}
    
}

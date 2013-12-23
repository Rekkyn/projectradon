package radon;

import java.util.Random;

import org.jbox2d.dynamics.*;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Entity {
    
    public float x, y;
    public float angle;
    public float prevX, prevY;
    public Vector2f velocity;
    public Vector2f prevVelocity;
    public Vector2f force;
    public boolean removed;
    public long ticksExisted = 0;
    public float width, height;
    public float invMass = 0;
    public float restitution = 0.8F;
    public Color col = new Color(0, 0, 0);
    Input input;
    Random rand = new Random();
    public boolean onGround;
    public boolean physics = false;
    public boolean gravity = false;
    /**
     * 0 = not on wall, 1 = left wall, 2 = right wall
     */
    public byte onWall;
    
    FixtureDef fd = new FixtureDef();
    Body body;
    BodyType type;
    
    public GameWorld world;
    
    public Entity(float x, float y, BodyType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    public void physics() {
        BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        bd.type = type;
        body = Collision.world.createBody(bd);
        body.createFixture(fd);
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
    
    public void init() {}
    
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
    }
    
    public abstract void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException;
    
    public void onHit() {}
    
}

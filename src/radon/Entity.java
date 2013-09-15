package radon;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class Entity {
    
    public float x, y;
    public float prevX, prevY;
    public Vector2f velocity;
    public Vector2f prevVelocity;
    public Vector2f force;
    public boolean removed;
    public float width, height;
    public float invMass = 0;
    public float restitution = 0.8F;
    Input input;
    Random rand = new Random();
    public boolean onGround;
    public boolean physics = false;
    /** 0 = not on wall, 1 = left wall, 2 = right wall */
    public byte onWall;
    
    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
        width = 0;
        height = 0;
        velocity = new Vector2f(0, 0);
        prevVelocity = new Vector2f(0, 0);
        force = new Vector2f(0, 0);
    }
    
    public Entity(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        velocity = new Vector2f(0, 0);
        prevVelocity = new Vector2f(0, 0);
        force = new Vector2f(0, 0);
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
        GameState state = game.getCurrentState();
        if (!(state instanceof World)) return;
        
        prevVelocity.set(velocity);
        
        if (physics) {
            invMass = 1F / (width * height);
        }
        
        prevX = x;
        prevY = y;
        
        velocity.add(force.scale(invMass));
        force.set(0, 0);
        
        x += velocity.x;
        y += velocity.y;
    }
    
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {}
    
}

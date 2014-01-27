package radon.guns;

import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import radon.Character;
import radon.Entity;

public class Bullet extends Entity {
    public int ticksOnGround = 0;
    public boolean hit = false;
    public Character c;
    
    public Bullet(Character c) {
        super(c.x, c.y, BodyType.DYNAMIC);
        this.c = c;
        // invMass = 0.99F;
        // restitution = 0.1F;
        gravity = true;
        col = new Color(50, 50, 50);
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        
        if (!hit) {
            // air resistance 0.99
        } else {
            // air resistance 0.95
        }
        if (onGround || Math.abs(velocity.x) < 0.01 && Math.abs(velocity.y) < 0.01) {
            velocity.x *= 0.9;
            ticksOnGround++;
        }
        
        if (ticksOnGround > 60) {
            col.a = 1F - (ticksOnGround - 60F) / 60F;
        }
        if (ticksOnGround > 120) remove();
    }
    
    @Override
    public void onHit() {
        hit = true;
    }
    
}
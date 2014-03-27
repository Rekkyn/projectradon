package radon.guns;

import org.jbox2d.collision.shapes.CircleShape;
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
        gravity = true;
        col = new Color(50, 50, 50);
        restitution = 0.1F;
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        // super.render(container, game, g);
        g.setColor(col);
        g.pushTransform();
        float angle = (float) (Math.atan2(velocity.y, velocity.x) * 180F / Math.PI);
        g.rotate(x, -y, -angle);
        g.fillRect(x - 0.2F, -y - 0.1F, 0.4F, 0.2F);
        g.rotate(x, -y, angle);
        g.popTransform();
    }
    
    @Override
    public void init() {
        super.init();
        setShape();
        body.setBullet(true);
        body.setFixedRotation(true);
        fixture.setRestitution(restitution);
        fixture.m_filter.categoryBits = (int) Math.pow(2, c.ID);
        fixture.m_filter.maskBits = ~(int) Math.pow(2, c.ID);
    }
    
    public void setShape() {
        CircleShape circle = new CircleShape();
        circle.setRadius(0.1f);
        fixture = body.createFixture(circle, 0);
        fixture.setFriction(2F);
        fixture.setDensity(0.5F);
        body.setLinearDamping(0.2F);
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        
        if (!hit) {
            // air resistance 0.99
        } else {
            // air resistance 0.95
        }
        if (/*onGround ||*/Math.abs(velocity.x) < 0.01 && Math.abs(velocity.y) < 0.01) {
            velocity.x *= 0.9;
            ticksOnGround++;
        }
        
        if (ticksOnGround > 60) {
            col.a = 1F - (ticksOnGround - 60F) / 60F;
        }
        if (ticksOnGround > 120) remove();
    }
    
}
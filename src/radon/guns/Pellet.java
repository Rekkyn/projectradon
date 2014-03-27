package radon.guns;

import org.jbox2d.collision.shapes.CircleShape;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import radon.Character;
import radon.GameWorld.Filtering;

public class Pellet extends Bullet {
    
    public Pellet(Character c) {
        super(c);
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(col);
        g.fillOval(x - 0.1F, -y - 0.1F, 0.2F, 0.2F);
    }
    
    @Override
    public void init() {
        subInit();
        CircleShape circle = new CircleShape();
        circle.setRadius(0.1f);
        fixture = body.createFixture(circle, 0);
        fixture.setFriction(2F);
        fixture.setDensity(0.3F);
        
        body.setBullet(true);
        body.setFixedRotation(true);
        body.setLinearDamping(1F);
        fixture.setRestitution(restitution);
        fixture.m_filter.categoryBits = Filtering.BULLET;
        fixture.m_filter.maskBits = Filtering.GROUND;
    }
}

package radon.guns;

import org.jbox2d.collision.shapes.CircleShape;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import radon.Character;

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
    public void setShape() {
        CircleShape circle = new CircleShape();
        circle.setRadius(0.1f);
        fixture = body.createFixture(circle, 0);
        fixture.setFriction(2F);
        fixture.setDensity(0.3F);
        body.setLinearDamping(1F);
    }
}

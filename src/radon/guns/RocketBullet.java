package radon.guns;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import radon.Character;

public class RocketBullet extends Bullet {
    public RocketBullet(Character c) {
        super(c);
        // invMass = 0.05F;
        col = new Color(112, 128, 144);
        gravity = false;
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        Vector2f v = new Vector2f(20, 0);
        // v.setTheta(prevVelocity.getTheta());
        // System.out.println(velocity.getTheta() + "     " +
        // prevVelocity.getTheta());
        // force.add(v);
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {}
    
    @Override
    public void onHit() {
        remove();
    }
    
}

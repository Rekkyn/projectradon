package radon.guns;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import radon.Character;
import radon.GameWorld;

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
        v.setTheta(prevVelocity.getTheta());
        // System.out.println(velocity.getTheta() + "     " +
        // prevVelocity.getTheta());
        // force.add(v);
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.pushTransform();
        g.translate(GameWorld.partialTicks * (x - prevX), GameWorld.partialTicks * (y - prevY));
        g.setColor(col);
        float xRotate = x;
        float yRotate = y;
        g.rotate(xRotate, yRotate, (float) velocity.getTheta());
        g.fillRect(x - 6, y - 3, 12, 6);
        g.rotate(xRotate, yRotate, (float) -velocity.getTheta());
        g.popTransform();
    }
    
    @Override
    public void onHit() {
        remove();
    }
    
}

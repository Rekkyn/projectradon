package radon.guns;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import radon.Player;
import radon.World;

public class RocketBullet extends Bullet {
    public RocketBullet(Player p) {
        super(p);
        invMass = 0.05F;
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
        force.add(v);
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.pushTransform();
        g.setColor(new Color(0, 0, 0));
        g.drawLine(x, y, x + prevVelocity.x, y + prevVelocity.y);
        g.drawLine(x, y, x + velocity.x, y + velocity.y);
        g.translate(World.partialTicks * (x - prevX), World.partialTicks * (y - prevY));
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

package radon.guns;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import radon.Character;
import radon.World;

public class HeavyBullet extends Bullet {

    public HeavyBullet(Character c) {
        super(c);
        invMass = 0.1F;
        col = new Color(178, 147, 41);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.pushTransform();
        g.translate(World.partialTicks * (x - prevX), World.partialTicks * (y - prevY));
        g.setColor(col);
        float xRotate = x;
        float yRotate = y;
        g.rotate(xRotate, yRotate, (float) velocity.getTheta());
        g.fillRect(x - 3, y - 1.5F, 6, 3);
        g.rotate(xRotate, yRotate, (float) -velocity.getTheta());
        g.popTransform();
    }

}

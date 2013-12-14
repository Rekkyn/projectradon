package radon.guns;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import radon.Character;
import radon.Entity;
import radon.World;

public class Bullet extends Entity {
    public int ticksOnGround = 0;
    public boolean hit = false;
    public Character c;

    public Bullet(Character c) {
        super(c.x, c.y);
        this.c = c;
        invMass = 0.99F;
        restitution = 0.1F;
        gravity = true;
        col = new Color(50, 50, 50);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.pushTransform();
        g.translate(World.partialTicks * (x - prevX), World.partialTicks * (y - prevY));
        g.setColor(col);
        float xRotate = x;
        float yRotate = y;
        g.rotate(xRotate, yRotate, (float) velocity.getTheta());
        g.fillRect(x - 2, y - 1, 4, 2);
        g.rotate(xRotate, yRotate, (float) -velocity.getTheta());
        g.popTransform();
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);

        if (!hit) {
            velocity.scale(0.99F);
        } else {
            velocity.scale(0.95F);
        }

        if (onGround) {
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
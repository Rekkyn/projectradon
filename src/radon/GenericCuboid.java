package radon;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GenericCuboid extends Entity {
    
    public Color col;
    
    public GenericCuboid(float x, float y, int colourR, int colourG, int colourB, float width, float height, boolean physicsactive) {
        super(x, y, width, height);
        col = new Color(colourR, colourG, colourB);
        this.width = width;
        this.height = height;
        physics = physicsactive;
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(col);
        g.pushTransform();
        g.translate(World.partialTicks * (x - prevX), World.partialTicks * (y - prevY));
        g.fillRect(x - width / 2, y - height / 2, width, height);
        g.setColor(Color.black);
        // g.drawLine(x, y, x + velocity.x * 10, y + velocity.y * 10);
        g.popTransform();
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        if (!(this instanceof Player)) {
            velocity.scale(0.8F);
        }
    }
}

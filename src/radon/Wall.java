package radon;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class Wall extends Entity {
    
    public Wall(float x, float y, int colourR, int colourG, int colourB, float width, float height) {
        super(x, y, BodyType.STATIC);
        col = new Color(colourR, colourG, colourB);
        this.width = width;
        this.height = height;
        
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width / 2, height / 2);
        
        fd.shape = ps;
        fd.density = 1.0F;
        fd.friction = 0.3F;
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.setColor(col);
        g.pushTransform();
        g.translate(GameWorld.partialTicks * (x - prevX), GameWorld.partialTicks * (y - prevY));
        g.scale(20, 20);
        g.fillRect(x - width / 2, y - height / 2, width, height);
        g.setColor(Color.black);
        // g.drawLine(x, y, x + velocity.x * 10, y + velocity.y * 10);
        g.popTransform();
    }
    
}

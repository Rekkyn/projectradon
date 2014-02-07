package radon;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import radon.GameWorld.Filtering;

public class DynamicBox extends Entity {
    
    public DynamicBox(float x, float y, int colourR, int colourG, int colourB, float width, float height, boolean gravity) {
        super(x, y, BodyType.DYNAMIC);
        col = new Color(colourR, colourG, colourB);
        this.width = width;
        this.height = height;
        this.gravity = gravity;
        restitution = 0.0F;
    }
    
    @Override
    public void init() {
        super.init();
        
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width / 2, height / 2);
        fixture = body.createFixture(ps, 1);
        fixture.setFriction(0.3F);
        fixture.setDensity(20);
        fixture.setRestitution(restitution);
        fixture.m_filter.categoryBits = Filtering.GROUND;
        fixture.m_filter.maskBits = Filtering.GROUND | Filtering.PLAYER | Filtering.BULLET;
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        super.render(container, game, g);
        g.fillRect(x - width / 2, -y - height / 2, width, height);
        g.setColor(Color.black);
        // g.drawLine(x, y, x + velocity.x * 10, y + velocity.y * 10);
    }
    
}

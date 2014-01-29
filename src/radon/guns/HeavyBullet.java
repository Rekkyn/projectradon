package radon.guns;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import radon.Character;

public class HeavyBullet extends Bullet {
    
    public HeavyBullet(Character c) {
        super(c);
        // invMass = 0.1F;
        col = new Color(178, 147, 41);
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {}
    
}

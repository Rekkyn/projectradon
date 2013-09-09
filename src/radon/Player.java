package radon;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Player extends Entity {
    
    public float walkSpeed = 3.0F;
    /** 0 = left, 1 = right */
    public byte direction = 0;
    
    public Player(float x, float y) {
        super(x, y);
        restitution = 0.0F;
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        velocity.y += 0.2;
        
        input = container.getInput();
        if (input.isKeyDown(Input.KEY_SPACE) && onGround) {
            velocity.y = -6;
        }
        if (input.isKeyDown(Input.KEY_A)) {
            if (onGround) {
                velocity.x = -walkSpeed;
            } else if (velocity.x > -2) {
                velocity.x = -walkSpeed / 2.5F;
            }
            direction = 0;
        } else if (input.isKeyDown(Input.KEY_D)) {
            if (onGround) {
                velocity.x = walkSpeed;
            } else if (velocity.x < 2) {
                velocity.x = walkSpeed / 2.5F;
            }
            direction = 1;
        } else {
            if (onGround) {
                velocity.x = 0;
            }
        }
    }
    
}

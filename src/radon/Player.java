package radon;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Player extends Entity {
    
    public float walkSpeed = 3.0F;
    public int walljumpCooldown = 0;
    
    public Player(float x, float y) {
        super(x, y);
        restitution = 0.0F;
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        velocity.y += 0.2;
                        
        input = container.getInput();
        if (input.isKeyPressed(Input.KEY_SPACE)) {
            if (onGround) {
                velocity.y = -6;
            } else if (onWall == 1) {
                velocity.set(0, -6).setTheta(-60);
                walljumpCooldown = 13;
            } else if (onWall == 2) {
                velocity.set(0, -6).setTheta(-120);
                walljumpCooldown = 13;
            }
        }
        
        walljumpCooldown--;
        if (walljumpCooldown < 0) walljumpCooldown = 0;
        
        if (input.isKeyDown(Input.KEY_A) && !input.isKeyDown(Input.KEY_D)) {
            if (onGround) {
                velocity.x = -walkSpeed;
            } else if (velocity.x >= -walkSpeed / 2.5F && walljumpCooldown == 0) {
                velocity.x = -walkSpeed / 2.5F;
            }
            if (onWall == 1 && walljumpCooldown == 0 && velocity.y > 0) {
                velocity.x = 0;
                velocity.y *= 0.9;
            }
        } else if (input.isKeyDown(Input.KEY_D) && !input.isKeyDown(Input.KEY_A)) {
            if (onGround) {
                velocity.x = walkSpeed;
            } else if (velocity.x <= walkSpeed / 2.5F && walljumpCooldown == 0) {
                velocity.x = walkSpeed / 2.5F;
            }
            if (onWall == 2 && walljumpCooldown == 0 && velocity.y > 0) {
                velocity.x = 0;
                velocity.y *= 0.9;
            }
        } else {
            if (onGround) {
                velocity.x = 0;
            }
        }
    }
    
}

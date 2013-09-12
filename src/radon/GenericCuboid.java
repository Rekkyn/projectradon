package radon;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import radon.Entity;


import org.newdawn.slick.state.StateBasedGame;


public class GenericCuboid extends Entity {
	 public float walkSpeed = 3.0F;
	    public int walljumpCooldown = 0;
	    boolean isControllable = false;
	public GenericCuboid(float x, float y, int colourR, int colourG, int colourB, float width, float height, boolean physicsactive, boolean isControllable) {
        
		super(x, y, new Color(colourR, colourG, colourB), width, height, physicsactive);
        this.isControllable = isControllable;
		restitution = 0.0F;
   
	
	// All our cuboids should be using this, it allows us to easily specify colour, width, height, physics and such. 
	//I made the random ones and the player use it. 
	//Adding cuboids is a lot cleaner now.
	//Also, we don't have to import slick's colour thingy every time, we can just enter the R G and B.
	
	
	
	
	
	
	}

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        
        if(isControllable == true){
        
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
}

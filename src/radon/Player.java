package radon;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Player extends GenericCuboid {
    
    public float walkSpeed = 3.0F;
    public int walljumpCooldown = 0;
    public static int R = 42;
    public static int G = 47;
    public static int B = 159;
    public static float width = 15;
    public static float height = 15;
    public static byte selectedSlot = 2;
    
    public Player(int x, int y) {
        super(x, y, R, G, B, width, height, true);
        restitution = 0.0F;
        gravity = true;
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
                
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
        
        if (input.isKeyPressed(Input.KEY_2)) selectedSlot = 2;
        if (input.isKeyPressed(Input.KEY_3)) selectedSlot = 3;
        
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            float dx = input.getMouseX() - x;
            float dy = input.getMouseY() - y;
            float angle = (float) (Math.atan2(dy, dx) * 180 / Math.PI);
            
            if (selectedSlot == 2) firePistol(angle);
            if (selectedSlot == 3) fireShotgun(angle);
        }
        
    }

    private void firePistol(float angle) {
        float bulletforce = 20;
        Bullet b = new Bullet(x, y);
        b.velocity.set(velocity);
        b.force.add(new Vector2f(bulletforce, 0));
        b.force.setTheta(angle);
        Vector2f v = new Vector2f(bulletforce, 0);
        v.setTheta(180+angle);
        force.add(v);
        
        World.add(b);
    }
    
    private void fireShotgun(float angle) {
        float bulletforce = 10;
        for (int i = 0; i < 5; i++) {
            float angleSpread = angle + (rand.nextFloat() * 2 - 1) * 10;
        Bullet b = new Bullet(x, y);
        b.velocity.set(velocity);
        b.force.add(new Vector2f(bulletforce, 0));
        b.force.setTheta(angleSpread);
        Vector2f v = new Vector2f(bulletforce, 0);
        v.setTheta(180+angleSpread);
        force.add(v);
        
        World.add(b);
        }
    }

}

package radon;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import radon.guns.ForcefulNature;
import radon.guns.Gun;
import radon.guns.Pistol;
import radon.guns.Shotgun;

public class Player extends GenericCuboid {
    
    public float walkSpeed = 3.0F;
    public int walljumpCooldown = 0;
    public static int R = 42;
    public static int G = 47;
    public static int B = 159;
    public static float width = 15;
    public static float height = 15;
    private Pistol pistol = new Pistol(this);
    private Shotgun shotgun = new Shotgun(this);
    private ForcefulNature fan = new ForcefulNature(this);
    public Gun selectedGun = pistol;
    public int fireDelay = selectedGun.autoFireRate;
    public boolean shotToBeFired = false;
    
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
        
        if (input.isKeyPressed(Input.KEY_2)) selectedGun = pistol;
        if (input.isKeyPressed(Input.KEY_3)) selectedGun = shotgun;
        if (input.isKeyPressed(Input.KEY_0)) selectedGun = fan;
        
        float dx = input.getMouseX() - x;
        float dy = input.getMouseY() - y;
        float angle = (float) (Math.atan2(dy, dx) * 180 / Math.PI);
        
        if (shotToBeFired && fireDelay >= selectedGun.manualFireRate) {
            selectedGun.fireManual(angle, fireDelay);
            fireDelay = 0;
            shotToBeFired = false;
        }
        
        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && fireDelay >= selectedGun.autoFireRate && selectedGun.autoFireRate != 0) {
            selectedGun.fireAuto(angle);
            fireDelay = 0;
            shotToBeFired = false;
        }
        
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && fireDelay != 0) {
            if (fireDelay >= selectedGun.manualFireRate) {
                selectedGun.fireManual(angle, fireDelay);
                fireDelay = 0;
            } else if (selectedGun.autoFireRate != 0) {
                shotToBeFired = true;
            }
        }
        
        fireDelay++;
        
    }
    
}

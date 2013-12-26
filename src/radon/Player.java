package radon;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import radon.guns.*;

public class Player extends Character {
    
    public float walkSpeed = 3.0F;
    public int walljumpCooldown = 0;
    private Pistol pistol = new Pistol(this);
    private RocketLauncher launcher = new RocketLauncher(this);
    private Shotgun shotgun = new Shotgun(this);
    private ForcefulNature fan = new ForcefulNature(this);
    private ForcefulleristNature forcefullest = new ForcefulleristNature(this);
    private float gunAngle;
    
    public Player(int x, int y) {
        super(x, y, 42, 47, 159, 20, 20, true);
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
        if (input.isKeyPressed(Input.KEY_1)) selectedGun = launcher;
        if (input.isKeyPressed(Input.KEY_Q) && input.isKeyPressed(Input.KEY_Z)) selectedGun = forcefullest;
        
        float dx = input.getMouseX() - x;
        float dy = input.getMouseY() - y;
        gunAngle = (float) (Math.atan2(dy, dx) * 180 / Math.PI);
        
        if (shotToBeFired && fireDelay >= selectedGun.manualFireRate) {
            selectedGun.fireManual(gunAngle, fireDelay);
            fireDelay = 0;
            shotToBeFired = false;
        }
        
        if (GameWorld.gunFocus) {
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && fireDelay >= selectedGun.autoFireRate && selectedGun.autoFireRate != 0) {
                selectedGun.fireAuto(gunAngle);
                fireDelay = 0;
                shotToBeFired = false;
            }
            
            if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && fireDelay != 0) {
                if (fireDelay >= selectedGun.manualFireRate) {
                    selectedGun.fireManual(gunAngle, fireDelay);
                    fireDelay = 0;
                } else if (selectedGun.autoFireRate != 0) {
                    shotToBeFired = true;
                }
            }
        }
        
        fireDelay++;
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        super.render(container, game, g);
        g.pushTransform();
        g.translate(GameWorld.partialTicks * (x - prevX), GameWorld.partialTicks * (y - prevY));
        g.setLineWidth(3);
        g.setColor(new Color(100, 100, 100));
        g.drawLine(x, y, x + 30 * (float) Math.cos(gunAngle * Math.PI / 180), y + 30 * (float) Math.sin(gunAngle * Math.PI / 180));
        g.popTransform();
    }
    
}

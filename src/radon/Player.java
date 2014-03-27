package radon;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import radon.guns.*;

public class Player extends Character {
    
    private Pistol pistol = new Pistol(this);
    private RocketLauncher launcher = new RocketLauncher(this);
    private Shotgun shotgun = new Shotgun(this);
    private ForcefulNature fan = new ForcefulNature(this);
    private ForcefulleristNature forcefullest = new ForcefulleristNature(this);
    
    public Player(int x, int y) {
        super(x, y, true);
        gravity = true;
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        
        input = container.getInput();
        
        if (input.isKeyDown(Input.KEY_A) && !input.isKeyDown(Input.KEY_D)) {
            move(MoveState.LEFT);
        } else if (input.isKeyDown(Input.KEY_D) && !input.isKeyDown(Input.KEY_A)) {
            move(MoveState.RIGHT);
        } else {
            move(MoveState.STOP);
        }
        
        if (input.isKeyPressed(Input.KEY_SPACE)) {
            jump();
        }
        
        if (input.isKeyPressed(Input.KEY_2)) selectedGun = pistol;
        if (input.isKeyPressed(Input.KEY_3)) selectedGun = shotgun;
        if (input.isKeyPressed(Input.KEY_0)) selectedGun = fan;
        if (input.isKeyPressed(Input.KEY_1)) selectedGun = launcher;
        if (input.isKeyPressed(Input.KEY_Q) && input.isKeyPressed(Input.KEY_Z)) selectedGun = forcefullest;
        
        float dx = GameWorld.mousePos(container).x - x;
        float dy = GameWorld.mousePos(container).y - y;
        gunAngle = (float) (Math.atan2(dy, dx) * 180 / Math.PI);
        
        selectedGun.calculateSpread(fireDelay);
        
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
        // g.translate(GameWorld.partialTicks * (x - prevX),
        // GameWorld.partialTicks * (y - prevY));
        g.setLineWidth(3F / 20F * Camera.zoom);
        g.setColor(new Color(100, 100, 100));
        g.drawLine(x, -y, x + 1.5F * (float) Math.cos(gunAngle * Math.PI / 180), -y - 1.5F * (float) Math.sin(gunAngle * Math.PI / 180));
    }
}

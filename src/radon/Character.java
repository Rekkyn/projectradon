package radon;

import org.jbox2d.common.Vec2;

import radon.guns.Gun;
import radon.guns.Pistol;

public class Character extends DynamicBox {
    
    public float walkSpeed = 3.0F;
    public int walljumpCooldown = 0;
    private Pistol pistol = new Pistol(this);
    public Gun selectedGun = pistol;
    public int fireDelay = selectedGun.autoFireRate;
    public boolean shotToBeFired = false;
    
    public Character(float x, float y, int colourR, int colourG, int colourB, float width, float height, boolean physicsactive) {
        super(x, y, colourR, colourG, colourB, width, height, true);
        // restitution = 0.0F;
        gravity = true;
    }
    
    public void move(MoveState state) {
        float desiredVel = 0;
        float change = 1.5F;
        float max = 5F;
        switch (state) {
            case LEFT:
                desiredVel = velocity.x - change > -max ? velocity.x - change : -max;
                break;
            case STOP:
                desiredVel = velocity.x * (1 - change / max);
                break;
            case RIGHT:
                desiredVel = velocity.x + change < max ? velocity.x + change : max;
                break;
        }
        float velChange = desiredVel - velocity.x;
        float impulse = body.getMass() * velChange; // disregard time factor
        body.applyLinearImpulse(new Vec2(impulse, 0), body.getWorldCenter());
    }
    
    public void jump() {
        body.applyLinearImpulse(new Vec2(0, 10), body.getWorldCenter());
    }
    
    public enum MoveState {
        LEFT, STOP, RIGHT
    }
}

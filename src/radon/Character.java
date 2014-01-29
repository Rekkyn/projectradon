package radon;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import radon.guns.Gun;
import radon.guns.Pistol;

public class Character extends Entity {
    
    public float walkSpeed = 5.0F;
    public int walljumpCooldown = 0;
    private Pistol pistol = new Pistol(this);
    public Gun selectedGun = pistol;
    public int fireDelay = selectedGun.autoFireRate;
    public boolean shotToBeFired = false;
    public int numContacts;
    public boolean onGround;
    public List<Float> contactAngles = new ArrayList<Float>();
    public List<Float> groundAngles = new ArrayList<Float>();
    
    public Character(float x, float y, int colourR, int colourG, int colourB, float width, float height, boolean physicsactive) {
        super(x, y, BodyType.DYNAMIC);
        // restitution = 0.0F;
        gravity = true;
    }
    
    @Override
    public void init() {
        super.init();
        
        CircleShape circle = new CircleShape();
        circle.setRadius(0.5f);
        fixture = body.createFixture(circle, 0);
        
        body.setBullet(true);
        body.setFixedRotation(true);
        body.setUserData(this);
        
        fixture.setFriction(0);
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        
        onGround = onGround();
        contactAngles.clear();
    }
    
    public boolean onGround() {
        groundAngles.clear();
        if (numContacts > 0) {
            boolean result = false;
            for (float angle : contactAngles) {
                if (angle < 90 + 60 && angle > 90 - 60) {
                    groundAngles.add(angle);
                    result = true;
                }
            }
            return result;
        }
        return false;
    }
    
    public void move(MoveState state) {
        float desiredVel = 0;
        float change = 1F;
        if (onGround) {
            Vec2 ground = new Vec2((float) Math.sin(Math.toRadians(groundAngles.get(0))), (float) -Math.cos(Math.toRadians(groundAngles
                    .get(0))));
            float groundvel = Vec2.dot(ground, velocity);
            switch (state) {
                case LEFT:
                    desiredVel = groundvel - change > -walkSpeed ? groundvel - change : -walkSpeed;
                    break;
                case STOP:
                    desiredVel = groundvel * (1 - change / walkSpeed);
                    break;
                case RIGHT:
                    desiredVel = groundvel + change < walkSpeed ? groundvel + change : walkSpeed;
                    break;
            }
            float velChange = desiredVel - groundvel;
            float impulse = body.getMass() * velChange; // disregard time factor
            Vec2 impulsevec = new Vec2(impulse * (float) Math.sin(Math.toRadians(groundAngles.get(0))), impulse
                    * (float) -Math.cos(Math.toRadians(groundAngles.get(0))));
            body.applyLinearImpulse(impulsevec, body.getWorldCenter());
            System.out.println(impulsevec);
        } else {
            float speed;
            if (numContacts == 0) {
                speed = walkSpeed / 2F;
            } else {
                speed = walkSpeed / 10F;
            }
            switch (state) {
                case LEFT:
                    if (velocity.x >= -speed) {
                        desiredVel = velocity.x - change > -speed ? velocity.x - change : -speed;
                    } else {
                        desiredVel = velocity.x;
                    }
                    break;
                case STOP:
                    desiredVel = velocity.x;
                    break;
                case RIGHT:
                    if (velocity.x <= speed) {
                        desiredVel = velocity.x + change < speed ? velocity.x + change : speed;
                    } else {
                        desiredVel = velocity.x;
                    }
                    break;
            }
            float velChange = desiredVel - velocity.x;
            float impulse = body.getMass() * velChange; // disregard time factor
            body.applyLinearImpulse(new Vec2(impulse, 0), body.getWorldCenter());
        }
    }
    
    public void jump() {
        if (onGround) body.applyLinearImpulse(new Vec2(0, 10), body.getWorldCenter());
    }
    
    public enum MoveState {
        LEFT, STOP, RIGHT
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        super.render(container, game, g);
        g.setColor(onGround ? new Color(42, 47, 159) : new Color(0, 0, 0));
        g.fillOval(x - 0.5F, -y - 0.5F, 1F, 1F);
    }
    
}

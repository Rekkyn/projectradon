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
    
    public float walkSpeed = 7.0F;
    private Pistol pistol = new Pistol(this);
    public Gun selectedGun = pistol;
    public int fireDelay = selectedGun.autoFireRate;
    public boolean shotToBeFired = false;
    public int numContacts;
    public boolean onGround;
    public boolean leftWall;
    public boolean rightWall;
    public int wallcooldown;
    public int walljumpCooldown;
    public static final int WALLJUMPDELAY = 15;
    public List<Float> contactAngles = new ArrayList<Float>();
    public List<Float> groundAngles = new ArrayList<Float>();
    public float gunAngle;
    public int ID;
    public int health, maxhealth;
    
    public Character(float x, float y) {
        super(x, y, BodyType.DYNAMIC);
        restitution = 0.0F;
        gravity = true;
        health = maxhealth = 100;
    }
    
    @Override
    public void init() {
        super.init();
        
        CircleShape circle = new CircleShape();
        circle.setRadius(0.5f);
        fixture = body.createFixture(circle, 0);
        fixture.setFriction(0.2F);
        
        body.setBullet(true);
        body.setFixedRotation(true);
        body.setUserData(this);
        fixture.setRestitution(restitution);;
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        
        checkCollisions();
        walljumpCooldown--;
        if (walljumpCooldown < 0) walljumpCooldown = 0;
        
        contactAngles.clear();
    }
    
    public void checkCollisions() {
        groundAngles.clear();
        boolean groundtemp = false;
        boolean lefttemp = false;
        boolean righttemp = false;
        if (numContacts > 0) {
            for (float angle : contactAngles) {
                if (angle < 150 && angle > 30) {
                    groundAngles.add(angle);
                    groundtemp = true;
                }
                if (angle > -30 && angle < 30) {
                    lefttemp = true;
                }
                if (angle > 150 || angle < -150) {
                    righttemp = true;
                }
            }
        }
        onGround = groundtemp;
        leftWall = lefttemp;
        rightWall = righttemp;
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
            float impulse = body.getMass() * velChange;
            Vec2 impulsevec = new Vec2(impulse * (float) Math.sin(Math.toRadians(groundAngles.get(0))), impulse
                    * (float) -Math.cos(Math.toRadians(groundAngles.get(0))));
            body.applyLinearImpulse(impulsevec, body.getWorldCenter());
        } else {
            float speed;
            speed = walkSpeed / 2F;
            body.setLinearDamping(0F);
            switch (state) {
                case LEFT:
                    if (leftWall) {
                        if (velocity.y > 0) {
                            break;
                        } else {
                            body.setLinearDamping(5F);
                        }
                    }
                    if (rightWall) {
                        wallcooldown++;
                    }
                    if (!rightWall && !leftWall || rightWall && wallcooldown > 10) {
                        if (velocity.x >= -speed) {
                            change *= -walljumpCooldown / (float) WALLJUMPDELAY + 1;
                            desiredVel = velocity.x - change > -speed ? velocity.x - change : -speed;
                        } else {
                            desiredVel = velocity.x;
                        }
                    }
                    break;
                case STOP:
                    desiredVel = velocity.x;
                    wallcooldown = 0;
                    break;
                case RIGHT:
                    if (rightWall) {
                        if (velocity.y > 0) {
                            break;
                        } else {
                            body.setLinearDamping(5F);
                        }
                    }
                    if (leftWall) {
                        wallcooldown++;
                    }
                    if (!rightWall && !leftWall || leftWall && wallcooldown > 10) {
                        if (velocity.x <= speed) {
                            change *= -walljumpCooldown / (float) WALLJUMPDELAY + 1;
                            desiredVel = velocity.x + change < speed ? velocity.x + change : speed;
                        } else {
                            desiredVel = velocity.x;
                        }
                    }
                    break;
            }
            float velChange = desiredVel - velocity.x;
            float impulse = body.getMass() * velChange; // disregard time factor
            body.applyLinearImpulse(new Vec2(impulse, 0), body.getWorldCenter());
        }
    }
    
    public void jump() {
        float height = 15F;
        if (onGround) {
            body.applyLinearImpulse(new Vec2(0, height), body.getWorldCenter());
        } else if (leftWall && rightWall) {
            // TODO: this
        } else if (leftWall) {
            body.setLinearVelocity(new Vec2(0, 0)); // TODO: this?
            body.applyLinearImpulse(new Vec2(body.getMass() * height * (float) Math.cos(Math.PI / 3), body.getMass() * height
                    * (float) Math.sin(Math.PI / 3)), body.getWorldCenter());
            body.setTransform(new Vec2(x + 0.01F, y), 0);
            walljumpCooldown = WALLJUMPDELAY;
            wallcooldown = 11;
        } else if (rightWall) {
            body.setLinearVelocity(new Vec2(0, 0));
            body.applyLinearImpulse(new Vec2(body.getMass() * height * (float) Math.cos(2 * Math.PI / 3), body.getMass() * height
                    * (float) Math.sin(2 * Math.PI / 3)), body.getWorldCenter());
            body.setTransform(new Vec2(x - 0.01F, y), 0);
            walljumpCooldown = WALLJUMPDELAY;
            wallcooldown = 11;
        }
    }
    
    public enum MoveState {
        LEFT, STOP, RIGHT
    }
    
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        super.render(container, game, g);
        g.setColor(Color.black);
        g.fillOval(x - 0.5F, -y - 0.5F, 1F, 1F);
        g.scale(1F / Camera.zoom, 1F / Camera.zoom);
        Font.centerText("" + health, x * Camera.zoom, (-y - 1.5F) * Camera.zoom, 2, g);
        g.scale(Camera.zoom, Camera.zoom);
    }
    
}

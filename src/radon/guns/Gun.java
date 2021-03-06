package radon.guns;

import java.util.Random;

import org.jbox2d.common.Vec2;

import radon.Character;
import radon.GameWorld;

public abstract class Gun {
    
    Random rand = new Random();
    
    public Character c;
    
    public int autoFireRate;
    public int manualFireRate;
    public float bulletSpread = 0;
    public float maxSpread;
    public float minSpread;
    public float bulletForce;
    
    public enum BulletType {
        NORMAL, HEAVY, ROCKET, PELLET
    }
    
    public Gun(Character character) {
        c = character;
    }
    
    public abstract void fireAuto(float angle);
    
    public abstract void fireManual(float angle, int fireDelay);
    
    public void fireBullet(float angle, float force, BulletType type) {
        Bullet b;
        switch (type) {
            case HEAVY:
                b = new HeavyBullet(c);
                break;
            case ROCKET:
                b = new RocketBullet(c);
                break;
            case PELLET:
                b = new Pellet(c);
                break;
            default:
                b = new Bullet(c);
        }
        
        GameWorld.add(b);
        b.body.setLinearVelocity(c.velocity);
        b.body.applyLinearImpulse(
                new Vec2(force * (float) Math.cos(angle * Math.PI / 180), force * (float) Math.sin(angle * Math.PI / 180)),
                b.body.getWorldCenter());
        c.body.applyLinearImpulse(
                new Vec2(-force * (float) Math.cos(angle * Math.PI / 180), -force * (float) Math.sin(angle * Math.PI / 180)),
                b.body.getWorldCenter());
        
    }
    
    public void calculateSpread(int fireDelay) {
        if (autoFireRate == 0 || fireDelay >= autoFireRate) {
            bulletSpread = minSpread;
        } else {
            bulletSpread = (fireDelay - manualFireRate) * (maxSpread - minSpread) / (manualFireRate - autoFireRate) + maxSpread;
        }
    }
    
}

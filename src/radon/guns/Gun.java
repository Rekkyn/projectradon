package radon.guns;

import java.util.Random;

import org.newdawn.slick.geom.Vector2f;

import radon.Character;
import radon.GameWorld;

public abstract class Gun {
    
    Random rand = new Random();
    
    public Character c;
    
    public int autoFireRate;
    public int manualFireRate;
    
    public enum BulletType {
        NORMAL, HEAVY, ROCKET
    }
    
    public Gun(Character character, int autoFireRate, int manualFireRate) {
        c = character;
        this.autoFireRate = autoFireRate;
        this.manualFireRate = manualFireRate;
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
            
            default:
                b = new Bullet(c);
        }
        
        // if (b instanceof RocketBullet) {
        
        // b.velocity.set(1.5F, (float) p.velocity.getTheta());
        
        // } else {
        b.velocity.set(c.velocity); // should bullets be fired with relative
        // speed to the player?
        // }
        b.force.add(new Vector2f(force, 0));
        b.force.setTheta(angle);
        Vector2f v = new Vector2f(force, 0);
        v.setTheta(180 + angle);
        c.force.add(v);
        
        GameWorld.add(b);
    }
    
}

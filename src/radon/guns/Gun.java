package radon.guns;

import java.util.Random;

import org.newdawn.slick.geom.Vector2f;

import radon.Player;
import radon.World;

public abstract class Gun {
    
    Random rand = new Random();
    
    public Player p;
    
    public int autoFireRate;
    public int manualFireRate;
    
    public enum BulletType {
        NORMAL, HEAVY
    }
    
    public Gun(Player p, int autoFireRate, int manualFireRate) {
        this.p = p;
        this.autoFireRate = autoFireRate;
        this.manualFireRate = manualFireRate;
    }
    
    public abstract void fireAuto(float angle);
    
    public abstract void fireManual(float angle, int fireDelay);
    
    public void fireBullet(float angle, float force, BulletType type) {
        Bullet b;
        switch (type) {
        case HEAVY:
            b = new HeavyBullet(p);
            break;
        
        default:
            b = new Bullet(p);
        }
        b.velocity.set(p.velocity); // should bullets be fired with relative
                                    // speed to the player?
        b.force.add(new Vector2f(force, 0));
        b.force.setTheta(angle);
        Vector2f v = new Vector2f(force, 0);
        v.setTheta(180 + angle);
        p.force.add(v);
        
        World.add(b);
    }
    
}

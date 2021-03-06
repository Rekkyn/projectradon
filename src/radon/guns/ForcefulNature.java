package radon.guns;

import radon.Player;

public class ForcefulNature extends Gun {
    
    public static final int autoFireRate = 80;
    public static final int manualFireRate = 45;
    public static final float bulletForce = 200;
    
    public ForcefulNature(Player p) {
        super(p);
    }
    
    @Override
    public void fireAuto(float angle) {
        for (int i = 0; i < 5; i++) {
            float angleSpread = angle + (rand.nextFloat() * 2 - 1) * (2 * c.velocity.length() + 5);
            
            fireBullet(angleSpread, bulletForce, BulletType.HEAVY);
        }
        
    }
    
    @Override
    public void fireManual(float angle, int fireDelay) {
        for (int i = 0; i < 5; i++) {
            float angleSpread = angle + (rand.nextFloat() * 2 - 1) * (2 * c.velocity.length() + 5);
            fireBullet(angleSpread, bulletForce, BulletType.HEAVY);
        }
    }
    
}

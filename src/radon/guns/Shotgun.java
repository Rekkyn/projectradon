package radon.guns;

import radon.Player;

public class Shotgun extends Gun {

    public Shotgun(Player p) {
        super(p);
        autoFireRate = 0;
        manualFireRate = 45;
        maxSpread = 12F;
        minSpread = 3F;
        bulletForce = 0.5F;
    }
    
    @Override
    public void fireAuto(float angle) {
        for (int i = 0; i < 7; i++) {
            float angleSpread = angle + (rand.nextFloat() * 3 - 1) * (2 * c.velocity.length() + 5);
            
            fireBullet(angleSpread, bulletForce, BulletType.NORMAL);
        }
    }
    
    @Override
    public void fireManual(float angle, int fireDelay) {
        for (int i = 0; i < 7; i++) {
            float angleSpread = angle + (rand.nextFloat() * 3 - 1) * (2 * c.velocity.length() + 5);
            
            fireBullet(angleSpread, bulletForce, BulletType.NORMAL);
        }
    }
}

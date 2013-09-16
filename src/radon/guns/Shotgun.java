package radon.guns;

import radon.Player;

public class Shotgun extends Gun {
    
    public static final int autoFireRate = 0;
    public static final int manualFireRate = 45;
    public static final float bulletForce = 15;
    
    public Shotgun(Player p) {
        super(p, autoFireRate, manualFireRate);
    }
    
    @Override
    public void fireAuto(float angle) {
        return;
    }
    
    @Override
    public void fireManual(float angle, int fireDelay) {
        float bulletforce = 15;
        for (int i = 0; i < 5; i++) {
            float angleSpread = angle + (rand.nextFloat() * 2 - 1) * 10;
            fireBullet(angleSpread, bulletforce);
        }
    }
}

package radon.guns;

import radon.Character;

public class Shotgun extends Gun {
    
    public Shotgun(Character c) {
        super(c);
        autoFireRate = 0;
        manualFireRate = 45;
        minSpread = maxSpread = 5F;
        bulletForce = 0.5F;
    }
    
    @Override
    public void fireAuto(float angle) {}
    
    @Override
    public void fireManual(float angle, int fireDelay) {
        for (int i = 0; i < 10; i++) {
            fireBullet(angle + (rand.nextFloat() * 2F - 1F) * bulletSpread, bulletForce, BulletType.PELLET);
        }
    }
    
    @Override
    public void calculateSpread(int fireDelay) {
        bulletSpread = c.velocity.length() + minSpread;
    }
}
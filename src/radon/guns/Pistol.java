package radon.guns;

import radon.Character;

public class Pistol extends Gun {
    
    public Pistol(Character character) {
        super(character);
        autoFireRate = 20;
        manualFireRate = 7;
        maxSpread = 12F;
        minSpread = 3F;
        bulletForce = 1F;
    }
    
    @Override
    public void fireAuto(float angle) {
        fireBullet(angle + (rand.nextFloat() * 2F - 1F) * bulletSpread, bulletForce, BulletType.NORMAL);
    }
    
    @Override
    public void fireManual(float angle, int fireDelay) {
        fireBullet(angle + (rand.nextFloat() * 2F - 1F) * bulletSpread, bulletForce, BulletType.NORMAL);
    }
}
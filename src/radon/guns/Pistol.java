package radon.guns;

import radon.Character;

public class Pistol extends Gun {

    public static final int autoFireRate = 20;
    public static final int manualFireRate = 7;
    public static final float bulletForce = 20;

    public Pistol(Character character) {
        super(character, autoFireRate, manualFireRate);
    }

    @Override
    public void fireAuto(float angle) {
        fireBullet(angle + (rand.nextFloat() * 2 - 1) * 3, bulletForce, BulletType.NORMAL);
    }

    @Override
    public void fireManual(float angle, int fireDelay) {
        float bulletSpread = 8 - 8 / autoFireRate * fireDelay; // 8 is the max
        // spread at
        // minimum delay

        fireBullet(angle + (rand.nextFloat() * 2 - 1) * bulletSpread, bulletForce, BulletType.NORMAL);

    }

}
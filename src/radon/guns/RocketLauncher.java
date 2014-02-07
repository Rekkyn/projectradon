package radon.guns;

import radon.Player;

public class RocketLauncher extends Gun {
    
    public static final int autoFireRate = 0;
    public static final int manualFireRate = 60;
    public static final float bulletForce = 300;
    
    public RocketLauncher(Player p) {
        super(p);
    }
    
    @Override
    public void fireAuto(float angle) {}
    
    @Override
    public void fireManual(float angle, int fireDelay) {
        fireBullet(angle, bulletForce, BulletType.ROCKET);
    }
}

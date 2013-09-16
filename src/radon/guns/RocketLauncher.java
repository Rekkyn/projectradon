package radon.guns;

import radon.Player;
import radon.guns.Gun.BulletType;

public class RocketLauncher extends Gun {

	
	public static final int autoFireRate = 0;
    public static final int manualFireRate = 60;
    public static final float bulletForce = 300;
    
    public RocketLauncher(Player p) {
        super(p, autoFireRate, manualFireRate);
    }
	
	
	
	
	
	@Override
	public void fireAuto(float angle) {
		

	}

	 @Override
	    public void fireManual(float angle, int fireDelay) {
	       
	            float angleSpread = angle;
	            fireBullet(angleSpread, bulletForce, BulletType.ROCKET);
	        

	}

}

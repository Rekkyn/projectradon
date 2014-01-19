package radon;

import radon.guns.Gun;
import radon.guns.Pistol;

public class Character extends DynamicBox {
    
    public float walkSpeed = 3.0F;
    public int walljumpCooldown = 0;
    private Pistol pistol = new Pistol(this);
    public Gun selectedGun = pistol;
    public int fireDelay = selectedGun.autoFireRate;
    public boolean shotToBeFired = false;
    
    public Character(float x, float y, int colourR, int colourG, int colourB, float width, float height, boolean physicsactive) {
        super(x, y, colourR, colourG, colourB, width, height, true);
        // restitution = 0.0F;
        gravity = true;
    }
    
    
}

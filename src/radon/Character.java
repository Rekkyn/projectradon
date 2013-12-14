package radon;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import radon.guns.Gun;
import radon.guns.Pistol;

public class Character extends GenericCuboid {

    public float walkSpeed = 3.0F;
    public int walljumpCooldown = 0;
    private Pistol pistol = new Pistol(this);
    public Gun selectedGun = pistol;
    public int fireDelay = selectedGun.autoFireRate;
    public boolean shotToBeFired = false;

    public Character(float x, float y, int colourR, int colourG, int colourB, float width, float height, boolean physicsactive) {
        super(x, y, colourR, colourG, colourB, width, height, true);
        restitution = 0.0F;
        gravity = true;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        super.render(container, game, g);
    }

}

package radon;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class AI extends Character {
    
    Random rand = new Random();
    
    MoveState state = MoveState.STOP;
    
    public AI(float x, float y) {
        super(x, y);
    }
    
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        super.update(container, game, delta);
        
        if (rand.nextInt(60) == 0) {
            int num = rand.nextInt(3);
            if (num == 0) {
                state = MoveState.LEFT;
            } else if (num == 1) {
                state = MoveState.RIGHT;
            } else {
                state = MoveState.STOP;
            }
        }
        
        move(state);
        
        if (rand.nextInt(60) == 0) {
            jump();
        }
    }
}

package radon;

import org.jbox2d.callbacks.ContactFilter;
import org.jbox2d.dynamics.Fixture;

import radon.guns.Bullet;

public class RadonContactFilter extends ContactFilter {
    
    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        Object objectA = fixtureA.getBody().getUserData();
        Object objectB = fixtureB.getBody().getUserData();
        
        if (objectA instanceof Character && objectB instanceof Bullet) {
            return filterBullet((Bullet) objectB, (Character) objectA);
        }
        if (objectB instanceof Character && objectA instanceof Bullet) {
            return filterBullet((Bullet) objectA, (Character) objectB);
        }
        return true;
    }
    
    public boolean filterBullet(Bullet b, Character c) {
        if (b.c == c) return false;
        return true;
    }
}

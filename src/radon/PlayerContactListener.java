package radon;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

import radon.guns.Bullet;

public class PlayerContactListener implements ContactListener {
    
    @Override
    public void beginContact(Contact contact) {
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        
        Object bodyDataA = contact.getFixtureA().getBody().getUserData();
        if (bodyDataA instanceof Character) {
            ((Character) bodyDataA).numContacts++;
        }
        
        Object bodyDataB = contact.getFixtureB().getBody().getUserData();
        if (bodyDataB instanceof Character) {
            ((Character) bodyDataB).numContacts++;
        }
        
        if (bodyDataA instanceof Character && bodyDataB instanceof Bullet) {
            doDamage((Character) bodyDataA, (Bullet) bodyDataB);
        } else if (bodyDataB instanceof Character && bodyDataA instanceof Bullet) {
            doDamage((Character) bodyDataB, (Bullet) bodyDataA);
        }
    }
    
    public void doDamage(Character c, Bullet b) {
        Vec2 vel = b.velocity.sub(c.velocity);
        if (vel.length() < 5 || b.velocity.length() < 5) return;
        b.remove();
        c.health -= b.velocity.length() * 0.5F;
        if (c.health <= 0) c.remove();
    }
    
    @Override
    public void endContact(Contact contact) {
        Object bodyData = contact.getFixtureA().getBody().getUserData();
        if (bodyData instanceof Character) {
            ((Character) bodyData).numContacts--;
        }
        
        bodyData = contact.getFixtureB().getBody().getUserData();
        if (bodyData instanceof Character) {
            ((Character) bodyData).numContacts--;
        }
    }
    
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}
    
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        
        Object bodyData = contact.getFixtureA().getBody().getUserData();
        if (bodyData instanceof Character) {
            float angle = (float) (Math.atan2(worldManifold.normal.y, worldManifold.normal.x) * 180F / Math.PI);
            if (!((Character) bodyData).contactAngles.contains(angle)) {
                ((Character) bodyData).contactAngles.add(angle);
            }
        }
        
        bodyData = contact.getFixtureB().getBody().getUserData();
        if (bodyData instanceof Character) {
            float angle = (float) (Math.atan2(worldManifold.normal.y, worldManifold.normal.x) * 180F / Math.PI);
            if (!((Character) bodyData).contactAngles.contains(angle)) {
                ((Character) bodyData).contactAngles.add(angle);
            }
        }
    }
    
}

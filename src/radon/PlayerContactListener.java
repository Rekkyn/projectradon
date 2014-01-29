package radon;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.dynamics.contacts.Contact;

public class PlayerContactListener implements ContactListener {
    
    @Override
    public void beginContact(Contact contact) {
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        
        Object bodyData = contact.getFixtureA().getBody().getUserData();
        if (bodyData instanceof Character) {
            ((Character) bodyData).numContacts++;
        }
        
        bodyData = contact.getFixtureB().getBody().getUserData();
        if (bodyData instanceof Character) {
            ((Character) bodyData).numContacts++;
        }
        
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

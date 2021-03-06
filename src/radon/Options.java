package radon;

import org.newdawn.slick.SlickException;

/** Created by James Ward (epic_jdog)
 * <p/>
 * Date: 13/12/13 Time: 10:06 PM */
public class Options {
    boolean visibility = false;
    boolean fullscreen = false;
    private String id;
    
    public Options(String id) {
        this.id = id;
    }
    
    public void toggleVisibility() {
        visibility = !visibility;
    }
    
    public boolean getVisibility() {
        return visibility;
    }
    
    public void setFullscreen(boolean boool) throws SlickException {
        fullscreen = boool;
        Game.appgc.setFullscreen(fullscreen);
    }
    
    public String getId() {
        return id;
    }
    
    public boolean getFullscreen() {
        return fullscreen;
    }
    
}

package radon;

public class Camera {
    public static float zoom = 20;
    public static float x = Game.width / zoom / 2;
    public static float y = Game.height / zoom / 2;
    public static Entity following;
    
    public static void setFollowing(Entity e) {
        following = e;
    }
    
    public static void update() {
        if (following != null) {
            x = following.x;
            y = following.y;
        }
    }
}

package radon;

public class Camera {
    public static float zoom = 20;
    public static float x = 0;
    public static float y = 0;
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

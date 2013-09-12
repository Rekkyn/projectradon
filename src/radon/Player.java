package radon;


import radon.GenericCuboid;

public class Player extends GenericCuboid {
    
    public float walkSpeed = 3.0F;
    public int walljumpCooldown = 0;
    public static int R = 42;
    public static int G = 200;
    public static int B = 159;
    public static float width = 15;
    public static float height = 15;
    public static boolean physicsactive = false;
    public static boolean isControllable = true;
    
    public Player(int x, int y) {
        super(x, y, R, G, B, width, height, physicsactive, isControllable);
        restitution = 0.0F;
    }
}
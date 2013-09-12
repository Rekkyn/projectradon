package radon;

public class Segment {
    
    EndPoint p1, p2;
    
    public Segment() {
    }
    
    public class EndPoint {
        
        float x, y;
        Segment s;
        
        public EndPoint(float x, float y, Segment s) {
            this.x = x;
            this.y = y;
            this.s = s;
        }
    }
    
}

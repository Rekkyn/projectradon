package radon;

import java.util.ArrayList;
import java.util.List;

import radon.Segment.EndPoint;

public class Visibility {
    
    public static List<Segment> segments = new ArrayList<Segment>();
    public static List<EndPoint> endpoints = new ArrayList<EndPoint>();
    
    public static void load(List<Entity> entities) {
        segments.clear();
        endpoints.clear();
        
        for (Entity e: entities) {
            addSegment(e.x - e.width / 2, e.y - e.height / 2, e.x + e.width / 2, e.y - e.height / 2); // top
            addSegment(e.x + e.width / 2, e.y - e.height / 2, e.x + e.width / 2, e.y + e.height / 2); // right
            addSegment(e.x - e.width / 2, e.y + e.height / 2, e.x + e.width / 2, e.y + e.height / 2); // bottom
            addSegment(e.x - e.width / 2, e.y - e.height / 2, e.x - e.width / 2, e.y + e.height / 2); // left
        }
    }
    
    public static void addSegment(float x1, float y1, float x2, float y2) {
        Segment s = new Segment();
        EndPoint p1 = s.new EndPoint(x1, y1, s);
        EndPoint p2 = s.new EndPoint(x2, y2, s);
        s.p1 = p1;
        s.p2 = p2;
        
        segments.add(s);
        endpoints.add(p1);
        endpoints.add(p2);
    }
    
}

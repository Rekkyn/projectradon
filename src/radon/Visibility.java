// Calculate visible area from a position
// Copyright 2012 Red Blob Games
// License: Apache v2
// Converted to Java by Rekkyn

/* 
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package radon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import radon.Segment.EndPoint;

public class Visibility {
    
    public static List<Segment> segments = new ArrayList<Segment>();
    public static List<EndPoint> endpoints = new ArrayList<EndPoint>();
    public static List<Segment> open = new ArrayList<Segment>();
    public static Vector2f light = new Vector2f();
    public static List<Vector2f> output = new ArrayList<Vector2f>();
    public static List<Color> outputColours = new ArrayList<Color>();
    
    public static void load(List<Entity> entities) {
        segments.clear();
        endpoints.clear();
        
        for (Entity e : entities) {
            if (!(e instanceof Player)) {
                Color col = e.col;
                addSegment(e.x - e.width / 2, e.y - e.height / 2, e.x + e.width / 2, e.y - e.height / 2, col); // top
                addSegment(e.x + e.width / 2, e.y - e.height / 2, e.x + e.width / 2, e.y + e.height / 2, col); // right
                addSegment(e.x - e.width / 2, e.y + e.height / 2, e.x + e.width / 2, e.y + e.height / 2, col); // bottom
                addSegment(e.x - e.width / 2, e.y - e.height / 2, e.x - e.width / 2, e.y + e.height / 2, col); // left
            }
        }
        
        addSegment(0, 0, Game.width, 0, new Color(0, 0, 0)); // top
        addSegment(Game.width, 0, Game.width, Game.height, new Color(0, 0, 0)); // right
        addSegment(0, Game.height, Game.width, Game.height, new Color(0, 0, 0)); // bottom
        addSegment(0, 0, 0, Game.height, new Color(0, 0, 0)); // left
    }
    
    public static void addSegment(float x1, float y1, float x2, float y2, Color col) {
        Segment s = new Segment(col);
        EndPoint p1 = s.new EndPoint(x1, y1, s);
        EndPoint p2 = s.new EndPoint(x2, y2, s);
        s.p1 = p1;
        s.p2 = p2;
        
        segments.add(s);
        endpoints.add(p1);
        endpoints.add(p2);
    }
    
    public static void setLightLocation(float x, float y) {
        light.set(x, y);
        
        for (Segment s : segments) {
            float dx = 0.5F * (s.p1.x + s.p2.x) - x;
            float dy = 0.5F * (s.p1.y + s.p2.y) - y;
            // NOTE: we only use this for comparison so we can use
            // distance squared instead of distance
            s.distance = dx * dx + dy * dy;
            
            // NOTE: future optimization: we could record the quadrant
            // and the y/x or x/y ratio, and sort by (quadrant,
            // ratio), instead of calling atan2. See
            // <https://github.com/mikolalysenko/compare-slope> for a
            // library that does this.
            s.p1.angle = (float) Math.atan2(s.p1.y - y, s.p1.x - x);
            s.p2.angle = (float) Math.atan2(s.p2.y - y, s.p2.x - x);
            
            double dAngle = s.p2.angle - s.p1.angle;
            if (dAngle <= -Math.PI) {
                dAngle += 2 * Math.PI;
            }
            if (dAngle > Math.PI) {
                dAngle -= 2 * Math.PI;
            }
            s.p1.begin = dAngle > 0.0;
            s.p2.begin = !s.p1.begin;
        }
    }
    
    // Run the algorithm, sweeping over all or part of the circle to find
    // the visible area, represented as a set of triangles
    public static void sweep() {
        float maxAngle = 999.0F;
        output.clear();
        outputColours.clear();
        Collections.sort(endpoints);
        
        open.clear();
        float beginAngle = 0.0F;
        
        // At the beginning of the sweep we want to know which
        // segments are active. The simplest way to do this is to make
        // a pass collecting the segments, and make another pass to
        // both collect and process them. However it would be more
        // efficient to go through all the segments, figure out which
        // ones intersect the initial sweep line, and then sort them.
        for (int pass = 0; pass < 2; pass++) {
            for (EndPoint p : endpoints) {
                if (pass == 1 && p.angle > maxAngle) {
                    // Early exit for the visualization to show the sweep
                    // process
                    break;
                }
                
                Segment current_old = open.isEmpty() ? null : open.get(0);
                
                if (p.begin) {
                    // Insert into the right place in the list
                    Segment node = null;
                    if (!open.isEmpty()) {
                        node = open.get(0);
                    }
                    Iterator<Segment> it = open.iterator();
                    while (node != null && _segment_in_front_of(p.segment, node, light) && it.hasNext()) {
                        node = it.next();
                    }
                    if (node == null) {
                        open.add(p.segment);
                    } else {
                        open.add(open.indexOf(node), p.segment);
                    }
                } else {
                    open.remove(p.segment);
                }
                
                Segment current_new = open.isEmpty() ? null : open.get(0);
                if (current_old != current_new) {
                    if (pass == 1) {
                        addTriangle(beginAngle, p.angle, current_old);
                    }
                    beginAngle = p.angle;
                }
            }
        }
    }
    
    // Helper: do we know that segment a is in front of b?
    // Implementation not anti-symmetric (that is to say,
    // _segment_in_front_of(a, b) != (!_segment_in_front_of(b, a)).
    // Also note that it only has to work in a restricted set of cases
    // in the visibility algorithm; I don't think it handles all
    // cases. See
    // http://www.redblobgames.com/articles/visibility/segment-sorting.html
    private static boolean _segment_in_front_of(Segment a, Segment b, Vector2f light) {
        // NOTE: we slightly shorten the segments so that
        // intersections of the endpoints (common) don't count as
        // intersections in this algorithm
        boolean A1 = leftOf(a, interpolate(b.p1, b.p2, 0.01F));
        boolean A2 = leftOf(a, interpolate(b.p2, b.p1, 0.01F));
        boolean A3 = leftOf(a, light);
        boolean B1 = leftOf(b, interpolate(a.p1, a.p2, 0.01F));
        boolean B2 = leftOf(b, interpolate(a.p2, a.p1, 0.01F));
        boolean B3 = leftOf(b, light);
        
        // NOTE: this algorithm is probably worthy of a short article
        // but for now, draw it on paper to see how it works. Consider
        // the line A1-A2. If both B1 and B2 are on one side and
        // relativeTo is on the other side, then A is in between the
        // viewer and B. We can do the same with B1-B2: if A1 and A2
        // are on one side, and relativeTo is on the other side, then
        // B is in between the viewer and A.
        if (B1 == B2 && B2 != B3) return true;
        if (A1 == A2 && A2 == A3) return true;
        if (A1 == A2 && A2 != A3) return false;
        if (B1 == B2 && B2 == B3) return false;
        
        // If A1 != A2 and B1 != B2 then we have an intersection.
        // Expose it for the GUI to show a message. A more robust
        // implementation would split segments at intersections so
        // that part of the segment is in front and part is behind.
        // demo_intersectionsDetected.push([a.p1, a.p2, b.p1, b.p2]);
        return false;
        
        // NOTE: previous implementation was a.d < b.d. That's simpler
        // but trouble when the segments are of dissimilar sizes. If
        // you're on a grid and the segments are similarly sized, then
        // using distance will be a simpler and faster implementation.
    }
    
    // Helper: leftOf(segment, point) returns true if point is
    // "left" of segment treated as a vector
    static final private boolean leftOf(Segment s, Vector2f p) {
        float cross = (s.p2.x - s.p1.x) * (p.y - s.p1.y) - (s.p2.y - s.p1.y) * (p.x - s.p1.x);
        return cross < 0;
    }
    
    // Return p*(1-f) + q*f
    static private Vector2f interpolate(EndPoint p, EndPoint q, float f) {
        return new Vector2f(p.x * (1 - f) + q.x * f, p.y * (1 - f) + q.y * f);
    }
    
    private static void addTriangle(float angle1, float angle2, Segment segment) {
        Vector2f p1 = light;
        Vector2f p2 = new Vector2f(light.x + (float) Math.cos(angle1), light.y + (float) Math.sin(angle1));
        Vector2f p3 = new Vector2f();
        Vector2f p4 = new Vector2f();
        
        if (segment != null) {
            // Stop the triangle at the intersecting segment
            p3.x = segment.p1.x;
            p3.y = segment.p1.y;
            p4.x = segment.p2.x;
            p4.y = segment.p2.y;
        } else {
            // Stop the triangle at a fixed distance; this probably is
            // not what we want, but it never gets used in the demo
            p3.x = light.x + (float) Math.cos(angle1) * 500;
            p3.y = light.y + (float) Math.sin(angle1) * 500;
            p4.x = light.x + (float) Math.cos(angle2) * 500;
            p4.y = light.y + (float) Math.sin(angle2) * 500;
        }
        
        Vector2f pBegin = lineIntersection(p3, p4, p1, p2);
        
        p2.x = light.x + (float) Math.cos(angle2);
        p2.y = light.y + (float) Math.sin(angle2);
        Vector2f pEnd = lineIntersection(p3, p4, p1, p2);
        
        if (output.isEmpty()) {
            output.add(pBegin);
        } else {
            output.add(0, pBegin);
        }
        output.add(0, pEnd);
        outputColours.add(0, segment.col);
    }
    
    public static Vector2f lineIntersection(Vector2f p1, Vector2f p2, Vector2f p3, Vector2f p4) {
        // From http://paulbourke.net/geometry/lineline2d/
        float s = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x))
                / ((p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y));
        return new Vector2f(p1.x + s * (p2.x - p1.x), p1.y + s * (p2.y - p1.y));
    }
    
}

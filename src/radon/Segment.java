package radon;

import org.newdawn.slick.Color;

public class Segment {

    EndPoint p1, p2;
    public float distance;
    public Color col;

    public Segment(Color col) {
        this.col = col;
    }

    public class EndPoint implements Comparable<EndPoint> {

        float x, y;
        Segment segment;
        public float angle;
        public boolean begin;

        public EndPoint(float x, float y, Segment segment) {
            this.x = x;
            this.y = y;
            this.segment = segment;
        }

        @Override
        public int compareTo(EndPoint o) {
            if (angle > o.angle) return 1;
            if (angle < o.angle) return -1;
            // But for ties (common), we want Begin nodes before End nodes
            if (!begin && o.begin) return 1;
            if (begin && !o.begin) return -1;
            return 0;
        }
    }

}

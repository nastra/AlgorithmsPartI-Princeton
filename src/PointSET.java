import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> set;

    public PointSET() {
        set = new TreeSet<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        set.add(p);
    }

    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        // all points in the set that are inside the rectangle
        Queue<Point2D> q = new Queue<>();
        for (Point2D point : set) {
            if (rect.contains(point)) {
                q.enqueue(point);
            }
        }
        return q;
    }

    public Point2D nearest(Point2D p) {
        // a nearest neighbor in the set to p; null if set is empty
        if (set.isEmpty()) {
            return null;
        }
        Point2D nearest = null;
        double min = Double.MAX_VALUE;
        for (Point2D point : set) {
            double dist = p.distanceTo(point);
            if (!point.equals(p) && Double.compare(dist, min) < 0) {
                min = dist;
                nearest = point;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        PointSET pset = new PointSET();
        Point2D p = new Point2D(0.2, 0.3);
        RectHV rect = new RectHV(0.2, 0.2, 0.6, 0.6);
        pset.insert(p);
        for (int i = 0; i < 1000; i++)
            pset.insert(new Point2D(StdRandom.uniform(), StdRandom.uniform()));
        rect.draw();
        StdDraw.circle(p.x(), p.y(), p.distanceTo(pset.nearest(p)));
        pset.draw();
        StdDraw.show(0);
        StdOut.println("Nearest to " + p.toString() + " = " + pset.nearest(p));
        for (Point2D point : pset.range(rect))
            StdOut.println("In Range: " + point.toString());
    }
}

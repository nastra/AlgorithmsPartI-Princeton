import java.util.Comparator;

public class Point implements Comparable<Point> {

    public final Comparator<Point> SLOPE_ORDER = new SlopeComparator();

    private int x, y;

    public Point(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);

    }

    public void drawTo(Point that) {
        StdDraw.line(x, y, that.x, that.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int compareTo(Point that) {
        if (this.y < that.y)
            return -1;
        if (this.y > that.y)
            return +1;
        if (this.x < that.x)
            return -1;
        if (this.x > that.x)
            return +1;
        return 0;
    }

    public double slopeTo(Point that) {
        // degenerate line segments
        if (this.x == that.x && this.y == that.y) {
            return Double.NEGATIVE_INFINITY;
        }

        double yy = that.y - this.y;
        double xx = that.x - this.x;

        // horizontal line segments
        if (yy == 0.0) {
            return +0.0;
        }

        // vertical line segments
        if (xx == 0.0) {
            return Double.POSITIVE_INFINITY;
        }

        return yy / xx;
    }

    private class SlopeComparator implements Comparator<Point> {

        @Override
        public int compare(Point one, Point two) {
            double firstSlope = slopeTo(one);
            double secondSlope = slopeTo(two);

            return Double.compare(firstSlope, secondSlope);
        }
    }

    // Unit test
    public static void main(String[] args) {
        Point point1, point2;

        /* Vertical line segments should be +Infinity */
        point1 = new Point(5, 10);
        point2 = new Point(5, 7);
        assert point1.slopeTo(point2) == Double.POSITIVE_INFINITY : "Vertical line segments should be +Infinity";

        /* Horizontal line segments should be +0.0 */
        point1 = new Point(12, 3);
        point2 = new Point(3, 3);
        assert point1.slopeTo(point2) == +0.0 : "Horizontal line segments should be +0.0";

        /* The slope of a point with himself should be -Infinity */
        Point p = new Point(1, 5);
        assert p.slopeTo(p) == Double.NEGATIVE_INFINITY : "The slpe of a point with himself should be -Infinity";
    }
}

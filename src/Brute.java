import java.util.Arrays;

public class Brute {

    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        // Rescale coordinate system for proper visualization.
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.002);

        Point[] points = new Point[N];

        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            p.draw();
            points[i] = p;
        }

        for (int i = 0; i < N; i++) {
            Point p = points[i];
            for (int j = i + 1; j < N; j++) {
                Point q = points[j];
                for (int k = j + 1; k < N; k++) {
                    Point r = points[k];
                    if (!collinear(p, q, r)) {
                        continue;
                    }
                    for (int l = k + 1; l < N; l++) {
                        Point s = points[l];
                        if (collinear(p, q, r, s)) {
                            Point[] arr = new Point[] {p, q, r, s};
                            Arrays.sort(arr);
                            arr[0].drawTo(arr[3]);
                            StdOut.printf("%s -> %s -> %s -> %s\n", arr[0], arr[1], arr[2], arr[3]);
                        }
                    }
                }
            }
        }

    }

    private static boolean collinear(Point ref, Point... points) {
        double refslope = ref.slopeTo(points[0]);
        for (int i = 1; i < points.length; i++) {
            if (refslope != ref.slopeTo(points[i])) {
                return false;
            }
        }
        return true;
    }
}

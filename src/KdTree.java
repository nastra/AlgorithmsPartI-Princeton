import java.util.Set;
import java.util.TreeSet;

public class KdTree {
    private static final RectHV DEFAULT_RECTANGLE = new RectHV(0, 0, 1, 1);
    private Node root;

    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node leftBottom; // the left/bottom subtree
        private Node rightTop; // the right/top subtree
        private int size;

        public Node(Point2D p, int size, RectHV rect) {
            super();
            this.p = p;
            this.size = size;
            this.rect = rect;
        }

        private int compare(Point2D that, boolean vertical) {
            if (vertical) {
                return Point2D.X_ORDER.compare(p, that);
            }
            return Point2D.Y_ORDER.compare(p, that);
        }
    }

    public KdTree() {
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        } else {
            return x.size;
        }
    }

    public void insert(Point2D p) {
        root = insert(root, p, true, null, false);
    }

    private Node insert(Node x, Point2D p, boolean vertical, Node prev, boolean leftSide) {
        if (null == x) {
            RectHV r = createRectangle(x, prev, p, vertical, leftSide);
            return new Node(p, 1, r);
        }
        if (x.p.equals(p)) {
            return x;
        }
        int cmp = x.compare(p, vertical);
        if (cmp > 0) {
            x.leftBottom = insert(x.leftBottom, p, !vertical, x, true);
        } else if (cmp <= 0) {
            x.rightTop = insert(x.rightTop, p, !vertical, x, false);
        }
        x.size = size(x.leftBottom) + 1 + size(x.rightTop);
        return x;
    }

    private RectHV createRectangle(Node curr, Node prev, Point2D p, boolean vertical, boolean leftSide) {
        RectHV rect = null;
        if (null == prev) {
            rect = DEFAULT_RECTANGLE;
        } else if (vertical) {
            if (leftSide) {
                rect = new RectHV(prev.rect.xmin(), prev.rect.ymin(), prev.rect.xmax(), prev.p.y());
            } else {
                rect = new RectHV(prev.rect.xmin(), prev.p.y(), prev.rect.xmax(), prev.rect.ymax());
            }
        } else {
            if (leftSide) {
                rect = new RectHV(prev.rect.xmin(), prev.rect.ymin(), prev.p.x(), prev.rect.ymax());
            } else {
                rect = new RectHV(prev.p.x(), prev.rect.ymin(), prev.rect.xmax(), prev.rect.ymax());
            }
        }
        return rect;
    }

    public boolean contains(Point2D p) {
        return contains(root, p, true);
    }

    private boolean contains(Node x, Point2D p, boolean vertical) {
        if (null == x) {
            return false;
        }
        if (x.p.equals(p)) {
            return true;
        }

        int cmp = x.compare(p, vertical);
        if (cmp > 0) {
            return contains(x.leftBottom, p, !vertical);
        } else if (cmp <= 0) {
            return contains(x.rightTop, p, !vertical);
        }
        return false;
    }

    public void draw() {
        draw(root, true, true);
    }

    private void draw(Node x, boolean vertical, boolean left) {
        if (x == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        StdDraw.point(x.p.x(), x.p.y());
        StdDraw.text(x.p.x(), x.p.y(), x.p.toString());
        StdDraw.setPenRadius();
        final RectHV r = x.rect;
        if (vertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), r.ymax(), x.p.x(), r.ymin());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(r.xmin(), x.p.y(), r.xmax(), x.p.y());
        }
        draw(x.leftBottom, !vertical, true);
        draw(x.rightTop, !vertical, false);
    }

    public Iterable<Point2D> range(RectHV rect) {
        Set<Point2D> q = new TreeSet<>();
        range(root, rect, q);
        return q;
    }

    private void range(Node x, RectHV rect, Set<Point2D> q) {
        if (null == x) {
            return;
        }

        if (rect.intersects(x.rect)) {
            if (rect.contains(x.p)) {
                q.add(x.p);
            }
            range(x.leftBottom, rect, q);
            range(x.rightTop, rect, q);
        }
    }

    public Point2D nearest(Point2D p) {
        if (null == root) {
            return null;
        }
        Node nearest = nearest(root, p, Double.MAX_VALUE, true);
        if (null == nearest) {
            return null;
        }
        return nearest.p;
    }

    private Node nearest(Node x, Point2D p, double shortestDist, boolean vertical) {
        if (null == x) {
            return null;
        }
        Node min = x;
        Point2D curr = x.p;
        double dist = p.distanceSquaredTo(curr);
        if (less(dist, shortestDist)) {
            shortestDist = p.distanceSquaredTo(curr);
            min = x;
        }

        Node left = null;
        Node right = null;
        if (vertical) {
            if (lessOrEqual(p.x(), curr.x())) {
                left = nearest(x.leftBottom, p, shortestDist, !vertical);
                if (null != left && less(left.p.distanceSquaredTo(p), shortestDist)) {
                    shortestDist = p.distanceSquaredTo(left.p);
                    min = left;
                }
                if (null != x.rightTop && less(x.rightTop.rect.distanceSquaredTo(p), shortestDist)) {
                    right = nearest(x.rightTop, p, shortestDist, !vertical);
                    if (null != right && less(right.p.distanceSquaredTo(p), shortestDist)) {
                        shortestDist = p.distanceSquaredTo(right.p);
                        min = right;
                    }
                }
            } else {
                right = nearest(x.rightTop, p, shortestDist, !vertical);
                if (null != right && less(right.p.distanceSquaredTo(p), shortestDist)) {
                    shortestDist = p.distanceSquaredTo(right.p);
                    min = right;
                }
                if (null != x.leftBottom && less(x.leftBottom.rect.distanceSquaredTo(p), shortestDist)) {
                    left = nearest(x.leftBottom, p, shortestDist, !vertical);
                    if (null != left && less(left.p.distanceSquaredTo(p), shortestDist)) {
                        shortestDist = p.distanceSquaredTo(left.p);
                        min = left;
                    }
                }
            }
        } else {
            if (lessOrEqual(p.y(), curr.y())) {
                left = nearest(x.leftBottom, p, shortestDist, !vertical);
                if (null != left && less(left.p.distanceSquaredTo(p), shortestDist)) {
                    shortestDist = p.distanceSquaredTo(left.p);
                    min = left;
                }
                if (null != x.rightTop && less(x.rightTop.rect.distanceSquaredTo(p), shortestDist)) {
                    right = nearest(x.rightTop, p, shortestDist, !vertical);
                    if (null != right && less(right.p.distanceSquaredTo(p), shortestDist)) {
                        shortestDist = p.distanceSquaredTo(right.p);
                        min = right;
                    }
                }
            } else {
                right = nearest(x.rightTop, p, shortestDist, !vertical);
                if (null != right && less(right.p.distanceSquaredTo(p), shortestDist)) {
                    shortestDist = p.distanceSquaredTo(right.p);
                    min = right;
                }
                if (null != x.leftBottom && less(x.leftBottom.rect.distanceSquaredTo(p), shortestDist)) {
                    left = nearest(x.leftBottom, p, shortestDist, !vertical);
                    if (null != left && less(left.p.distanceSquaredTo(p), shortestDist)) {
                        shortestDist = p.distanceSquaredTo(left.p);
                        min = left;
                    }
                }
            }
        }
        // if (null != left && less(left.p.distanceSquaredTo(p), shortestDist)) {
        // shortestDist = p.distanceSquaredTo(left.p);
        // min = left;
        // }
        // if (null != right && less(right.p.distanceSquaredTo(p), shortestDist)) {
        // shortestDist = p.distanceSquaredTo(right.p);
        // min = right;
        // }
        return min;
    }

    private boolean less(double a, double b) {
        return Double.compare(a, b) < 0;
    }

    private boolean lessOrEqual(double a, double b) {
        return Double.compare(a, b) <= 0;
    }

//    Iterable<Point2D> points() {
//        Queue<Point2D> q = new Queue<>();
//        inorder(root, q);
//        return q;
//    }

    private void inorder(Node x, Queue<Point2D> q) {
        if (null == x) {
            return;
        }
        inorder(x.leftBottom, q);
        q.enqueue(x.p);
        inorder(x.rightTop, q);
    }

    public static void main(String[] args) {
        Point2D p = new Point2D(0.5, 0.5);
        KdTree tree = new KdTree();
        tree.insert(p);
        RectHV rect = new RectHV(0.2, 0.2, 0.6, 0.6);
        tree.insert(p);
        System.out.println(tree.contains(p));
        StdDraw.circle(p.x(), p.y(), p.distanceTo(tree.nearest(p)));
        StdOut.println("Nearest to " + p.toString() + " = " + tree.nearest(p));
        for (int i = 0; i < 1000; i++) {
            Point2D pf = new Point2D(StdRandom.uniform(), StdRandom.uniform());
            tree.insert(pf);
            if (!tree.contains(pf)) {
                System.out.println("FUCK");
            }
        }
        rect.draw();
        StdDraw.circle(p.x(), p.y(), p.distanceTo(tree.nearest(p)));
        StdOut.println("Nearest to " + p.toString() + " = " + tree.nearest(p));

        tree.draw();
        StdDraw.show(0);
        for (Point2D point : tree.range(rect))
            StdOut.println("In Range: " + point.toString());
    }
}

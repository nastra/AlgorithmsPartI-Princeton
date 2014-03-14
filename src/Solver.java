import java.util.Comparator;

public class Solver {
    private Node result;

    private class Node implements Comparable<Node> {
        Node prev;
        Board value;
        int moves = 0;
        int priority;

        public Node(Board value, Node previous) {
            super();
            this.value = value;
            prev = previous;
            if (null != previous) {
                this.moves = previous.moves + 1;
            } else {
                this.moves = 0;
            }
            // priority = this.value.hamming() + moves;
            priority = this.value.manhattan() + moves;
        }

        @Override
        public String toString() {
            return value.toString() + "moves: " + moves + "\npriority: " + priority;
        }

        @Override
        public int compareTo(Node o) {
            return this.priority - o.priority;
        }
    }

    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        Node root = new Node(initial, null);
        MinPQ<Node> heap = new MinPQ<>(new ManhattanComparator());
        heap.insert(root);

        Board twin = initial.twin();
        Node twinRoot = new Node(twin, null);
        MinPQ<Node> twinHeap = new MinPQ<>(new ManhattanComparator());
        twinHeap.insert(twinRoot);

        solve(heap, twinHeap);
    }

    private void solve(MinPQ<Node> heap, MinPQ<Node> twinHeap) {
        while (!heap.isEmpty() && !twinHeap.isEmpty()) {
            if (null != perform(heap)) {
                return;
            }

            if (null != perform(twinHeap)) {
                result = null;
                return;
            }
        }
    }

    private Node perform(MinPQ<Node> heap) {
        Node n = heap.delMin();
        if (n.value.isGoal()) {
            result = n;
            return result;
        }
        for (Board board : n.value.neighbors()) {
            Node x = new Node(board, n);
            if (null != n.prev && n.prev.value.equals(board)) {
                // don't add neighbors that are same as previous board
                continue;
            }
            heap.insert(x);
        }
        return null;
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return null != result;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if no solution
        if (!isSolvable()) {
            return -1;
        }
        return result.moves;
    }

    public Iterable<Board> solution() {
        if (null == result) {
            return null;
        }
        Stack<Board> stack = new Stack<>();
        Node x = result;
        while (null != x) {
            stack.push(x.value);
            x = x.prev;
        }
        return stack;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();

        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

    private static class ManhattanComparator implements Comparator<Node> {

        @Override
        public int compare(Node one, Node two) {
            int distOne = one.priority;
            int distTwo = two.priority;
            if (distOne > distTwo) {
                return 1;
            }
            if (distOne < distTwo) {
                return -1;
            }
            return 0;
        }
    }
}

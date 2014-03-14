public class Board {
    private final int[][] blocks;
    private final int N;
    private int hamming = -1;
    private int manhattan = -1;

    public Board(int[][] blocks) {
        N = blocks.length;
        this.blocks = new int[N][N];
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                this.blocks[row][col] = blocks[row][col];
            }
        }
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        if (hamming != -1) {
            return hamming;
        }
        int total = 0;
        int target = 0;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                int val = blocks[row][col];
                target++;
                if (val == 0) {
                    continue;
                } else if (val != target) {
                    total++;
                }
            }
        }
        hamming = total;
        return hamming;
    }

    public int manhattan() {
        if (manhattan != -1) {
            return manhattan;
        }
        int total = 0;
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j) {
                if (blocks[i][j] == 0)
                    continue;
                int oj = blocks[i][j] % N;
                int oi = blocks[i][j] / N;
                if (oj == 0) {
                    oj = N - 1;
                    --oi;
                } else {
                    --oj;
                }
                total += Math.abs(oi - i) + Math.abs(oj - j);
            }
        manhattan = total;
        return manhattan;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    // public Board twin() {
    // int[][] twin = copy();
    // while (true) {
    // int row = StdRandom.uniform(N);
    // int col = StdRandom.uniform(N);
    // int adjacentCol = 0;
    // if (col == 0) {
    // adjacentCol = col + 1;
    // } else if (col == N - 1) {
    // adjacentCol = col - 1;
    // } else {
    // adjacentCol = col + 1;
    // }
    // int one = twin[row][col];
    // int two = twin[row][adjacentCol];
    // twin[row][col] = two;
    // twin[row][adjacentCol] = one;
    // if (one != 0 && two != 0) {
    // break;
    // }
    // }
    // return new Board(twin);
    // }
    //
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (null == y) {
            return false;
        }
        if (!this.getClass().equals(y.getClass())) {
            return false;
        }
        Board other = (Board) y;
        if (this.dimension() != other.dimension()) {
            return false;
        }
        if (this.hamming() != other.hamming()) {
            return false;
        }
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if (this.blocks[row][col] != other.blocks[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private int[][] copySquareArray(int[][] original) {
        int len = original.length;
        int[][] copy = new int[len][len];
        for (int row = 0; row < len; row++) {
            assert original[row].length == len;
            for (int col = 0; col < len; col++)
                copy[row][col] = original[row][col];
        }
        return copy;
    }

    public Board twin() {
        int dim = N;
        int[][] copy = copySquareArray(blocks);
        if (N <= 1)
            return new Board(copy);
        // Find zero so that we don't exchange with the blank
        // This looks like a O(dim^2) algorithm, but on average it should finish
        // in O(1).
        int row = 0;
        int col = 0;
        int value = 0;
        int lastValue = blocks[0][0];
        zerosearch: for (row = 0; row < dim; row++) {
            for (col = 0; col < dim; col++) {
                value = blocks[row][col];
                // Check col>0 because swap must occur on same row
                if (value != 0 && lastValue != 0 && col > 0)
                    break zerosearch;
                lastValue = value;
            }
        }
        copy[row][col] = lastValue;
        copy[row][col - 1] = value;
        return new Board(copy);
    }

    private int[][] swap(int[][] array, int fromRow, int fromCol, int toRow, int toCol) {
        int[][] copy = copySquareArray(array);
        int tmp = copy[toRow][toCol];
        copy[toRow][toCol] = copy[fromRow][fromCol];
        copy[fromRow][fromCol] = tmp;
        return copy;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int dim = N;
        Queue<Board> q = new Queue<Board>();
        // Find zero
        int row = 0;
        int col = 0;
        zerosearch: for (row = 0; row < dim; row++) {
            for (col = 0; col < dim; col++) {
                if (blocks[row][col] == 0) {
                    break zerosearch;
                }
            }
        }
        if (row > 0)
            q.enqueue(new Board(swap(blocks, row, col, row - 1, col)));
        if (row < dim - 1)
            q.enqueue(new Board(swap(blocks, row, col, row + 1, col)));
        if (col > 0)
            q.enqueue(new Board(swap(blocks, row, col, row, col - 1)));
        if (col < dim - 1)
            q.enqueue(new Board(swap(blocks, row, col, row, col + 1)));
        return q;
    }

}

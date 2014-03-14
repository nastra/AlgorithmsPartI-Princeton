

/**
 * 
 * @author nastra - Eduard Tudenhoefner
 */
public class Percolation {

    private int n;
    private boolean[] grid;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF fullness;
    private int gridSize;
    private int virtualTop;
    private int virtualBotton;

    /**
     * Create N-by-N grid, with all sites blocked.
     */
    public Percolation(int n) {
        this.n = n;
        grid = new boolean[n * n];
        this.gridSize = n * n + 2;
        uf = new WeightedQuickUnionUF(gridSize);
        fullness = new WeightedQuickUnionUF(gridSize);
        virtualTop = indexOf(n, n) + 1;
        virtualBotton = indexOf(n, n) + 2;
    }

    public void open(int i, int j) {
        if (isOpen(i, j)) {
            return;
        }
        int index = indexOf(i, j);
        grid[index] = true;

        if (i == 1) {
            uf.union(index, virtualTop);
            fullness.union(index, virtualTop);
        }

        if (i == n) {
            uf.union(index, virtualBotton);
        }

        if (i > 1 && isOpen(i - 1, j)) {
            // we are not at the top
            uf.union(indexOf(i - 1, j), index);
            fullness.union(indexOf(i - 1, j), index);
        }
        if (i < n && isOpen(i + 1, j)) {
            // we are not at the botton
            uf.union(indexOf(i + 1, j), index);
            fullness.union(indexOf(i + 1, j), index);
        }
        if (j > 1 && isOpen(i, j - 1)) {
            // we are not at the left
            uf.union(indexOf(i, j - 1), index);
            fullness.union(indexOf(i, j - 1), index);
        }
        if (j < n && isOpen(i, j + 1)) {
            // we are not at the right
            uf.union(indexOf(i, j + 1), index);
            fullness.union(indexOf(i, j + 1), index);
        }
    }

    public boolean isOpen(int i, int j) {
        return grid[indexOf(i, j)];
    }

    public boolean isFull(int i, int j) {
        return fullness.connected(indexOf(i, j), virtualTop);
    }

    public boolean percolates() {
        return uf.connected(virtualTop, virtualBotton);
    }

    private int indexOf(int i, int j) {
        if (i > n || j > n || i < 1 || j < 1) {
            String message = String.format("Indices [%d, %d] are outside bounds [%d]", i, j, n);
            throw new IndexOutOfBoundsException(message);
        }
        return (i - 1) * n + (j - 1);
    }
}

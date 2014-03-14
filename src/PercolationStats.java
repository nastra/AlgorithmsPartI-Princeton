
public class PercolationStats {

	private double[] thresholds;
	private int T;

	/**
	 * Constructor for PercolationStats Perform T independent computational
	 * experiments on an NxN grid
	 */
	public PercolationStats(int N, int T) {
		if (N <= 0 || T <= 0) {
			throw new IllegalArgumentException("N and T must be greater than 0");
		}
		this.T = T;
		double size = (double) N * N;
		thresholds = new double[T];
		int count = 0;
		for (int x = 0; x < T; x++) {
			Percolation p = new Percolation(N);
			count = 0;
			while (!p.percolates()) {
				int i = StdRandom.uniform(1, N + 1);
				int j = StdRandom.uniform(1, N + 1);
				if (!p.isOpen(i, j)) {
					p.open(i, j);
					count++;
				}
			}
			thresholds[x] = ((double) count) / size;
		}
	}

	/**
	 * Returns the sample mean of percolation threshold
	 */
	public double mean() {
		return StdStats.mean(thresholds);
	}

	/**
	 * Returns the sample standard deviation of the percolation threshold
	 */
	public double stddev() {
		if (thresholds.length <= 1) {
			return Double.NaN;
		}
		return StdStats.stddev(thresholds);
	}

	public double confidenceLo() {
		return mean() - (1.96 * stddev() / Math.sqrt(T));
	}

	public double confidenceHi() {
		return mean() + (1.96 * stddev() / Math.sqrt(T));
	}

	/**
	 * Test Client
	 */
	public static void main(String[] args) {
		Integer N = new Integer(args[0]);
		Integer T = new Integer(args[1]);
		Out out = new Out();
		PercolationStats stats = new PercolationStats(N, T);
		out.println("mean                    = " + stats.mean());
		out.println("stddev                  = " + stats.stddev());
		out.println("95% confidence interval = " + stats.confidenceLo() + ", "
				+ stats.confidenceHi());
		out.close();
	}
}

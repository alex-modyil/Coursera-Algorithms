import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF qfA;
    private final WeightedQuickUnionUF qfB;
    private final boolean[] isOpen;
    private final int topIndex;
    private final int bottomIndex;
    private final int n;
    private int openedSites = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n <= 0");
        }

        this.n = n;
        topIndex = 0;
        bottomIndex = n * n + 1;
        qfB = new WeightedQuickUnionUF(n * n + 2);
        qfA = new WeightedQuickUnionUF(n * n + 1);
        isOpen = new boolean[n * n + 2];
        isOpen[topIndex] = true;
        isOpen[bottomIndex] = true;
    }

    private int indexOf(int row, int col) {
        if (row < 1 || row > n) {
            throw new IllegalArgumentException("Row out of bounds.");
        }
        if (col < 1 || col > n) {
            throw new IllegalArgumentException("Column out of bounds.");
        }
        return (row - 1) * n + col;
    }

    private void tryUnion(int rowA, int colA, int rowB, int colB) {
        if (0 < rowB && rowB <= n && 0 < colB && colB <= n &&
            isOpen(rowB, colB)) {
            qfB.union(indexOf(rowA, colA), indexOf(rowB, colB));
            qfA.union(indexOf(rowA, colA), indexOf(rowB, colB));
        }
    }

    public void open(int row, int col) {
        int currIndex = indexOf(row, col);
        isOpen[currIndex] = true;

        if (row == 1) {
            qfB.union(currIndex, topIndex);
            qfA.union(currIndex, topIndex);
        }
        if (row == n) {
            qfB.union(currIndex, bottomIndex);
        }

        tryUnion(row, col, row - 1, col);
        tryUnion(row, col, row + 1, col);
        tryUnion(row, col, row, col - 1);
        tryUnion(row, col, row, col + 1);

        openedSites = openedSites + 1;
    }

    public int numberOfOpenSites() {
        return openedSites;
    }

    public boolean isOpen(int row, int col) {
        return isOpen[indexOf(row, col)];
    }

    public boolean isFull(int row, int col) {
        return qfA.connected(topIndex, indexOf(row, col));
    }

    public boolean percolates() {
        return qfB.connected(topIndex, bottomIndex);
    }
}
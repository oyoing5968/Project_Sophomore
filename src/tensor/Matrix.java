package tensor;

public interface Matrix extends Cloneable {
    int getRowCount();
    int getColumnCount();
    Scalar get(int row, int col);
    void set(int row, int col, Scalar value);
    String toString();
    boolean equals(Object obj);
    Matrix clone();
    void add(Matrix other);
    void multiply(Matrix other);
    boolean isIdentityMatrix();
    void swapColumns(int col1, int col2);
    void scaleColumn(int colIndex, Scalar scalar);
    void addScaledRow(int targetRow, int sourceRow, Scalar scalar);
    void addScaledColumn(int targetCol, int sourceCol, Scalar scalar);
    Matrix subMatrix(int rowStart, int rowEnd, int colStart, int colEnd);

}
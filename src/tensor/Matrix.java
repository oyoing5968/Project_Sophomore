package tensor;

public interface Matrix {

    Scalar getValue(int row, int col);
    void setValue(int row, int col, Scalar val);

    int rowSize();//13번
    int colSize();//13번번
    String toString();//14번번
    boolean equals(Object obj);
    Matrix clone();
    void add(Matrix other);
    void multiply(Matrix other);
    static Matrix add(Matrix m1, Matrix m2) {
        if (m1.rowSize() != m2.rowSize() || m1.colSize() != m2.colSize()) {
            throw new IllegalArgumentException("행렬의 크기가 다릅니다.");
        }
        Scalar[][] arr = new Scalar[m1.rowSize()][m1.colSize()];
        for (int i = 0; i < m1.rowSize(); i++) {
            for (int j = 0; j < m1.colSize(); j++) {
                arr[i][j] = Scalar.add(m1.getValue(i, j), m2.getValue(i, j));
            }
        }
        return new MatrixImpl(arr);
    }
    static Matrix multiply(Matrix m1, Matrix m2) {
        if (m1.colSize() != m2.rowSize()) {
            throw new IllegalArgumentException("행렬 곱셈 조건이 맞지 않습니다.");
        }
        int m = m1.rowSize();
        int n = m1.colSize();
        int l = m2.colSize();
        Scalar[][] arr = new Scalar[m][l];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < l; j++) {
                java.math.BigDecimal sum = java.math.BigDecimal.ZERO;
                for (int k = 0; k < n; k++) {
                    java.math.BigDecimal a = new java.math.BigDecimal(m1.getValue(i, k).getValue());
                    java.math.BigDecimal b = new java.math.BigDecimal(m2.getValue(k, j).getValue());
                    sum = sum.add(a.multiply(b));
                }
                arr[i][j] = new ScalarImpl(sum.toPlainString());
            }
        }
        return new MatrixImpl(arr);
    }
    static Matrix attachHMatrix(Matrix m1, Matrix m2) { //32번
        if (m1.rowSize() != m2.rowSize()) {
            throw new IllegalArgumentException("행 개수가 다릅니다.");
        }
        int rows = m1.rowSize();
        int cols1 = m1.colSize();
        int cols2 = m2.colSize();
        Scalar[][] arr = new Scalar[rows][cols1 + cols2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols1; j++) {
                arr[i][j] = m1.getValue(i, j).clone();
            }
            for (int j = 0; j < cols2; j++) {
                arr[i][cols1 + j] = m2.getValue(i, j).clone();
            }
        }
        return new MatrixImpl(arr);
    }
    static Matrix attachVMatrix(Matrix m1, Matrix m2) { //33번
        if (m1.colSize() != m2.colSize()) {
            throw new IllegalArgumentException("열 개수가 다릅니다.");
        }
        int rows1 = m1.rowSize();
        int rows2 = m2.rowSize();
        int cols = m1.colSize();
        Scalar[][] arr = new Scalar[rows1 + rows2][cols];
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < cols; j++) {
                arr[i][j] = m1.getValue(i, j).clone();
            }
        }
        for (int i = 0; i < rows2; i++) {
            for (int j = 0; j < cols; j++) {
                arr[rows1 + i][j] = m2.getValue(i, j).clone();
            }
        }
        return new MatrixImpl(arr);
    }

    Vector getRowVector(int row);//34
    Vector getColVector(int col);//35

    Matrix extractSubMatrix(int rowStart, int rowEnd, int colStart, int colEnd);//36

    Matrix minorSubMatrix(int row, int col);//37

    Matrix transposeMatrix(Matrix m);//38번

    double trace(Matrix m);//39번

    boolean isSquare(Matrix m);//40번
    boolean isUpperTriangular(Matrix m);//41번
    boolean isLowerTriangular(Matrix m);//42번
    boolean isIdentity(Matrix m);//43번
    boolean isZero(Matrix m);//44번

    Matrix rowSwap(int row1, int row2);//45번
    Matrix colSwap(int col1, int col2);//46번

    Matrix rowMultiply(int index, Scalar val);//47번
    Matrix colMultiply(int index, Scalar val);//48번

    Matrix rowAddOtherRow(int targetRow, int sourceRow, Scalar factor);//49번, targetRow에 sourceRow의 factor배 를더함
    Matrix colAddOtherCol(int targetCol, int sourceCol, Scalar factor);//50번, targetCol에 sourceCol의 factor배 더함

    Matrix getRREF(Matrix m);//51번, 본인의 rref 리턴턴
    Matrix isRREF(Matrix m);//52번, 본인의 rref 여부 리턴

    Matrix getMatrix(Matrix m);//53번, 본인의 행렬 리턴
    Matrix getInverseMatrix(Matrix m);//54번, 본인의 역행렬 리턴

}
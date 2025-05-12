package tensor;

public interface Matrix extends Cloneable {
    int rowSize(); //13번
    int colSize(); //13번
    Scalar getValue(int row, int col); //11번
    void setValue(int row, int col, Scalar value); //11번
    String toString();
    boolean equals(Object obj);
    Matrix clone();
    Matrix add(Matrix other);
    Matrix multiply(Matrix other); //23번
    static Matrix add(Matrix m1, Matrix m2) {
        Matrix result = m1.clone();
        return result.add(m2);
    } //28번
    static Matrix multiply(Matrix m1, Matrix m2) {
        Matrix result = m1.clone();
        return result.multiply(m2);
    } //29번

    static attachHMatrix(Matrix m1, Matrix m2){
        return null;
    } //32번

    static attachVMatrix(Matrix m1, Matrix m2){
        return null;
    } //33번

    Vector getRowVector(int row); //34번
    Vector getColVector(int col); //35번

    Matrix extractSubMatrix(int rowStart, int rowEnd, int colStart, int colEnd); //36번
    Matrix minorSubMatrix(int delrow, int delcol); //37번
    Matrix transposeMatrix(); //38번
    double trace(); //39번

    boolean isSquare(Matrix m); //40번
    boolean isUpperTriangular(Matrix m); //41번
    boolean isLowerTriangular(Matrix m); //42번
    boolean isIdentity(Matrix m); //43번
    boolean isZero(Matrix m); //44번

    Matrix RowSwap(int row1, int row2); //45번
    Matrix ColumnSwap(int col1, int col2); //46번

    Matrix rowMultiply(int index, Scalar val); //47번
    Matrix colMultiply(int index, Scalar val); //48번



}
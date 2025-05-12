package tensor;

public class Factory {

    // Scalar 생성
    public static Scalar createScalar(String val) {
        return new ScalarImpl(val);
    }

    public static Scalar createScalar(int i, int j) {
        return new ScalarImpl(i, j);
    }

    // Vector 생성
    public static Vector createVector(int n, Scalar val) {
        return new VectorImpl(n, val);
    }

    public static Vector createVector(int i, int j, int n) {
        return new VectorImpl(i, j, n);
    }

    public static Vector createVector(Scalar[] arr) {
        return new VectorImpl(arr);
    }

    // Matrix 생성
    public static Matrix createMatrix(int m, int n, Scalar val) {
        return new MatrixImpl(m, n, val);
    }

    public static Matrix createMatrix(int i, int j, int m, int n) {
        return new MatrixImpl(i, j, m, n);
    }

    public static Matrix createMatrix(Scalar[][] arr) {
        return new MatrixImpl(arr);
    }

    public static Matrix createMatrix(String csvPath) {
      return new MatrixImpl(csvPath);
    }

    public static Matrix createIdentityMatrix(int n) {
        return new MatrixImpl(n);
    }

}
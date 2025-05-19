package tensor;

public class Tensors {
    // Scalar 연산
    public static Scalar add(Scalar s1, Scalar s2) {
        return Scalar.add(s1, s2);
    }
    public static Scalar multiply(Scalar s1, Scalar s2) {
        return Scalar.multiply(s1, s2);
    }

    // Vector 연산
    public static Vector add(Vector v1, Vector v2) {
        return Vector.add(v1, v2);
    }
    public static Vector multiply(Vector v, Scalar s) {
        return Vector.multiply(v, s);
    }

    // Matrix 연산
    public static Matrix add(Matrix m1, Matrix m2) {
        return Matrix.add(m1, m2);
    }
    public static Matrix multiply(Matrix m1, Matrix m2) {
        return Matrix.multiply(m1, m2);
    }
    public static Matrix attachHMatrix(Matrix m1, Matrix m2) {
        return Matrix.attachHMatrix(m1, m2);
    }
    public static Matrix attachVMatrix(Matrix m1, Matrix m2) {
        return Matrix.attachVMatrix(m1, m2);
    }
    // 필요시 추가 연산도 여기에 래핑 가능
}
package tensor;

public class Tensors {

    public static Scalar add(Scalar a, Scalar b) {
        Scalar result = a.clone();
        result.add(b);
        return result;
    }

    public static Scalar multiply(Scalar a, Scalar b) {
        Scalar result = a.clone();
        result.multiply(b);
        return result;
    }

    public static Vector add(Vector a, Vector b) {
        if (a.size() != b.size()) {
            throw new DimensionMismatchException("Vector size mismatch.");
        }
        Scalar[] sum = new Scalar[a.size()];
        for (int i = 0; i < a.size(); i++) {
            sum[i] = Tensors.add(a.getValue(i), b.getValue(i));
        }
        return Factory.createVector(sum);
    }

    public static Vector multiply(Vector v, Scalar s) {
        Scalar[] prod = new Scalar[v.size()];
        for (int i = 0; i < v.size(); i++) {
            prod[i] = Tensors.multiply(v.getValue(i), s);
        }
        return Factory.createVector(prod);
    }

    public static Matrix add(Matrix a, Matrix b) {
        if (a.getRowCount() != b.getRowCount() || a.getColumnCount() != b.getColumnCount()) {
            throw new DimensionMismatchException("Matrix size mismatch.");
        }
        Scalar[][] sum = new Scalar[a.getRowCount()][a.getColumnCount()];
        for (int i = 0; i < a.getRowCount(); i++) {
            for (int j = 0; j < a.getColumnCount(); j++) {
                sum[i][j] = Tensors.add(a.get(i, j), b.get(i, j));
            }
        }
        return Factory.createMatrix(sum);
    }

    public static Matrix multiply(Matrix a, Matrix b) {
        if (a.getColumnCount() != b.getRowCount()) {
            throw new DimensionMismatchException("Matrix multiplication dimension mismatch.");
        }
        int m = a.getRowCount();
        int n = b.getColumnCount();
        int k = a.getColumnCount();
        Scalar[][] prod = new Scalar[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Scalar sum = Factory.createScalar("0");
                for (int t = 0; t < k; t++) {
                    Scalar temp = Tensors.multiply(a.get(i, t), b.get(t, j));
                    sum.add(temp);
                }
                prod[i][j] = sum;
            }
        }
        return Factory.createMatrix(prod);
    }
    public static Matrix transpose(Matrix m) {
        return ((MatrixImpl) m).transpose();
    }

    public static Matrix minor(Matrix m, int row, int col) {
        return ((MatrixImpl) m).minor(row, col);
    }

    public static Scalar trace(Matrix m) {
        return ((MatrixImpl) m).trace();
    }

    public static Scalar determinant(Matrix m) {
        return ((MatrixImpl) m).determinant();
    }

    public static Matrix rref(Matrix m) {
        return ((MatrixImpl) m).rref();
    }

    public static Matrix inverse(Matrix m) {
        return ((MatrixImpl) m).inverse();
    }

    public static Matrix toRowMatrix(Vector v) {
        return ((VectorImpl) v).toRowMatrix();
    }

    public static Matrix toColumnMatrix(Vector v) {
        return ((VectorImpl) v).toColumnMatrix();
    }

}
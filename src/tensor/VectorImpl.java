package tensor;

import java.util.*;

class VectorImpl implements Vector {
    private List<Scalar> elements;

    VectorImpl(int n, Scalar val) { //3번
        elements = new ArrayList<>();
        for (int i = 0; i < n; i++) elements.add(val.clone());
    }

    VectorImpl(int i, int j, int n) { //4번
        elements = new ArrayList<>();
        for (int k = 0; k < n; k++) {
            elements.add(new ScalarImpl((double) i, (double) j)); // ← 형변환 추가
        }
    }

    VectorImpl(Scalar[] arr) { //5번
        elements = new ArrayList<>();
        for (Scalar s : arr) elements.add(s.clone());
    }

    public int size() { return elements.size(); } //13번
    public Scalar get(Scalar index) { return elements.get(index); } //11번
    public void set(int index, Scalar value) { elements.set(index, value.clone()); } //11번
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");
        for (Scalar s : elements) sb.append(s.toString()).append(" ");
        sb.append("]");
        return sb.toString();
    }
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) return false;
        Vector other = (Vector) obj;
        if (this.size() != other.size()) return false;
        for (int i = 0; i < size(); i++) if (!getValue(i).equals(other.getValue(i))) return false;
        return true;
    }
    public Vector clone() {
        Scalar[] copy = new Scalar[size()];
        for (int i = 0; i < size(); i++) copy[i] = getValue(i).clone();
        return new VectorImpl(copy);
    }
    public void add(Vector other) {
        if (size() != other.size()) throw new DimensionMismatchException("Vector size mismatch");
        for (int i = 0; i < size(); i++) getValue(i).add(other.getValue(i));
    }
    public void multiply(Scalar scalar) {
        for (Scalar s : elements) s.multiply(scalar);
    }
    // Vector를 1 x N 행렬로 변환
    public Matrix toRowMatrix() {
        Scalar[][] arr = new Scalar[1][size()];
        for (int i = 0; i < size(); i++) {
            arr[0][i] = getValue(i).clone();
        }
        return new MatrixImpl(arr);
    }

    // Vector를 N x 1 행렬로 변환
    public Matrix toColumnMatrix() {
        Scalar[][] arr = new Scalar[size()][1];
        for (int i = 0; i < size(); i++) {
            arr[i][0] = getValue(i).clone();
        }
        return new MatrixImpl(arr);
    }


}
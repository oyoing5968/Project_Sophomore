package tensor;
import java.util.*;




class VectorImpl implements Vector {
    private List<Scalar> elements;
    VectorImpl(int n, Scalar val) {//03번
        elements = new ArrayList<>();
        for (int i = 0; i < n; i++) elements.add(val.clone());
    }
    VectorImpl(int i, int j, int n) {
        elements = new ArrayList<>();
        for (int k = 0; k < n; k++) elements.add(new ScalarImpl(i, j));
    }
    VectorImpl(Scalar[] arr) {
        elements = new ArrayList<>();
        for (Scalar s : arr) elements.add(s.clone());
    }

    public Scalar getValue(int index) {
        if (index < 0 || index >= elements.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return elements.get(index).clone();  // 읽기 - 복사해서 반환
    }

    public void setValue(int index, Scalar val) {
        if (index < 0 || index >= elements.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        elements.set(index, val.clone());  // 쓰기 - 복사해서 저장
    }

    public int size() {
        return elements.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).toString());
            if (i < elements.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector)) return false;
        Vector other = (Vector) obj;
        if (this.size() != other.size()) return false;
        for (int i = 0; i < this.size(); i++) {
            if (!this.getValue(i).equals(other.getValue(i))) return false;
        }
        return true;
    }

    @Override
    public Vector clone() {
        Scalar[] arr = new Scalar[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            arr[i] = elements.get(i).clone();
        }
        return new VectorImpl(arr);
    }

    @Override
    public void add(Vector other) {
        if (this.size() != other.size()) {
            throw new IllegalArgumentException("벡터의 길이가 다릅니다.");
        }
        for (int i = 0; i < this.size(); i++) {
            this.elements.get(i).add(other.getValue(i));
        }
    }

    @Override
    public void multiply(Scalar scalar) {
        for (int i = 0; i < this.size(); i++) {
            this.elements.get(i).multiply(scalar);
        }
    }

    @Override
    public Matrix toVerticalMatrix() { //30번
        int n = this.size();
        Scalar[][] arr = new Scalar[n][1];
        for (int i = 0; i < n; i++) {
            arr[i][0] = this.getValue(i).clone();
        }
        return new MatrixImpl(arr);
    }

    @Override
    public Matrix toHorizentalMatrix() { //31번
        int n = this.size();
        Scalar[][] arr = new Scalar[1][n];
        for (int i = 0; i < n; i++) {
            arr[0][i] = this.getValue(i).clone();
        }
        return new MatrixImpl(arr);
    }




}
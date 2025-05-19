package tensor;

public interface Vector {


    Scalar getValue(int index);
    void setValue(int index, Scalar val);
    int size();
    String toString();
    boolean equals(Object obj);
    Vector clone();
    void add(Vector other);
    void multiply(Scalar scalar);

    static Vector add(Vector v1, Vector v2) {
        if (v1.size() != v2.size()) {
            throw new IllegalArgumentException("벡터의 길이가 다릅니다.");
        }
        Scalar[] arr = new Scalar[v1.size()];
        for (int i = 0; i < v1.size(); i++) {
            arr[i] = Scalar.add(v1.getValue(i), v2.getValue(i));
        }
        return new VectorImpl(arr);
    }

    static Vector multiply(Vector v, Scalar s) {
        Scalar[] arr = new Scalar[v.size()];
        for (int i = 0; i < v.size(); i++) {
            arr[i] = Scalar.multiply(v.getValue(i), s);
        }
        return new VectorImpl(arr);
    }
    //30
    Matrix toVerticalMatrix();
    //31
    Matrix toHorizentalMatrix();

}
package tensor;

public interface Vector extends Cloneable {
    int size();
    Scalar getValue(int index);
    void setValue(int index, Scalar value);
    String toString();
    boolean equals(Object obj);
    Vector clone();
    void add(Vector other);
    void multiply(Scalar scalar);
    static Vector add(Vector v1, Scalar v2);
    static Vector multiply(Vector v, Scalar s);
    Matrix toVerticalMatrix(); //30
    Matrix toHorizontalMatrix(); //31
}
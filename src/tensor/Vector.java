package tensor;

public interface Vector extends Cloneable {
    int size();
    Scalar get(int index);
    void set(int index, Scalar value);
    String toString();
    boolean equals(Object obj);
    Vector clone();
    void add(Vector other);
    void multiply(Scalar scalar);
}
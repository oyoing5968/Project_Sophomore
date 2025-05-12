package tensor;
import java.math.BigDecimal;

public interface Scalar extends Comparable<Scalar>, Cloneable {
    BigDecimal getValue();
    void setValue(BigDecimal value);
    String toString();
    boolean equals(Object obj);
    Scalar clone();
    void add(Scalar other);
    void multiply(Scalar other);
    static Scalar add(Scalar a, Scalar b);
    static Scalar multiply(Scalar a, Scalar b);

}
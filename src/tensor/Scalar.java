package tensor;

public interface Scalar {
    String getValue();
    void setValue(String val);
    String toString();

    boolean equals(Object obj);
    Scalar clone();
    void add(Scalar other);
    void multiply(Scalar other);
    int compareTo(Scalar other);
    static Scalar add(Scalar s1, Scalar s2) {
        java.math.BigDecimal v1 = new java.math.BigDecimal(s1.getValue());
        java.math.BigDecimal v2 = new java.math.BigDecimal(s2.getValue());
        return new ScalarImpl(v1.add(v2).toPlainString());
    }

    static Scalar multiply(Scalar s1, Scalar s2) {
        java.math.BigDecimal v1 = new java.math.BigDecimal(s1.getValue());
        java.math.BigDecimal v2 = new java.math.BigDecimal(s2.getValue());
        return new ScalarImpl(v1.multiply(v2).toPlainString());
    }

}
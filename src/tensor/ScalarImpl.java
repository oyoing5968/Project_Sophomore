package tensor;

import java.math.BigDecimal;
import java.util.Random;

class ScalarImpl implements Scalar {
    private BigDecimal value;

    ScalarImpl(String val) { //1번
        this.value = new BigDecimal(val);
    }

    ScalarImpl(double i, double j) { //2번
        if (i >= j) throw new IllegalArgumentException("i must be less than j");
        Random rand = new Random();
        int randomValue = rand.nextInt(j - i) + i;
        this.value = new BigDecimal(randomValue);
    }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal val) { this.value = val; }
    public String toString() { return value.stripTrailingZeros().toPlainString(); } //12번
    public boolean equals(Object obj) {
        if (!(obj instanceof Scalar)) return false;
        return value.compareTo(((Scalar) obj).getValue()) == 0;
    }
    public Scalar clone() { return new ScalarImpl(value.toPlainString()); }
    public int compareTo(Scalar o) { return value.compareTo(o.getValue()); }
    public void add(Scalar val) { value = value.add(val.getValue()); }
    public void multiply(Scalar val) { value = value.multiply(val.getValue()); }
}
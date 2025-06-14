package tensor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.Objects;

class ScalarImpl implements Scalar, Comparable<Scalar> {
    private BigDecimal value;

    //01
    ScalarImpl(String val) {
        BigDecimal parsedValue;
        try {
            parsedValue = new BigDecimal(val);
        } catch (NumberFormatException e) {
            throw new TensorException("스칼라 값이 올바르지 않습니다.");
        }
        try {
            this.value = parsedValue.setScale(5, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            throw new TensorException("스칼라 값의 소수점 처리 중 산술 오류가 발생하였습니다.");
        }
    }


    //02
    ScalarImpl(int i, int j) {
        if (i >= j) throw new IllegalArgumentException("i must be less than j");
        Random rand = new Random();
        BigDecimal range = new BigDecimal(j - i);
        BigDecimal randomFactor = new BigDecimal(rand.nextDouble());
        BigDecimal calculatedValue = randomFactor.multiply(range).add(new BigDecimal(i));
        value = calculatedValue.setScale(5, RoundingMode.HALF_UP);
    }

    // 12
    @Override
    public void setValue(String val) {
        BigDecimal parsed;
        try {
            parsed = new BigDecimal(val);
        } catch (NumberFormatException e) {
            throw new TensorException("올바른 값을 입력하지 않으셨습니다.");
        }
        BigDecimal scaled = parsed.setScale(5, RoundingMode.HALF_UP);
        this.value = scaled;
    }

    @Override
    public String getValue() {
        return value.stripTrailingZeros().toPlainString();
    }

    //14
    @Override
    public String toString() {
        BigDecimal val = value.setScale(5, RoundingMode.HALF_UP);
        return val.stripTrailingZeros().toPlainString();
    }

    //15
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Scalar)) return false;
        Scalar other = (Scalar) obj;

        BigDecimal val1 = this.value;
        BigDecimal val2 = new BigDecimal(other.getValue());
        BigDecimal epsilon = new BigDecimal("0.000001");
        BigDecimal diff = val1.subtract(val2).abs();

        return diff.compareTo(epsilon) <= 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    //16
    @Override
    public int compareTo(Scalar other) {
        BigDecimal bigDecimalValue = new BigDecimal(other.getValue());
        return value.compareTo(bigDecimalValue);
    }

    //17
    @Override
    public Scalar clone() {
        return new ScalarImpl(value.toPlainString());
    }

    //18
    @Override
    public void add(Scalar other) {
        BigDecimal otherValue = new BigDecimal(other.getValue());
        BigDecimal result = this.value.add(otherValue);
        this.value = result.setScale(5, RoundingMode.HALF_UP);
    }

    //19
    @Override
    public void multiply(Scalar other) {
        BigDecimal multiplier = new BigDecimal(other.getValue());
        BigDecimal product = this.value.multiply(multiplier);
        this.value = product.setScale(5, RoundingMode.HALF_UP);
    }

    //24
    static Scalar add(Scalar s1, Scalar s2) {
        BigDecimal v1 = new BigDecimal(s1.getValue());
        BigDecimal v2 = new BigDecimal(s2.getValue());
        return new ScalarImpl(v1.add(v2).toPlainString());
    }

    //25
    static Scalar multiply(Scalar s1, Scalar s2) {
        BigDecimal v1 = new BigDecimal(s1.getValue());
        BigDecimal v2 = new BigDecimal(s2.getValue());
        return new ScalarImpl(v1.multiply(v2).toPlainString());
    }
}

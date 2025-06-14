package tensor;

import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

class VectorImpl implements Vector {
    private List<Scalar> elements;

    //03
    VectorImpl(int n, Scalar val) {
        elements = new ArrayList<>(Collections.nCopies(n, val.clone()));
        for (int i = 1; i < n; i++) {
            elements.set(i, val.clone());
        }
    }

    //04
    VectorImpl(int i, int j, int n) {
        elements = new ArrayList<>();
        for (int count = 0; count < n; count++) {
            Scalar scalar = new ScalarImpl(i, j);
            elements.add(scalar);
        }
    }

    //05
    VectorImpl(Scalar[] arr) {
        elements = new ArrayList<>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            Scalar copiedScalar = arr[i].clone();
            elements.add(copiedScalar);
        }
    }
    //11
    @Override
    public void setValue(int index, Scalar val) {
        if (index < 0 || index >= elements.size()) {
            throw new IndexOutOfBoundsException("인덱스가 범위를 벗어났습니다: " + index);
        }
        elements.set(index, val.clone());
    }

    @Override
    public Scalar getValue(int index) {
        if (index < 0 || index >= elements.size()) {
            throw new IndexOutOfBoundsException("인덱스가 범위를 벗어났습니다: " + index);
        }
        return elements.get(index).clone();
    }

    //13
    @Override
    public int size() {
        return elements.size();
    }

    //14
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < elements.size(); i++) {
            BigDecimal val = new BigDecimal(elements.get(i).getValue());
            val = val.setScale(5, RoundingMode.HALF_UP);
            sb.append(val.stripTrailingZeros().toPlainString());
            if (i < elements.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // 15
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Vector)) return false;
        Vector that = (Vector) obj;
        if (this.size() != that.size()) return false;

        BigDecimal tolerance = new BigDecimal("0.000001");
        for (int i = 0, n = this.size(); i < n; i++) {
            BigDecimal x = new BigDecimal(this.getValue(i).getValue());
            BigDecimal y = new BigDecimal(that.getValue(i).getValue());
            BigDecimal diff = x.subtract(y).abs();
            if (diff.compareTo(tolerance) > 0) {
                return false;
            }
        }
        return true;
    }

    //17
    @Override
    public Vector clone() {
        int n = elements.size();
        List<Scalar> clones = new ArrayList<>(n);
        for (Scalar s : elements) {
            clones.add(s.clone());
        }
        Scalar[] clonedArr = clones.toArray(new Scalar[0]);
        return new VectorImpl(clonedArr);
    }

    //20
    @Override
    public void add(Vector rhs) {
        int len = size();
        if (rhs.size() != len) {
            throw new IllegalArgumentException("벡터의 길이가 다릅니다.");
        }
        ListIterator<Scalar> iter = elements.listIterator();
        int idx = 0;
        while (iter.hasNext()) {
            Scalar mine   = iter.next();
            Scalar theirs = rhs.getValue(idx++);
            mine.add(theirs);
        }
    }

    //21
    @Override
    public void multiply(Scalar factor) {
        for (Scalar elem : elements) {
            elem.multiply(factor);
        }
    }

    //30
    @Override
    public Matrix toVerticalMatrix() {
        int n = this.size();
        Scalar[][] arr = new Scalar[n][1];
        for (int i = 0; i < n; i++) {
            arr[i][0] = this.getValue(i).clone();
        }
        return new MatrixImpl(arr);
    }

    //31
    @Override
    public Matrix toHorizontalMatrix() {
        Scalar[] rowData = new Scalar[size()];
        int idx = 0;
        for (Scalar elem : elements) {
            rowData[idx++] = elem.clone();
        }
        return new MatrixImpl(new Scalar[][] { rowData });
    }

    //26
    static Vector add(Vector v1, Vector v2) {
        if (v1.size() != v2.size()) {
            throw new IllegalArgumentException("벡터의 길이가 다릅니다.");
        }
        Scalar[] arr = new Scalar[v1.size()];
        for (int i = 0; i < v1.size(); i++) {
            arr[i] = ScalarImpl.add(v1.getValue(i), v2.getValue(i));
        }
        return new VectorImpl(arr);
    }

    //27
    static Vector multiply(Vector v, Scalar s) {
        Vector result = v.clone();
        result.multiply(s);
        return result;
    }
}

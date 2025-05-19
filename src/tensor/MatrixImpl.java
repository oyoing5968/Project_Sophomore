package tensor;
import java.io.*;
import java.util.*;
import java.math.BigDecimal;
class MatrixImpl implements Matrix {
    private List<List<Scalar>> elements;

    MatrixImpl(int m, int n, Scalar val) {
        if (m <= 0 || n <= 0) throw new IllegalArgumentException("Invalid dimensions.");
        elements = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            List<Scalar> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(val.clone());
            }
            elements.add(row);
        }
    }
    MatrixImpl(int i, int j, int m, int n) {
        if (i >= j || m <= 0 || n <= 0) throw new IllegalArgumentException("Invalid parameters.");
        elements = new ArrayList<>();
        for (int row = 0; row < m; row++) {
            List<Scalar> newRow = new ArrayList<>();
            for (int col = 0; col < n; col++) {
                newRow.add(new ScalarImpl(i, j));
            }
            elements.add(newRow);
        }
    }
    MatrixImpl(String csvPath) throws IOException {
        elements = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(csvPath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");
            List<Scalar> row = new ArrayList<>();
            for (String token : tokens) {
                row.add(new ScalarImpl(token.trim()));
            }
            elements.add(row);
        }
        reader.close();
    }



    MatrixImpl(Scalar[][] arr) {
        elements = new ArrayList<>();
        for (Scalar[] row : arr) {
            List<Scalar> newRow = new ArrayList<>();
            for (Scalar s : row) {
                newRow.add(s.clone());
            }
            elements.add(newRow);
        }
    }

    MatrixImpl(int n) {
        elements = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Scalar> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(new ScalarImpl(i == j ? "1" : "0"));
            }
            elements.add(row);
        }
    }

    MatrixImpl Identity(int n){
        return null;
    }

    public Scalar getValue(int row, int col) {
        if (row < 0 || row >= getRowCount() || col < 0 || col >= getColumnCount()) {
            throw new IndexOutOfBoundsException("Matrix index out of bounds: (" + row + ", " + col + ")");
        }
        return elements.get(row).get(col).clone();
    }

    public void setValue(int row, int col, Scalar val) {
        if (row < 0 || row >= getRowCount() || col < 0 || col >= getColumnCount()) {
            throw new IndexOutOfBoundsException("Matrix index out of bounds: (" + row + ", " + col + ")");
        }
        elements.get(row).set(col, val.clone());
    }
    //여기까지함

    public int getRowCount() {//13번
        return elements.size();
    }
    public int getColumnCount() {
        if (elements.isEmpty()) return 0;
        return elements.get(0).size();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.size(); i++) {
            sb.append("[");
            List<Scalar> row = elements.get(i);
            for (int j = 0; j < row.size(); j++) {
                sb.append(row.get(j).toString());
                if (j < row.size() - 1) sb.append(", ");
            }
            sb.append("]");
            if (i < elements.size() - 1) sb.append("\n");
        }
        return sb.toString();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix)) return false;
        Matrix other = (Matrix) obj;
        if (this.rowSize() != other.rowSize() || this.colSize() != other.colSize()) return false;
        for (int i = 0; i < this.rowSize(); i++) {
            for (int j = 0; j < this.colSize(); j++) {
                if (!this.getValue(i, j).equals(other.getValue(i, j))) return false;
            }
        }
        return true;
    }

    @Override
    public int rowSize() {
        return getRowCount();
    }
    @Override
    public int colSize() {
        return getColumnCount();
    }

    @Override
    public Matrix clone() {
        int rows = this.rowSize();
        int cols = this.colSize();
        Scalar[][] arr = new Scalar[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arr[i][j] = this.getValue(i, j).clone();
            }
        }
        return new MatrixImpl(arr);
    }

    @Override
    public void add(Matrix other) {
        if (this.rowSize() != other.rowSize() || this.colSize() != other.colSize()) {
            throw new IllegalArgumentException("행렬의 크기가 다릅니다.");
        }
        for (int i = 0; i < this.rowSize(); i++) {
            for (int j = 0; j < this.colSize(); j++) {
                this.getValue(i, j).add(other.getValue(i, j));
            }
        }
    }

    @Override
    public void multiply(Matrix other) {
        if (this.colSize() != other.rowSize()) {
            throw new IllegalArgumentException("행렬 곱셈 조건이 맞지 않습니다.");
        }
        int m = this.rowSize();
        int n = this.colSize();
        int l = other.colSize();
        List<List<Scalar>> result = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            List<Scalar> row = new ArrayList<>();
            for (int j = 0; j < l; j++) {
                BigDecimal sum = BigDecimal.ZERO;
                for (int k = 0; k < n; k++) {
                    BigDecimal a = new BigDecimal(this.getValue(i, k).getValue());
                    BigDecimal b = new BigDecimal(other.getValue(k, j).getValue());
                    sum = sum.add(a.multiply(b));
                }
                row.add(new ScalarImpl(sum.toPlainString()));
            }
            result.add(row);
        }
        this.elements = result;
    }

    @Override
    public Vector getRowVector(int row) { //34번
        if (row < 0 || row >= this.rowSize()) {
            throw new IndexOutOfBoundsException("행 인덱스가 범위를 벗어났습니다.");
        }
        Scalar[] arr = new Scalar[this.colSize()];
        for (int j = 0; j < this.colSize(); j++) {
            arr[j] = this.getValue(row, j).clone();
        }
        return new VectorImpl(arr);
    }

    @Override
    public Vector getColVector(int col) { //35번
        if (col < 0 || col >= this.colSize()) {
            throw new IndexOutOfBoundsException("열 인덱스가 범위를 벗어났습니다.");
        }
        Scalar[] arr = new Scalar[this.rowSize()];
        for (int i = 0; i < this.rowSize(); i++) {
            arr[i] = this.getValue(i, col).clone();
        }
        return new VectorImpl(arr);
    }

    @Override
    public Matrix extractSubMatrix(int rowStart, int rowEnd, int colStart, int colEnd) { //36번
        if (rowStart < 0 || rowEnd > this.rowSize() || colStart < 0 || colEnd > this.colSize() || rowStart >= rowEnd || colStart >= colEnd) {
            throw new IndexOutOfBoundsException("부분 행렬 인덱스가 범위를 벗어났습니다.");
        }
        int rows = rowEnd - rowStart;
        int cols = colEnd - colStart;
        Scalar[][] arr = new Scalar[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arr[i][j] = this.getValue(rowStart + i, colStart + j).clone();
            }
        }
        return new MatrixImpl(arr);
    }

    @Override
    public Matrix minorSubMatrix(int row, int col) { //37번
        if (row < 0 || row >= this.rowSize() || col < 0 || col >= this.colSize()) {
            throw new IndexOutOfBoundsException("minor 인덱스가 범위를 벗어났습니다.");
        }
        int rows = this.rowSize() - 1;
        int cols = this.colSize() - 1;
        Scalar[][] arr = new Scalar[rows][cols];
        int r = 0;
        for (int i = 0; i < this.rowSize(); i++) {
            if (i == row) continue;
            int c = 0;
            for (int j = 0; j < this.colSize(); j++) {
                if (j == col) continue;
                arr[r][c] = this.getValue(i, j).clone();
                c++;
            }
            r++;
        }
        return new MatrixImpl(arr);
    }

    @Override
    public Matrix transposeMatrix(Matrix m) { //38번
        int rows = m.rowSize();
        int cols = m.colSize();
        Scalar[][] arr = new Scalar[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arr[j][i] = m.getValue(i, j).clone();
            }
        }
        return new MatrixImpl(arr);
    }

    @Override
    public double trace(Matrix m) { //39번
        if (m.rowSize() != m.colSize()) {
            throw new IllegalArgumentException("정사각 행렬이 아닙니다.");
        }
        java.math.BigDecimal sum = java.math.BigDecimal.ZERO;
        for (int i = 0; i < m.rowSize(); i++) {
            sum = sum.add(new java.math.BigDecimal(m.getValue(i, i).getValue()));
        }
        return sum.doubleValue();
    }

    @Override
    public boolean isSquare(Matrix m) { //40번
        return m.rowSize() == m.colSize();
    }

    @Override
    public boolean isUpperTriangular(Matrix m) { //41번
        if (!isSquare(m)) return false;
        for (int i = 1; i < m.rowSize(); i++) {
            for (int j = 0; j < i; j++) {
                if (!m.getValue(i, j).equals(new ScalarImpl("0"))) return false;
            }
        }
        return true;
    }

    @Override
    public boolean isLowerTriangular(Matrix m) { //42번
        if (!isSquare(m)) return false;
        for (int i = 0; i < m.rowSize(); i++) {
            for (int j = i + 1; j < m.colSize(); j++) {
                if (!m.getValue(i, j).equals(new ScalarImpl("0"))) return false;
            }
        }
        return true;
    }

    @Override
    public boolean isIdentity(Matrix m) { //43번
        if (!isSquare(m)) return false;
        for (int i = 0; i < m.rowSize(); i++) {
            for (int j = 0; j < m.colSize(); j++) {
                if (i == j) {
                    if (!m.getValue(i, j).equals(new ScalarImpl("1"))) return false;
                } else {
                    if (!m.getValue(i, j).equals(new ScalarImpl("0"))) return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isZero(Matrix m) { //44번
        for (int i = 0; i < m.rowSize(); i++) {
            for (int j = 0; j < m.colSize(); j++) {
                if (!m.getValue(i, j).equals(new ScalarImpl("0"))) return false;
            }
        }
        return true;
    }

    @Override
    public Matrix rowSwap(int row1, int row2) { //45번
        if (row1 < 0 || row2 < 0 || row1 >= this.rowSize() || row2 >= this.rowSize()) {
            throw new IndexOutOfBoundsException("행 인덱스가 범위를 벗어났습니다.");
        }
        Matrix copy = this.clone();
        for (int j = 0; j < this.colSize(); j++) {
            Scalar temp = copy.getValue(row1, j);
            copy.setValue(row1, j, copy.getValue(row2, j));
            copy.setValue(row2, j, temp);
        }
        return copy;
    }

    @Override
    public Matrix colSwap(int col1, int col2) { //46번
        if (col1 < 0 || col2 < 0 || col1 >= this.colSize() || col2 >= this.colSize()) {
            throw new IndexOutOfBoundsException("열 인덱스가 범위를 벗어났습니다.");
        }
        Matrix copy = this.clone();
        for (int i = 0; i < this.rowSize(); i++) {
            Scalar temp = copy.getValue(i, col1);
            copy.setValue(i, col1, copy.getValue(i, col2));
            copy.setValue(i, col2, temp);
        }
        return copy;
    }

    @Override
    public Matrix rowMultiply(int index, Scalar val) { //47번
        if (index < 0 || index >= this.rowSize()) {
            throw new IndexOutOfBoundsException("행 인덱스가 범위를 벗어났습니다.");
        }
        Matrix copy = this.clone();
        for (int j = 0; j < this.colSize(); j++) {
            Scalar newVal = copy.getValue(index, j).clone();
            newVal.multiply(val);
            copy.setValue(index, j, newVal);
        }
        return copy;
    }

    @Override
    public Matrix colMultiply(int index, Scalar val) { //48번
        if (index < 0 || index >= this.colSize()) {
            throw new IndexOutOfBoundsException("열 인덱스가 범위를 벗어났습니다.");
        }
        Matrix copy = this.clone();
        for (int i = 0; i < this.rowSize(); i++) {
            Scalar newVal = copy.getValue(i, index).clone();
            newVal.multiply(val);
            copy.setValue(i, index, newVal);
        }
        return copy;
    }



    @Override
    public Matrix rowAddOtherRow(int targetRow, int sourceRow, Scalar factor) { //49번
        Matrix copy = this.clone();
        for (int j = 0; j < copy.colSize(); j++) {
            Scalar addVal = copy.getValue(sourceRow, j).clone();
            addVal.multiply(factor);
            Scalar newVal = copy.getValue(targetRow, j).clone();
            newVal.add(addVal);
            copy.setValue(targetRow, j, newVal);
        }
        return copy;
    }

    @Override
    public Matrix colAddOtherCol(int targetCol, int sourceCol, Scalar factor) { //50번
        Matrix copy = this.clone();
        for (int i = 0; i < copy.rowSize(); i++) {
            Scalar addVal = copy.getValue(i, sourceCol).clone();
            addVal.multiply(factor);
            Scalar newVal = copy.getValue(i, targetCol).clone();
            newVal.add(addVal);
            copy.setValue(i, targetCol, newVal);
        }
        return copy;
    }


    @Override
    public Matrix getRREF(Matrix m) { //51번
        Matrix copy = m.clone();
        int lead = 0;
        int rowCount = copy.rowSize();
        int colCount = copy.colSize();
        for (int r = 0; r < rowCount; r++) {
            if (lead >= colCount) break;
            int i = r;
            while (i < rowCount && copy.getValue(i, lead).equals(new ScalarImpl("0"))) {
                i++;
            }
            if (i == rowCount) {
                lead++;
                if (lead >= colCount) break;
                r--;
                continue;
            }
            // swap rows i and r
            if (i != r) {
                for (int k = 0; k < colCount; k++) {
                    Scalar temp = copy.getValue(r, k);
                    copy.setValue(r, k, copy.getValue(i, k));
                    copy.setValue(i, k, temp);
                }
            }
            // divide row r by leading value
            Scalar lv = copy.getValue(r, lead).clone();
            for (int k = 0; k < colCount; k++) {
                Scalar val = copy.getValue(r, k).clone();
                if (!lv.equals(new ScalarImpl("0"))) {
                    val = new ScalarImpl((new java.math.BigDecimal(val.getValue()).divide(new java.math.BigDecimal(lv.getValue()), java.math.MathContext.DECIMAL128)).toPlainString());
                }
                copy.setValue(r, k, val);
            }
            // eliminate other rows
            for (int i2 = 0; i2 < rowCount; i2++) {
                if (i2 != r) {
                    Scalar lv2 = copy.getValue(i2, lead).clone();
                    for (int k = 0; k < colCount; k++) {
                        Scalar val = copy.getValue(i2, k).clone();
                        Scalar sub = copy.getValue(r, k).clone();
                        sub.multiply(lv2);
                        val = new ScalarImpl((new java.math.BigDecimal(val.getValue()).subtract(new java.math.BigDecimal(sub.getValue()), java.math.MathContext.DECIMAL128)).toPlainString());
                        copy.setValue(i2, k, val);
                    }
                }
            }
            lead++;
        }
        return copy;
    }

    @Override
    public Matrix isRREF(Matrix m) { //52번
        // 실제 RREF 판별: 각 행의 leading 1은 그 위, 아래 모두 0, leading 1의 위치는 오른쪽으로 이동
        int prevLead = -1;
        for (int i = 0; i < m.rowSize(); i++) {
            int lead = -1;
            for (int j = 0; j < m.colSize(); j++) {
                if (!m.getValue(i, j).equals(new ScalarImpl("0"))) {
                    if (!m.getValue(i, j).equals(new ScalarImpl("1"))) return null;
                    lead = j;
                    break;
                }
            }
            if (lead <= prevLead) return null;
            for (int k = 0; k < m.rowSize(); k++) {
                if (k != i && lead != -1 && !m.getValue(k, lead).equals(new ScalarImpl("0"))) return null;
            }
            prevLead = lead;
        }
        return m;
    }

    @Override
    public Matrix getMatrix(Matrix m) { //53번
        return m.clone();
    }

    @Override
    public Matrix getInverseMatrix(Matrix m) { //54번
        int n = m.rowSize();
        if (n != m.colSize()) throw new IllegalArgumentException("정사각 행렬만 역행렬을 구할 수 있습니다.");
        // 단위행렬 생성
        Scalar[][] identity = new Scalar[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                identity[i][j] = new ScalarImpl(i == j ? "1" : "0");
            }
        }
        Matrix aug = new MatrixImpl(n, 2 * n, new ScalarImpl("0"));
        // 왼쪽에 m, 오른쪽에 단위행렬을 합친 확장행렬 생성
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aug.setValue(i, j, m.getValue(i, j).clone());
                aug.setValue(i, j + n, identity[i][j].clone());
            }
        }
        // 가우스-조르당 소거법
        for (int i = 0; i < n; i++) {
            Scalar diag = aug.getValue(i, i).clone();
            if (diag.equals(new ScalarImpl("0"))) throw new ArithmeticException("역행렬이 존재하지 않습니다.");
            for (int j = 0; j < 2 * n; j++) {
                Scalar val = aug.getValue(i, j).clone();
                val = new ScalarImpl((new java.math.BigDecimal(val.getValue()).divide(new java.math.BigDecimal(diag.getValue()), java.math.MathContext.DECIMAL128)).toPlainString());
                aug.setValue(i, j, val);
            }
            for (int k = 0; k < n; k++) {
                if (k == i) continue;
                Scalar factor = aug.getValue(k, i).clone();
                for (int j = 0; j < 2 * n; j++) {
                    Scalar val = aug.getValue(k, j).clone();
                    Scalar sub = aug.getValue(i, j).clone();
                    sub.multiply(factor);
                    val = new ScalarImpl((new java.math.BigDecimal(val.getValue()).subtract(new java.math.BigDecimal(sub.getValue()), java.math.MathContext.DECIMAL128)).toPlainString());
                    aug.setValue(k, j, val);
                }
            }
        }
        // 오른쪽 n x n 부분이 역행렬
        Scalar[][] inv = new Scalar[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv[i][j] = aug.getValue(i, j + n).clone();
            }
        }
        return new MatrixImpl(inv);
    }
}
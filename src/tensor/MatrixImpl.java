package tensor;

import java.io.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

class MatrixImpl implements Matrix {
    private List<List<Scalar>> elements;

    // 06
    MatrixImpl(int m, int n, Scalar val) {
        if (m <= 0 || n <= 0) throw new IllegalArgumentException("행렬의 크기를 잘못 설정하셨습니다.");
        elements = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            List<Scalar> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(val.clone());
            }
            elements.add(row);
        }
    }

    // 07
    MatrixImpl(int i, int j, int m, int n) {
        if (i >= j || m <= 0 || n <= 0) throw new IllegalArgumentException("잘못된 인자를 입력하였습니다.");
        elements = new ArrayList<>();
        for (int row = 0; row < m; row++) {
            List<Scalar> newRow = new ArrayList<>();
            for (int col = 0; col < n; col++) {
                newRow.add(new ScalarImpl(i, j));
            }
            elements.add(newRow);
        }
    }

    // 08
    MatrixImpl(String csvPath) throws IOException {
        elements = new ArrayList<>();
        try (FileReader fr = new FileReader(csvPath);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                List<Scalar> row = new ArrayList<>();
                String[] stringValues = line.split(",");
                for (String strVal : stringValues) {
                    row.add(new ScalarImpl(strVal.trim()));
                }
                elements.add(row);
            }
        }
    }

    // 09
    MatrixImpl(Scalar[][] arr) {
        elements = new ArrayList<>();
        for (Scalar[] rw : arr) {
            List<Scalar> row = new ArrayList<>();
            for (Scalar scal : rw) {
                row.add(scal.clone());
            }
            elements.add(row);
        }
    }

    // 10
    MatrixImpl(int size) {
        if (size <= 0) throw new IllegalArgumentException("잘못된 인자를 입력하였습니다.");

        Scalar one = new ScalarImpl("1");
        Scalar zero = new ScalarImpl("0");
        elements = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            List<Scalar> row = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    row.add(one.clone());
                } else {
                    row.add(zero.clone());
                }
            }
            elements.add(row);
        }
    }

    // 11
    @Override
    public void setValue(int row, int col, Scalar val) {
        if (row < 0 || row >= rowSize() || col < 0 || col >= colSize()) {
            throw new IndexOutOfBoundsException("인덱스 범위를 벗어났습니다.");
        }
        elements.get(row).set(col, val.clone());
    }

    @Override
    public Scalar getValue(int row, int col) {
        if (row < 0 || row >= rowSize() || col < 0 || col >= colSize()) {
            throw new IndexOutOfBoundsException("인덱스 범위를 벗어났습니다.");
        }
        return elements.get(row).get(col).clone();
    }

    // 13
    @Override
    public int rowSize() {
        return elements.size();
    }

    @Override
    public int colSize() {
        if (elements.isEmpty()) return 0;
        return elements.get(0).size();
    }

    // 14
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rowSize(); i++) {
            sb.append("[");
            List<Scalar> row = elements.get(i);
            for (int j = 0; j < row.size(); j++) {
                // 소수점 5자리까지 표시
                BigDecimal val = new BigDecimal(row.get(j).getValue());
                val = val.setScale(5, RoundingMode.HALF_UP);
                sb.append(val.stripTrailingZeros().toPlainString());
                if (j < row.size() - 1) sb.append(", ");
            }
            sb.append("]");
            if (i < rowSize() - 1) sb.append("\n");
        }
        return sb.toString();
    }

    // 15
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Matrix)) throw new ClassCastException("주어진 객체가 행렬 아닙니다.");
        Matrix other = (Matrix) obj;
        if (this.rowSize() != other.rowSize() || this.colSize() != other.colSize()) return false;

        BigDecimal epsilon = new BigDecimal("0.000001");
        for (int i = 0; i < this.rowSize(); i++) {
            for (int j = 0; j < this.colSize(); j++) {
                BigDecimal val1 = new BigDecimal(this.getValue(i, j).getValue());
                BigDecimal val2 = new BigDecimal(other.getValue(i, j).getValue());
                if (val1.subtract(val2).abs().compareTo(epsilon) > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // 17
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

    //22
    @Override
    public void add(Matrix other) {
        if (this.rowSize() != other.rowSize() || this.colSize() != other.colSize()) {
            throw new SizeMismatchException("행렬 덧셈 조건이 맞지 않습니다.");
        }
        for (int i = 0; i < rowSize(); i++) {
            for (int j = 0; j < colSize(); j++) {
                Scalar selfElem = elements.get(i).get(j);
                selfElem.add(other.getValue(i, j));
            }
        }
    }

    // 23
    @Override
    public void multiply(Matrix other) {
        if (this.colSize() != other.rowSize()) {
            throw new SizeMismatchException("행렬 곱셈 조건이 맞지 않습니다.");
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

    // 32
    @Override
    public Matrix attachHMatrix(Matrix other) {
        if (rowSize() != other.rowSize()) {
            throw new SizeMismatchException("두 행렬의 행수가 같지 않습니다.");
        }
        int newRows = rowSize();
        int newCols = colSize() + other.colSize();
        Scalar[][] newElements = new Scalar[newRows][newCols];
        for (int i = 0; i < newRows; i++) {
            for (int j = 0; j < colSize(); j++) {
                Scalar currentScalar = this.getValue(i, j);
                newElements[i][j] = (currentScalar != null) ? currentScalar.clone() : null;
            }
            for (int j = 0; j < other.colSize(); j++) {
                Scalar otherScalar = other.getValue(i, j);
                newElements[i][colSize() + j] = (otherScalar != null) ? otherScalar.clone() : null;
            }
        }
        return new MatrixImpl(newElements);
    }

    // 33
    @Override
    public Matrix attachVMatrix(Matrix other) {
        if (colSize() != other.colSize()) {
            throw new SizeMismatchException("두 행렬의 열 수가 같지 않습니다.");
        }
        int newRows = rowSize() + other.rowSize();
        int newCols = colSize();
        Scalar[][] newElements = new Scalar[newRows][newCols];
        for (int i = 0; i < rowSize(); i++) {
            for (int j = 0; j < newCols; j++) {
                Scalar currentScalar = this.getValue(i, j);
                newElements[i][j] = (currentScalar != null) ? currentScalar.clone() : null;
            }
        }
        for (int i = 0; i < other.rowSize(); i++) {
            for (int j = 0; j < newCols; j++) {
                Scalar otherScalar = other.getValue(i, j);
                newElements[rowSize() + i][j] = (otherScalar != null) ? otherScalar.clone() : null;
            }
        }
        return new MatrixImpl(newElements);
    }

    // 34
    @Override
    public Vector getRowVector(int row) {
        if (row < 0 || row >= rowSize()) {
            throw new IndexOutOfBoundsException("행 인덱스가 범위를 벗어났습니다.");
        }
        Scalar[] arr = new Scalar[colSize()];
        for (int j = 0; j < colSize(); j++) {
            arr[j] = this.getValue(row, j).clone();
        }
        return new VectorImpl(arr);
    }

    // 35
    @Override
    public Vector getColVector(int col) {
        if (col < 0 || col >= colSize()) {
            throw new IndexOutOfBoundsException("열 인덱스가 범위를 벗어났습니다.");
        }
        Scalar[] arr = new Scalar[rowSize()];
        for (int i = 0; i < rowSize(); i++) {
            arr[i] = this.getValue(i, col).clone();
        }
        return new VectorImpl(arr);
    }

    // 36
    @Override
    public Matrix extractSubMatrix(int rowStart, int rowEnd, int colStart, int colEnd) {
        if (rowStart < 0 || rowEnd > rowSize() || colStart < 0 || colEnd > colSize()
                || rowStart >= rowEnd || colStart >= colEnd) {
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

    // 37
    @Override
    public Matrix minorSubMatrix(int row, int col) {
        if (row < 0 || row >= rowSize() || col < 0 || col >= colSize()) {
            throw new IndexOutOfBoundsException("인덱스 범위를 벗어났습니다.");
        }
        int rows = rowSize() - 1;
        int cols = colSize() - 1;
        Scalar[][] arr = new Scalar[rows][cols];
        int r = 0;
        for (int i = 0; i < rowSize(); i++) {
            if (i == row) continue;
            int c = 0;
            for (int j = 0; j < colSize(); j++) {
                if (j == col) continue;
                arr[r][c] = this.getValue(i, j).clone();
                c++;
            }
            r++;
        }
        return new MatrixImpl(arr);
    }

    // 38
    @Override
    public Matrix transposeMatrix() {
        int rows = rowSize();
        int cols = colSize();
        Scalar[][] arr = new Scalar[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arr[j][i] = this.getValue(i, j).clone();
            }
        }
        return new MatrixImpl(arr);
    }

    // 39
    @Override
    public Scalar trace() {
        if (!isSquare()) {
            throw new NotSquareMatrixException("정사각 행렬이 아닙니다.");
        }
        Scalar sumElements = new ScalarImpl("0");
        for (int i = 0; i < rowSize(); i++) {
            Scalar diag = this.getValue(i, i);
            if (diag == null) {
                throw new NullPointerException("대각 원소가 null입니다.");
            }
            sumElements.add(diag);
        }
        return sumElements;
    }

    // 40
    @Override
    public boolean isSquare() {
        return rowSize() == colSize();
    }

    // 41
    @Override
    public boolean isUpperTriangular() {
        if (!isSquare()) {
            throw new NotSquareMatrixException("정사각 행렬이 아닙니다.");
        }
        Scalar zeroScalar = new ScalarImpl("0");
        for (int i = 0; i < rowSize(); i++) {
            for (int j = 0; j < i; j++) {
                if (!getValue(i, j).equals(zeroScalar)) {
                    return false;
                }
            }
        }
        return true;
    }

    // 42
    @Override
    public boolean isLowerTriangular() {
        if (!isSquare()) {
            throw new NotSquareMatrixException("정사각 행렬이 아닙니다.");
        }
        Scalar zeroScalar = new ScalarImpl("0");
        for (int i = 0; i < rowSize(); i++) {
            for (int j = i + 1; j < colSize(); j++) {
                if (!getValue(i, j).equals(zeroScalar)) {
                    return false;
                }
            }
        }
        return true;
    }

    // 43
    @Override
    public boolean isIdentity() {
        if (!isSquare()) {
            throw new NotSquareMatrixException("정사각 행렬이 아닙니다.");
        }
        Scalar oneScalar = new ScalarImpl("1");
        Scalar zeroScalar = new ScalarImpl("0");
        for (int i = 0; i < rowSize(); i++) {
            for (int j = 0; j < colSize(); j++) {
                Scalar current = getValue(i, j);
                if (i == j) {
                    if (!current.equals(oneScalar)) return false;
                } else {
                    if (!current.equals(zeroScalar)) return false;
                }
            }
        }
        return true;
    }

    // 44
    @Override
    public boolean isZero() {
        Scalar zeroScalar = new ScalarImpl("0");
        for (int i = 0; i < rowSize(); i++) {
            for (int j = 0; j < colSize(); j++) {
                if (!getValue(i, j).equals(zeroScalar)) {
                    return false;
                }
            }
        }
        return true;
    }

    // 45
    @Override
    public void rowSwap(int row1, int row2) {
        if (row1 < 0 || row1 >= rowSize() || row2 < 0 || row2 >= rowSize()) {
            throw new IndexOutOfBoundsException("행 인덱스가 범위를 벗어났습니다.");
        }
        if (row1 == row2) return;
        List<Scalar> tmp = elements.get(row1);
        elements.set(row1, elements.get(row2));
        elements.set(row2, tmp);
    }

    // 46
    @Override
    public void colSwap(int col1, int col2) {
        if (col1 < 0 || col1 >= colSize() || col2 < 0 || col2 >= colSize()) {
            throw new IndexOutOfBoundsException("열 인덱스가 범위를 벗어났습니다.");
        }
        if (col1 == col2) return;
        for (int i = 0; i < rowSize(); i++) {
            List<Scalar> currentRow = elements.get(i);
            Scalar tmp = currentRow.get(col1);
            currentRow.set(col1, currentRow.get(col2));
            currentRow.set(col2, tmp);
        }
    }

    // 47
    @Override
    public void rowMultiply(int row, Scalar val) {
        if (row < 0 || row >= rowSize()) {
            throw new IndexOutOfBoundsException("행 인덱스가 범위를 벗어났습니다.");
        }
        if (val == null) {
            throw new NullPointerException("곱할 스칼라 값은 null일 수 없습니다.");
        }
        List<Scalar> currentRow = elements.get(row);
        for (int i = 0; i < colSize(); i++) {
            Scalar elem = currentRow.get(i);
            if (elem != null) {
                elem.multiply(val);
            } else {
                throw new NullPointerException("null은 곱셈을 할 수 없습니다.");
            }
        }
    }

    // 48
    @Override
    public void colMultiply(int col, Scalar val) {
        if (col < 0 || col >= colSize()) {
            throw new IndexOutOfBoundsException("열 인덱스가 범위를 벗어났습니다.");
        }
        if (val == null) {
            throw new NullPointerException("곱할 스칼라 값은 null일 수 없습니다.");
        }
        for (int i = 0; i < rowSize(); i++) {
            Scalar elem = elements.get(i).get(col);
            if (elem != null) {
                elem.multiply(val);
            } else {
                throw new NullPointerException("null은 곱셈을 할 수 없습니다.");
            }
        }
    }

    // 49
    @Override
    public void rowAddOtherRow(int targetRow, int sourceRow, Scalar val) {
        if (targetRow < 0 || targetRow >= rowSize()) {
            throw new IndexOutOfBoundsException("target 행 인덱스가 범위를 벗어났습니다.");
        }
        if (sourceRow < 0 || sourceRow >= rowSize()) {
            throw new IndexOutOfBoundsException("source 행 인덱스가 범위를 벗어났습니다.");
        }
        if (val == null) {
            throw new NullPointerException("곱할 스칼라 값은 null일 수 없습니다.");
        }
        List<Scalar> tgtRow = elements.get(targetRow);
        List<Scalar> srcRow = elements.get(sourceRow);
        for (int j = 0; j < colSize(); j++) {
            Scalar t = tgtRow.get(j);
            Scalar s = srcRow.get(j);
            if (t == null || s == null) {
                if (s == null) throw new NullPointerException("source 행의 요소가 null입니다.");
                if (t == null) throw new NullPointerException("target 행의 요소가 null입니다.");
            }
            Scalar tmp = s.clone();
            tmp.multiply(val);
            t.add(tmp);
        }
    }

    // 50
    @Override
    public void colAddOtherCol(int targetCol, int sourceCol, Scalar val) {
        if (targetCol < 0 || targetCol >= colSize()) {
            throw new IndexOutOfBoundsException("target 열 인덱스가 범위를 벗어났습니다.");
        }
        if (sourceCol < 0 || sourceCol >= colSize()) {
            throw new IndexOutOfBoundsException("source 열 인덱스가 범위를 벗어났습니다.");
        }
        if (val == null) {
            throw new NullPointerException("곱할 스칼라 값은 null일 수 없습니다.");
        }
        for (int i = 0; i < rowSize(); i++) {
            List<Scalar> row = elements.get(i);
            Scalar t = row.get(targetCol);
            Scalar s = row.get(sourceCol);
            if (t == null || s == null) {
                if (s == null) throw new NullPointerException("source 열의 요소가 null입니다.");
                if (t == null) throw new NullPointerException("target 열의 요소가 null입니다.");
            }
            Scalar tmp = s.clone();
            tmp.multiply(val);
            t.add(tmp);
        }
    }

    // 51
    @Override
    public Matrix getRREF() {
        Matrix copy = this.clone();
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
            if (i != r) {
                for (int k = 0; k < colCount; k++) {
                    Scalar tmp = copy.getValue(r, k);
                    copy.setValue(r, k, copy.getValue(i, k));
                    copy.setValue(i, k, tmp);
                }
            }
            Scalar lv = copy.getValue(r, lead).clone();
            for (int k = 0; k < colCount; k++) {
                Scalar val = copy.getValue(r, k).clone();
                if (!lv.equals(new ScalarImpl("0"))) {
                    val = new ScalarImpl((new BigDecimal(val.getValue())
                            .divide(new BigDecimal(lv.getValue()), MathContext.DECIMAL128))
                            .toPlainString());
                }
                copy.setValue(r, k, val);
            }
            for (int i2 = 0; i2 < rowCount; i2++) {
                if (i2 != r) {
                    Scalar lv2 = copy.getValue(i2, lead).clone();
                    for (int k = 0; k < colCount; k++) {
                        Scalar val = copy.getValue(i2, k).clone();
                        Scalar sub = copy.getValue(r, k).clone();
                        sub.multiply(lv2);
                        val = new ScalarImpl((new BigDecimal(val.getValue())
                                .subtract(new BigDecimal(sub.getValue()), MathContext.DECIMAL128))
                                .toPlainString());
                        copy.setValue(i2, k, val);
                    }
                }
            }
            lead++;
        }
        return copy;
    }

    // 52
    @Override
    public boolean isRREF() {
        Scalar zeroScalar = new ScalarImpl("0");
        Scalar oneScalar = new ScalarImpl("1");
        int previousLeadCol = -1;
        boolean encounteredZeroRow = false;

        for (int r = 0; r < rowSize(); r++) {
            int currentLeadCol = -1;
            for (int c = 0; c < colSize(); c++) {
                if (!getValue(r, c).equals(zeroScalar)) {
                    currentLeadCol = c;
                    break;
                }
            }
            if (currentLeadCol != -1) {
                if (encounteredZeroRow) {
                    return false;
                }
                if (!getValue(r, currentLeadCol).equals(oneScalar)) {
                    return false;
                }
                if (currentLeadCol <= previousLeadCol) {
                    return false;
                }
                for (int k = 0; k < rowSize(); k++) {
                    if (k != r) {
                        if (!getValue(k, currentLeadCol).equals(zeroScalar)) {
                            return false;
                        }
                    }
                }
                previousLeadCol = currentLeadCol;
            } else {
                encounteredZeroRow = true;
            }
        }
        return true;
    }

    // 53
    @Override
    public Scalar getDeterminant() {
        if (!isSquare()) {
            throw new SizeMismatchException("행렬식은 정사각 행렬에서만 구할 수 있습니다.");
        }
        int n = rowSize();
        if (n == 0) {
            throw new SizeMismatchException("0x0 행렬의 행렬식은 지원하지 않습니다.");
        }
        if (n == 1) {
            Scalar element = this.getValue(0, 0);
            if (element == null) {
                throw new NullPointerException("1x1 행렬의 요소가 null입니다.");
            }
            return element.clone();
        }
        if (n == 2) {
            Scalar a = this.getValue(0, 0);
            Scalar b = this.getValue(0, 1);
            Scalar c = this.getValue(1, 0);
            Scalar d = this.getValue(1, 1);
            if (a == null || b == null || c == null || d == null) {
                throw new NullPointerException("2x2 행렬의 요소 중 null 값이 있습니다.");
            }
            Scalar temp_ad = a.clone();
            temp_ad.multiply(d.clone());
            Scalar temp_bc = b.clone();
            temp_bc.multiply(c.clone());
            Scalar minusOne = new ScalarImpl("-1");
            temp_bc.multiply(minusOne);
            temp_ad.add(temp_bc);
            return temp_ad;
        }
        Scalar determinantSum = new ScalarImpl("0");
        Scalar flippedSign = new ScalarImpl("1");
        for (int j = 0; j < colSize(); j++) {
            Scalar elementInFirstRow = this.getValue(0, j);
            if (elementInFirstRow == null) {
                throw new NullPointerException("행렬 요소가 null입니다.");
            }
            Matrix minorMat = this.minorSubMatrix(0, j);
            Scalar minorDet = minorMat.getDeterminant();
            Scalar term = elementInFirstRow.clone();
            term.multiply(minorDet);
            term.multiply(flippedSign);
            determinantSum.add(term);
            flippedSign.multiply(new ScalarImpl("-1"));
        }
        return determinantSum;
    }

    // 54
    @Override
    public Matrix getInverseMatrix() {
        if (!isSquare()) {
            throw new SizeMismatchException("정사각 행렬만 역행렬을 구할 수 있습니다.");
        }
        int n = rowSize();
        if (n == 0) {
            throw new SizeMismatchException("0x0 행렬의 역행렬은 정의되지 않습니다.");
        }
        Scalar determinantValue = this.getDeterminant();
        BigDecimal detBd = new BigDecimal(determinantValue.getValue());
        if (detBd.compareTo(BigDecimal.ZERO) == 0) {
            throw new SizeMismatchException("역행렬이 존재하지 않습니다 (행렬식이 0입니다).");
        }
        // 2×2 특수 케이스
        if (n == 2) {
            Scalar a = getValue(0, 0);
            Scalar b = getValue(0, 1);
            Scalar c = getValue(1, 0);
            Scalar d = getValue(1, 1);
            Scalar[][] inv = new Scalar[2][2];
            inv[0][0] = new ScalarImpl(
                    (new BigDecimal(d.getValue())
                            .divide(new BigDecimal(determinantValue.getValue()), 10, RoundingMode.HALF_UP))
                            .toPlainString()
            );
            inv[0][1] = new ScalarImpl(
                    (new BigDecimal(b.getValue()).multiply(new BigDecimal("-1"))
                            .divide(new BigDecimal(determinantValue.getValue()), 10, RoundingMode.HALF_UP))
                            .toPlainString()
            );
            inv[1][0] = new ScalarImpl(
                    (new BigDecimal(c.getValue()).multiply(new BigDecimal("-1"))
                            .divide(new BigDecimal(determinantValue.getValue()), 10, RoundingMode.HALF_UP))
                            .toPlainString()
            );
            inv[1][1] = new ScalarImpl(
                    (new BigDecimal(a.getValue())
                            .divide(new BigDecimal(determinantValue.getValue()), 10, RoundingMode.HALF_UP))
                            .toPlainString()
            );
            return new MatrixImpl(inv);
        }
        Scalar[][] cofactorMatrixData = new Scalar[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Matrix minorMat = this.minorSubMatrix(i, j);
                Scalar minorDet = minorMat.getDeterminant();
                Scalar sign = new ScalarImpl(((i + j) % 2 == 0) ? "1" : "-1");
                cofactorMatrixData[i][j] = minorDet.clone();
                cofactorMatrixData[i][j].multiply(sign);
            }
        }
        Matrix cofactorMatrix = new MatrixImpl(cofactorMatrixData);
        Matrix adjugateMatrix = cofactorMatrix.transposeMatrix();
        BigDecimal oneOverDetVal = BigDecimal.ONE.divide(detBd, MathContext.DECIMAL128);
        Scalar oneOverDeterminant = new ScalarImpl(oneOverDetVal.toPlainString());

        Scalar[][] inverseMatrixVal = new Scalar[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Scalar adjElement = adjugateMatrix.getValue(i, j);
                if (adjElement == null) {
                    throw new NullPointerException("수반 행렬의 요소가 null입니다.");
                }
                inverseMatrixVal[i][j] = adjElement.clone();
                inverseMatrixVal[i][j].multiply(oneOverDeterminant);
            }
        }
        return new MatrixImpl(inverseMatrixVal);
    }

    // 28
    static Matrix add(Matrix m1, Matrix m2) {
        Matrix result = m1.clone();
        result.add(m2);
        return result;
    }

    // 29
    static Matrix multiply(Matrix m1, Matrix m2) {
        Matrix result = m1.clone();
        result.multiply(m2);
        return result;
    }

    // 32
    static Matrix attachHMatrix(Matrix m1, Matrix m2) {
        return m1.attachHMatrix(m2);
    }

    // 33
    static Matrix attachVMatrix(Matrix m1, Matrix m2) {
        return m1.attachVMatrix(m2);
    }
}

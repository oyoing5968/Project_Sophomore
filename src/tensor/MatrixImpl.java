package tensor;

import java.io.*;
import java.util.*;
import java.math.BigDecimal;

class MatrixImpl implements Matrix {
    private List<List<Scalar>> elements;

    MatrixImpl(int m, int n, Scalar val) { //6번
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

    MatrixImpl(int i, int j, int m, int n) { //7번
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
    MatrixImpl(String csvPath) throws IOException {  //9번
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

    MatrixImpl(Scalar[][] arr) { //8번
        elements = new ArrayList<>();
        for (Scalar[] row : arr) {
            List<Scalar> newRow = new ArrayList<>();
            for (Scalar s : row) {
                newRow.add(s.clone());
            }
            elements.add(newRow);
        }
    }

    MatrixImpl(int Size) { //10번
        if (Size <= 0) throw new IllegalArgumentException("Invalid dimension.");

        Scalar one = new ScalarImpl("1");
        Scalar zero = new ScalarImpl("0");

        elements = new ArrayList<>();

        for (int i = 0; i < Size; i++) {
            List<Scalar> row = new ArrayList<>();
            for (int j = 0; j < Size; j++) {
                row.add(zero.clone());
            }
            elements.add(row);
        }
    }


    public int getRowCount() {
        return elements.size();
    }

    public int getColumnCount() {
        if (elements.isEmpty()) return 0;
        return elements.get(0).size();
    }

    public Scalar get(int row, int col) { //11번
        return elements.get(row).get(col);
    }

    public void set(int row, int col, Scalar value) { //11번
        elements.get(row).set(col, value.clone());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (List<Scalar> row : elements) {
            for (Scalar s : row) {
                sb.append(s.toString()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix)) return false;
        Matrix other = (Matrix) obj;
        if (this.getRowCount() != other.getRowCount() || this.getColumnCount() != other.getColumnCount()) return false;
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                if (!this.get(i, j).equals(other.get(i, j))) return false;
            }
        }
        return true;
    }

    public Matrix clone() {
        Scalar[][] copied = new Scalar[this.getRowCount()][this.getColumnCount()];
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                copied[i][j] = this.get(i, j).clone();
            }
        }
        return new MatrixImpl(copied);
    }

    public void add(Matrix other) { //
        if (this.getRowCount() != other.getRowCount() || this.getColumnCount() != other.getColumnCount())
            throw new DimensionMismatchException("Matrix size mismatch.");
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                this.get(i, j).add(other.get(i, j));
            }
        }
    }

    public void multiply(Matrix other) { //
        if (this.getColumnCount() != other.getRowCount())
            throw new DimensionMismatchException("Matrix multiplication dimension mismatch.");
        int m = this.getRowCount();
        int n = other.getColumnCount();
        Scalar[][] result = new Scalar[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Scalar sum = new ScalarImpl("0");
                for (int k = 0; k < this.getColumnCount(); k++) {
                    Scalar temp = this.get(i, k).clone();
                    temp.multiply(other.get(k, j));
                    sum.add(temp);
                }
                result[i][j] = sum;
            }
        }
        this.elements.clear();
        for (int i = 0; i < m; i++) {
            List<Scalar> newRow = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                newRow.add(result[i][j]);
            }
            this.elements.add(newRow);
        }
    }

    // 고급 기능 추가
    public Matrix transpose() {
        int m = this.getRowCount();
        int n = this.getColumnCount();
        Scalar[][] transposed = new Scalar[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                transposed[j][i] = this.get(i, j).clone();
            }
        }
        return new MatrixImpl(transposed);
    }

    public Matrix minor(int rowToRemove, int colToRemove) {
        int m = this.getRowCount();
        int n = this.getColumnCount();
        Scalar[][] result = new Scalar[m - 1][n - 1];
        for (int i = 0, r = 0; i < m; i++) {
            if (i == rowToRemove) continue;
            for (int j = 0, c = 0; j < n; j++) {
                if (j == colToRemove) continue;
                result[r][c] = this.get(i, j).clone();
                c++;
            }
            r++;
        }
        return new MatrixImpl(result);
    }

    public Scalar trace() {
        if (this.getRowCount() != this.getColumnCount())
            throw new DimensionMismatchException("Trace only for square matrices.");
        Scalar sum = new ScalarImpl("0");
        for (int i = 0; i < getRowCount(); i++) {
            sum.add(this.get(i, i));
        }
        return sum;
    }

    public Scalar determinant() {
        if (this.getRowCount() != this.getColumnCount())
            throw new DimensionMismatchException("Determinant only for square matrices.");
        int n = getRowCount();
        if (n == 1) return this.get(0, 0).clone();
        Scalar det = new ScalarImpl("0");
        for (int col = 0; col < n; col++) {
            Matrix minor = this.minor(0, col);
            Scalar cofactor = this.get(0, col).clone();
            Scalar minorDet = ((MatrixImpl) minor).determinant();
            if (col % 2 != 0) cofactor.multiply(new ScalarImpl("-1"));
            cofactor.multiply(minorDet);
            det.add(cofactor);
        }
        return det;
    }

    public Matrix rref() {
        MatrixImpl copy = (MatrixImpl) this.clone();
        int lead = 0;
        int rowCount = copy.getRowCount();
        int colCount = copy.getColumnCount();

        for (int r = 0; r < rowCount; r++) {
            if (colCount <= lead) break;
            int i = r;
            while (copy.get(i, lead).getValue().compareTo(BigDecimal.ZERO) == 0) {
                i++;
                if (i == rowCount) {
                    i = r;
                    lead++;
                    if (lead == colCount) return copy;
                }
            }
            Collections.swap(copy.elements, i, r);
            Scalar div = copy.get(r, lead).clone();
            for (int j = 0; j < colCount; j++) {
                Scalar val = copy.get(r, j).clone();
                val = new ScalarImpl(val.getValue().divide(div.getValue(), 20, BigDecimal.ROUND_HALF_UP).toPlainString());
                copy.set(r, j, val);
            }
            for (int j = 0; j < rowCount; j++) {
                if (j != r) {
                    Scalar mult = copy.get(j, lead).clone();
                    for (int k = 0; k < colCount; k++) {
                        Scalar val = copy.get(j, k).clone();
                        Scalar temp = copy.get(r, k).clone();
                        temp.multiply(mult);
                        val = new ScalarImpl(val.getValue().subtract(temp.getValue()).toPlainString());
                        copy.set(j, k, val);
                    }
                }
            }
            lead++;
        }
        return copy;
    }
    // 특정 행을 Vector로 추출
    public Vector getRowVector(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= getRowCount())
            throw new IndexOutOfBoundsException("Row index out of bounds.");

        Scalar[] row = new Scalar[getColumnCount()];
        for (int j = 0; j < getColumnCount(); j++) {
            row[j] = get(rowIndex, j).clone();
        }
        return new VectorImpl(row);
    }

    // 특정 열을 Vector로 추출
    public Vector getColumnVector(int colIndex) {
        if (colIndex < 0 || colIndex >= getColumnCount())
            throw new IndexOutOfBoundsException("Column index out of bounds.");

        Scalar[] col = new Scalar[getRowCount()];
        for (int i = 0; i < getRowCount(); i++) {
            col[i] = get(i, colIndex).clone();
        }
        return new VectorImpl(col);
    }
    // 두 행렬을 가로로 이어붙이기 (column 방향)
    public static Matrix horizontalConcat(Matrix left, Matrix right) {
        if (left.getRowCount() != right.getRowCount()) {
            throw new DimensionMismatchException("Row counts must match for horizontal concatenation.");
        }

        Scalar[][] combined = new Scalar[left.getRowCount()][left.getColumnCount() + right.getColumnCount()];
        for (int i = 0; i < left.getRowCount(); i++) {
            for (int j = 0; j < left.getColumnCount(); j++) {
                combined[i][j] = left.get(i, j).clone();
            }
            for (int j = 0; j < right.getColumnCount(); j++) {
                combined[i][left.getColumnCount() + j] = right.get(i, j).clone();
            }
        }
        return new MatrixImpl(combined);
    }

    // 두 행렬을 세로로 이어붙이기 (row 방향)
    public static Matrix verticalConcat(Matrix top, Matrix bottom) {
        if (top.getColumnCount() != bottom.getColumnCount()) {
            throw new DimensionMismatchException("Column counts must match for vertical concatenation.");
        }

        Scalar[][] combined = new Scalar[top.getRowCount() + bottom.getRowCount()][top.getColumnCount()];
        for (int i = 0; i < top.getRowCount(); i++) {
            for (int j = 0; j < top.getColumnCount(); j++) {
                combined[i][j] = top.get(i, j).clone();
            }
        }
        for (int i = 0; i < bottom.getRowCount(); i++) {
            for (int j = 0; j < bottom.getColumnCount(); j++) {
                combined[top.getRowCount() + i][j] = bottom.get(i, j).clone();
            }
        }
        return new MatrixImpl(combined);
    }
    // 상삼각행렬인지 판별
    public boolean isUpperTriangular() {
        if (getRowCount() != getColumnCount()) return false;
        for (int i = 1; i < getRowCount(); i++) {
            for (int j = 0; j < i; j++) {
                if (get(i, j).getValue().compareTo(BigDecimal.ZERO) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // 하삼각행렬인지 판별
    public boolean isLowerTriangular() {
        if (getRowCount() != getColumnCount()) return false;
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = i + 1; j < getColumnCount(); j++) {
                if (get(i, j).getValue().compareTo(BigDecimal.ZERO) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Zero Matrix인지 판별
    public boolean isZeroMatrix() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                if (get(i, j).getValue().compareTo(BigDecimal.ZERO) != 0) {
                    return false;
                }
            }
        }
        return true;
    }
    // 두 행을 교환
    public void swapRows(int row1, int row2) {
        if (row1 < 0 || row2 < 0 || row1 >= getRowCount() || row2 >= getRowCount()) {
            throw new IndexOutOfBoundsException("Row index out of bounds.");
        }
        Collections.swap(elements, row1, row2);
    }

    // 특정 행을 스케일링 (배수 곱하기)
    public void scaleRow(int rowIndex, Scalar scalar) {
        if (rowIndex < 0 || rowIndex >= getRowCount()) {
            throw new IndexOutOfBoundsException("Row index out of bounds.");
        }
        for (int j = 0; j < getColumnCount(); j++) {
            Scalar temp = get(rowIndex, j).clone();
            temp.multiply(scalar);
            set(rowIndex, j, temp);
        }
    }
    // 역행렬 구하기 (Gauss-Jordan 방법)
    public Matrix inverse() {
        if (getRowCount() != getColumnCount()) {
            throw new DimensionMismatchException("Inverse only for square matrices.");
        }

        int n = getRowCount();
        MatrixImpl augmented = (MatrixImpl) this.clone();
        MatrixImpl identity = (MatrixImpl) (new MatrixImpl(n, n, new ScalarImpl("0"))).identity(n);

        for (int i = 0; i < n; i++) {
            // 피벗이 0이면 밑에 행과 스왑
            if (augmented.get(i, i).getValue().compareTo(BigDecimal.ZERO) == 0) {
                boolean swapped = false;
                for (int j = i + 1; j < n; j++) {
                    if (augmented.get(j, i).getValue().compareTo(BigDecimal.ZERO) != 0) {
                        augmented.swapRows(i, j);
                        identity.swapRows(i, j);
                        swapped = true;
                        break;
                    }
                }
                if (!swapped) throw new ArithmeticException("Matrix is singular and cannot be inverted.");
            }

            // 피벗을 1로 만들기
            Scalar pivot = augmented.get(i, i).clone();
            for (int j = 0; j < n; j++) {
                Scalar augVal = augmented.get(i, j).clone();
                Scalar idVal = identity.get(i, j).clone();
                augVal = new ScalarImpl(augVal.getValue().divide(pivot.getValue(), 20, BigDecimal.ROUND_HALF_UP).toPlainString());
                idVal = new ScalarImpl(idVal.getValue().divide(pivot.getValue(), 20, BigDecimal.ROUND_HALF_UP).toPlainString());
                augmented.set(i, j, augVal);
                identity.set(i, j, idVal);
            }

            // 다른 행들에서 i번째 열을 0으로 만들기
            for (int k = 0; k < n; k++) {
                if (k == i) continue;
                Scalar factor = augmented.get(k, i).clone();
                for (int j = 0; j < n; j++) {
                    Scalar augVal = augmented.get(k, j).clone();
                    Scalar idVal = identity.get(k, j).clone();
                    Scalar tempAug = augmented.get(i, j).clone();
                    Scalar tempId = identity.get(i, j).clone();

                    tempAug.multiply(factor);
                    tempId.multiply(factor);

                    augVal = new ScalarImpl(augVal.getValue().subtract(tempAug.getValue()).toPlainString());
                    idVal = new ScalarImpl(idVal.getValue().subtract(tempId.getValue()).toPlainString());

                    augmented.set(k, j, augVal);
                    identity.set(k, j, idVal);
                }
            }
        }

        return identity;
    }
    public boolean isIdentityMatrix() {
        if (getRowCount() != getColumnCount()) return false;
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                BigDecimal val = get(i, j).getValue();
                if (i == j && val.compareTo(BigDecimal.ONE) != 0) return false;
                if (i != j && val.compareTo(BigDecimal.ZERO) != 0) return false;
            }
        }
        return true;
    }

    public void swapColumns(int col1, int col2) {
        if (col1 < 0 || col2 < 0 || col1 >= getColumnCount() || col2 >= getColumnCount())
            throw new IndexOutOfBoundsException("Column index out of bounds.");
        for (List<Scalar> row : elements) {
            Collections.swap(row, col1, col2);
        }
    }

    public void scaleColumn(int colIndex, Scalar scalar) {
        if (colIndex < 0 || colIndex >= getColumnCount())
            throw new IndexOutOfBoundsException("Column index out of bounds.");
        for (int i = 0; i < getRowCount(); i++) {
            Scalar temp = get(i, colIndex).clone();
            temp.multiply(scalar);
            set(i, colIndex, temp);
        }
    }

    public void addScaledRow(int targetRow, int sourceRow, Scalar scalar) {
        if (targetRow < 0 || sourceRow < 0 || targetRow >= getRowCount() || sourceRow >= getRowCount())
            throw new IndexOutOfBoundsException("Row index out of bounds.");
        for (int j = 0; j < getColumnCount(); j++) {
            Scalar temp = get(sourceRow, j).clone();
            temp.multiply(scalar);
            Scalar newVal = get(targetRow, j).clone();
            newVal.add(temp);
            set(targetRow, j, newVal);
        }
    }

    public void addScaledColumn(int targetCol, int sourceCol, Scalar scalar) {
        if (targetCol < 0 || sourceCol < 0 || targetCol >= getColumnCount() || sourceCol >= getColumnCount())
            throw new IndexOutOfBoundsException("Column index out of bounds.");
        for (int i = 0; i < getRowCount(); i++) {
            Scalar temp = get(i, sourceCol).clone();
            temp.multiply(scalar);
            Scalar newVal = get(i, targetCol).clone();
            newVal.add(temp);
            set(i, targetCol, newVal);
        }
    }

    public Matrix subMatrix(int rowStart, int rowEnd, int colStart, int colEnd) {
        if (rowStart < 0 || rowEnd >= getRowCount() || colStart < 0 || colEnd >= getColumnCount()
                || rowStart > rowEnd || colStart > colEnd)
            throw new IndexOutOfBoundsException("Invalid submatrix range.");

        Scalar[][] sub = new Scalar[rowEnd - rowStart + 1][colEnd - colStart + 1];
        for (int i = rowStart; i <= rowEnd; i++) {
            for (int j = colStart; j <= colEnd; j++) {
                sub[i - rowStart][j - colStart] = get(i, j).clone();
            }
        }
        return new MatrixImpl(sub);
    }







}
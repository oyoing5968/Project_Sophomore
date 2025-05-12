package test;

import tensor.*;

public class Test {
    public static void main(String[] args) {
        System.out.println("===== SCALAR TEST =====");
        Scalar s1 = Factory.createScalar("5");
        Scalar s2 = Factory.createScalar("3");
        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
        System.out.println("s1 + s2 = " + Tensors.add(s1, s2));
        System.out.println("s1 * s2 = " + Tensors.multiply(s1, s2));
        System.out.println("s1 == clone(s1): " + s1.equals(s1.clone()));
        System.out.println("s1.compareTo(s2): " + s1.compareTo(s2));

        System.out.println("\n===== VECTOR TEST =====");
        Vector v1 = Factory.createVector(3, s1);
        Vector v2 = Factory.createVector(1, 5, 3);
        System.out.println("v1: " + v1);
        System.out.println("v2: " + v2);
        System.out.println("v1 + v2 = " + Tensors.add(v1, v2));
        System.out.println("v2 * 2 = " + Tensors.multiply(v2, Factory.createScalar("2")));
        System.out.println("v1 == clone(v1): " + v1.equals(v1.clone()));

        System.out.println("\n===== ARRAY TO VECTOR TEST =====");
        Scalar[] arr = {
                Factory.createScalar("10"),
                Factory.createScalar("20"),
                Factory.createScalar("30")
        };

        System.out.println("\n===== MATRIX TEST =====");
        Matrix m1 = Factory.createMatrix(2, 3, s2);
        Matrix m2 = Factory.createMatrix(1, 5, 2, 3);
        System.out.println("m1:\n" + m1);
        System.out.println("m2:\n" + m2);
        System.out.println("m1 + m2:\n" + Tensors.add(m1, m2));
        Matrix m3 = Factory.createMatrix(3, 2, Factory.createScalar("1"));
        Matrix m4 = Factory.createMatrix(2, 3, Factory.createScalar("2"));
        System.out.println("m4:\n" + m4);
        System.out.println("m3:\n" + m3);
        System.out.println("m4 * m3:\n" + Tensors.multiply(m4, m3));

        System.out.println("\n===== ARRAY TO MATRIX TEST =====");
        Scalar[][] array = {
                {Factory.createScalar("1"), Factory.createScalar("2")},
                {Factory.createScalar("3"), Factory.createScalar("4")}
        };
        Matrix fromArray = Factory.createMatrix(array);
        System.out.println("Matrix from Scalar[][]:\n" + fromArray);

        System.out.println("\n===== MATRIX ADVANCED TEST =====");
        Matrix square = Factory.createMatrix(3, 3, Factory.createScalar("0"));
        square.setValue(0, 0, Factory.createScalar("2"));
        square.setValue(0, 1, Factory.createScalar("3"));
        square.setValue(0, 2, Factory.createScalar("1"));
        square.setValue(1, 0, Factory.createScalar("4"));
        square.setValue(1, 1, Factory.createScalar("1"));
        square.setValue(1, 2, Factory.createScalar("5"));
        square.setValue(2, 0, Factory.createScalar("6"));
        square.setValue(2, 1, Factory.createScalar("2"));
        square.setValue(2, 2, Factory.createScalar("1"));

        System.out.println("Square matrix:\n" + square);
        System.out.println("Transpose:\n" + Tensors.transpose(square));
        System.out.println("Minor (remove 1st row, 1st col):\n" + Tensors.minor(square, 0, 0));
        System.out.println("Trace: " + Tensors.trace(square));
        System.out.println("Determinant: " + Tensors.determinant(square));
        System.out.println("RREF:\n" + Tensors.rref(square));

        System.out.println("\n===== IDENTITY MATRIX TEST =====");
        Matrix identity = Factory.createIdentityMatrix(4);
        System.out.println("Identity Matrix (4x4):\n" + identity);

        System.out.println("\n===== CLONING TEST =====");
        System.out.println("Clone of m1:\n" + m1.clone());

        System.out.println("\n===== VECTOR TO MATRIX TEST =====");
        Vector v = Factory.createVector(1, 5, 4);
        System.out.println("Original Vector: " + v);
        Matrix rowMatrix = Tensors.toRowMatrix(v);
        Matrix colMatrix = Tensors.toColumnMatrix(v);
        System.out.println("Row Matrix:\n" + rowMatrix);
        System.out.println("Column Matrix:\n" + colMatrix);

        System.out.println("\n===== INVERSE TEST =====");
        Matrix m = Factory.createMatrix(2, 2, Factory.createScalar("0"));
        m.setValue(0, 0, Factory.createScalar("4"));
        m.setValue(0, 1, Factory.createScalar("7"));
        m.setValue(1, 0, Factory.createScalar("2"));
        m.setValue(1, 1, Factory.createScalar("6"));
        System.out.println("Matrix m:\n" + m);
        Matrix inv = Tensors.inverse(m);
        System.out.println("Inverse of m:\n" + inv);
        Matrix prod = Tensors.multiply(m, inv);
        System.out.println("m * inv(m):\n" + prod);
        System.out.println("===== 추가 메서드 테스트 =====");
// 3x3 단위행렬 생성
        //Matrix identity = Factory.createIdentityMatrix(3);
        System.out.println("Identity Matrix:\n" + identity);
        System.out.println("isIdentityMatrix? " + identity.isIdentityMatrix());

// 열 교환 테스트
        //Matrix m1 = Factory.createMatrix(3, 3, Factory.createScalar("1"));
        m1.setValue(0, 1, Factory.createScalar("2"));  // 인터페이스에서 제공
        System.out.println("\nBefore swapColumns(0,1):\n" + m1);
        m1.swapColumns(0, 1);
        System.out.println("After swapColumns(0,1):\n" + m1);

// 열 스칼라배 테스트
        m1.scaleColumn(1, Factory.createScalar("2"));
        System.out.println("After scaleColumn(1, 2):\n" + m1);

// 행 덧셈 테스트: row0 += row1 * 2
        m1.addScaledRow(0, 1, Factory.createScalar("2"));
        System.out.println("After addScaledRow(0, 1, 2):\n" + m1);

// 열 덧셈 테스트: col2 += col0 * 3
        m1.addScaledColumn(2, 0, Factory.createScalar("3"));
        System.out.println("After addScaledColumn(2, 0, 3):\n" + m1);

// 부분 행렬 추출 테스트
        Matrix sub = m1.subMatrix(0, 1, 1, 2);
        System.out.println("SubMatrix from (0~1, 1~2):\n" + sub);

        System.out.println("\n===== DONE =====");

    }
}
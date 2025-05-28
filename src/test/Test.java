
package test;
import tensor.*;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        try {
            // 1. 스칼라 생성 (String)
            System.out.println("1. 스칼라 생성 (String)");
            System.out.println("인자값: \"15\"");
            Scalar expected1 = Factory.createScalar("15");
            Scalar sA = Factory.createScalar("15");
            System.out.println("기댓값: " + expected1);
            System.out.println("결과: " + sA);
            System.out.println(sA.equals(expected1) ? "통과\n" : "실패\n");

            // 2. 스칼라 생성 (int, int) 무작위
            Random random = new Random();
            System.out.println("2. 스칼라 생성 (int i, int j) 무작위");
            double randomValue = 1 + random.nextDouble() * 99; // 1~10 사이의 무작위값
            Scalar sB = Factory.createScalar(String.valueOf(randomValue));
            System.out.println("i = 1, j = 100");
            System.out.println(sB);
            double value = Double.parseDouble(sB.getValue());
            boolean isWithinRange = value >= 1 && value <= 100;
            System.out.println("기댓값 범위: [1, 100]");
            System.out.println(isWithinRange ? "통과\n" : "실패\n");

            // 3. 벡터 생성 (n, val)
            System.out.println("3. 벡터 생성 (n, val)");
            System.out.println("인자값: n=5, val=3");
            Vector vA = Factory.createVector(5, Factory.createScalar("3"));
            Vector expected3 = Factory.createVector(new Scalar[] {
                    Factory.createScalar("3"),
                    Factory.createScalar("3"),
                    Factory.createScalar("3"),
                    Factory.createScalar("3"),
                    Factory.createScalar("3")
            });
            System.out.println("기댓값: " + expected3);
            System.out.println("결과: " + vA);
            System.out.println(vA.equals(expected3) ? "통과\n" : "실패\n");

            // 4. 벡터 생성 (i, j, n) 무작위
            System.out.println("4. 벡터 생성 (i, j, n) 무작위");
            System.out.println("인자값: i=1, j=100 사이 무작위, n=4");
            Scalar[] randomVector = new Scalar[4];
            for (int i = 0; i < 4; i++) {
                double val = 1 + random.nextDouble() * 99;
                randomVector[i] = Factory.createScalar(String.valueOf(val));
            }
            Vector vB = Factory.createVector(randomVector);
            System.out.println("결과: " + vB);
            System.out.println("기댓값: 모든 원소 ∈ [1, 100]");
            boolean allInRange = true;
            for (int i = 0; i < vB.size(); i++) {
                double val = Double.parseDouble(vB.getValue(i).getValue());
                if (val < 1 || val > 100    ) {
                    allInRange = false;
                    break;
                }
            }
            System.out.println(allInRange ? "통과\n" : "실패\n");

            // 5. 벡터 생성 (배열)
            System.out.println("5. 벡터 생성 (배열)");
            System.out.println("인자값: [10, 20, 30]");
            Scalar[] arr = {
                    Factory.createScalar("10"),
                    Factory.createScalar("20"),
                    Factory.createScalar("30")
            };
            Vector vC = Factory.createVector(arr);
            Vector expected5 = Factory.createVector(new Scalar[] {
                    Factory.createScalar("10"),
                    Factory.createScalar("20"),
                    Factory.createScalar("30")
            });
            System.out.println("기댓값: " + expected5);
            System.out.println("결과: " + vC);
            System.out.println(vC.equals(expected5) ? "통과\n" : "실패\n");


            // 6. 행렬 생성 (m, n, val)
            System.out.println("6. 행렬 생성 (m, n, val)");
            System.out.println("인자값: m=3, n=4, val=42");
            Matrix mA = Factory.createMatrix(3, 4, Factory.createScalar("42"));
            Matrix expected6 = Factory.createMatrix(3, 4, Factory.createScalar("42"));
            System.out.println("기댓값: " + expected6);
            System.out.println("결과: " + mA);
            System.out.println(mA.equals(expected6) ? "통과\n" : "실패\n");


            // 7. 행렬 생성 (i, j, m, n) 무작위
            System.out.println("7. 행렬 생성 (i, j, m, n) 무작위");
            System.out.println("인자값: i=1, j=100 사이 무작위, m=2, n=3");
            Scalar[][] randomMatrix = new Scalar[2][3];
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    double val = 1 + random.nextDouble() * 99;
                    randomMatrix[i][j] = Factory.createScalar(String.valueOf(val));
                }
            }
            Matrix mB = Factory.createMatrix(randomMatrix);
            System.out.println("생성된 행렬: " + mB);
            boolean allMatrixInRange = true;
            for (int i = 0; i < mB.rowSize(); i++) {
                for (int j = 0; j < mB.colSize(); j++) {
                    double val = Double.parseDouble(mB.getValue(i, j).getValue());
                    if (val < 1 || val > 100) {
                        allMatrixInRange = false;
                        break;
                    }
                }
                if (!allMatrixInRange) break;
            }
            System.out.println("검사 결과: " + (allMatrixInRange ? "모든 원소가 범위 내" : "범위를 벗어난 값 있음"));
            System.out.println(allMatrixInRange ? "통과\n" : "실패\n");



            // 8. 행렬 생성 (csv 파일)
            try {
                // CSV 파일 작성
                java.io.PrintWriter pw = new java.io.PrintWriter("matrix.csv");
                String[] csvLines = {
                        "10, 20, 30",
                        "40, 50, 60",
                        "70, 80, 90"
                };
                for (String line : csvLines) pw.println(line);
                pw.close();

                System.out.println("8. 행렬 생성 (csv 파일)");
                System.out.println("CSV 파일 내용:");
                for (String line : csvLines) System.out.println(line);

                Matrix mCsv = Factory.createMatrix("matrix.csv");
                System.out.println("생성된 행렬:");
                System.out.println(mCsv);

                // 행렬 내용 검증
                boolean csvMatrixValid = true;
                int expectedValue = 10;
                outerLoop:
                for (int i = 0; i < mCsv.rowSize(); i++) {
                    for (int j = 0; j < mCsv.colSize(); j++) {
                        if (!mCsv.getValue(i, j).getValue().equals(String.valueOf(expectedValue))) {
                            csvMatrixValid = false;
                            break outerLoop;
                        }
                        expectedValue += 10;
                    }
                }
                System.out.println(csvMatrixValid ? "통과\n" : "실패\n");

                // 사용한 파일 삭제
                new java.io.File("matrix.csv").delete();
            } catch (Exception e) {
                System.out.println("CSV 파일 처리 중 예외 발생: " + e.getMessage());
            }


            // 9. 행렬 생성 (배열)
            Scalar[][] arr2 = {
                    {Factory.createScalar("11"), Factory.createScalar("12")},
                    {Factory.createScalar("13"), Factory.createScalar("14")},
                    {Factory.createScalar("15"), Factory.createScalar("16")}
            };
            System.out.println("9. 행렬 생성 (배열)");
            System.out.println("입력된 스칼라 배열:");
            for (int i = 0; i < arr2.length; i++) {
                System.out.print("[ ");
                for (int j = 0; j < arr2[i].length; j++) {
                    System.out.print(arr2[i][j].getValue());
                    if (j < arr2[i].length - 1) System.out.print(", ");
                }
                System.out.println(" ]");
            }
            Matrix mC = Factory.createMatrix(arr2);
            Matrix expected9 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("11"), Factory.createScalar("12")},
                    {Factory.createScalar("13"), Factory.createScalar("14")},
                    {Factory.createScalar("15"), Factory.createScalar("16")}
            });
            System.out.println("기댓값: " + expected9);
            System.out.println("결과: " + mC);
            System.out.println(mC.equals(expected9) ? "통과\n" : "실패\n");


            // 10. 단위행렬 생성
            System.out.println("10. 단위행렬 생성");
            System.out.println("입력값: 크기 n = 3");
            Matrix mD = Factory.createIdentityMatrix(3);
            System.out.println("생성된 결과:");
            Matrix expected10 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("1"), Factory.createScalar("0"), Factory.createScalar("0")},
                    {Factory.createScalar("0"), Factory.createScalar("1"), Factory.createScalar("0")},
                    {Factory.createScalar("0"), Factory.createScalar("0"), Factory.createScalar("1")}
            });
            System.out.println("기댓값: " + expected10);
            System.out.println("결과: " + mD);
            System.out.println(mD.equals(expected10) ? "통과\n" : "실패\n");



            // 11. 벡터의 특정 위치 요소 접근 및 설정
            System.out.println("11. 벡터의 특정 위치 요소 접근 및 설정");
            int idx11 = 1;
            vA.setValue(idx11, Factory.createScalar("5"));
            Scalar expected11 = Factory.createScalar("5");
            Scalar result11 = vA.getValue(idx11);
            System.out.println("대상 벡터: " + vA);
            System.out.println("조회할 위치: 인덱스 " + (idx11 + 1));
            System.out.println("기댓값: " + expected11);
            System.out.println("획득된 값: " + result11);



            // 11-1. 행렬의 특정 위치 요소 지정/조회
            System.out.println("11-1. 행렬의 특정 위치 요소 지정/조회");
            Matrix mTest = Factory.createMatrix(2, 2, Factory.createScalar("0"));
            Scalar expected11_1 = Factory.createScalar("7");
            mTest.setValue(0, 1, expected11_1);
            Scalar result11_1 = mTest.getValue(0, 1);
            System.out.println("현재 행렬 상태:\n" + mTest);
            System.out.println("변경 위치: 행 0, 열 1");
            System.out.println("입력 값: " + expected11_1);
            System.out.println("획득된 값: " + result11_1);
            System.out.println(result11_1.equals(expected11_1) ? "통과\n" : "실패\n");

            // 12. 스칼라 값을 지정/조회
            System.out.println("12. 스칼라 값을 지정/조회");
            System.out.println("초기 값: " + sA);
            String newValue = "11.5";
            sA.setValue(newValue);
            System.out.println("새로운 값: " + newValue);
            Scalar expectedAnswer12 = Factory.createScalar(newValue);
            String actualValue = sA.getValue();
            System.out.println("현재 저장된 값: " + actualValue);
            System.out.println("기댓 값: " + expectedAnswer12);
            boolean isCorrect = sA.equals(expectedAnswer12);
            System.out.println(isCorrect ? "통과\n" : "실패\n");


            // 13. (only 벡터, 행렬) 크기 정보를 조회
            System.out.println("13. (only 벡터, 행렬) 크기 정보를 조회");
            vA = Factory.createVector(2, Factory.createScalar("10"));
            mA = Factory.createMatrix(3, 3, Factory.createScalar("24"));
            System.out.println("원본 벡터: " + vA);
            System.out.println("원본 행렬:\n" + mA);
            int expectedVectorSize = 2;
            int expectedRow = 3;
            int expectedCol = 3;
            System.out.println("기댓값: 벡터 크기 = " + expectedVectorSize + ", 행렬 행 = " + expectedRow + ", 열 = " + expectedCol);
            int actualVectorSize = vA.size();
            int actualRow = mA.rowSize();
            int actualCol = mA.colSize();
            System.out.println("결과: 벡터 크기 = " + actualVectorSize + ", 행렬 행 = " + actualRow + ", 열 = " + actualCol);
            boolean isSizeCorrect = (actualVectorSize == expectedVectorSize &&
                    actualRow == expectedRow &&
                    actualCol == expectedCol);
            System.out.println(isSizeCorrect ? "통과\n" : "실패\n");


            // 14. toString() 객체를 출력
            System.out.println("14. toString() 객체를 출력");
            String expectedScalarStr = "11.5";
            String expectedVectorStr = "[10, 10]";
            String expectedMatrixStr = "[24, 24, 24]\n" + "[24, 24, 24]\n" + "[24, 24, 24]";
            String actualScalarStr = sA.toString();
            String actualVectorStr = vA.toString();
            String actualMatrixStr = mA.toString();
            System.out.println("대상 스칼라: " + actualScalarStr);
            System.out.println("대상 벡터: " + actualVectorStr);
            System.out.println("대상 행렬:\n" + actualMatrixStr);
            System.out.println("기댓값 (스칼라): " + expectedScalarStr);
            System.out.println("기댓값 (벡터): " + expectedVectorStr);
            System.out.println("기댓값 (행렬):\n" + expectedMatrixStr);
            boolean scalarMatch = actualScalarStr.equals(expectedScalarStr);
            boolean vectorMatch = actualVectorStr.equals(expectedVectorStr);
            boolean matrixMatch = actualMatrixStr.equals(expectedMatrixStr);
            System.out.println((scalarMatch && vectorMatch && matrixMatch) ? "통과\n" : "실패\n");



            // 15. equals() 객체의 동등성 판단 (스칼라 + 벡터)
            try {
                // 스칼라 비교
                sA = Factory.createScalar("6.13");
                Scalar s2 = Factory.createScalar("6.13");
                System.out.println("15s. equals() 객체의 동등성 판단 (스칼라)");
                System.out.println("비교 대상 1: " + sA);
                System.out.println("비교 대상 2: " + s2);
                System.out.println("기댓값: 같다");
                String scalarResult = sA.equals(s2) ? "같다" : "다르다";
                System.out.println("결과: " + scalarResult);
                System.out.println(scalarResult.equals("같다") ? "통과\n" : "실패\n");

                // 벡터 비교
                vA = Factory.createVector(2, Factory.createScalar("613"));
                Vector v2 = Factory.createVector(new Scalar[] {
                        Factory.createScalar("613"),
                        Factory.createScalar("613")
                });
                System.out.println("15v. equals() 객체의 동등성 판단 (벡터)");
                System.out.println("비교 대상 1: " + vA);
                System.out.println("비교 대상 2: " + v2);
                System.out.println("기댓값: 같다");
                String vectorResult = vA.equals(v2) ? "같다" : "다르다";
                System.out.println("결과: " + vectorResult);
                System.out.println(vectorResult.equals("같다") ? "통과\n" : "실패\n");

            } catch (tensor.TensorException e) {
                System.out.println("예외 발생: " + e.getMessage());
                System.out.println("실패\n");
            }



            // 16. Comparable 스칼라 대소 비교
            sA = Factory.createScalar("10");
            Scalar s3 = Factory.createScalar("10");
            String expectedAnswer16 = "같음";
            System.out.println("16. Comparable 스칼라 대소 비교");
            System.out.println("첫 번째 스칼라: " + sA);
            System.out.println("두 번째 스칼라: " + s3);
            System.out.println("기댓값: " + expectedAnswer16);
            String actualResult;
            if (sA.compareTo(s3) == 0) {
                actualResult = "같음";
                System.out.println("→ 비교 결과: 두 스칼라는 값이 같습니다.");
            }
            else {
                actualResult = sA.compareTo(s3) > 0 ? "큼" : "작음";
                System.out.println("→ 비교 결과: 첫 번째 스칼라가 두 번째보다 " + actualResult + "니다.");
            }
            System.out.println(expectedAnswer16.equals(actualResult) ? "통과\n" : "실패\n");

            // 17. clone() 객체 복제

            Vector expectedVector = Factory.createVector(new Scalar[] {
                    Factory.createScalar("613"),
                    Factory.createScalar("613"),
            });
            Vector clonedVector = vA.clone();
            System.out.println("17v. clone() 객체 복제 (벡터)");
            System.out.println("원본 벡터: " + vA);
            System.out.println("복제된 벡터: " + clonedVector);
            System.out.println("기댓값: " + expectedVector);
            System.out.println(clonedVector.equals(expectedVector) ? "통과\n" : "실패\n");
            sA = Factory.createScalar("11.5");
            Scalar expectedScalar = Factory.createScalar("11.5");
            Scalar clonedScalar = sA.clone();
            System.out.println("17s. clone() 객체 복제 (스칼라)");
            System.out.println("원본 스칼라: " + sA);
            System.out.println("복제된 스칼라: " + clonedScalar);
            System.out.println("기댓값: " + expectedScalar);
            System.out.println(clonedScalar.equals(expectedScalar) ? "통과\n" : "실패\n");

            // 18. 스칼라 덧셈
            System.out.println("18. 스칼라 덧셈");
            Scalar left = Factory.createScalar("4.5");
            Scalar right = Factory.createScalar("2.5");
            Scalar expected = Factory.createScalar("7.0");
            System.out.println("첫 스칼라: " + left);
            System.out.println("뒷 스칼라: " + right);
            left.add(right);
            System.out.println("결과: " + left);
            System.out.println("기댓 값: " + expected);
            System.out.println(left.equals(expected) ? "통과\n" : "실패\n");

            // 19. 스칼라 곱셈
            System.out.println("19. 스칼라 곱셈");
            Scalar left2 = Factory.createScalar("7");
            Scalar right2 = Factory.createScalar("5");
            Scalar expectedProduct = Factory.createScalar("35");
            System.out.println("첫 스칼라: " + left2);
            System.out.println("뒷 스칼라: " + right2);
            left2.multiply(right2);
            System.out.println("곱셈 결과: " + left2);
            System.out.println("기댓 값: " + expectedProduct);
            System.out.println(left2.equals(expectedProduct) ? "통과\n" : "실패\n");


            // 20. 벡터 덧셈
            System.out.println("20. 벡터 덧셈");

            vA = Factory.createVector(4, Factory.createScalar("9.9"));
            Vector vD = Factory.createVector(4, Factory.createScalar("10.2"));
            Vector expectedVector20 = Factory.createVector(4, Factory.createScalar("20.1"));
            System.out.println("입력 벡터 1: " + vA);
            System.out.println("입력 벡터 2: " + vD);
            System.out.println("기대 결과: " + expectedVector20);
            try {
                vA.add(vD);
                System.out.println("실제 결과: " + vA);
                System.out.println(vA.equals(expectedVector20) ? "통과\n" : "실패\n");
            } catch (SizeMismatchException e) {
                System.out.println("예외 발생: " + e.getMessage());
            }

            // 21. 벡터 스칼라 곱셈
            System.out.println("21. 벡터 스칼라 곱셈");
            vA = Factory.createVector(new Scalar[]{
                    Factory.createScalar("10"),
                    Factory.createScalar("11"),
                    Factory.createScalar("12"),
                    Factory.createScalar("13")
            });
            Scalar scale = Factory.createScalar("6");
            Vector expectedVector21 = Factory.createVector(new Scalar[]{
                    Factory.createScalar("60"),
                    Factory.createScalar("66"),
                    Factory.createScalar("72"),
                    Factory.createScalar("78")
            });
            System.out.println("입력 벡터: " + vA);
            System.out.println("곱할 스칼라 값: " + scale);
            System.out.println("기대 결과 벡터: " + expectedVector21);
            vA.multiply(scale);
            System.out.println("실제 계산 결과: " + vA);
            System.out.println(vA.equals(expectedVector21) ? "통과\n" : "실패\n");


            // 22. 행렬 덧셈 테스트
            System.out.println("22. 두 행렬의 합 연산 검증");

            Matrix mF = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("1"), Factory.createScalar("2"), Factory.createScalar("3")},
                    {Factory.createScalar("4"), Factory.createScalar("5"), Factory.createScalar("6")}
            });
            Matrix matadd = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("11"), Factory.createScalar("12"), Factory.createScalar("13")},
                    {Factory.createScalar("14"), Factory.createScalar("15"), Factory.createScalar("16")}
            });
            Matrix expectedSum = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("12"), Factory.createScalar("14"), Factory.createScalar("16")},
                    {Factory.createScalar("18"), Factory.createScalar("20"), Factory.createScalar("22")}
            });

            System.out.println("첫 번째 행렬:\n" + mF);
            System.out.println("두 번째 행렬:\n" + matadd);
            System.out.println("기대 결과 행렬:\n" + expectedSum);

            try {
                Matrix actualSum = Tensors.add(mF, matadd);
                System.out.println("계산된 결과:\n" + actualSum);
                System.out.println(actualSum.equals(expectedSum) ? "통과\n" : "실패\n");
            } catch (SizeMismatchException e) {
                System.out.println("예외 발생:" + e.getMessage());
            }


            // 23. 행렬 곱 연산 확인
            System.out.println("23. 두 행렬의 곱셈 결과 확인");
            Matrix mE = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("4"), Factory.createScalar("7")},
                    {Factory.createScalar("5"), Factory.createScalar("8")},
                    {Factory.createScalar("6"), Factory.createScalar("9")}
            });
            Matrix multiplicand = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("11"), Factory.createScalar("11"), Factory.createScalar("11")},
                    {Factory.createScalar("11"), Factory.createScalar("11"), Factory.createScalar("11")}
            });
            Matrix expectedProduct23 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("165"), Factory.createScalar("264")},
                    {Factory.createScalar("165"), Factory.createScalar("264")}
            });
            System.out.println("피연산 행렬 (왼쪽):\n" + multiplicand);
            System.out.println("곱할 행렬 (오른쪽):\n" + mE);
            System.out.println("기대 곱셈 결과:\n" + expectedProduct23);
            // 실제 곱셈 수행
            multiplicand.multiply(mE);
            System.out.println("계산된 결과:\n" + multiplicand);
            System.out.println(multiplicand.equals(expectedProduct23) ? "통과\n" : "실패\n");


            // 24. 스칼라 덧셈
            System.out.println("24. 두 스칼라 값의 정적 덧셈 연산");
            Scalar leftOperand24 = Factory.createScalar("11");
            Scalar rightOperand24 = Factory.createScalar("6.1303");
            Scalar expectedSum24 = Factory.createScalar("17.1303");

            System.out.println("첫 스칼라: " + leftOperand24);
            System.out.println("뒷 스칼라: " + rightOperand24);
            System.out.println("기대 결과: " + expectedSum24);

            Scalar actualSum24 = Tensors.add(leftOperand24, rightOperand24);

            System.out.println("계산 결과: " + actualSum24);
            System.out.println(actualSum24.equals(expectedSum24) ? "통과\n" : "실패\n");


            // 25. 스칼라 곱셈
            System.out.println("25. 스칼라 곱셈 ");
            Scalar multiplicand25 = Factory.createScalar("11");
            Scalar multiplier25 = Factory.createScalar("6.1303");
            Scalar expectedProduct25 = Factory.createScalar("67.4333");

            System.out.println("첫 스칼라: " + multiplicand25);
            System.out.println("뒷 스칼라: " + multiplier25);
            System.out.println("기대 결과: " + expectedProduct25);

            Scalar actualProduct25 = Tensors.multiply(multiplicand25, multiplier25);

            System.out.println("계산 결과: " + actualProduct25);
            System.out.println(actualProduct25.equals(expectedProduct25) ? "통과\n" : "실패\n");


            // 26. 벡터의 덧셈
            System.out.println("26. 벡터의 덧셈");
            Vector leftVector26 = vA;
            Vector rightVector26 = vD;
            System.out.println("첫 벡터: " + leftVector26);
            System.out.println("뒷 벡터: " + rightVector26);
            Vector expectedSum26 = Factory.createVector(new Scalar[]{
                    Factory.createScalar("70.2"),
                    Factory.createScalar("76.2"),
                    Factory.createScalar("82.2"),
                    Factory.createScalar("88.2")
            });
            System.out.println("기대 결과 벡터: " + expectedSum26);
            Vector actualSum26 = Tensors.add(leftVector26, rightVector26);
            System.out.println("연산 결과 벡터: " + actualSum26);
            System.out.println(actualSum26.equals(expectedSum26) ? "통과\n" : "실패\n");


            // 27. 벡터 * 스칼라
            System.out.println("27. 벡터 * 스칼라");
            Vector originalVector27 = vA;
            Scalar scalarFactor27 = sA;
            System.out.println("대상 벡터: " + originalVector27);
            System.out.println("곱할 스칼라 값: " + scalarFactor27);
            Vector expectedProduct27 = Factory.createVector(new Scalar[]{
                    Factory.createScalar("690"),
                    Factory.createScalar("759"),
                    Factory.createScalar("828"),
                    Factory.createScalar("897")
            });
            System.out.println("기대 결과: " + expectedProduct27);
            Vector actualProduct27 = Tensors.multiply(originalVector27, scalarFactor27);
            System.out.println("실제 결과: " + actualProduct27);
            System.out.println(actualProduct27.equals(expectedProduct27) ? "통과\n" : "실패\n");


            // 28. 행렬 덧셈
            System.out.println("28. 행렬 덧셈");
            Matrix leftMatrix28 = Factory.createMatrix(2, 3, Factory.createScalar("11"));
            Matrix rightMatrix28 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("1"), Factory.createScalar("3"), Factory.createScalar("5")},
                    {Factory.createScalar("2"), Factory.createScalar("4"), Factory.createScalar("6")}
            });
            Matrix expectedResult28 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("12"), Factory.createScalar("14"), Factory.createScalar("16")},
                    {Factory.createScalar("13"), Factory.createScalar("15"), Factory.createScalar("17")}
            });
            System.out.println("피연산자 1:\n" + leftMatrix28);
            System.out.println("피연산자 2:\n" + rightMatrix28);
            Matrix actualResult28 = Tensors.add(leftMatrix28, rightMatrix28);
            System.out.println("기대 결과:\n" + expectedResult28);
            System.out.println("계산 결과:\n" + actualResult28);
            System.out.println(actualResult28.equals(expectedResult28) ? "통과\n" : "실패\n");


            // 29. 행렬 곱셈
            System.out.println("29. 행렬 곱셈");
            Matrix leftMatrix29 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("11"), Factory.createScalar("11"), Factory.createScalar("11")},
                    {Factory.createScalar("11"), Factory.createScalar("11"), Factory.createScalar("11")}
            });
            Matrix rightMatrix29 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("1"), Factory.createScalar("4")},
                    {Factory.createScalar("2"), Factory.createScalar("5")},
                    {Factory.createScalar("3"), Factory.createScalar("6")}
            });
            Matrix expectedProduct29 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("66"), Factory.createScalar("165")},
                    {Factory.createScalar("66"), Factory.createScalar("165")}
            });
            System.out.println("피연산자 행렬 A:\n" + leftMatrix29);
            System.out.println("피연산자 행렬 B:\n" + rightMatrix29);
            Matrix actualResult29 = Tensors.multiply(leftMatrix29, rightMatrix29);
            System.out.println("기대 곱셈 결과:\n" + expectedProduct29);
            System.out.println("계산된 곱셈 결과:\n" + actualResult29);
            System.out.println(actualResult29.equals(expectedProduct29) ? "통과\n" : "실패\n");


            // 30. toVerticalMatrix 벡터
            System.out.println("30. toVerticalMatrix 벡터");

            Vector vector30 = Factory.createVector(new Scalar[] {
                    Factory.createScalar("6.13"),
                    Factory.createScalar("3.61"),
                    Factory.createScalar("1.63")
            });
            Matrix verticalMatrix30 = vector30.toVerticalMatrix();

            Matrix expectedVertical30 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("6.13")},
                    {Factory.createScalar("3.61")},
                    {Factory.createScalar("1.63")}
            });
            System.out.println("대상 벡터: " + vector30);
            System.out.println("변환 결과 (세로):");
            System.out.println(verticalMatrix30);
            System.out.println("기댓값:");
            System.out.println(expectedVertical30);
            System.out.println(verticalMatrix30.equals(expectedVertical30) ? "통과\n" : "실패\n");


            // 31. toHorizontalMatrix 벡터
            System.out.println("31. toHorizontalMatrix 벡터");
            Vector vector31 = Factory.createVector(new Scalar[] {
                    Factory.createScalar("6.13"),
                    Factory.createScalar("3.61"),
                    Factory.createScalar("1.63")
            });
            Matrix horizontalMatrix31 = vector31.toHorizontalMatrix();
            Matrix expectedHorizontal31 = Factory.createMatrix(new Scalar[][] {
                    {
                            Factory.createScalar("6.13"),
                            Factory.createScalar("3.61"),
                            Factory.createScalar("1.63")
                    }
            });
            System.out.println("대상 벡터: " + vector31);
            System.out.println("변환 결과 (가로):");
            System.out.println(horizontalMatrix31);
            System.out.println("기댓값:");
            System.out.println(expectedHorizontal31);
            System.out.println(horizontalMatrix31.equals(expectedHorizontal31) ? "통과\n" : "실패\n");

            // 32. attachHMatrix
            System.out.println("32. attachHMatrix");
            Matrix leftMatrix32 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("6"), Factory.createScalar("4")},
                    {Factory.createScalar("5"), Factory.createScalar("3")}
            });
            Matrix rightMatrix32 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("3"), Factory.createScalar("5")},
                    {Factory.createScalar("4"), Factory.createScalar("6")}
            });
            Matrix combined32 = Tensors.attachHMatrix(leftMatrix32, rightMatrix32);
            Matrix expectedCombined32 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("6"), Factory.createScalar("4"), Factory.createScalar("3"), Factory.createScalar("5")},
                    {Factory.createScalar("5"), Factory.createScalar("3"), Factory.createScalar("4"), Factory.createScalar("6")}
            });
            System.out.println("행렬 1:\n" + leftMatrix32);
            System.out.println("행렬 2:\n" + rightMatrix32);
            System.out.println("결과:\n" + combined32);
            System.out.println("기댓값:\n" + expectedCombined32);
            System.out.println(combined32.equals(expectedCombined32) ? "통과\n" : "실패\n");


            // 33. attachVMatrix
            System.out.println("33. attachVMatrix");
            Matrix upperMatrix33 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("6"), Factory.createScalar("6"), Factory.createScalar("6")}
            });
            Matrix lowerMatrix33 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("1"), Factory.createScalar("1"), Factory.createScalar("1")},
                    {Factory.createScalar("3"), Factory.createScalar("3"), Factory.createScalar("3")}
            });
            Matrix mergedMatrix33 = Tensors.attachVMatrix(upperMatrix33, lowerMatrix33);
            Matrix expectedMerged33 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("6"), Factory.createScalar("6"), Factory.createScalar("6")},
                    {Factory.createScalar("1"), Factory.createScalar("1"), Factory.createScalar("1")},
                    {Factory.createScalar("3"), Factory.createScalar("3"), Factory.createScalar("3")}
            });
            System.out.println("상단 행렬:\n" + upperMatrix33);
            System.out.println("하단 행렬:\n" + lowerMatrix33);
            System.out.println("결합된 결과 (세로 병합):\n" + mergedMatrix33);
            System.out.println("기댓값:\n" + expectedMerged33);
            System.out.println(mergedMatrix33.equals(expectedMerged33) ? "통과\n" : "실패\n");



            // 34. getRowVector
            System.out.println("34. getRowVector");
            Matrix matrix34 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("3"), Factory.createScalar("6")},
                    {Factory.createScalar("1"), Factory.createScalar("3")}
            });
            int targetRowIndex34 = 0;
            Vector expectedRowVector34 = Factory.createVector(new Scalar[]{
                    Factory.createScalar("3"), Factory.createScalar("6")
            });
            System.out.println("대상 행렬:\n" + matrix34);
            System.out.println("추출할 행 번호: " + targetRowIndex34);
            System.out.println("기댓값: " + expectedRowVector34);
            Vector actualRowVector34 = matrix34.getRowVector(targetRowIndex34);
            System.out.println("추출 결과: " + actualRowVector34);
            System.out.println(actualRowVector34.equals(expectedRowVector34) ? "통과\n" : "실패\n");


            // 35. getColVector
            System.out.println("35. getColVector ");
            Matrix matrix35 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("3"), Factory.createScalar("6")},
                    {Factory.createScalar("1"), Factory.createScalar("3")}
            });
            int targetColIndex35 = 0;
            Vector expectedColVector35 = Factory.createVector(new Scalar[]{
                    Factory.createScalar("3"), Factory.createScalar("1")
            });
            System.out.println("대상 행렬:\n" + matrix35);
            System.out.println("추출할 열 번호: " + targetColIndex35);
            System.out.println("기댓값: " + expectedColVector35);
            Vector actualColVector35 = matrix35.getColVector(targetColIndex35);
            System.out.println("추출 결과: " + actualColVector35);
            System.out.println(actualColVector35.equals(expectedColVector35) ? "통과\n" : "실패\n");


            // 36. extractSubMatrix
            System.out.println("36. extractSubMatrix");
            Matrix matrix36 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("3"), Factory.createScalar("6")},
                    {Factory.createScalar("1"), Factory.createScalar("3")}
            });
            int rowStart = 0, rowEnd = 1;
            int colStart = 0, colEnd = 1;
            Matrix expectedSubMatrix36 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("3")}
            });
            System.out.println("원본 행렬:\n" + matrix36);
            System.out.println("추출 범위: 행(" + rowStart + " ~ " + (rowEnd - 1) + "), 열(" + colStart + " ~ " + (colEnd - 1) + ")");
            System.out.println("기댓값:\n" + expectedSubMatrix36);
            Matrix actualSubMatrix36 = matrix36.extractSubMatrix(rowStart, rowEnd, colStart, colEnd);
            System.out.println("실제 추출 결과:\n" + actualSubMatrix36);
            System.out.println(actualSubMatrix36.equals(expectedSubMatrix36) ? "통과\n" : "실패\n");


            // 37. minorSubMatrix
            System.out.println("37. minorSubMatrix");
            Matrix matrix37 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("3"), Factory.createScalar("6")},
                    {Factory.createScalar("1"), Factory.createScalar("3")}
            });
            int removeRow = 0;
            int removeCol = 0;
            Matrix expectedMinor37 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("3")}
            });
            System.out.println("원본 행렬:\n" + matrix37);
            System.out.println("제외할 위치: 행 " + removeRow + ", 열 " + removeCol);
            System.out.println("기댓값:\n" + expectedMinor37);
            Matrix actualMinor37 = matrix37.minorSubMatrix(removeRow, removeCol);
            System.out.println("실제 추출 결과:\n" + actualMinor37);
            System.out.println(actualMinor37.equals(expectedMinor37) ? "통과\n" : "실패\n");


            // 38. transposeMatrix
            System.out.println("38. transposeMatrix");
            Matrix originalMatrix38 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("3"), Factory.createScalar("6")},
                    {Factory.createScalar("1"), Factory.createScalar("3")}
            });
            Matrix expectedTranspose38 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("3"), Factory.createScalar("1")},
                    {Factory.createScalar("6"), Factory.createScalar("3")}
            });
            System.out.println("원본 행렬:");
            System.out.println(originalMatrix38);
            System.out.println("기댓값:");
            System.out.println(expectedTranspose38);
            Matrix actualTranspose38 = originalMatrix38.transposeMatrix();
            System.out.println("전치 결과:");
            System.out.println(actualTranspose38);
            System.out.println(actualTranspose38.equals(expectedTranspose38) ? "통과\n" : "실패\n");


            // 39. 대각합
            System.out.println("39. 대각합");
            Matrix traceMatrix = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("33"), Factory.createScalar("66")},
                    {Factory.createScalar("11"), Factory.createScalar("33")}
            });
            System.out.println("대상 행렬:");
            System.out.println(traceMatrix);
            Scalar diagonalSum39 = traceMatrix.trace();
            System.out.println("계산된 대각합: " + diagonalSum39);
            Scalar expectedTrace39 = Factory.createScalar("66");
            System.out.println("기댓값: " + expectedTrace39);
            System.out.println(diagonalSum39.equals(expectedTrace39) ? "통과\n" : "실패\n");


            // 40. 정사각 행렬 판별
            System.out.println("40. 정사각 행렬 판별");
            Matrix squareCandidate40 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("13"), Factory.createScalar("61")},
                    {Factory.createScalar("11"), Factory.createScalar("16")}
            });
            System.out.println("검사 대상 행렬:");
            System.out.println(squareCandidate40);
            boolean expectedAnswer40 = true;
            System.out.println("기댓값: " + expectedAnswer40);
            boolean result40 = squareCandidate40.isSquare();
            System.out.println("정사각 행렬 여부: " + result40);
            System.out.println(result40 == expectedAnswer40 ? "통과\n" : "실패\n");



            // 41. 상삼각 행렬인지 확인
            System.out.println("41. 상삼각 행렬 판별");
            Matrix upperTriangularCandidate41 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("11"), Factory.createScalar("11"), Factory.createScalar("11")},
                    {Factory.createScalar("0"), Factory.createScalar("1"), Factory.createScalar("11")},
                    {Factory.createScalar("0"), Factory.createScalar("0"), Factory.createScalar("11")}
            });
            System.out.println("검사 대상 행렬:");
            System.out.println(upperTriangularCandidate41);
            boolean expectedAnswer41 = true;
            System.out.println("기댓값: " + expectedAnswer41);
            boolean result41 = upperTriangularCandidate41.isUpperTriangular();
            System.out.println("상삼각행렬 여부: " + result41);
            System.out.println(result41 == expectedAnswer41 ? "통과\n" : "실패\n");


            // 42. 하삼각 행렬 판별
            System.out.println("42. 하삼각 행렬 판별");
            Matrix lowerTriangularCandidate42 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("22"), Factory.createScalar("0"), Factory.createScalar("0")},
                    {Factory.createScalar("22"), Factory.createScalar("2"), Factory.createScalar("0")},
                    {Factory.createScalar("22"), Factory.createScalar("22"), Factory.createScalar("22")}
            });
            System.out.println("검사 대상 행렬:");
            boolean expectedAnswer42 = true;
            System.out.println(lowerTriangularCandidate42);
            System.out.println("기댓값: " + expectedAnswer42);
            boolean result42 = lowerTriangularCandidate42.isLowerTriangular();
            System.out.println("하삼각행렬 여부: " + result42);
            System.out.println(result42 == expectedAnswer42 ? "통과\n" : "실패\n");


            // 43.  단위행렬 판별
            System.out.println("43.  단위행렬 판별");
            Matrix identityCandidate43 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("1"), Factory.createScalar("0"), Factory.createScalar("0")},
                    {Factory.createScalar("0"), Factory.createScalar("1"), Factory.createScalar("0")},
                    {Factory.createScalar("0"), Factory.createScalar("0"), Factory.createScalar("1")}
            });
            System.out.println("검사 대상 행렬:");
            System.out.println(identityCandidate43);
            boolean expectedAnswer43 = true;
            System.out.println("기댓값: " + expectedAnswer43);
            boolean result43 = identityCandidate43.isIdentity();
            System.out.println("단위행렬 여부: " + result43);
            System.out.println(result43 == expectedAnswer43 ? "통과\n" : "실패\n");


            // 44. 영행렬 판별
            System.out.println("44. 영행렬 판별");
            Matrix zeroCandidate44 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("0"), Factory.createScalar("0")},
                    {Factory.createScalar("0"), Factory.createScalar("0")}
            });
            System.out.println("검사 대상 행렬:");
            System.out.println(zeroCandidate44);
            boolean expectedAnswer44 = true;
            System.out.println("기댓값: " + expectedAnswer44);
            boolean result44 = zeroCandidate44.isZero();
            System.out.println("영행렬 여부: " + result44);
            System.out.println(result44 == expectedAnswer44 ? "통과\n" : "실패\n");



            // 45. rowSwap() - 행 교환
            System.out.println("45. rowSwap() - 행 교환");
            Matrix matrixBeforeSwap45 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("6"), Factory.createScalar("1")},
                    {Factory.createScalar("33"), Factory.createScalar("3")}
            });
            Matrix expectedSwapped45 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("33"), Factory.createScalar("3")},
                    {Factory.createScalar("6"), Factory.createScalar("1")}
            });
            System.out.println("전 행렬:");
            System.out.println(matrixBeforeSwap45);
            System.out.println("교환할 행 번호: 0, 1");
            matrixBeforeSwap45.rowSwap(0, 1);
            System.out.println("후 행렬:");
            System.out.println(matrixBeforeSwap45);
            System.out.println("기댓값:\n" + expectedSwapped45);
            System.out.println(matrixBeforeSwap45.equals(expectedSwapped45) ? "통과\n" : "실패\n");



            // 46. 열 교환
            System.out.println("46. 열 교환");
            Matrix matrixBeforeSwap46 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("11"), Factory.createScalar("16")},
                    {Factory.createScalar("13"), Factory.createScalar("12")}
            });
            Matrix expectedSwapped46 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("16"), Factory.createScalar("11")},
                    {Factory.createScalar("12"), Factory.createScalar("13")}
            });
            System.out.println("변경 전 행렬:\n" + matrixBeforeSwap46);
            System.out.println("교환 대상 열: 0열 <-> 1열");
            System.out.println("기댓값:\n" + expectedSwapped46);
            matrixBeforeSwap46.colSwap(0, 1);
            System.out.println("실제 결과:\n" + matrixBeforeSwap46);
            System.out.println(matrixBeforeSwap46.equals(expectedSwapped46) ? "통과\n" : "실패\n");


            // 47. 행 스칼라 곱
            System.out.println("47. 행 스칼라 곱");
            Matrix matrixBeforeRowScale47 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("5"), Factory.createScalar("4")},
                    {Factory.createScalar("4"), Factory.createScalar("3")}
            });
            Scalar scaleFactor47 = Factory.createScalar("5");
            Matrix expectedRowScaled47 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("25"), Factory.createScalar("20")},
                    {Factory.createScalar("4"), Factory.createScalar("3")}
            });
            System.out.println("원본 행렬:\n" + matrixBeforeRowScale47);
            System.out.println("적용 대상 행: 0행, 곱할 값: " + scaleFactor47);
            System.out.println("기댓값:\n" + expectedRowScaled47);
            matrixBeforeRowScale47.rowMultiply(0, scaleFactor47);
            System.out.println("실제 결과:\n" + matrixBeforeRowScale47);
            System.out.println(matrixBeforeRowScale47.equals(expectedRowScaled47) ? "통과\n" : "실패\n");


            // 48. 열 스칼라 곱
            System.out.println("48. 열 스칼라 곱");
            Matrix matrix48 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("21"), Factory.createScalar("13")},
                    {Factory.createScalar("6"), Factory.createScalar("44")}
            });
            Matrix expectedAnswer48 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("21"), Factory.createScalar("26")},
                    {Factory.createScalar("6"), Factory.createScalar("88")}
            });
            System.out.println("원본 행렬:\n" + matrix48);
            System.out.println("곱할 열: 1열, 스칼라: 2");
            System.out.println("기댓값:\n" + expectedAnswer48);
            matrix48.colMultiply(1, Factory.createScalar("2"));
            System.out.println("실제 결과:\n" + matrix48);
            System.out.println(matrix48.equals(expectedAnswer48) ? "통과\n" : "실패\n");



            // 49. 행에 다른 행의 상수배 더하기
            System.out.println("49. 행에 다른 행의 상수배 더하기");
            Matrix matrix49 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("10"), Factory.createScalar("13")},
                    {Factory.createScalar("7"), Factory.createScalar("1")}
            });
            Matrix expectedAnswer49 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("24"), Factory.createScalar("15")},
                    {Factory.createScalar("7"), Factory.createScalar("1")}
            });
            System.out.println("원본 행렬:\n" + matrix49);
            System.out.println("대상 행: 0행, 더할 행: 1행, 스칼라: 2");
            System.out.println("기댓값:\n" + expectedAnswer49);
            matrix49.rowAddOtherRow(0, 1, Factory.createScalar("2"));
            System.out.println("실제 결과:\n" + matrix49);
            System.out.println(matrix49.equals(expectedAnswer49) ? "통과\n" : "실패\n");


            // 50. 열에 다른 열의 상수배 더하기
            System.out.println("50. 열에 다른 열의 상수배 더하기");
            Matrix matrix50 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("13"), Factory.createScalar("3")},
                    {Factory.createScalar("10"), Factory.createScalar("2")}
            });
            Matrix expectedAnswer50 = Factory.createMatrix(new Scalar[][]{
                    {Factory.createScalar("25"), Factory.createScalar("3")},
                    {Factory.createScalar("18"), Factory.createScalar("2")}
            });
            System.out.println("원본 행렬:\n" + matrix50);
            System.out.println("대상 열: 0열, 더할 열: 1열, 스칼라: 24");
            System.out.println("기댓값:\n" + expectedAnswer50);
            matrix50.colAddOtherCol(0, 1, Factory.createScalar("4"));
            System.out.println("실제 결과:\n" + matrix50);
            System.out.println(matrix50.equals(expectedAnswer50) ? "통과\n" : "실패\n");


            // 51. RREF 변환
            System.out.println("51. RREF 변환");
            Matrix matrix51 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("1"), Factory.createScalar("2"), Factory.createScalar("-1"), Factory.createScalar("-4")},
                    {Factory.createScalar("2"), Factory.createScalar("3"), Factory.createScalar("-1"), Factory.createScalar("-11")},
                    {Factory.createScalar("-2"), Factory.createScalar("0"), Factory.createScalar("-3"), Factory.createScalar("22")}
            });
            System.out.println("원본 행렬:\n" + matrix51);
            Matrix rrefResult51 = matrix51.getRREF();
            Matrix expectedRREF51 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("1"), Factory.createScalar("0"), Factory.createScalar("0"), Factory.createScalar("-8")},
                    {Factory.createScalar("0"), Factory.createScalar("1"), Factory.createScalar("0"), Factory.createScalar("1")},
                    {Factory.createScalar("0"), Factory.createScalar("0"), Factory.createScalar("1"), Factory.createScalar("-2")}
            });
            System.out.println("RREF 변환 결과:\n" + rrefResult51);
            System.out.println("기댓값:\n" + expectedRREF51);
            System.out.println(rrefResult51.equals(expectedRREF51) ? "통과\n" : "실패\n");



            // 52. RREF 판별
            System.out.println("52. RREF 판별");

            Matrix matrix52 = Factory.createMatrix(new Scalar[][] {
                    {Factory.createScalar("1"), Factory.createScalar("2"), Factory.createScalar("-1"), Factory.createScalar("-4")},
                    {Factory.createScalar("2"), Factory.createScalar("3"), Factory.createScalar("-1"), Factory.createScalar("-11")},
                    {Factory.createScalar("-2"), Factory.createScalar("0"), Factory.createScalar("-3"), Factory.createScalar("22")}
            });
            boolean actualIsRref52 = matrix52.isRREF();
            boolean expectedIsRref52 = false;
            System.out.println("검사 대상 행렬:\n" + matrix52);
            System.out.println("기댓값: " + (expectedIsRref52 ? "RREF임" : "RREF가 아님"));
            System.out.println("RREF 여부: " + (actualIsRref52 ? "RREF임" : "RREF가 아님"));
            System.out.println(actualIsRref52 == expectedIsRref52 ? "통과\n" : "실패\n");

            // 53. 행렬식 구하기
            try {
                Matrix mDet = Factory.createMatrix(new Scalar[][]{
                        {Factory.createScalar("4"), Factory.createScalar("3")},
                        {Factory.createScalar("3"), Factory.createScalar("2")}
                });
                Scalar expectedAnswer53 = Factory.createScalar("-1");
                System.out.println("53. 행렬식 구하기");
                System.out.println("원본 행렬:\n" + mDet);
                System.out.println("기댓값: " + expectedAnswer53);
                Scalar result53 = mDet.getDeterminant();
                System.out.println("행렬식: " + result53);
                System.out.println(result53.equals(expectedAnswer53) ? "통과\n" : "실패\n");
            } catch (NotSquareMatrixException e) {
                System.out.println("행렬식 계산 예외 발생: " + e.getMessage());
            }

            // 54. 역행렬 구하기
            try {
                Matrix mInvert = Factory.createMatrix(new Scalar[][]{
                        {Factory.createScalar("8"), Factory.createScalar("-1")},
                        {Factory.createScalar("-6"), Factory.createScalar("1")}
                });
                Matrix expectedAnswer54 = Factory.createMatrix(new Scalar[][]{
                        {Factory.createScalar("0.5"), Factory.createScalar("0.5")},
                        {Factory.createScalar("3"), Factory.createScalar("4")}
                });
                System.out.println("54. 역행렬 구하기");
                System.out.println("원본 행렬:\n" + mInvert);
                System.out.println("기댓값:\n" + expectedAnswer54);
                Matrix result54 = mInvert.getInverseMatrix();
                System.out.println("역행렬:\n" + result54);
                System.out.println(result54.equals(expectedAnswer54) ? "통과\n" : "실패\n");
            } catch (NotSquareMatrixException e) {
                System.out.println("역행렬 계산 예외 발생: " + e.getMessage());
            }
        } catch (TensorException e) {
            System.out.println("텐서 연산 중 예외 발생: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("예상치 못한 예외 발생: " + e.getMessage());
        }
        System.out.println("End");
    }
}

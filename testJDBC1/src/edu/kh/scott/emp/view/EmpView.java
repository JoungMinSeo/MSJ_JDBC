package edu.kh.scott.emp.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.scott.emp.model.service.EmpService;
import edu.kh.scott.emp.model.vo.Emp;

// View 
// - 클라이언트의 요청을 입력 받고, 
//   요청에 대한 결과(응답)를 출력하는 클래스
public class EmpView {

	private Scanner sc = new Scanner(System.in);
	private EmpService service = new EmpService();

	public void displayMenu() {

		int sel = 0;

		do {

			try {

				System.out.println("==================================");
				System.out.println("[Main Menu]");
				System.out.println("1. 전체 사원 정보 조회");
				System.out.println("2. 사번으로 사원 정보 조회");
				System.out.println("3. 새로운 사원 정보 삽입");
				System.out.println("4. 사번으로 사원 정보 수정");
				// 사번, 직책, 급여, 커미션 입력 받아
				// 사번이 일치하는 사원의 직책, 급여, 커미션을 수정

				System.out.println("5. 사번으로 사원 정보 삭제");
				// 사번을 입력 받아 일치하는 사번을 가진 사원 정보 삭제

				System.out.println("6. 사번, 이름이 모두 일치하는 사원 정보 조회");
				System.out.println("0. 프로그램 종료");
				System.out.println("==================================");

				System.out.print("메뉴 선택 >> ");
				sel = sc.nextInt();
				sc.nextLine();
				System.out.println();

				switch (sel) {
				case 1:
					selectAll();
					break;
				case 2:
					selectOne();
					break;
				case 3:
					insertEmp();
					break;
				case 4:
					updateEmp();
					break;
				case 5:
					deleteEmp();
					break;
				case 6:
					selectEmp();
					break;
				case 0:
					System.out.println("프로그램 종료.");
					break;
				default:
					System.out.println("잘못 입력하셨습니다.");
				}

			} catch (InputMismatchException e) {
				System.out.println("정수만 입력해주세요.");
				sel = -1;
				sc.nextLine();
			}

		} while (sel != 0);
	}

	// 1. 전체 사원 정보 조회 View
	private void selectAll() {

		System.out.println("[전체 사원 정보 조회]");

		try {
			// 1-1. DB에서 EMP 테이블의 모든 내용을 반환 받아 empList에 저장
			// view -> service -> dao 순서로 요청 전달

			List<Emp> empList = service.selectAll();

			// 1-15. 조회 결과인 empList가 비어있을 경우 == 조회 결과 없음
			if (empList.isEmpty()) {
				System.out.println("조회 결과가 없습니다.");
			}

			// 1-16. 조회 결과인 empList가 비어있지 않은 경우 == 조회 결과 있음
			else {

				for (int i = 0; i < empList.size(); i++) {
					System.out.println((i + 1) + " > " + empList.get(i));
					// 1 > Emp[empNo=7356 ...
					// 2 > Emp[empNo=1234 ...
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 2. 사번으로 사원 정보 조회
	private void selectOne() {
		System.out.println("[사번으로 사원 정보 조회]");

		try {
			System.out.print("사번 입력 : ");
			int input = sc.nextInt();
			sc.nextLine();

			// 2-1. 입력 받은 사번을 EmpService의 selectOne() 메소드로 전달한 후
			// 조회 결과를 반환 받아 Emp e 변수에 저장
			Emp e = service.selectOne(input);

			// 2-13. 조회 결과 있을때는 toString() 출력,
			// 없을 때는 "조회 결과 없음" 출력
			if (e != null) {
				System.out.println(e);
			} else {
				System.out.println("조회 결과 없음");
			}

		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	// 6. 사번, 이름이 모두 일치하는 사원 정보 조회
	private void selectEmp() {
		System.out.println("[사번, 이름 일치 사원 정보 조회]");

		try {
			System.out.print("사번 입력 : ");
			int empNo = sc.nextInt();

			System.out.print("이름 입력 : ");
			String eName = sc.next();

			Emp emp = service.selectEmp(empNo, eName);

			if (emp != null) {
				System.out.println(emp);
			} else {
				System.out.println("조회 결과 없음.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 3. 사원 정보 삽입 View
	private void insertEmp() {

		try {
			System.out.println("[사원 정보 삽입]");

			System.out.print("이름 : ");
			String eName = sc.next();

			System.out.print("직책 : ");
			String job = sc.next();

			System.out.print("직속 상사 번호 : ");
			int mgr = sc.nextInt();

			System.out.print("급여 : ");
			int sal = sc.nextInt();

			System.out.print("커미션 : ");
			int comm = sc.nextInt();

			System.out.print("부서번호 : ");
			int deptNo = sc.nextInt();
			sc.nextLine(); // 버퍼에 남은 개행문자 제거

			// 3-1. 입력 받은 값을 한 번에 매개변수로 전달할 수 있도록
			// Emp VO 객체를 만들어 객체 필드에 값 저장
			Emp newEmp = new Emp(eName, job, mgr, sal, comm, deptNo);

			// 3-2. 입력 받은 값을 한 번에 저장한 객체 newEmp를
			// DB에 삽입할 수 있도록 하는 EmpService의 insertEmp(newEmp) 메소드 호출
			// + 결과 값 반환
			int result = service.insertEmp(newEmp);

			// 3-12. 삽입 결과인 result에 따라 알맞은 결과화면 출력
			if (result > 0) {
				System.out.println("사원 정보 삽입 성공!");
			} else {
				System.out.println("사원 정보 삽입 실패...");
			}

			// TEST 시 주의 사항
			// 1. 관리자 번호는 EMP 테이블 EMPNO 에 있는 값만 사용
			// 2. 직책은 영어 9글자까지만 가능
			// 3. 부서번호는 10, 20, 30, 40만 가능

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateEmp() {
		try {
			System.out.println("[사번으로 사원 정보 수정]");

			System.out.print("변경할 사원 번호 : ");
			int empNo = sc.nextInt();
			sc.nextLine();
			System.out.print("직책 : ");
			String job = sc.next();

			System.out.print("급여 : ");
			int sal = sc.nextInt();

			System.out.print("커미션 : ");
			int comm = sc.nextInt();
			sc.nextLine();

			Emp uEmp = new Emp(empNo, job, sal, comm);

			int result = service.updateEmp(uEmp);

			if (result > 0) {
				System.out.println("사원 정보가 변경되었습니다.");
			} else {
				System.out.println("변경 실패하였습니다.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 5. 사번으로 사원 정보 삭제
	private void deleteEmp() {
		System.out.println("[사번으로 사원 정보 수정]");

		try {
			System.out.print("사번입력 :");
			int empno = sc.nextInt();
			sc.nextLine();

			int result = service.deleteEmp(empno);

			if (result > 0) {
				System.out.println("사원 정보 삭제 성공!");

			} else {
				System.out.println("사원 정보 삭제 실패");
			}

		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}

}

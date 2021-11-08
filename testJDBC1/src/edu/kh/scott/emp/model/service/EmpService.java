package edu.kh.scott.emp.model.service;

import java.sql.SQLException;
import java.util.List;

import edu.kh.scott.emp.model.dao.EmpDAO;
import edu.kh.scott.emp.model.vo.Emp;

// Service : 비즈니스 로직
// - 데이터 가공
public class EmpService {

	private EmpDAO dao = new EmpDAO();

	public List<Emp> selectAll() throws ClassNotFoundException, SQLException {
// 접근제한자     반환형       메소드명(매개변수)  [throws 예외처리구문]

		// 1-2. DB에 전체 사원 정보를 조회하는 SQL을 전달하고
		// 결과를 반환 받는 EmpDAO의 selectAll() 메소드를 호출
		List<Emp> empList = dao.selectAll();

		// 1-14. dao에서 반환 받은 DB 조회 결과 담겨있는 empList를
		// 호출부인 EmpView.selectAll()로 반환
		return empList;
	}

	// 사번으로 사원 정보 조회 Service
	public Emp selectOne(int input) throws Exception {
		// -> 입력 받은 사번

		// 2-2. 전달 받은 사번 input을 그대로 EmpDAO의 selectOne() 메소드로 전달하여
		// DB 조회 결과를 반환 받아 Emp e 변수에 저장
		Emp e = dao.selectOne(input);

		// 2-12 . dao 조회 결과를 그대로 호출부로 반환
		return e;
	}

	// 사번, 이름 일치 사원 정보 조회
	public Emp selectEmp(int empNo, String eName) throws Exception {

		Emp emp = dao.selectEmp(empNo, eName);

		return emp;
	}

	// 사원 정보 삽입용 Service
	public int insertEmp(Emp newEmp) throws Exception {

		// 3-3. 매개변수로 전달 받은 newEmp를
		// DB에 삽입할 수 있도록 dao.insertEmp(newEmp)를 호출 후 결과 값 반환.
		int result = dao.insertEmp(newEmp);

		// 3-11. DB 삽입 결과인 result를 반환
		return result;
	}

	public int updateEmp(Emp uEmp) throws Exception {
		int result = dao.updateEmp(uEmp);
		return result;
	}

	
	public int deleteEmp(int empno) throws Exception {
		int result = dao.deleteEmp(empno);
		return result;
	}

}

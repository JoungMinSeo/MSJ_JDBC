package edu.kh.scott.emp.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.scott.emp.model.vo.Emp;

// DAO(Database Access Object)
// - 자바와 데이터베이스 연결을 위한 구문을 작성하는 클래스
// - DB연결, SQL 전달, ResultSet 또는 DML 결과 반환
public class EmpDAO {

	private Connection conn = null;
	// DB 연결 정보를 담은 객체
	// -> java 프로그램과 DB 사이를 연결해주는 일종의 통로

	private Statement stmt = null;
	// Connection 객체를 통해 DB에 SQL을 전달하고
	// 결과(result set 또는 성공한 행의 개수)를 반환 받는 객체

	private PreparedStatement pstmt = null;
	// Statement의 자식으로 향상된 기능을 제공
	// '?' (위치 홀더) 기호를 이용하여 리터럴 값을 동적으로 작성하게함.
	// ex) SELECT * FROM EMP WHERE EMPNO = ? AND ENAME = ?
	// pstmt.setInt(1, empNo);
	// --> 1 번째 ? 자리에 empNo 값 추가
	// pstmt.setString(2, eName);
	// --> 2 번째 ? 자리에 eName 값 추가 + 들어가는 값 양쪽에 '' 자동 추가

	private ResultSet rs = null;
	// DB에서 SELECT 질의 성공 시 반환되는
	// Result Set을 저장하는 객체

	// 사원 정보 모두 조회
	public List<Emp> selectAll() throws ClassNotFoundException, SQLException {

		List<Emp> empList = null;

		try {
			// 1-3. DB 연결 준비
			// 1) JDBC 드라이버 클래스 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// JDBC 드라이버 역할 : Java와 DB 사이를 연결 해주는 역할
			// 해당 드라이버를 호출함으로써 메모리에 로드(적재, 올라감)됨.
			// -> DB와 연결할 준비가 됨.

			// 2) DB 정보를 입력하여 연결된 Connection 객체 만들기

			// DB 연결 주소(각자 컴퓨터에 있는 오라클 DB 연결 주소)
			String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";

			// jdbc:oracle:thin: -> JDBC 드라이버 타입이 thin 타입임을 의미
			// @127.0.0.1 -> 루프백 아이피(현재 컴퓨터)
			// :1521 -> 오라클 리스너 포트
			// :xe -> 오라클 버전 이름(expression)
			conn = DriverManager.getConnection(url, "SCOTT", "TIGER");

			// 1-4. 전달할 SQL 구문 작성
			// *** (주의사항!!!) sql구문 마지막에 절대로 세미콜론(;)을 붙이면 안된다!!!
			String sql = "SELECT * FROM EMP";

			// 1-5. sql을 전달하고 결과를 반환 받아올 Statement 객체 생성
			stmt = conn.createStatement();

			// 1-6. Statement에 sql을 담고 DB에 전달하여 실행 후
			// 결과인 ResultSet을 받아와 rs에 저장
			rs = stmt.executeQuery(sql);

			// 중간 확인
			// System.out.println(conn);
			// System.out.println(rs);

			// 1-7. rs에 저장된 내용을 옮겨 담을 List 객체 생성
			empList = new ArrayList<Emp>();

			// 1-8. rs에 저장된 ResultSet을 한 행씩 접근해서 꺼낸 다음
			// 꺼낸 데이터로 Emp 객체를 생성하고
			// 생성된 Emp 객체를 empList에 추가
			while (rs.next()) {
				// rs.next() : 한 번 호출될 때 마다 다음 행을 접근
				// -> 한 행식 순서대로 접근할 수 있는 이유
				// == CURSOR(커서) 가 rs.next() 호출 될 때 마다 다음 행으로 이동
				// -> 커서가 이동한 행에 값이 있으면 true, 없으면 false

				// 1-9. 접근한 행의 컬럼을 얻어와 변수에 저장
				int empNo = rs.getInt("EMPNO"); // EMPNO 컬럼의 정수값을 얻어옴
				String eName = rs.getString("ENAME");
				String job = rs.getString("JOB");
				int mgr = rs.getInt("MGR");
				Date hireDate = rs.getDate("HIREDATE");
				int sal = rs.getInt("SAL");
				int comm = rs.getInt("COMM");
				int deptNo = rs.getInt("DEPTNO");

				// 1-10. Emp 객체 생성
				Emp e = new Emp(empNo, eName, job, mgr, hireDate, sal, comm, deptNo);

				// 1-11. empList에 Emp 객체 추가
				empList.add(e);
			}

			// 중간확인 2
			/*
			 * for(Emp e : empList) { System.out.println(e); }
			 */

		} finally {
			// 1-13. 사용한 JDBC 객체(Connection, Statement, ResultSet) 닫기
			// --> 닫기 == close() == 사용 자원 반환
			try {
				// 생성 순서 반대로 close() 작성
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		// 1-12. empList 반환
		return empList;
	}

	// 사번으로 사원 정보 조회 dao
	public Emp selectOne(int input) throws Exception {
		// 예외 최상위 부모 Exception을 이용해 발생하는 모든 예외를 호출부로 던짐.

		// 2-3. DB에서 조회된 회원 정보를 저장할 변수 Emp e 선언
		Emp e = null;

		// 2-4. DB관련 구문 시도 후 자원을 반환하기 위한 try-finally 구문 작성
		try {
			// 2-5. DB 연결 준비
			// 1) JDBC 드라이버 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// -> ClassNotFoundException 예외처리 필요

			// 2) DB 서버 url 작성 후
			// DriverManager를 이용하여 Connection 얻어오기(== Connection 생성)
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			// localhost == 127.0.0.1(루프백 아이피) == 현재 컴퓨터

			conn = DriverManager.getConnection(url, "SCOTT", "TIGER");

			// 2-6. 전달할 SQL 구문 작성
			String sql = "SELECT * FROM EMP WHERE EMPNO = " + input;
			// 전달 받은 input을 where절 조건 값으로 사용하는 SQL을 만듦.

			// 2-7. Statement 객체 생성
			stmt = conn.createStatement();

			// 2-8. SQL을 DB에 전달 후 수행 결과를 반환 받아 ResultSet rs에 저장
			rs = stmt.executeQuery(sql);
			// executeQuery(sql) : select문을 수행하고 결과인 ResultSet을 반환 하는 메소드
			// executeUpdate(sql) : DML문을 수행하고 성공한 행의 개수(int 타입)을 반환 메소드

			// * rs에는 조회 결과 행이 0 또는 1개가 있음.
			// 0행 == 조회 결과 없음.
			// 1행 == 조회 결과 있음. -> 행의 정보를 Emp 객체에 담아서 Emp e 변수에 저장

			// 2-9. 조회 결과가 있을 경우 Emp 객체 생성
			if (rs.next()) {
				// while 아니라 if문을 쓰는 이유
				// -> 조회되는 행의 개수가 많아야 1행 이므로 반복할 필요가 없어서.

				int empNo = rs.getInt("EMPNO"); // EMPNO 컬럼에 있는 정수값 얻어오기
				// == input

				String eName = rs.getString("ENAME");
				String job = rs.getString("JOB");
				int mgr = rs.getInt("MGR");
				Date hireDate = rs.getDate("HIREDATE");
				int sal = rs.getInt("SAL");
				int comm = rs.getInt("COMM");
				int deptNo = rs.getInt("DEPTNO");

				e = new Emp(empNo, eName, job, mgr, hireDate, sal, comm, deptNo);
			}

		} finally {
			// 2-10. 사용한 JDBC 객체 자원 반환
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();

			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}

		// 2-11. 조회 결과가 담긴 Emp e 변수를 반환
		return e; // null 또는 한 사원의 정보가 담긴 Emp 객체 주소
	}

	// 사번, 이름 일치 사원 정보 조회 dao
	public Emp selectEmp(int empNo, String eName) throws Exception {

		Emp emp = null; // 조회 결과 저장용 변수 선언

		try {

			// DB 연결
			Class.forName("oracle.jdbc.driver.OracleDriver"); // JDBC 드라이버 로드

			String url = "jdbc:oracle:thin:@localhost:1521:xe"; // DB 서버 url

			conn = DriverManager.getConnection(url, "SCOTT", "TIGER"); // Connection 얻어오기

			// SQL 수행 후 결과 반환
			String sql = "SELECT * FROM EMP WHERE EMPNO = " + empNo + " AND ENAME = '" + eName + "'";

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			// 결과 있으면 Emp 객체 생성
			if (rs.next()) {
				int empNo2 = rs.getInt("EMPNO"); // EMPNO 컬럼에 있는 정수값 얻어오기
				String eName2 = rs.getString("ENAME");
				String job = rs.getString("JOB");
				int mgr = rs.getInt("MGR");
				Date hireDate = rs.getDate("HIREDATE");
				int sal = rs.getInt("SAL");
				int comm = rs.getInt("COMM");
				int deptNo = rs.getInt("DEPTNO");

				emp = new Emp(empNo2, eName2, job, mgr, hireDate, sal, comm, deptNo);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return emp;
	}

	// 사원 정보 삽입 DAO
	public int insertEmp(Emp newEmp) throws Exception {

		int result = 0; // 결과 저장용 변수

		try {

			// 3-4. DB 연결
			Class.forName("oracle.jdbc.driver.OracleDriver"); // JDBC 드라이버 로드
			String url = "jdbc:oracle:thin:@localhost:1521:xe"; // DB 서버 url
			conn = DriverManager.getConnection(url, "SCOTT", "TIGER"); // Connection 얻어오기

			/*
			 * 주의할 점! 1. 지금 수행할 것은 DML(INSERT) 구문이다! --> DML 수행 내용이 바로 DB에 반영 되는가? NO -->
			 * COMMIT 이전 까지 트랜잭션에 임시 저장됨.
			 * 
			 * 2. 입력 값이 6개 -> Statement 방식의 SQL 구문으로는 오타율이 높음. --> PreparedStatement 를 사용!
			 */

			// JDBC 구문이 수행될 경우 별도의 조작이 없으면
			// DML 구문은 자동으로 COMMIT되도록 하는 AutoCommit이 켜져 있음.
			// 이를 방지하기 위해 AutoCommit을 끔 -> 마지막에 수동으로 제어
			conn.setAutoCommit(false);

			// 3-5. SQL 구문 작성 (PreparedStatement 용으로 작성)
			String sql = "INSERT INTO EMP VALUES(SEQ_EMPNO.NEXTVAL, ?, ?, ?, SYSDATE, ?, ?, ?)";

			// 3-6. Connection에 PreparedS4tatement 객체를 생성하고
			// 거기에 ?가 포함된 SQL을 적재
			pstmt = conn.prepareStatement(sql);

			// 3-7. pstmt에 적재된 sql구문의 위치 홀더(?)에 알맞은 값 대입
			pstmt.setString(1, newEmp.geteName());
			pstmt.setString(2, newEmp.getJob());
			pstmt.setInt(3, newEmp.getMgr());
			pstmt.setInt(4, newEmp.getSal());
			pstmt.setInt(5, newEmp.getComm());
			pstmt.setInt(6, newEmp.getDeptNo());

			// 3-8. SQL 구문 수행 후 결과를 반환받아 result에 저장
			result = pstmt.executeUpdate(); // DML 이니까 executeUpdate()

			/*
			 * DML 구문을 수행할 경우 Result Set이 아닌 삽입, 수정, 삭제에 성공한 행의 개수가 반환된다. --> 행의 개수는 정수(int)
			 * 자료형을 띈다. --> int 자료형 변수 result에 DML 수행 결과 저장이 가능함.
			 */

			// 3-9. DML 수행 결과인 result 값에 따라서
			// DML 구문 성공인 경우 commit, 실패인 경우 rollback을 수행
			// --> 트랜잭션 처리
			if (result > 0)
				conn.commit(); // 성공 시 commit
			else
				conn.rollback(); // 실패 시 rollback

		} finally {
			// 3-10. 사용한 JDBC 자원 반환
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public int updateEmp(Emp uEmp) throws Exception {
		int result = 0;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "SCOTT", "TIGER");
			conn.setAutoCommit(false);

			// String sql = "UPDATE EMP SET (JOB,SAL,COMM) =(SELECT ?,?,? FROM DUAL) WHERE
			// EMPNO = ?";

			String sql = "UPDATE EMP SET JOB = ?, SAL = ?, COMM = ? WHERE EMPNO = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uEmp.getJob());
			pstmt.setInt(2, uEmp.getSal());
			pstmt.setInt(3, uEmp.getComm());
			pstmt.setInt(4, uEmp.getEmpNo());

			result = pstmt.executeUpdate();

			if (result > 0)
				conn.commit();
			else
				conn.rollback();

		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return result;
	}

	
	
	public int deleteEmp(int empno) throws Exception {
		int result = 0; // 결과 저장용 변수

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";

			conn = DriverManager.getConnection(url, "SCOTT", "TIGER");
			conn.setAutoCommit(false);

			String sql = "DELETE FROM EMP WHERE EMPNO = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, empno); 

			result = pstmt.executeUpdate();// DML이니까 executeUpdate()

			if (result > 0)
				conn.commit(); // 성공시 commit
			else
				conn.rollback(); // 실패시 rollback

		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
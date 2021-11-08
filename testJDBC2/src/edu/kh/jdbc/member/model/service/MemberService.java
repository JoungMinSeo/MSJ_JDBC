package edu.kh.jdbc.member.model.service;

// import static  : 지정된 클래스에 있는 static 멤버를 호출할 때 클래스명을 생략하게 해줌
import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.sql.Connection;

import edu.kh.jdbc.member.model.dao.MemberDAO;
import edu.kh.jdbc.member.model.vo.Member;
import edu.kh.jdbc.view.JDBCView;

// Service 클래스 역할 
// 1. 매개변수로 전달 받은 데이터 또는 dao 반환 데이터의 가공 처리
// 2. Connection 생성 및 트랜잭션 처리
// --> 1,2 번을 묶어 비즈니스 로직
public class MemberService {

	private MemberDAO dao = new MemberDAO();
	
	
	/** 회원 가입 Service
	 * @param mem
	 * @return result
	 * @throws Exception 
	 */
	public int signUp(Member mem) throws Exception {
		// Service 메소드 하나가 하나의 트랜잭션이 된다.
		
		// 1. 커넥션 얻어오기(새로운 Connection 생성)
		Connection conn = getConnection();
		
		// 2. 커넥션과 매개변수로 전달 받은 값을 dao로 다시 전달
		int result = dao.signUp(conn, mem);
		
		// 3. DML 수행 시 결과에 따른 트랜잭션 처리
		if(result > 0) { // DML 성공
			commit(conn);
		}else {
			rollback(conn);
		}
		
		// 4. 사용했던 Connection 반환
		close(conn);
		
		// 5. 결과를 View로 리턴
		return result;
	}

	

	/** 로그인 Service
	 * @param memId
	 * @param memPw
	 * @return loginMember
	 * @throws Exception
	 */
	public Member login(String memId, String memPw) throws Exception{
		
		// ** Service에 메소드 만들면 Connection 부터 얻어오기
		Connection conn = getConnection();
		
		// Connection과 전달 받은 매개변수를 dao의 login() 메소드의 매개변수로 전달
		Member loginMember = dao.login(conn, memId, memPw);
		
		// SELECT 수행 시 별도의 트랜잭션 처리가 필요 없다!
		close(conn); // Connection 객체 반환
		
		return loginMember;
	}



	/** 회원 정보 수정 Service
	 * @param memNo
	 * @param updateName
	 * @param updatePhone
	 * @return result
	 * @throws Exception
	 */
	public int updateMember(int memNo, String updateName, String updatePhone) throws Exception {

		Connection conn = getConnection();
		
		int result = dao.updateMember(conn, memNo, updateName, updatePhone);
		
		// update 수행 결과 트랜잭션 처리
		if(result > 0) {
			commit(conn);
			
			// + loginMember 변수에 저장된 이전 회원 정보를
			//   수정된 회원 정보로 변경
			JDBCView.loginMember.setMemNm(updateName);
			JDBCView.loginMember.setMemPhone(updatePhone);
		}
		
		else {
			rollback(conn);
		}
		
		// conn 반환
		close(conn);
		
		// 수정 결과 반환
		return result;
	}



	/** 비밀번호 변경 Service
	 * @param currPw
	 * @param newPw1
	 * @return reuslt
	 * @throws Exception
	 */
	public int updatePw(String currPw, String newPw1) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.updatePw(conn, currPw, newPw1);
		
		if(result > 0) 	commit(conn);
		else			rollback(conn);
		
		close(conn);
		
		return result;
	}



	/** 회원 탈퇴 Service
	 * @param currPw
	 * @return result
	 * @throws Exception
	 */
	public int secession(String currPw) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.secession(conn, currPw);
		
		// 트랜잭션 처리
		if(result > 0)	commit(conn);
		else			rollback(conn);
			
		close(conn);
		
		return result;
	}

	
}





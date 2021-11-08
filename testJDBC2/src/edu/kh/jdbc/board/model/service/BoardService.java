package edu.kh.jdbc.board.model.service;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.board.model.dao.BoardDAO;
import edu.kh.jdbc.board.model.vo.Board;

public class BoardService {

	private BoardDAO dao = new BoardDAO();

	/**
	 * 게시글 목록 조회 Service
	 * 
	 * @return boardList
	 * @throws Exception
	 */
	public List<Board> selectAllBoard() throws Exception {
		Connection conn = getConnection();

		List<Board> boardList = dao.selectAllBoard(conn);

		// select를 수행했기 때문에 별도에 트랜잭션 처리 없이 바로 conn 반환
		close(conn);

		return boardList;
	}

	/**
	 * 게시글 상세 조회 Service
	 * 
	 * @param in
	 * @return bo
	 * @throws Exception
	 */
	public Board selectBoard(int in) throws Exception {
		Connection conn = getConnection();
		Board bo = dao.selectBoard(conn, in);
		close(conn);
		return bo;
	}

	/**
	 * 게시글 삽입 Service
	 * 
	 * @param input
	 * @param input2
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(String input, String input2) throws Exception {
		Connection conn = getConnection();
		int result = dao.insertBoard(conn, input, input2);

		if (result > 0) {
			commit(conn);
		} else {
			rollback(conn);
		}
		close(conn);
		return result;
	}

	/**
	 * 게시글 삭제 Service
	 * 
	 * @param boardNo
	 * @return result
	 */
	public int deleteBoard(int boardNo) throws Exception {

		Connection conn = getConnection();

		int result = dao.deleteBoard(conn, boardNo);

		if (result > 0)	commit(conn);
		else 			rollback(conn);

		close(conn);

		return result;
	}

	
	
	/** 게시글 상세 조회 Service
	 * @param boardNo
	 * @return board
	 * @throws Exception
	 */
	public Board newSelectBoard(int boardNo) throws Exception{
		// 1. 커넥션 생성
		Connection conn = getConnection();
		
		// 2. DAO에 게시글 상세조회 메소드 호출 후 결과 반환 받기
		Board board = dao.newSelectBoard(conn, boardNo);
		// -> 조회 결과가 없을 경우 null, 있으면 Board 객체를 참조하고 있는 상태
		
		// 3. board가 참조하는 객체가 있는 경우 == 조회 내용이 있는 경우
		//    --> 조회 수 증가 코드 작성
		if(board != null) {
			// 조회 수 증가를 위해서는 BOARD 테이블의 READ_COUNT 컬럼을 
			// 1 증가한 값으로 수정(UPDATE)해야 한다!
			
			int result = dao.updateReadCount(conn, boardNo);
			
			// UPDATE(DML)을 수행했기 때문에 result 결과에 따른 트랜잭션 처리 필요
			if(result > 0) {
				commit(conn);
				
				// 이미 조회 수 증가 전에 조회해둔 board의 readCount를 
				// UPDATE된 DB READ_COUNT 컬럼 값과 똑같이 1 증가 시킴
				board.setReadCount( board.getReadCount() + 1   );
				
				// 자기 글은 조회 수 증가 X
				// 동일한 접속자는 일정 시간마다 만 조회 수 증가
				
			}else {
				rollback(conn);
			}
		}
		
		return board;
	}

	
	/**게시글 삽입 Service
	 * @param boardTitle
	 * @param string
	 * @return board
	 * @throws Exception
	 */
	public Board newInsertBoard(String boardTitle, String boardContent) throws Exception{
		
		Connection conn = getConnection();
		
		// 1. 삽입될 다음 게시글 번호를 얻어옴.
		int boardNo = dao.nextBoardNo(conn);
		
		// 2. 얻어온 번호를 이용해서 게시글 삽입을 진행
		int result = dao.newInsertBoard(conn, boardNo, boardTitle, boardContent);
		
		// 3. 삽입을 성공한 경우
		//    --> commit + boardNo를 이용해서 새로 작성한 글  상세조회
		Board board = null; // 상세 조회 결과 저장용 변수
		
		if(result > 0) {
			commit(conn);
			
			board = newSelectBoard(boardNo);
			
		}else {
			rollback(conn);
		}
		
		return board;
	}
	
	
	
	

}

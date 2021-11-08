package edu.kh.jdbc.board.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.kh.jdbc.board.model.vo.Board;
import edu.kh.jdbc.view.JDBCView;

public class BoardDAO {

	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private Properties prop = null;

	public BoardDAO() { // 기본생성자
		try {

			prop = new Properties();
			prop.loadFromXML(new FileInputStream("board-query.xml"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 게시글 목록 조회 DAO
	 * 
	 * @param conn
	 * @return boardList
	 * @throws Exception
	 */
	public List<Board> selectAllBoard(Connection conn) throws Exception {

		List<Board> boardList = null;

		try {
			String sql = prop.getProperty("selectAllBoard");

			// sql 구문에 위치홀더(?)가 없으므로 Statement 객체를 활용할 예정
			stmt = conn.createStatement();

			// Statement : Connection을 통해 SQL을 수행하고 결과를 반환 받는 역할을 하는 객체
			rs = stmt.executeQuery(sql);

			boardList = new ArrayList<Board>();

			while (rs.next()) {

				// int boardNo = rs.getInt("BOARD_NO"); // 컬럼명 작성을 권장
				int boardNo = rs.getInt(1); // 조회 결과의 1번째 컬럼 값
				String boardTitle = rs.getString(2);
				Date createDt = rs.getDate(3);
				String memId = rs.getString(4);
				int readCount = rs.getInt(5);

				// 얻어온 컬럼값을 객체에 저장한 후 List에 추가
				boardList.add(new Board(boardNo, boardTitle, createDt, memId, readCount));
			}

		} finally {
			close(rs);
			close(stmt);
		}

		return boardList;
	}

	public Board selectBoard(Connection conn, int in) throws Exception {
		Board bo = null;

		try {
			String sql = prop.getProperty("selectBoard");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, in);
			rs = pstmt.executeQuery();
//	         BOARD_NO, BOARD_TITLE, MEM_ID, CREATE_DT, READ_COUNT,BOARD_CONTENT

			if (rs.next()) {
				int boardNo = rs.getInt("BOARD_NO");
				String boardTitle = rs.getString("BOARD_TITLE");
				Date createDt = rs.getDate("CREATE_DT");
				String memId = rs.getString("MEM_ID");
				int readCount = rs.getInt("READ_COUNT");
				String boardContent = rs.getString("BOARD_CONTENT");

				bo = new Board(boardNo, boardTitle, boardContent, createDt, memId, readCount);
			}

		} finally {
			close(rs);
			close(pstmt);
		}

		return bo;
	}

	/**
	 * 게시글 삽입 DAO
	 * 
	 * @param conn
	 * @param input
	 * @param input2
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(Connection conn, String input, String input2) throws Exception {

		int result = 0;

		try {
			String sql = prop.getProperty("insertBoard");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, input);
			pstmt.setString(2, input2);
			pstmt.setInt(3, JDBCView.loginMember.getMemNo());

			result = pstmt.executeUpdate();

		} finally {
			close(pstmt);
		}
		return result;
	}

	/**
	 * 게시글 삭제 DAO
	 * 
	 * @param conn
	 * @param boardNo
	 * @return result
	 */
	public int deleteBoard(Connection conn, int boardNo) throws Exception {

		int result = 0;

		try {
			String sql = prop.getProperty("deleteBoard");
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, boardNo);
			pstmt.setInt(2, JDBCView.loginMember.getMemNo());

			result = pstmt.executeUpdate();

		} finally {
			close(pstmt);
		}

		return result;
	}

	
	
	/** 게시글 상세 조회 DAO
	 * @param conn 
	 * @param boardNo
	 * @return board
	 * @throws Exception
	 */
	public Board newSelectBoard(Connection conn, int boardNo) throws Exception {
		
		Board board = null; // 결과 저장용 변수 선언
		
		try {
			// board-query.xml -> Properties prop -> prop.getProperty("newSelectBoard")
			String sql = prop.getProperty("newSelectBoard");
			
			// Connection에 PreparedStatement 객체를 만들고,
			// 위치홀더가 포함된 SQL 구문을 배치
			pstmt = conn.prepareStatement(sql);
			
			// 위치홀더에 알맞은 값 set
			pstmt.setInt(1, boardNo);
			
			// SQL 구문 수행 후 결과를 반환 받음
			// -> SELECT 수행 -> ResultSet 반환 -> rs에 대입
			rs = pstmt.executeQuery();
			
			// ResultSet에 저장된 내용을 꺼냄
			// -> WHERE 조건에 BOARD_NO 라는 PK가 사용됨.
			// --> PK는 NOT NULL + UNIQUE 제약조건의 특징을 가지고 있으며
			//	    테이블 내의 각 행을 구분하는 식별자 역할. (테이블 마다 PK 제약조건은 하나만 존재 가능)
			// --> PK를 조건으로 사용했다? == 조회 결과가 없거나 있으면 1행만 나온다.
			// --> rs.next()를 호출할 때 if문 / while문 둘 중 어떤 걸 사용해되는지에 대한 구분 가능
			
			if(rs.next()) { // 조회 결과가 한 행이 있는 경우
				String boardTitle = rs.getString("BOARD_TITLE");
				Date createDt = rs.getDate("CREATE_DT");
				String memId = rs.getString("MEM_ID");
				int readCount = rs.getInt("READ_COUNT");
				String boardContent = rs.getString("BOARD_CONTENT");
				
				board = new Board(boardNo, boardTitle, boardContent, createDt, memId, readCount);
			}
			
		}finally {
			close(rs);
			close(pstmt);
		}
		
		return board;
	}

	
	
	/** 조회 수 증가 DAO
	 * @param conn 
	 * @param boardNo
	 * @return result
	 * @throws Exception
	 */
	public int updateReadCount(Connection conn, int boardNo) throws Exception{

		int result = 0;
		
		try {
			String sql = prop.getProperty("updateReadCount");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
		}
		
		return result;
	}

	
	
	/** 다음 게시글 번호를 얻어오는 DAO
	 * @param conn
	 * @return boardNo
	 * @throws Exception
	 */
	public int nextBoardNo(Connection conn) throws Exception{
		int boardNo = 0;
		
		try {
			String sql = prop.getProperty("nextBoardNo");
			
			// sql에 위치 홀더가 없으므로 Statement 객체를 이용
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) {
				boardNo = rs.getInt("BOARD_NO");
			}
			
		}finally {
			close(rs);
			close(stmt);
		}
		
		return boardNo;
	}

	
	
	/** 게시글 삽입 DAO
	 * @param conn
	 * @param boardNo
	 * @param boardTitle
	 * @param boardContent
	 * @return result
	 * @throws Exception
	 */
	public int newInsertBoard(Connection conn, 
			int boardNo, String boardTitle, String boardContent) throws Exception {

		int result = 0;
		
		try {
			String sql = prop.getProperty("newInsertBoard");
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNo);
			pstmt.setString(2, boardTitle);
			pstmt.setString(3, boardContent);
			pstmt.setInt(4, JDBCView.loginMember.getMemNo());
			
			result = pstmt.executeUpdate();
			
		}finally {
			close(pstmt);
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
}

package edu.kh.jdbc.board.model.vo;

import java.sql.Date;

// V_BOARD 뷰 조회 결과를 담을 객체
// + BOARD 테이블 DML을 위한 데이터 저장 객체

public class Board {
	private int boardNo;
	private String boardTitle;
	private String boardContent;
	private Date createDt;
	private String memId;
	private int readCount;
	private int memNo;
	
	public Board() { }

	public Board(int boardNo, String boardTitle, Date createDt, String memId, int readCount) {
		super();
		this.boardNo = boardNo;
		this.boardTitle = boardTitle;
		this.createDt = createDt;
		this.memId = memId;
		this.readCount = readCount;
	}


	public Board(int boardNo, String boardTitle, String boardContent, Date createDt, String memId, int readCount) {
		super();
		this.boardNo = boardNo;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.createDt = createDt;
		this.memId = memId;
		this.readCount = readCount;
	}

	public Board(String boardTitle, String boardContent, int memNo) {
		super();
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.memNo = memNo;
	}

	public int getBoardNo() {
		return boardNo;
	}

	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}

	public String getBoardTitle() {
		return boardTitle;
	}

	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}

	public String getBoardContent() {
		return boardContent;
	}

	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public int getMemNo() {
		return memNo;
	}

	public void setMemNo(int memNo) {
		this.memNo = memNo;
	}

	@Override
	public String toString() {
		return "Board [boardNo=" + boardNo + ", boardTitle=" + boardTitle + ", boardContent=" + boardContent
				+ ", createDt=" + createDt + ", memId=" + memId + ", readCount=" + readCount + ", memNo=" + memNo + "]";
	}
	
	
}




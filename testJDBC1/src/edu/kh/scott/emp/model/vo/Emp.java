package edu.kh.scott.emp.model.vo;

import java.sql.Date;

// VO(Value Object) : 값을 기록하는 객체를 만들기 위한 클래스
// -> DB 테이블의 한 행의 정보를 기록하는 객체
public class Emp {
	
	private int empNo; //사번
	private String eName; // 이름
	private String job; // 직급
	private int mgr; // 관리자 사번
	private Date hireDate; // 입사일
	// java.sql.Date;   (주의!)
	private int sal;	// 급여
	private int comm;	// 커미션
	private int deptNo;	// 부서번호
	
	// * VO 필수 작성 
	// 기본생성자
	// getter/setter 

	// * 필수 아닌 것들
	// 매개변수 있는 생성자 
	// toString(), equals(), hashCode()
	
	public Emp() {}
	
	
	
	public Emp(int empNo, String job, int sal, int comm) {
		super();
		this.empNo = empNo;
		this.job = job;
		this.sal = sal;
		this.comm = comm;
	}



	public Emp(String eName, String job, int mgr, int sal, int comm, int deptNo) {
		super();
		this.eName = eName;
		this.job = job;
		this.mgr = mgr;
		this.sal = sal;
		this.comm = comm;
		this.deptNo = deptNo;
	}


	public Emp(int empNo, String eName, String job, int mgr, Date hireDate, int sal, int comm, int deptNo) {
		super();
		this.empNo = empNo;
		this.eName = eName;
		this.job = job;
		this.mgr = mgr;
		this.hireDate = hireDate;
		this.sal = sal;
		this.comm = comm;
		this.deptNo = deptNo;
	}

	public int getEmpNo() {
		return empNo;
	}

	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public int getMgr() {
		return mgr;
	}

	public void setMgr(int mgr) {
		this.mgr = mgr;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public int getSal() {
		return sal;
	}

	public void setSal(int sal) {
		this.sal = sal;
	}

	public int getComm() {
		return comm;
	}

	public void setComm(int comm) {
		this.comm = comm;
	}

	public int getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(int deptNo) {
		this.deptNo = deptNo;
	}

	@Override
	public String toString() {
		return "Emp [empNo=" + empNo + ", eName=" + eName + ", job=" + job + ", mgr=" + mgr + ", hireDate=" + hireDate
				+ ", sal=" + sal + ", comm=" + comm + ", deptNo=" + deptNo + "]";
	}

	
	
}





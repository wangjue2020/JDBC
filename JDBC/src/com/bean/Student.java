package com.bean;

public class Student {
	private int flowId;
	private int examType;
	private String id;
	private String examId;
	private String name;
	private String location;
	private int grade;

	public Student() {
		super();
	}

	public Student(int examType, String id, String examId, String name, int grade) {
		super();
		this.examType = examType;
		this.id = id;
		this.examId = examId;
		this.name = name;
		this.grade = grade;
	}

	public int getFlowId() {
		return flowId;
	}

	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}

	public int getExamType() {
		return examType;
	}

	public void setExamType(int examType) {
		this.examType = examType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "流水号：" + flowId + "\n四级/六级：" + examType + "\n身份证号：" + id + "\n准考证号：" + examId + "\n学生姓名："
				+ name + "\n区域：" + location +"\n成绩：" + grade;
	}

	

}

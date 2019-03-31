package javanet.c01.entity;

public class Student {
	private String id="unknow";
	private String name="unknow";
	private String grade="unknow";

	public Student() {

	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		try {
			int i = Integer.parseInt(grade);
			if (i > 100)
				i = 100;
			if (i < 0)
				i = 0;
			this.grade = grade;
		} catch (Exception e) {
			this.grade = "0";
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id.getBytes().length > 8) {
			this.id = "Overflow";
		} else {
			for (int i = 0; i < 8 - id.length(); i++) {
				id += " ";
			}
			this.id = id;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name.getBytes().length > 20) {
			this.name = "Overflow";
		} else {
			for (int i = 0; i < 20 - name.length(); i++) {
				name += " ";
			}
			this.name = name;
		}
	}
}

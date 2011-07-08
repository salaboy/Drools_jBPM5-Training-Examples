package org.plugtree.training.model;

public class Person {

	private Integer age;
	private Job job;
	private Credit credit;

	public Person(Integer age) {
		this.age = age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getAge() {
		return age;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Job getJob() {
		return job;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public Credit getCredit() {
		return credit;
	}

}

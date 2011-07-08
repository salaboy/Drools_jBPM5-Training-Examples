package org.plugtree.training.model;

public class Job {

	private int old;
	private float salary;
	
	public Job(int old, float salary) {
		this.old = old;
		this.salary = salary;
	}

	public void setOld(int old) {
		this.old = old;
	}
	public int getOld() {
		return old;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
	public float getSalary() {
		return salary;
	}

}
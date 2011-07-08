package org.plugtree.training.model;

import org.plugtree.training.enums.CreditStatus;

public class Credit {
	
	private CreditStatus status;
	
	public Credit(CreditStatus status) {
		this.status = status;
	}

	public void setStatus(CreditStatus status) {
		this.status = status;
	}

	public CreditStatus getStatus() {
		return status;
	}

}

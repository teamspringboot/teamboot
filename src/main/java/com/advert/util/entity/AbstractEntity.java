package com.advert.util.entity;

import java.util.Date;

public abstract class AbstractEntity implements Entity {

	private static final long serialVersionUID = 161587517963558888L;

	/** 序列号 */
	protected Long id;

	/** 版本号 */
	private Integer version = new Integer(1);

	/** 创建者 */
	private String createdBy;

	/** 创建时间 */
	private Date createdDt = new Date();

	private Date createdTimestamp;

	/** 最后更新者 */
	private String updatedBy;

	/** 最后更新时间 */
	private Date updatedDt;

	private Date updatedTimestamp;
	/**
	 * 事务处理ID（requestId或者messageId）
	 */
	private String transactionId;

	public Long getId() {
		return id;
	}

	public void setSid(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/*public void setDefaultUpdateBy() {
		this.setUpdatedBy(LoginUtil.getUpcUserId());
		this.setTransactionId(LoginUtil.getTransactionId());
	}

	public void setDefaultCreateBy() {
		this.setCreatedBy(LoginUtil.getUpcUserId());
		this.setTransactionId(LoginUtil.getTransactionId());
	}*/

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public Date getUpdatedTimestamp() {
		return updatedTimestamp;
	}

	public void setUpdatedTimestamp(Date updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}

}

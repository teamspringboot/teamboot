package com.advert.entity;

import com.advert.util.entity.AbstractEntity;


/**
 * 
 * <p>Title: pes-common</p>
 * <p>Description: 参数类型管理，参数明细</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: sgai</p>
 * @author chenwei
 * @version 2018年3月20日
 */
public class ReferenceTableDetail extends AbstractEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1386986882064559996L;

	private Long referenceTableSid;

    private String referenceTableId;

    private String key1Value;

    private String key2Value;

    private String data1Value;

    private String data2Value;

    private String data3Value;

    private String data4Value;

    private String data5Value;

    public Long getReferenceTableSid() {
        return referenceTableSid;
    }

    public void setReferenceTableSid(Long referenceTableSid) {
        this.referenceTableSid = referenceTableSid;
    }

    public String getReferenceTableId() {
        return referenceTableId;
    }

    public void setReferenceTableId(String referenceTableId) {
        this.referenceTableId = referenceTableId;
    }

    public String getKey1Value() {
        return key1Value;
    }

    public void setKey1Value(String key1Value) {
        this.key1Value = key1Value;
    }

    public String getKey2Value() {
        return key2Value;
    }

    public void setKey2Value(String key2Value) {
        this.key2Value = key2Value;
    }

    public String getData1Value() {
        return data1Value;
    }

    public void setData1Value(String data1Value) {
        this.data1Value = data1Value;
    }

    public String getData2Value() {
        return data2Value;
    }

    public void setData2Value(String data2Value) {
        this.data2Value = data2Value;
    }

    public String getData3Value() {
        return data3Value;
    }

    public void setData3Value(String data3Value) {
        this.data3Value = data3Value;
    }

    public String getData4Value() {
        return data4Value;
    }

    public void setData4Value(String data4Value) {
        this.data4Value = data4Value;
    }

    public String getData5Value() {
        return data5Value;
    }

    public void setData5Value(String data5Value) {
        this.data5Value = data5Value;
    }
}
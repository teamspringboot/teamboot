package com.advert.entity;

import java.util.List;

import com.advert.util.entity.AbstractEntity;


public class MdCommonType extends AbstractEntity implements Comparable<MdCommonType> {
	
	private Long parentSid;
	
    private String typeId;

    private String typeName;

    private String typeDesc;
    
    private Integer typeLevel;
    
    private String extCol1;
    
    private String extCol1Desc;
    
    private String extCol2;
    
    private String extCol2Desc;
    
    private String extCol3;
    
    private String extCol3Desc;

    private String iconCls;
    
    private Long nodeType;
    
    private Integer sequence;
    
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Long getNodeType() {
		return nodeType;
	}

	public void setNodeType(Long nodeType) {
		this.nodeType = nodeType;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

	public Long getParentSid() {
		return parentSid;
	}

	public void setParentSid(Long parentSid) {
		this.parentSid = parentSid;
	}

	public Integer getTypeLevel() {
		return typeLevel;
	}

	public void setTypeLevel(Integer typeLevel) {
		this.typeLevel = typeLevel;
	}

	public String getExtCol1() {
		return extCol1;
	}

	public void setExtCol1(String extCol1) {
		this.extCol1 = extCol1;
	}

	public String getExtCol1Desc() {
		return extCol1Desc;
	}

	public void setExtCol1Desc(String extCol1Desc) {
		this.extCol1Desc = extCol1Desc;
	}

	public String getExtCol2() {
		return extCol2;
	}

	public void setExtCol2(String extCol2) {
		this.extCol2 = extCol2;
	}

	public String getExtCol2Desc() {
		return extCol2Desc;
	}

	public void setExtCol2Desc(String extCol2Desc) {
		this.extCol2Desc = extCol2Desc;
	}

	public String getExtCol3() {
		return extCol3;
	}

	public void setExtCol3(String extCol3) {
		this.extCol3 = extCol3;
	}

	public String getExtCol3Desc() {
		return extCol3Desc;
	}

	public void setExtCol3Desc(String extCol3Desc) {
		this.extCol3Desc = extCol3Desc;
	}

	@Override
	public int compareTo(final MdCommonType mdCommonType) {
		return parentSid.compareTo(mdCommonType.getParentSid());
	}

}
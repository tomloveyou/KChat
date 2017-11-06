package com.mfc.appframe.bean;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

public class User extends DataSupport implements Serializable {
	// [{"type":"UnitGridInfo","values":[{"UNITGRID_SN":2344}]},{"type":"UserInfo","values":[{"OPERATORID":6851,"ORGID":12327,"USERNO":"B0889","USERNAME":"唐莎","ORGCODE":"51010021","ORGNAME":"信用信息及网络商品交易监督处"}]},{"type":"GridInfo","values":[{"GRID_NAME":"cdgs","GRID_SN":315},{"GRID_NAME":"cdgs","GRID_SN":315}]}]
	
	/**
	 * 
	 *@属性 serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 操作员ID
	 */
	private String OPERATORID;
	
	/**
	 * 机构ID
	 */
	private String ORGID;
	
	/**
	 * 用户账号
	 */
	private String USERNO;
	
	/**
	 * 用户名称
	 */
	private String USERNAME;
	
	/**
	 * 机构编号
	 */
	private String ORGCODE;
	
	/**
	 * 机构名称
	 */
	private String ORGNAME;
	
	private int id;

	public User(){};
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User(String oPERATORID, String oRGID, String uSERNO,
			String uSERNAME, String oRGCODE, String oRGNAME) {
		super();
		OPERATORID = oPERATORID;
		ORGID = oRGID;
		USERNO = uSERNO;
		USERNAME = uSERNAME;
		ORGCODE = oRGCODE;
		ORGNAME = oRGNAME;
	}

	public String getOPERATORID() {
		return OPERATORID;
	}

	public void setOPERATORID(String oPERATORID) {
		OPERATORID = oPERATORID;
	}

	public String getORGID() {
		return ORGID;
	}

	public void setORGID(String oRGID) {
		ORGID = oRGID;
	}

	public String getUSERNO() {
		return USERNO;
	}

	public void setUSERNO(String uSERNO) {
		USERNO = uSERNO;
	}

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public String getORGCODE() {
		return ORGCODE;
	}

	public void setORGCODE(String oRGCODE) {
		ORGCODE = oRGCODE;
	}

	public String getORGNAME() {
		return ORGNAME;
	}

	public void setORGNAME(String oRGNAME) {
		ORGNAME = oRGNAME;
	}

	@Override
	public String toString() {
		return "User [OPERATORID=" + OPERATORID + ", ORGID=" + ORGID
				+ ", USERNO=" + USERNO + ", USERNAME=" + USERNAME
				+ ", ORGCODE=" + ORGCODE + ", ORGNAME=" + ORGNAME + "]";
	}
	
	

}

package com.mfc.appframe.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import org.litepal.crud.DataSupport;

/**
 * 
 *  <dd> 项目名称：gsmobile
 *  <dd> 类名称：UserOperationLogInfoVo 
 *  <dd> 类描述： 用户操作日志信息Vo
 *  <dd> 创建时间：2016-4-25下午4:23:03 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 *  <dd> 完成时间：无
 * </dl>
 * @author wangsm
 * @see
 * @version
 */
public class UserOperationInfoVo extends DataSupport implements Serializable{

	/**
	 * 
	 *@属性 serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal SN;//记录流水号
	private String USERID;//用户id
	private String USERNAME;//用户名称
	private String ORGID;//机构id
	private String ORGNAME;//机构名称
	private String OPERATION_TYPE;//操作类型
	private String PARENT_OPERATION_TYPE;//父操作类型
	private String OPERATION_NAME;//操作名称
	private BigDecimal VISITS;//累计操作次数
	private String OPERATION_TIME;//操作时间
	
	private long id;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public BigDecimal getSN() {
		return SN;
	}
	public void setSN(BigDecimal sN) {
		SN = sN;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	
	public String getORGID() {
		return ORGID;
	}
	public void setORGID(String oRGID) {
		ORGID = oRGID;
	}
	
	public String getORGNAME() {
		return ORGNAME;
	}
	public void setORGNAME(String oRGNAME) {
		ORGNAME = oRGNAME;
	}
	
	public String getOPERATION_TYPE() {
		return OPERATION_TYPE;
	}
	public void setOPERATION_TYPE(String oPERATION_TYPE) {
		OPERATION_TYPE = oPERATION_TYPE;
	}
	
	public String getPARENT_OPERATION_TYPE() {
		return PARENT_OPERATION_TYPE;
	}
	public void setPARENT_OPERATION_TYPE(String pARENT_OPERATION_TYPE) {
		PARENT_OPERATION_TYPE = pARENT_OPERATION_TYPE;
	}
	
	public String getOPERATION_NAME() {
		return OPERATION_NAME;
	}
	public void setOPERATION_NAME(String oPERATION_NAME) {
		OPERATION_NAME = oPERATION_NAME;
	}
	
	public BigDecimal getVISITS() {
		return VISITS;
	}
	public void setVISITS(BigDecimal vISITS) {
		VISITS = vISITS;
	}
	
	public String getOPERATION_TIME() {
		return OPERATION_TIME;
	}
	public void setOPERATION_TIME(String oPERATION_TIME) {
		OPERATION_TIME = oPERATION_TIME;
	}
}

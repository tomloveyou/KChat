package com.mfc.appframe.bean;

import java.util.ArrayList;
import java.util.List;

/***
 * Litepal数据库配置文件的javabean
 * <dl>  Class Description
 *  <dd> 项目名称：gsmobile
 *  <dd> 类名称：
 *  <dd> 类描述： 此类用于动态修改litepal配置文件的中间类，用于暂时保存xml数据。
 *  <dd> 创建时间：2016-3-30上午11:05:28 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version
 */
public class LitePalXmlBean {
	/**
	 * The version of database.
	 */
	private int version;

	/**
	 * The name of database.
	 */
	private String dbName;

	/**
	 * The case of table names and column names and SQL.
	 */
	private String cases;

	/**
	 * All the model classes that want to map in the database. Each class should
	 * be given the full name including package name.
	 */
	private List<String> classNames = new ArrayList<String>();

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getCases() {
		return cases;
	}

	public void setCases(String cases) {
		this.cases = cases;
	}

	public List<String> getClassNames() {
		return classNames;
	}

	public void setClassNames(List<String> classNames) {
		this.classNames = classNames;
	}
	
	public void addClassName (String className) {
		classNames.add(className);
	}
}

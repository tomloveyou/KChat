package com.mfc.appframe.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import org.litepal.LitePalApplication;
import org.litepal.exceptions.ParseConfigurationFileException;
import org.litepal.parser.LitePalParser;
import org.litepal.util.AppCacheDirUtil;
import org.litepal.util.Const;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.mfc.appframe.bean.LitePalXmlBean;

import android.content.res.AssetManager;

/**
 * <dl>  Class Description
 *  <dd> 项目名称：HSTCFrame
 *  <dd> 类名称：
 *  <dd> 类描述： 对litepal框架的litepal.xml配置文件的操作工具类
 *  			为了让litepal支持多用户多数据库功能，我将配置文件复制到data/data/我们app/cache目录下
 *  			以便动态的对配置文件进行更改（修改数据库名称，以便自动创建不同用户数据库），asset目录下文件是无法
 *  			在代码中修改的。
 *  <dd> 创建时间：2016-4-27下午2:47:25 2016
 *  <dd> 修改人：无
 *  <dd> 修改时间：无
 *  <dd> 修改备注：无
 * </dl>
 * @author lirj
 * @see
 * @version
 */
public class LitepalXmlUtils {

	/**
	 * Node name dbname.
	 */
	static final String NODE_DB_NAME = "dbname";

	/**
	 * Node name version.
	 */
	static final String NODE_VERSION = "version";

	/**
	 * Node name list. Currently not used.
	 */
	static final String NODE_LIST = "list";

	/**
	 * Node name mapping.
	 */
	static final String NODE_MAPPING = "mapping";

	/**
	 * Node name column case.
	 */
	static final String NODE_CASES = "cases";

	/**
	 * Attribute name value, for dbname and version node.
	 */
	static final String ATTR_VALUE = "value";

	/**
	 * Attribute name class, for mapping node.
	 */
	static final String ATTR_CLASS = "class";

	private static LitepalXmlUtils updater;

	private LitepalXmlUtils() {
	}

	public static LitepalXmlUtils getInstance() {
		if (updater == null) {
			updater = new LitepalXmlUtils();
		}
		return updater;
	}

	/***
	 * <b>方法描述：更新配置文件数据库名称 </b> <dd>方法作用：
	 * 不同用户登录时需要为不同用户创建不同数据库，这时需要调用此方法，创建不同数据库 <dd>适用条件： <dd>执行流程：
	 * 读取data/data/cache/litepal.xml--修改dbname节点--覆盖litepal.xml <dd>使用方法： <dd>
	 * 注意事项：
	 * 
	 * @param dbname
	 *            数据库名称
	 * @since Met 1.0
	 * @see
	 */
	public void updateDBname(String dbname) {
		LitePalXmlBean xmlBean = parseLitepalXml();
		if (xmlBean != null) {
			xmlBean.setDbName(dbname);
			serializeLitepalXml(xmlBean);
		}
	}

	/***
	 * 将bean序列化为xml文件 <b>方法描述： </b> <dd>方法作用： <dd>适用条件： <dd>执行流程： <dd>使用方法： <dd>
	 * 注意事项：
	 * 
	 * @param bean
	 * @since Met 1.0
	 * @see
	 */
	public void serializeLitepalXml(LitePalXmlBean bean) {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(
					AppCacheDirUtil.getCacheDirPath(LitePalApplication
							.getContext())
							+ File.separator
							+ Const.LitePal.CONFIGURATION_FILE_NAME), "utf-8");
			// 获取XmlSerializer对象
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlSerializer xmlSerializer = factory.newSerializer();
			// 设置输出流对象
			xmlSerializer.setOutput(writer);

			xmlSerializer.startDocument("utf-8", true);
			// 编写litepal.xml根节点
			xmlSerializer.startTag(null, "litepal");
			// 编写dbname节点
			xmlSerializer.startTag(null, "dbname");
			xmlSerializer.attribute(null, "value", bean.getDbName());
			DebugUtil.log("修改后的litepal配置文件数据库名称："+bean.getDbName());
			DebugUtil.log("修改后的litepal配置文件路径："+AppCacheDirUtil.getCacheDirPath(LitePalApplication
					.getContext())
					+ File.separator
					+ Const.LitePal.CONFIGURATION_FILE_NAME);
			xmlSerializer.endTag(null, "dbname");
			// 编写version节点
			xmlSerializer.startTag(null, "version");
			xmlSerializer.attribute(null, "value", bean.getVersion() + "");
			xmlSerializer.endTag(null, "version");
			// 编写list节点
			xmlSerializer.startTag(null, "list");
			if (bean.getClassNames().size() > 0) {
				for (String className : bean.getClassNames()) {
					// 编写mapping节点
					xmlSerializer.startTag(null, "mapping");
					xmlSerializer.attribute(null, "class", className);
					xmlSerializer.endTag(null, "mapping");
				}
			}
			xmlSerializer.endTag(null, "list");
			xmlSerializer.endTag(null, "litepal");
			xmlSerializer.endDocument();
			xmlSerializer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 解析litepal.xml文件 <b>方法描述： </b> <dd>方法作用： <dd>适用条件： <dd>执行流程： <dd>使用方法： <dd>
	 * 注意事项：
	 * 
	 * @return 返回LitePalXmlBean对象
	 * @since Met 1.0
	 * @see
	 */
	public LitePalXmlBean parseLitepalXml() {
		LitePalXmlBean litePalAttr;
		try {
			litePalAttr = new LitePalXmlBean();
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new LitePalParser().getConfigInputStream(),
					"UTF-8");
			int eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = xmlPullParser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG: {
					if (NODE_DB_NAME.equals(nodeName)) {
						String dbName = xmlPullParser.getAttributeValue("",
								ATTR_VALUE);
						litePalAttr.setDbName(dbName);
					} else if (NODE_VERSION.equals(nodeName)) {
						String version = xmlPullParser.getAttributeValue("",
								ATTR_VALUE);
						litePalAttr.setVersion(Integer.parseInt(version));
					} else if (NODE_MAPPING.equals(nodeName)) {
						String className = xmlPullParser.getAttributeValue("",
								ATTR_CLASS);
						litePalAttr.addClassName(className);
					} else if (NODE_CASES.equals(nodeName)) {
						String cases = xmlPullParser.getAttributeValue("",
								ATTR_VALUE);
						litePalAttr.setCases(cases);
					}
					break;
				}
				default:
					break;
				}
				eventType = xmlPullParser.next();
			}
		} catch (XmlPullParserException e) {
			throw new ParseConfigurationFileException(
					ParseConfigurationFileException.FILE_FORMAT_IS_NOT_CORRECT);
		} catch (IOException e) {
			throw new ParseConfigurationFileException(
					ParseConfigurationFileException.IO_EXCEPTION);
		}
		return litePalAttr;
	}

	/***
	 * <b>方法描述：复制配置文件到缓存目录 </b>
	 * <dd>方法作用： 
	 * <dd>适用条件： 
	 * <dd>执行流程： 
	 * <dd>使用方法： 
	 * <dd>注意事项： 
	 * @since Met 1.0
	 * @see
	 */
	public void copyLitepalXmlToCache() {
		AssetManager assetManager = LitePalApplication.getContext().getAssets();
		InputStream in = null;
		FileOutputStream fos = null;
		try {
			String[] fileNames = assetManager.list("");
			if (fileNames != null && fileNames.length > 0) {
				for (String fileName : fileNames) {
					if (Const.LitePal.CONFIGURATION_FILE_NAME
							.equalsIgnoreCase(fileName)) {
						in = assetManager.open(fileName,
								AssetManager.ACCESS_BUFFER);//从assets目录下读取litepal.xml输入流
					}
				}
			}

			if (in == null) {
				throw new ParseConfigurationFileException(
						ParseConfigurationFileException.CAN_NOT_FIND_LITEPAL_FILE);
			}

			File file = new File(//创建data/data/cache/litepal.xml文件
					AppCacheDirUtil.getCacheDirPath(LitePalApplication
							.getContext())
							+ File.separator
							+ Const.LitePal.CONFIGURATION_FILE_NAME);
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) != -1) {//拷贝assets下的litepal.xml到cache目录
				fos.write(buffer, 0, len);
			}
			DebugUtil.log("数据库配置文件拷贝成功！目录："+file.getAbsolutePath());
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}

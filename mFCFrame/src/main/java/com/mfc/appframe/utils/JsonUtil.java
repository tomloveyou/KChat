package com.mfc.appframe.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtil {
	  
	public static Map<String,Object> json2Map(String json) {  
		try {
			JSONObject js = new JSONObject(json);
			Map<String,Object> map = populate(js);  
			return map;  
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
    }
    
	/***
	 * 将JSONObject转换成map，如果JSONObject中包含JSONArray则将JSONArray转换成List集合，如包含JSONObject则继续转换成map
	 * @param jsonObject 需要转换的JSONObject
	 * @return 返回转换后的Map对象
	 */
	private static Map<String,Object> populate(JSONObject jsonObject) {  
		Map<String,Object> map = new HashMap<String, Object>();
        for (Iterator<String> iterator = jsonObject.keys(); iterator  
                .hasNext();) {  
            try {
            	String key = iterator.next();
				if (jsonObject.get(key).getClass().equals(JSONObject.class)) {//当key对应的值也是jsonObject时 ，采用递归算法 
				    map.put(key, populate(jsonObject.getJSONObject(key)));  
				} 
				else if (jsonObject.get(key).getClass().equals(JSONArray.class)) {//如果对应的值是jsonArray，则将其解析成list存到map中  
				    map.put(key, populateArray(jsonObject.getJSONArray(key)));  
				} else {  
				    map.put(key, jsonObject.get(key));  
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}  
        }  
  
        return map;  
    }
	
	/***
	 * 将JSONArray转换成List集合，集合中如果有JSONObject则转换成map，如果直接是Object则直接存入集合
	 * @param jsonArray
	 * @return
	 */
	public static List<Object> populateArray(JSONArray jsonArray) {  
		List<Object> list = new ArrayList<Object>();
		try {
				for (int i = 0; i < jsonArray.length(); i++)
				if (jsonArray.get(i).getClass().equals(JSONArray.class)) {  
				    list.add(populateArray(jsonArray.getJSONArray(i)));  
				} 
				else if (jsonArray.get(i).getClass().equals(JSONObject.class)) {  
				    list.add(populate(jsonArray.getJSONObject(i)));  
				} 
				else {  
				    list.add(jsonArray.get(i));  
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}  
		return list;
      }
   
}

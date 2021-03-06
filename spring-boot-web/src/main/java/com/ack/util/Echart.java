package com.ack.util;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * 
 * @author chenZhao
 * @version creatTime:	2017年2月6日	下午4:49:18
 */
public class Echart {

	/**
	 * 
	 */
	private  String chartString="";

	/**
	 * 默认初始构造折线图模板
	 */
	public Echart(){
		chartString="{title: {text: '#title#'},tooltip : {trigger: 'axis'},legend: {data:#stack#},toolbox: {feature: {saveAsImage: {}}},grid: {left: '3%',right: '4%',bottom: '3%',containLabel: true},xAxis : [{type : 'category',boundaryGap : false,data : #xAxis#}],yAxis : [{type : 'value'}],series : #stackData#}";
	}
	/**
	 * 构造自定义图形模板（根据echart3官网示例
	 * @param charS
	 */
	public Echart(String charS){
		chartString=charS;
	}
	/**
	 * replacement的value为String类型时，直接根据key替换，否则转为Json》String后再替换
	 * @param replacement
	 * @return 直接返回此次替换后的option
	 */
	public  String setOption(LinkedHashMap<String, Object> replacement){
		Set<Entry<String, Object>> entrySet=replacement.entrySet();
		Iterator<Entry<String, Object>> iterator=entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, Object>entry=(Entry<String, Object>) iterator.next();
			//如果value非String，则将value转为json再转为String
			String value=entry.getValue()+"";
			Object objValue=entry.getValue() ;
			if(!(objValue instanceof String)){
				if(objValue instanceof List||objValue instanceof Object[]){//如果是数组或集合
					JSONArray jsonArray=(JSONArray) JSONArray.toJSON(objValue);
					value=jsonArray.toString();
				}else{
					JSONObject jsonObjecct=(JSONObject) JSONObject.toJSON(entry.getValue());
					value=jsonObjecct.toString();
				}
			}
			//根据key替换Option字符串里的字符
			chartString=chartString.replace(entry.getKey()+"", value);
		}
		return chartString;
	}
	
	/**
	 * 分析组装echart折线图需要的series部分String
	 * @param varList 提示：list记录中首字段必须是lineName
	 * @return
	 */
	public static StringBuffer getLineSeriesStr(List<LinkedHashMap<String, Object>> varList) {
		StringBuffer seriesStr=new StringBuffer("[");
		for (int i = 0; i < varList.size(); i++) {
			LinkedHashMap<String, Object> row=varList.get(i);
			Set<Entry<String, Object>> entrySet=row.entrySet();
			Iterator<Entry<String, Object>> iter=entrySet.iterator();
			String lineStr="";
			ArrayList<Object> list=new ArrayList<>();
			String lineName="";
			for (int j = 0;iter.hasNext(); j++) {//组装一条折线数据
				Entry<String, Object> entry=iter.next();
				if(j==0){//map中一行记录的首值
					lineName="name:'"+entry.getValue();
				}else{
					list.add(entry.getValue());
				}
				String data=JSONArray.toJSONString(list).toString();
				lineStr="{"+lineName+"',type:'line',data:"+data+",areaStyle: {normal: {}}"+"}";
			}
			if(i==varList.size()-1)
				seriesStr.append(lineStr+"]");
			else
				seriesStr.append(lineStr+",");
		}
		return seriesStr;
	}
	
	/**
	 * 分析组装echart饼图需要的series部分String
	 * @param varList 提示：list记录中首字段必须是lineName
	 * @return
	 */
	public static StringBuffer getPieSeriesStr(List<LinkedHashMap<String, Object>> varList) {
		StringBuffer seriesStr=new StringBuffer("[");
		for (int i = 0; i < varList.size(); i++) {
			LinkedHashMap<String, Object> row=varList.get(i);
			Set<Entry<String, Object>> entrySet=row.entrySet();
			Iterator<Entry<String, Object>> iter=entrySet.iterator();
			String lineStr="";
			String lineName="";
			String lineValue="";
			for (int j = 0;iter.hasNext(); j++) {//组装一条折线数据
				Entry<String, Object> entry=iter.next();
				if(j==0){//map中一行记录的首值
					lineName="name:'"+entry.getValue();
				}else{
					lineValue=entry.getValue()+"";
				}
				lineStr="{"+lineName+"',value:"+lineValue+"}";
			}
			if(i==varList.size()-1)
				seriesStr.append(lineStr+"]");
			else
				seriesStr.append(lineStr+",");
		}
		return seriesStr;
	}
}

package com.util;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * json转换类
 */
public class JsonUtil 
{
	private static GsonBuilder gsonBuilder;
	private static Gson gson;
	
	static
	{
		gsonBuilder=new GsonBuilder();
		gsonBuilder.enableComplexMapKeySerialization();
		gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
		gson=gsonBuilder.create();
	}
	/** 获取gson对象 **/
	public static Gson getGson(){
		return gson;
	}
	
	public static String toJson(Object object)
	{
		return gson.toJson(object);
	}
	
	public static String toJson(Object[] arr)
	{
		return gson.toJson(arr);
	}
	
	public static String toJson(char[] arr)
	{
		return gson.toJson(arr);
	}
	
	public static String toJson(String[] arr)
	{
		return gson.toJson(arr);
	}
	
	public static String toJson(short[] arr)
	{
		return gson.toJson(arr);
	}
	
	public static String toJson(int[] arr)
	{
		return gson.toJson(arr);
	}
	
	public static String toJson(long[] arr)
	{
		return gson.toJson(arr);
	}
	
	public static String toJson(float[] arr)
	{
		return gson.toJson(arr);
	}
	
	public static String toJson(double[] arr)
	{
		return gson.toJson(arr);
	}
	
	public static Object fromJson(String jsonString,Class<?> _class)
	{
		return gson.fromJson(jsonString,_class);
	}
	
	/**
	 * 将Json格式的字符串转换成指定对象组成的List返回
	 * <br>例如：List<"String"> list = json2List("……", new TypeToken<"List<"String">">(){});
	 * <br>     List<"Map<"Integer, Object">"> maplist = json2List("……", new TypeToken<"List<"Map<"Integer, Object">">">(){});
	 * @param <T>   泛型标识
	 * @param jsonString    JSON数据格式字符串
	 * @param typeToken     目标类型器，标识需要转换成的目标List对象
	 * @return
	 */
	public static <T> List<T> fromJson(String jsonString, TypeToken<List<T>> typeToken){
	    Type type = typeToken.getType(); 
	    return gson.fromJson(jsonString, type); 
	}
	
	public static void main(String[] args)
	{
		Map<String, String> maps = new LinkedHashMap<String, String>();;
		maps.put("qq", "<html> fdfdfd\"</html>");
		String str=JsonUtil.toJson(maps);
		System.out.println(str);
		maps.clear();
		maps = (Map<String, String>) JsonUtil.fromJson(str, Map.class);
		System.out.println(maps.get("qq"));
	}
}

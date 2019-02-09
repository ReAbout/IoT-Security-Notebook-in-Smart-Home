package com.jd.smartcloudmobilesdk.demo.ifttt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConvertJson {

	/**
	 * 对象转换为Json
	 * @param obj 
	 * @return
	 */
	public static String object2json(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Boolean
				|| obj instanceof Short || obj instanceof Double
				|| obj instanceof Long || obj instanceof BigDecimal
				|| obj instanceof BigInteger || obj instanceof Byte) {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(array2json((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(list2json((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(map2json((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(set2json((Set<?>) obj));
		} /*else {
            json.append(bean2json(obj));
        }*/
		return json.toString();
	}

	//    /**
	//     * 对象bean转换为Json
	//     * @param bean
	//     * @return
	//     */
	//    public static String bean2json(Object bean) {
	//        StringBuilder json = new StringBuilder();
	//        json.append("{");
	//        PropertyDescriptor[] props = null;
	//        try {
	//            props = Introspector.getBeanInfo(bean.getClass(), Object.class)
	//                    .getPropertyDescriptors();
	//        } catch (IntrospectionException e) {
	//        }
	//        if (props != null) {
	//            for (int i = 0; i < props.length; i++) {
	//                try {
	//                    String name = object2json(props[i].getName());
	//                    String value = object2json(props[i].getReadMethod().invoke(
	//                            bean));
	//                    json.append(name);
	//                    json.append(":");
	//                    json.append(value);
	//                    json.append(",");
	//                } catch (Exception e) {
	//                }
	//            }
	//            json.setCharAt(json.length() - 1, '}');
	//        } else {
	//            json.append("}");
	//        }
	//        return json.toString();
	//    }

	/**
	 * List集合转换为Json
	 * @param list
	 * @return
	 */
	public static Object list2json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json;
	}

	/**
	 * 对象数组转换为Json
	 * @param array
	 * @return
	 */
	public static String array2json(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * Map集合转换为Json
	 * @param map
	 * @return
	 */
	public static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * Json转map
	 * @param map
	 * @return
	 */
	public static Map<String, Object> parseJSON2Map(String jsonStr) throws Exception{  
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Gson gson = new Gson();
		Map<String, Object> map = gson.fromJson(jsonStr, type);
		for(Object k : map.keySet()){  
			Object v = map.get(k);   
			//如果内层还是数组的话，继续解析  
			if(v instanceof JSONArray){  
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
				Iterator<JSONObject> it = ((List)v).iterator();  
				while(it.hasNext()){  
					JSONObject json2 = it.next();  
					list.add(parseJSON2Map(json2.toString()));  
				}  
				map.put(k.toString(), list);  
			} else {  
				map.put(k.toString(), v);  
			}  
		}  
		return map;  
	}  

	/**
	 * 解析map
	 * @param map
	 * @return
	 */
	public static Map<String, String> parseMap(String jsonStr){
		Type type = new TypeToken<Map<String, String>>() {}.getType();
		Gson gson = new Gson();
        String str="";
        int i=0;
        int j=0;
        if(jsonStr.contains("/")) {
            i =jsonStr.indexOf("/");
            j =jsonStr.indexOf("=");
            i =i-j-1;
          str =  jsonStr.replace("/", "");
        }else{
            str =  jsonStr;
        }
        Map<String, String> map =null;
        try {
            map = gson.fromJson(str, type);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (map!=null){
            for(String k : map.keySet()){
                String v = map.get(k);
                if(jsonStr.contains("/")) {
                    v = v.substring(0, i) + "/" + v.substring(i, v.length());
                }
                map.put(k, v);
            }
        }
        return map;
    }

    /**
	 * Set集合转为Json
	 * @param set
	 * @return
	 */
	public static String set2json(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * 字符串转换为Json
	 * @param s
	 * @return
	 */
	public static String string2json(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
}

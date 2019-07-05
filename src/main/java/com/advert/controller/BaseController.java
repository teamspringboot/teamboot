package com.advert.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.advert.exception.Response;
//import com.advert.mapper.CustomObjectMapper;
import com.advert.pageing.AppServiceHelper;
import com.advert.pageing.BaseService;
import com.advert.util.StringUtil;
import com.advert.util.entity.AbstractEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;


public class BaseController<T>  {

	protected HttpServletRequest request;  
    protected HttpServletResponse response;  
    protected HttpSession session; 
	
	public static final String STATUS = "status";
	public static final String WARN = "warn";
	public static final String MESSAGE = "message";

	protected Map<String, String> qm = new HashMap<String, String>();
	// the model
	protected T entity;

	// model's Class
	protected Class<T> entityClass;

	// model's ClassName
	protected String entityClassName;

	// list页面显示的对象列表
	protected List<T> entities;

	// logger for subclass
	private static final  Logger logger = LoggerFactory.getLogger(BaseController.class);

	public Map<String, String> getQm() {
		return qm;
	}

	public void setQm(Map<String, String> qm) {
		this.qm = qm;
	}
	
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	
	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * 实现ModelDriven接口方法，返回模型对象
	 * @see ModelDriven
	 */
	public T getModel() {
		return entity;
	}
	// 获取Application
	// protected ServletContext getApplication() {
	// return ServletActionContext.getServletContext();
	// }

	@SuppressWarnings("unchecked")
	public BaseController() {
		super();
		// 通过反射取得Entity的Class.
		try {
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			entityClassName = entityClass.getSimpleName();
		} catch (RuntimeException e) {
			//logger.error("error detail:", e);
		}

	}

	/**
	 * 根据字符串输出JSON
	 * 
	 * @param jsonStr
	 * @return
	 */
	protected String ajaxJson(String jsonStr) {
		return ajax(jsonStr, "text/json");
	}

	/**
	 * AJAX输出，返回输出内容
	 * 
	 * @param content
	 * @param type
	 * @return
	 */
	private String ajax(String content, String type) {
		response.setContentType(type + ";charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			Writer writer = response.getWriter();
			try {
				logger.debug("content" + content);
				writer.write(content);
			} finally {
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}
	protected Map<String, Object> convertParams(Map<String, String> qm) {
		Map<String, Object> params = new HashMap<String, Object>();
		Set<Map.Entry<String, String>> p = qm.entrySet();
		for (Map.Entry<String, String> me : p) {
			String name = me.getKey();
			String value = me.getValue();
			if (StringUtils.isNotEmpty(value)) {
				params.put(name, value);
			}
		}
		return params;
	}
	/**
	 * 将查询结果生成Json串返回到客户端Response流中
	 * 
	 * @param object 对象
	 * @param config Json或Gson对象
	 * @param wrapper 是否用TotalJson包装类输出
	 * @param <T> 实体对象类型
	 * @param <C> JsonConfig或GsonConfig
	 * @return
	 */
	protected <T, C> String ajaxJson(Object object, C config, boolean wrapper) {
		// if (null != object)
		// {
		// if (config instanceof JsonConfig)
		// {
		// return ajax(ExtHelper.getJsonFromList(object, (JsonConfig) config, wrapper), "text/json");
		// }
		// else if (config instanceof GsonConfig)
		// {
		// return ajax(ExtHelper.getGsonFromList(object, (GsonConfig) config, wrapper), "text/json");
		// }
		// }
		return "";
	}
	
	protected Gson gson = new GsonBuilder()
	.registerTypeAdapter(BigDecimal.class, new JsonDeserializer<BigDecimal>() {
		@Override
		public BigDecimal deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
				throws JsonParseException {
			try {
				String dateString=json.getAsString();
				if(org.apache.commons.lang.StringUtils.isNotBlank(dateString)){
					return new BigDecimal(dateString);
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}
	})
	.registerTypeAdapter(Long.class, new JsonDeserializer<Long>() {
		@Override
		public Long deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
				throws JsonParseException {
			try {
				String dateString=json.getAsString();
				if(org.apache.commons.lang.StringUtils.isNotBlank(dateString)){
					return new Long(dateString);
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}
	})
	.registerTypeAdapter(Integer.class, new JsonDeserializer<Integer>() {
		@Override
		public Integer deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
				throws JsonParseException {
			try {
				String dateString=json.getAsString();
				if(org.apache.commons.lang.StringUtils.isNotBlank(dateString)){
					return new Integer(dateString);
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}
	})
	.registerTypeAdapter(Double.class, new JsonDeserializer<Double>() {
		@Override
		public Double deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
				throws JsonParseException {
			try {
				String dateString=json.getAsString();
				if(org.apache.commons.lang.StringUtils.isNotBlank(dateString)){
					return new Double(dateString);
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}
	})
	.registerTypeAdapter(byte[].class, new JsonDeserializer<byte[]>() {
		public byte[] deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
				throws JsonParseException {
			try {
				String str=json.getAsString();
				if(org.apache.commons.lang.StringUtils.isNotBlank(str)){
					return str.getBytes("UTF-8");
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}
	})
	.registerTypeAdapter(Boolean.class, new JsonDeserializer<Boolean>() {
		public Boolean deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
				throws JsonParseException {
			try {
				String str=json.getAsString();
				if(org.apache.commons.lang.StringUtils.isNotBlank(str)){
					return new Boolean(str);
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}
	})
	.registerTypeAdapter(boolean.class, new JsonDeserializer<Boolean>() {
		public Boolean deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
				throws JsonParseException {
			try {
				String str=json.getAsString();
				if(org.apache.commons.lang.StringUtils.isNotBlank(str)){
					return new Boolean(str).booleanValue();
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		}
	})
	.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
				dfDate=new SimpleDateFormat("yyyy-MM-dd");

		@Override
		public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
				throws JsonParseException {
			try {
				String dateString=json.getAsString();
				if(org.apache.commons.lang.StringUtils.isNotEmpty(dateString)){
					if(dateString.length()==10){
						return dfDate.parse(dateString);
					}else{
						return df.parse(json.getAsString());
					}
				}
				return null;
			} catch (ParseException e) {
				return null;
			}
		}
	}).setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	/**
	 * 将json数据写入此次响应流中
	 * 
	 * @param str json数据
	 */
	protected void jsonWriter(String str) {
		Writer writer = null;
		try {
			try {
				response.setCharacterEncoding("UTF-8");
				writer = response.getWriter();
				writer.write(str);
			} finally {
				writer.flush(); // 强制输出所有内容
				writer.close(); // 关闭
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将查询结果集响应回视图
	 * 
	 * @param list 结果集
	 * @param <T> 实体对象类型
	 * 
	 * @return json
	 */
	protected <T> String ajaxJson(List<T> list) {
		String strs = "{\"results\":" + list.size() + ",\"items\":" + gson.toJson(list) + ",\"success\":true}";
		jsonWriter(strs);
		return strs;

	}

	//[20140909]add by liuxing 增加java.util.Set类型的json转换方法 Start
	protected <V> String ajaxJson(Set<V> list) {
		String strs = "{\"results\":" + list.size() + ",\"items\":" + gson.toJson(list) + ",\"success\":true}";
		jsonWriter(strs);
		return strs;
	}
	
	protected <V> String ajaxJson(Page<T> page) {
		String strs = "{\"results\":" + page.getResult().size() + ",\"totalProperty\":" + page.getTotal() + ",\"items\":"
				+ gson.toJson( page.getResult()) + ",\"success\":true}";
		jsonWriter(strs);
		return strs;
	}
	//[20140909]add by liuxing 增加java.util.Set类型的json转换方法 End
	
	/**
	 * 操作失败结果信息
	 * 
	 * @param list 结果集
	 * @param <T> 实体对象类型
	 * 
	 * @return json
	 */
	protected String ajaxJson(boolean result, String message) {
		String strs = "{\"success\":" + result + ",\"message\":\"" + message + "\"}";
		logger.debug(strs);
		jsonWriter(strs);
		return strs;
	}
	/**
	 * 操作失败结果信息
	 * 
	 * @param list 结果集
	 * @param <T> 实体对象类型
	 * 
	 * @return json
	 */
	protected String ajaxJson(boolean result,String message,String resultData) {
		String strs = "{\"success\":" + result + ",\"message\":\"" + message + "\",\"resultData\":\"" +resultData + "\"}";
		logger.debug(strs);
		jsonWriter(strs);
		return strs;
	}
	
	protected String ajaxJson(boolean result,String message,JSONObject resultData) {
		String strs = "{\"success\":" + result + ",\"message\":\"" 
				+ (StringUtils.isNotBlank(message)?message:"") + "\",\"resultData\":" +resultData.toString() + "}";
		logger.debug(strs);
		jsonWriter(strs);
		return strs;
	}
	/**
	 * 读取request中的json信息
	 * 
	 * @return jsonStr
	 */
	public String getJsonFromRequest() {
		String jsonStr = null;
		try {
			BufferedReader br = request.getReader();
			jsonStr = br.readLine();
		} catch (IOException e) {
			logger.error("BaseController|getJsonFromRequest() Exception: " + e.getMessage());
			return "";
		}
		logger.debug(jsonStr);
		return jsonStr;
	}

	/**
	 * 将json解析成相应实体对象
	 * 
	 * @param json json数据
	 * @param cla 实体类
	 * @param <T> 实体对象类型
	 * @return 实体对象
	 */
    protected <T> T jsonToObject(String json, Class<T> cla)
    {
        if (StringUtils.isNotEmpty(json) && cla != null)
        {
            return gson.fromJson(json, cla);
        }
        return null;
    }
    
    //[20140902]add by lx start
    /**
     * gson将json字符串转化为json数组
     * @param json json字符串
     * @param cla 转化成的List的泛型类型
     * @return java.util.ArrayList<V>
     */
    protected <V> List<V> jsonToList(String json,Class<V> cla){
    	if (StringUtils.isNotEmpty(json) && cla != null)
        {
            return gson.fromJson(json, new TypeToken<List<V>>(){}.getType());
        }
        return null;
    }
  //[20140902]add by lx end
//    @Autowired
//    private CustomObjectMapper customObjectMapper;
    
	/**
	 * 将 json 解析成相应实体对象集合
	 * 对 jsonToObject() 进行了功能扩展，该方法可以获得实体对象的集合
	 * 
	 * @param json json数据
	 * @param cla 实体类
	 * @param <T> 实体对象类型
	 * @return 实体对象集合
	 */
	protected List<T> getObjectsFromJsonByObjectMapper(String json, Class<T> cla) {
		if (StringUtils.isNotEmpty(json) && cla != null) {
			try {
				List<T> list = new ArrayList<T>();
				String[] strs = new String[]{json}; // 多条记录Json串，分割成单独的。将每一条记录作为一个数组元素

				if (json.startsWith("[") && json.endsWith("]")) // 删除多条记录会进入此分支
				{
					// substring(1,json.length()-1)去除字符串中的“【” 和 “】”。新字符串从原串的第二个开始，倒数第二个结束
					// "outTime":"1"},{"id":"0001", 两条记录间的连接方式： },{ 。
					// 目的要将多条记录分割成数组，将 “,” 替换成 “$” 。故采用正则表达式转换连接方式为： }${ 。
					// \\ 正则的特殊字符的表示
					// 局部测试代码
					// System.out.println(json.substring(1,json.length()-1).replaceAll(a,b ));
					// //结果："outTime":"1"}${"id":"4028800

					String a = "\\},\\{";
					String b = "\\}\\$\\{"; // String b = "\\}$\\{" ; 这种做法是错误的
											// replaceAll()方法出现异常：java.lang.IllegalArgumentException: Illegal group
											// reference
					// 原因是第一个参数支持正则表达式，replacement中出现“$”,会按照$1$2的分组
					// 模式进行匹配，当编译器发现“$”后跟的不是整数的时候，就会抛出“非法的组引用”的异常。
					// 所以我们在使用replaceAll(regex, replacement)函数的时候要特别小心。如果真的要把String中的字符替
					// 换成"$AAA"的话，可以对replacement 进行“$”的转义处理。

					strs = json.substring(1, json.length() - 1).replaceAll(a, b).split("\\$");

					// 局部测试代码如下：
					// for(int i=0 ; i < strs.length ; i++)
					// {
					// System.out.println("strs[" + i +"]" + strs[i]);
					// }
					// System.out.println("strs.length = " + strs.length);

				} else if (json.startsWith("{") && json.endsWith("}")) // 删除一条记录会进入此分支
				{
					strs = json.split("$"); // 因为没有$这个字符，故字符串会被整体分割成一个数组
				}

				for (String str : strs) {
					/** 使用customObjectMapper将json转化为object
					  * 原因：action与前台js传值转json方式保持一致
					  */
					list.add(gson.fromJson(str, cla));
				}
				return list;

			} catch (Exception e) {
				System.out.println("BaseController : getListObjectsFromJson() function Excetion!");
				e.printStackTrace();
			}
		}
		return null;
	}
    
	/**
	 * 将 json 解析成相应实体对象集合
	 * 对 jsonToObject() 进行了功能扩展，该方法可以获得实体对象的集合
	 * 
	 * @param json json数据
	 * @param cla 实体类
	 * @param <T> 实体对象类型
	 * @return 实体对象集合
	 */
	protected List<T> getListObjectsFromJson(String json, Class<T> cla) {
		if (StringUtils.isNotEmpty(json) && cla != null) {
			try {
				List<T> list = new ArrayList<T>();
				String[] strs = new String[]{json}; // 多条记录Json串，分割成单独的。将每一条记录作为一个数组元素

				if (json.startsWith("[") && json.endsWith("]")) // 删除多条记录会进入此分支
				{
					// substring(1,json.length()-1)去除字符串中的“【” 和 “】”。新字符串从原串的第二个开始，倒数第二个结束
					// "outTime":"1"},{"id":"0001", 两条记录间的连接方式： },{ 。
					// 目的要将多条记录分割成数组，将 “,” 替换成 “$” 。故采用正则表达式转换连接方式为： }${ 。
					// \\ 正则的特殊字符的表示
					// 局部测试代码
					// System.out.println(json.substring(1,json.length()-1).replaceAll(a,b ));
					// //结果："outTime":"1"}${"id":"4028800

					String a = "\\},\\{";
					String b = "\\}\\$\\{"; // String b = "\\}$\\{" ; 这种做法是错误的
											// replaceAll()方法出现异常：java.lang.IllegalArgumentException: Illegal group
											// reference
					// 原因是第一个参数支持正则表达式，replacement中出现“$”,会按照$1$2的分组
					// 模式进行匹配，当编译器发现“$”后跟的不是整数的时候，就会抛出“非法的组引用”的异常。
					// 所以我们在使用replaceAll(regex, replacement)函数的时候要特别小心。如果真的要把String中的字符替
					// 换成"$AAA"的话，可以对replacement 进行“$”的转义处理。

					strs = json.substring(1, json.length() - 1).replaceAll(a, b).split("\\$");

					// 局部测试代码如下：
					// for(int i=0 ; i < strs.length ; i++)
					// {
					// System.out.println("strs[" + i +"]" + strs[i]);
					// }
					// System.out.println("strs.length = " + strs.length);

				} else if (json.startsWith("{") && json.endsWith("}")) // 删除一条记录会进入此分支
				{
					strs = json.split("$"); // 因为没有$这个字符，故字符串会被整体分割成一个数组
				}

				for (String str : strs) {
					list.add(gson.fromJson(str, cla));
				}
				return list;

			} catch (Exception e) {
				System.out.println("BaseController : getListObjectsFromJson() function Excetion!");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	protected List<T> getListObjectsFromJsonByJsonObject(String json, Class<T> cla) {
		if (StringUtils.isNotEmpty(json) && (cla != null)) {
			try {
				List<T> list = new ArrayList<T>();
				String[] strs = new String[]{json};

				if (json.startsWith("[") && json.endsWith("]")) {

					String a = "\\},\\{";
					String b = "\\}\\$\\{";

					strs = json.substring(1, json.length() - 1).replaceAll(a, b).split("\\$");

				} else if (json.startsWith("{") && json.endsWith("}")) // 删除一条记录会进入此分支
				{
					strs = json.split("$"); // 因为没有$这个字符，故字符串会被整体分割成一个数组
				}

				for (String str : strs) {
					JSONObject jsonObj = JSONObject.fromObject(str);

					list.add((T) jsonObj.toBean(jsonObj, entityClass));
				}
				return list;

			} catch (Exception e) {
				System.out.println("BaseController : getListObjectsFromJson() function Excetion!");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	// 保存数据
	@RequestMapping("/addBatchFromJson")
	public Response add() {
		String jsonStr = getJsonFromRequest();
		List<T> entityLs = getObjectsFromJsonByObjectMapper(jsonStr, entityClass);
		for (T entity : entityLs) {
			AbstractEntity ae = (AbstractEntity)entity;
			ae.setCreatedDt(new Date());
		}
		getBaseService().insertAll(entityLs);
		return new Response().success(AppServiceHelper.getMessage("operateSuccess", new String[]{}, request.getLocale()));
	}
	
	// 更新数据
	@RequestMapping("/updateBatchFromJson")
		public Response update() {
			String jsonStr = getJsonFromRequest();
			List<T> entityLs = getObjectsFromJsonByObjectMapper(jsonStr, entityClass);
			getBaseService().updateAll(entityLs);
			return new Response().success(AppServiceHelper.getMessage("operateSuccess", new String[]{}, request.getLocale()));
		}

	
	protected <V> List<V> jsonToObectList(String json, Class<V> cla) {
		if (StringUtils.isNotEmpty(json) && cla != null) {
			try {
				List list = new ArrayList();
				String[] strs = new String[]{json}; // 多条记录Json串，分割成单独的。将每一条记录作为一个数组元素
				if (json.startsWith("[") && json.endsWith("]")) // 删除多条记录会进入此分支
				{
					
					String a = "\\},\\{";
					String b = "\\}\\$\\{"; 

					strs = json.substring(1, json.length() - 1).replaceAll(a, b).split("\\$");

				} else if (json.startsWith("{") && json.endsWith("}")) // 删除一条记录会进入此分支
				{
					strs = json.split("$"); // 因为没有$这个字符，故字符串会被整体分割成一个数组
				}

				for (String str : strs) {
					list.add(gson.fromJson(str, cla));
				}
				return list;

			} catch (Exception e) {
				System.out.println("BaseController : getListObjectsFromJson() function Excetion!");
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 返回一个Json串
	 * 
	 * @param list
	 * @param <T>
	 * @return
	 */
	public <T> String jsonString(List<T> list) {
		String strs = "\"data\":" + gson.toJson(list) + ",\"success\":true}";

		return strs;
	}



	/**
	 * 分页查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/findByPage")
	public PageInfo<T> findByPage() throws Exception {
		Map<String, Object> params = preparePageParams();
		PageInfo<T> page = getBaseService().findByPage(params);	
		return page; 
	}
	
	
	@RequestMapping("/findByParams")
	public Response findByParams() throws Exception {
		Map<String, Object> params = preparePageParams();
		List<T> list = getBaseService().findByParams(params);
		return new Response().success(list); 
	}
	
	/**
	 * 组装查询参数
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected Map<String, Object> preparePageParams() {
		Map<String, Object> params = this.convertParams(this.getQm());
		String sortStr = (String)request.getParameter("sort");
		if(null!=sortStr){
			sortStr = sortStr.substring(1,sortStr.length()-1);
			Map<String,String> sortMap =jsonToObject(sortStr, Map.class);
			//设置排序参数
			params.put("order", StringUtil.str2DbColumn(sortMap.get("property")) + " " + sortMap.get("direction"));
		}
		return params;
	}

	public Map<String, Object> prepareBusinessParams(){
		Map<String, Object> params = preparePageParams();
		return params;
	}
	
	/**
	 * 不分页查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/read")
	public Response read() throws Exception {
		Map<String, Object> params = preparePageParams();
		List<T> entityLs = getBaseService().find(params);
		return new Response().success(entityLs); 
	}

	/**
	 * 取得Service接口对象
	 * 默认以实体对象类名的前缀小写形式+Service获取对应的Service对象实例
	 * 详见初始化构造函数
	 */
	@SuppressWarnings("unchecked")
	protected BaseService<T> getBaseService() {
		BaseService<T> baseService = (BaseService<T>) findBean(StringUtils.uncapitalize(entityClassName)
				+ "ServiceImpl");
		logger.debug("baseService=" + baseService);
		return baseService;
	}

	protected Object findBean(String beanId) {
		return AppServiceHelper.findBean(beanId);
	}
	
	// 调阅数据
	@RequestMapping("/findAll")
	public Response findAll() {
		List<T> entityLs = getBaseService().findAll();
		return new Response().success(entityLs); // 返回一个String类型的Json串
	}
	
	// 保存数据
	@RequestMapping("/add")
	public Response add(@RequestBody T entity) {
		getBaseService().insert(entity);
		return new Response().success(AppServiceHelper.getMessage("operateSuccess", new String[]{}, request.getLocale()));
	}

	// 批量保存数据
	@RequestMapping("/addBatch")
	public Response addBatch(@RequestBody List<T> entityLs) {
		getBaseService().insertAll(entityLs);
		return new Response().success(AppServiceHelper.getMessage("operateSuccess", new String[]{}, request.getLocale()));
	}
		
	// 批量更新数据
	@RequestMapping("/update")
	public Response update(@RequestBody T entity) {
		AbstractEntity ae = (AbstractEntity)entity;
		ae.setUpdatedDt(new Date());	
		getBaseService().update(entity);
		return new Response().success(AppServiceHelper.getMessage("operateSuccess", new String[]{}, request.getLocale()));
	}
	
	// 更新数据
	@RequestMapping("/updateBatch")
	public Response updateBatch(@RequestBody List<T> entityLs) {
		for (T entity : entityLs) {
			AbstractEntity ae = (AbstractEntity)entity;
			ae.setUpdatedDt(new Date());		
		}
		getBaseService().updateAll(entityLs);
		return new Response().success(AppServiceHelper.getMessage("operateSuccess", new String[]{}, request.getLocale()));
	}

	// 删除数据
	@RequestMapping("/destroy")
	public Response destroy() {
		String jsonStr = getJsonFromRequest();
		List<T> entityLs = getListObjectsFromJson(jsonStr, entityClass);
		getBaseService().deleteAll(entityLs);
		return new Response().success(AppServiceHelper.getMessage("operateSuccess", new String[]{}, request.getLocale()));
	}
	
	//与前台数据展示区域绑定的数据汇总
	@RequestMapping("/getQuerySummary")
	public Response getQuerySummary() {
		Map<String, Object> params = preparePageParams();
		Map<String, Object> summaryMap = getBaseService().getQuerySummary(params);
		return new Response().success(summaryMap);
	}


	@RequestMapping("/getWorkflowVariables")
	public Map<String, Object> getWorkflowVariables(HttpServletRequest request)
			throws ParseException {
		Map<String, Object> variables = new HashMap<String, Object>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
		    String parameterName = (String) parameterNames.nextElement();
		    if (parameterName.startsWith("p_")) {
		        // 参数结构：p_B_name，p为参数的前缀，B为类型，name为属性名称
		        String[] parameter = parameterName.split("_");
		        if (parameter.length == 3) {
		            String paramValue = request.getParameter(parameterName);
		            Object value = paramValue;
		            if (parameter[1].equals("B")) {
		                value = BooleanUtils.toBoolean(paramValue);
		            } else if (parameter[1].equals("DT")) {
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		                value = sdf.parse(paramValue);
		            }
		            variables.put(parameter[2], value);
		        } else {
		            throw new RuntimeException("invalid parameter for activiti variable: " + parameterName);
		        }
		    }
		}
		return variables;
	}
	
    @ModelAttribute  
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){  
        this.request = request;  
        this.response = response;  
        this.session = request.getSession();  
    } 
    
	@ModelAttribute
	public void setQueryParam(HttpServletRequest request) {
		Map<String, String> qm = new HashMap<String, String>();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = (String) parameterNames.nextElement();
			if (parameterName.startsWith("qm.")) {
				String paramValue = request.getParameter(parameterName);
				String[] parameter = StringUtils.split(parameterName,".");
				if (parameter.length == 2) {
					qm.put(parameter[1], paramValue);
				}
			}
		}
		this.qm = qm;
	}
	protected String ajaxJsonWithMetaSuccess(String jsonStr) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"meta\":{\"success\":true,\"message\":\"ok\"},");
		sb.append("data:");
		sb.append(jsonStr);
		sb.append("}");
		return ajax(sb.toString(), "text/json");
	}
}

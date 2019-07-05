package com.advert.pageing;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.advert.exception.DaoException;
import com.advert.util.ExceptionUtil;
import com.advert.util.StringUtil;
import com.advert.util.entity.AbstractEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class PageingService<T extends AbstractEntity> implements BaseService<T>{
	public Class<T> entityClass;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public String entityClassName;
	

	
	protected Object selfProxy() {
		return AppServiceHelper.findBean(StringUtils.uncapitalize(entityClassName) + "ServiceImpl");
	}

	public void updateSuper() {
		logger.debug("updateSuper");
	}
	@SuppressWarnings("unchecked")
	public PageingService() {
		try {
			Object genericClz = getClass().getGenericSuperclass();
			if (genericClz instanceof ParameterizedType) {
				entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
						.getActualTypeArguments()[0];
				entityClassName = entityClass.getSimpleName();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	public static String uncapitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
            .append(Character.toLowerCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
	
	@SuppressWarnings("unchecked")
	public void BaseCRUDService() {
		try {
			Object genericClz = getClass().getGenericSuperclass();
			if (genericClz instanceof ParameterizedType) {
				entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
						.getActualTypeArguments()[0];
				entityClassName = entityClass.getSimpleName();
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	protected BaseMapper<T> getBaseMapper() {
		BaseMapper<T> baseMapper = null;
		if (this.entityClass != null) {
			baseMapper = (BaseMapper<T>) AppServiceHelper
					.findBean(uncapitalize(entityClassName) + "Mapper");
		}
		return baseMapper;
	}
	@Override
	public List<T> find(Map<String, Object> params) {
		List<T> list = null;
		try{
			list = getBaseMapper().find(params);
		}catch(Exception e){
			throw new DaoException("PageingService->find()", new String[]{e.getMessage()}, e);
		}
		return list;
	}

	
	
	@Override
	public void delete(T entity) {
		try {
			getBaseMapper().delete(entity);
		} catch (DataIntegrityViolationException e) {
			logger.error("DataIntegrityViolationException", e);
			throw new DaoException("DataIntegrityViolationException", new String[] { entity.getId().toString(),
					"delete", ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		} catch (Exception e) {
			logger.error("Other Exception", e);
			throw new DaoException("DeleteDataException", new String[] { entity.getId().toString(), "delete",
					ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		}
		
	}

	@Override
	public int update(T entity) {
		int count;
		try {
			entity.setUpdatedDt(new Date());
			count = getBaseMapper().update(entity);
			if (count == 0) {
				throw new DaoException("DataNotFoundException", new String[] { entity.getId().toString(), "update" },
						null);
			}
			entity.setVersion(entity.getVersion() + 1);
			return count;
		} catch (DuplicateKeyException e) {
			logger.error("DuplicateKeyException", e);
			throw new DaoException("DuplicateKeyException", new String[] { entity.getId().toString(), "update",
					ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		} catch (DataIntegrityViolationException e) {
			logger.error("DataIntegrityViolationException", e);
			throw new DaoException("DataIntegrityViolationException", new String[] { entity.getId().toString(),
					"update", ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		} catch (Exception e) {
			logger.error("Other Exception", e);
			throw new DaoException("UpdateDataException", new String[] { entity.getId().toString(), "update",
					ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		}
	}
 
	@Override
	public PageInfo<T> findByPage(Map<String,Object> paramMap) {
		PageInfo<T> pageInfo = null;
		try{
			int firstRow = (Integer.parseInt(paramMap.get("firstRow").toString()));
			int pageSize = (Integer.parseInt(paramMap.get("pageSize").toString()));
			PageHelper.startPage(firstRow,pageSize);
			List<T> list = getBaseMapper().find(paramMap);
			pageInfo = new PageInfo<T>(list);
		}catch(Exception e){
			throw new DaoException("PageingService->findByPage", new String[]{e.getMessage()}, e);
		}
		return pageInfo;
	}
	public List<T> findByParams(Map<String, Object> params) {
		try {
			List<T> list = getBaseMapper().find(params);
			return list;
		} catch (Exception e) {
			throw new DaoException("DataAccessException", new String[] { params.toString(), "findByParams",
					ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		}
	}
	@Override
	public Long insert(T entity) {
		try {

			if (entity.getCreatedDt() == null) {
				entity.setCreatedDt(new Date());
			}

			getBaseMapper().insert(entity);
			return entity.getId();
		} catch (DuplicateKeyException e) {
			logger.error("DuplicateKeyException", e);
			throw new DaoException("DuplicateKeyException",
					new String[] { entity.getId() == null ? "null" : entity.getId().toString(), "insert",
							ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) },
					e);
		} catch (Exception e) {
			logger.error("Other", e);
			throw new DaoException("InsertDataException",
					new String[] { entity.getId() == null ? "null" : entity.getId().toString(), "insert",
							ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) },
					e);
		}
	}
	@Override
	public int deleteByPrimaryKey(Long sid) {
		try {
			int count = getBaseMapper().deleteByPrimaryKey(sid);
			return count;
		} catch (Exception e) {
			logger.error("deleteByPrimaryKey Exception", e);
			throw new DaoException("DeleteDataException", new String[] { sid.toString(), "deleteByPrimaryKey",
					ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		}
	}
	@Override
	public void deleteAll(Collection<T> collection) {
		for (T entity : collection) {
			this.delete(entity);
		}
		
	}
	@Override
	public void deleteAllByPrimaryKey(Collection<Long> sids) {
		try {
			getBaseMapper().deleteAllByPrimaryKey(sids);
		} catch (Exception e) {
			logger.error("deleteAllByPrimaryKey", e);
			throw new DaoException("DeleteAllException",
					new String[] { Arrays.toString(sids.toArray()), "deleteAllByPrimaryKey",
							ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) },
					e);
		}
		
	}
	@Override
	public int deleteByParams(Map<String, Object> params) {
		try {
			int count = getBaseMapper().deleteByParams(params);
			return count;
		} catch (Exception e) {
			logger.error("deleteByParams", e);
			throw new DaoException("DeleteDataException", new String[] { params.toString(), "deleteByParams",
					ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		}
	}
	@Override
	public List<T> findAll() {
		try {
			List<T> list = getBaseMapper().find(null);
			return list;
		} catch (Exception e) {
			logger.error("findAll", e);
			throw new DaoException("DataAccessException", new String[] { "", "findAll",
					ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		}
	}
	@Override
	public void insertAll(Collection<T> collection) {
		for (T entity : collection) {	
			entity.setCreatedDt(new Date());
			this.insert(entity);
		}
		
	}
	@Override
	public void updateAll(Collection<T> collection) {
		for (T entity : collection) {
			this.update(entity);
		}
		
	}
	@Override
	public T findByPrimaryKey(Long sid) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sid", sid);
			List<T> list = getBaseMapper().find(params);
			if (list != null && list.size() > 0) {
				if (list.size() == 1) {
					return list.get(0);
				} else {
					throw new DaoException("MultiDataFoundException",
							new String[] { sid.toString(), "findByPrimaryKey" }, null);
				}
			} else {
				return null;
			}
		} catch (DaoException e) {
			logger.error("DaoException", e);
			throw e;
		} catch (Exception e) {
			logger.error("Other Exeption", e);
			throw new DaoException("DataAccessException", new String[] { sid.toString(), "findByPrimaryKey",
					ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		}
	}
	@Override
	public List<T> findByPrimaryKeys(Long[] sids) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sids", sids);
			List<T> list = getBaseMapper().find(params);
			return list;
		} catch (Exception e) {
			logger.error("findByPrimaryKeys", e);
			throw new DaoException("DataAccessException", new String[] { Arrays.toString(sids), "findByPrimaryKeys",
					ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		}
	}
	@Override
	public T findUniqueByParams(Map<String, Object> params) {
		try {
			List<T> list = getBaseMapper().find(params);
			if (list == null || list.size() == 0) {
				return null;
			} else if (list.size() == 1) {
				return list.get(0);
			} else {
				throw new DaoException("MultiDataFoundException",
						new String[] { params.toString(), "findUniqueByParams" }, null);
			}
		} catch (DaoException e) {
			logger.error("findUniqueByParams", e);
			throw e;
		} catch (Exception e) {
			logger.error("Other Exception", e);
			throw new DaoException("DataAccessException", new String[] { params.toString(), "findUniqueByParams",
					ExceptionUtil.getExceptionStackTrace(e, ExceptionUtil.ExceptionLogLength) }, e);
		}
	}
	
	public void insertHistory(Map<String, Object> params) {
		getBaseMapper().insertHistory(params);
	}
	/**
	 * 与前台数据展示区域绑定的数据汇总接口
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> getQuerySummary(Map<String, Object> params) {
		Map<String, Object> summaryResult = new HashMap<String, Object>();
		Map<String, Object> summaryMap = getBaseMapper().getQuerySummary(params);
		Iterator it = summaryMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = (Entry<String, Object>) it.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			summaryResult.put(StringUtil.underlineToCamel(key), value);
		}
		return summaryResult;
	}
	/**
	 * 找到旧实体
	 * 
	 * @param params
	 * @return
	 */
	public T findOldEntity(Collection<T> collection, T entity) {
		for (T e : collection) {
			if (e.getId() == 0 - entity.getId()) {
				return e;
			}
		}
		return null;
	}
}
package com.advert.pageing;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.advert.exception.DaoException;
import com.advert.util.ExceptionUtil;
import com.advert.util.entity.AbstractEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class PageingService<T extends AbstractEntity> implements BaseService<T>{
	public Class<T> entityClass;

	public String entityClassName;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public int update(T entity) {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int deleteByPrimaryKey(Long sid) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void deleteAll(Collection<T> collection) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void deleteAllByPrimaryKey(Collection<Long> sids) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int deleteByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void insertAll(Collection<T> collection) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateAll(Collection<T> collection) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public T findByPrimaryKey(Long sid) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<T> findByPrimaryKeys(Long[] sids) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public T findUniqueByParams(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Map<String, Object> getQuerySummary(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}
}

package com.advert.mapper;

import java.util.Map;

import com.advert.entity.Users;

public interface UsersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Users record);

    int insertSelective(Users record);

    public Users selectByPrimaryKey(Map<String,Object> map);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);
}
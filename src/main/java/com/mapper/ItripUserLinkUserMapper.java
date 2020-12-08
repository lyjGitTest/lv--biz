package com.mapper;

import com.po.ItripUserLinkUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItripUserLinkUserMapper {

        public ItripUserLinkUser getItripUserLinkUserById(@Param(value = "id") Long id);

        public List<ItripUserLinkUser> getItripUserLinkUserByUserId(@Param(value = "userId") Long userId)throws Exception;

        /**查询所有常用联系人和条件查询**/
        public List<ItripUserLinkUser>	getItripUserLinkUserListByMap(Map<String, Object> param)throws Exception;
        public Integer getItripUserLinkUserCountByMap(Map<String, Object> param)throws Exception;
        /**新增常用联系人**/
        public Integer insertItripUserLinkUser(ItripUserLinkUser itripUserLinkUser)throws Exception;
        /**修改常用联系人**/
        public Integer updateItripUserLinkUser(ItripUserLinkUser itripUserLinkUser)throws Exception;
        /**删除常用联系人**/
        public Integer deleteItripUserLinkUserByIds(@Param(value = "ids") Long[] ids)throws Exception;
}

package com.serivce;

import com.po.ItripUserLinkUser;

import java.util.List;
import java.util.Map;

public interface ItripUserLinkUserSerivce {
    //查询常用联系人
    public List<ItripUserLinkUser> getItripUserLinkUserListByMap(Map<String,Object> param)throws Exception;
    //添加常用联系人
    public int insertItripUserLinkUser(ItripUserLinkUser linkUser)throws Exception;
    //删除常用联络人
    public int deleteItripUserLinkUserByIds(Long[] ids)throws Exception;
    //修改常用联系人
    public int updateItripUserLinkUser(ItripUserLinkUser linkUser)throws Exception;
}

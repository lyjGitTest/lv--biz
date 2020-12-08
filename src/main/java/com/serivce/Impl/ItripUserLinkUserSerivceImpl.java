package com.serivce.Impl;

import com.mapper.ItripUserLinkUserMapper;
import com.po.ItripUserLinkUser;
import com.serivce.ItripUserLinkUserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItripUserLinkUserSerivceImpl implements ItripUserLinkUserSerivce {
    @Autowired
    private ItripUserLinkUserMapper itripUserLinkUserMapper;

    public ItripUserLinkUserMapper getItripUserLinkUserMapper() {
        return itripUserLinkUserMapper;
    }

    public void setItripUserLinkUserMapper(ItripUserLinkUserMapper itripUserLinkUserMapper) {
        this.itripUserLinkUserMapper = itripUserLinkUserMapper;
    }

    @Override
    public List<ItripUserLinkUser> getItripUserLinkUserListByMap(Map<String, Object> param) throws Exception {
        return itripUserLinkUserMapper.getItripUserLinkUserListByMap(param);
    }

    @Override
    public int insertItripUserLinkUser(ItripUserLinkUser linkUser) throws Exception {
        return itripUserLinkUserMapper.insertItripUserLinkUser(linkUser);
    }

    @Override
    public int deleteItripUserLinkUserByIds(Long[] ids) throws Exception {
        return itripUserLinkUserMapper.deleteItripUserLinkUserByIds(ids);
    }

    @Override
    public int updateItripUserLinkUser(ItripUserLinkUser linkUser) throws Exception {
        return itripUserLinkUserMapper.updateItripUserLinkUser(linkUser);
    }
}

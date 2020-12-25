package com.serivce.Impl;

import com.mapper.ItripHotelOrderMapper;
import com.mapper.ItripOrderLinkUserMapper;
import com.po.ItripHotelOrder;
import com.serivce.ItripOrderLinkUserService;
import com.util.vo.ItripOrderLinkUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
@Transactional
public class ItripOrderLinkUserServiceImpl implements ItripOrderLinkUserService {
    @Autowired
    private ItripOrderLinkUserMapper itripOrderLinkUserMapper;
    @Override
    public List<ItripOrderLinkUserVO> getItripOrderLinkUserListByMap(Map<String, Object> param) throws Exception {
        return itripOrderLinkUserMapper.getItripOrderLinkUserListByMap(param);
    }
}

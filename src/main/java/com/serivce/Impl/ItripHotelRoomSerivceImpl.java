package com.serivce.Impl;

import com.mapper.ItripHotelRoomMapper;
import com.serivce.ItripHotelRoomSerivce;
import com.util.vo.ItripHotelRoomVO;
import com.util.vo.ItripImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItripHotelRoomSerivceImpl implements ItripHotelRoomSerivce {
@Autowired
private ItripHotelRoomMapper itripHotelRoomMapper;
    @Override
    public List<ItripImageVO> getimg(Long targetId) throws Exception {
        return null;
    }

    @Override
    public List<ItripHotelRoomVO> getItripHotelRoomListByMap(Map<String, Object> param) throws Exception {
        return itripHotelRoomMapper.getItripHotelRoomListByMap(param);
    }
}

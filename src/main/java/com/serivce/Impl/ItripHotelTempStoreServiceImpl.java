package com.serivce.Impl;

import com.mapper.ItripHotelTempStoreMapper;
import com.po.ItripHotelTempStore;
import com.serivce.ItripHotelTempStoreService;
import com.util.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
@Transactional
public class ItripHotelTempStoreServiceImpl implements ItripHotelTempStoreService {
    @Autowired
    private ItripHotelTempStoreMapper itripHotelTempStoreMapper;

    @Override
    public List<StoreVO> queryRoomStore(Map<String, Object> param) throws Exception {
        //实时库存表
        //itripHotelTempStoreMapper.flushStore(param);
        return itripHotelTempStoreMapper.queryRoomStore(param);
    }
}

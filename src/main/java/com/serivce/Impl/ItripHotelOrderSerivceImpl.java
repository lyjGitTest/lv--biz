package com.serivce.Impl;

import com.mapper.TtripHotelOrderMapper;
import com.po.TtripHotelOrder;
import com.serivce.ItripHotelOrderSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class ItripHotelOrderSerivceImpl implements ItripHotelOrderSerivce {
   @Autowired
   private TtripHotelOrderMapper ttripHotelOrderMapper;
    @Override
    public List<TtripHotelOrder> finddingdanAll(Long orderId) {
        return ttripHotelOrderMapper.finddingdanAll(orderId);
    }
}

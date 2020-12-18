package com.mapper;

import com.po.TtripHotelOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TtripHotelOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TtripHotelOrder record);

    int insertSelective(TtripHotelOrder record);
public List<TtripHotelOrder> finddingdanAll(@Param(value = "orderId") Long orderId);
    TtripHotelOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TtripHotelOrder record);

    int updateByPrimaryKeyWithBLOBs(TtripHotelOrder record);

    int updateByPrimaryKey(TtripHotelOrder record);
}
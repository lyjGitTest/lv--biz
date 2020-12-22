package com.serivce;

import com.po.ItripHotelOrder;

import java.util.Map;

public interface ItripHotelOrderService {
    /**添加订单**/
    public Map<String,Object> insertItripHotelOrder(ItripHotelOrder itripHotelOrder)throws Exception;
}

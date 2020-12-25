package com.serivce;

import com.po.ItripHotelOrder;
import com.util.Page;
import com.util.vo.ItripListHotelOrderVO;
import com.util.vo.ItripPersonalOrderRoomVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItripHotelOrderService {
    /**添加订单**/
    public Map<String,Object> insertItripHotelOrder(ItripHotelOrder itripHotelOrder)throws Exception;
    public ItripPersonalOrderRoomVO getItripHotelOrderRoomInfoById( Long id)throws Exception;
    public ItripHotelOrder getItripHotelOrderById(Long id)throws Exception;
    public Page<ItripListHotelOrderVO> getOrderListByMap(Map<String, Object> param,Integer pageNo,Integer pageSize)throws Exception;

}

package com.serivce;

import com.po.ItripHotelTempStore;
import com.util.vo.StoreVO;

import java.util.List;
import java.util.Map;

public interface ItripHotelTempStoreService {
    /**通过酒店id和房间Id加预定时间查询实时库存**/
    public List<StoreVO> queryRoomStore(Map<String, Object> param)throws Exception;

}

package com.serivce;

import com.po.ItripHotelOrder;
import com.util.vo.ItripOrderLinkUserVO;

import java.util.List;
import java.util.Map;

public interface ItripOrderLinkUserService {
    public List<ItripOrderLinkUserVO>	getItripOrderLinkUserListByMap(Map<String, Object> param)throws Exception;
}

package com.serivce.Impl;

import com.mapper.ItripHotelOrderMapper;
import com.mapper.ItripOrderLinkUserMapper;
import com.po.ItripHotelOrder;
import com.po.ItripOrderLinkUser;
import com.serivce.ItripHotelOrderService;
import com.util.vo.ItripListHotelOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItripHotelOrderServiceImpl implements ItripHotelOrderService {
    @Autowired
    private ItripHotelOrderMapper itripHotelOrderMapper;
    @Autowired
    private ItripOrderLinkUserMapper itripOrderLinkUserMapper;
    @Override
    public Map<String,Object> insertItripHotelOrder(ItripHotelOrder itripHotelOrder) throws Exception {
        int flag=itripHotelOrderMapper.insertItripHotelOrder(itripHotelOrder);
        if(flag>0){
            ItripOrderLinkUser itripOrderLinkUser=new ItripOrderLinkUser();
            Map<String,Object> param=new HashMap<>();
            param.put("orderNo",itripHotelOrder.getOrderNo());
            List<ItripListHotelOrderVO> itripListHotelOrderVOS=itripHotelOrderMapper.getOrderListByMap(param);
            itripOrderLinkUser.setOrderId(itripListHotelOrderVOS.get(0).getId());
            itripOrderLinkUser.setLinkUserId(itripHotelOrder.getUserId());
            itripOrderLinkUser.setLinkUserName(itripHotelOrder.getLinkUserName());
            itripOrderLinkUser.setCreationDate(itripHotelOrder.getCreationDate());
            itripOrderLinkUser.setCreatedBy(itripHotelOrder.getCreatedBy());
            int flag1=itripOrderLinkUserMapper.insertItripOrderLinkUser(itripOrderLinkUser);
            if(flag1>0){
                param.put("id",itripListHotelOrderVOS.get(0).getId().toString());
                param.put("orderNo",itripHotelOrder.getOrderNo());
                return param;
            }
        }
        return null;
    }
}

package com.serivce.Impl;

import com.mapper.ItripHotelOrderMapper;
import com.mapper.ItripOrderLinkUserMapper;
import com.po.ItripHotelOrder;
import com.po.ItripOrderLinkUser;
import com.serivce.ItripHotelOrderService;
import com.util.Constants;
import com.util.EmptyUtils;
import com.util.Page;
import com.util.vo.ItripListHotelOrderVO;
import com.util.vo.ItripPersonalOrderRoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Override
    public ItripPersonalOrderRoomVO getItripHotelOrderRoomInfoById(Long id) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderRoomInfoById(id);
    }

    @Override
    public ItripHotelOrder getItripHotelOrderById(Long id) throws Exception {
        return itripHotelOrderMapper.getItripHotelOrderById(id);
    }

    @Override
    public Page<ItripListHotelOrderVO> getOrderListByMap(Map<String, Object> param,Integer pageNo,Integer pageSize) throws Exception {
        Integer c=itripHotelOrderMapper.getOrderCountByMap(param);
        pageNo= EmptyUtils.isEmpty(pageNo)?Constants.DEFAULT_PAGE_NO:pageNo;
        pageSize=EmptyUtils.isEmpty(pageSize)?Constants.DEFAULT_PAGE_SIZE:pageSize;
        Page page=new Page(pageNo,pageSize,c);
        param.put("beginPos",page.getBeginPos());
        param.put("pageSize",page.getPageSize());
        List<ItripListHotelOrderVO> itripListHotelOrderVOS=itripHotelOrderMapper.getOrderListByMap(param);
        for(ItripListHotelOrderVO itripListHotelOrderVO:itripListHotelOrderVOS) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = sdf.format(itripListHotelOrderVO.getCreationDate1()); //格式化成yyyy-MM-dd格式的时间字符串
            Date newDate = sdf.parse(strDate);
            java.sql.Date resultDate = new java.sql.Date(newDate.getTime());
            itripListHotelOrderVO.setCheckInDate(resultDate);

            SimpleDateFormat sdf1=new SimpleDateFormat("MM-dd");
            String strDate1 = sdf1.format(itripListHotelOrderVO.getCheckInDate());
            String date=sdf1.format(itripListHotelOrderVO.getCheckOutDate());
            itripListHotelOrderVO.setCreationDate(strDate1+"/"+date);
        }
        page.setRows(itripListHotelOrderVOS);
        return page;
    }
}

package com.controller;

import com.po.Dto;
import com.serivce.ItripHotelTempStoreService;
import com.util.DtoUtil;
import com.util.vo.RoomStoreVO;
import com.util.vo.StoreVO;
import com.util.vo.ValidateRoomStoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/hotelorder/")
public class ItripHotelOrderController {
    @Autowired
    private ItripHotelTempStoreService itripHotelTempStoreService;
        /**通过酒店id和房间Id加预定时间查询相关信息(订单生成前信息展示)**/
    @RequestMapping(value = "/getpreorderinfo")
    public Dto<RoomStoreVO> getpreorderinfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ValidateRoomStoreVO validateRoomStoreVO){
        System.out.println("通过酒店id和房间Id加预定时间查询相关信息(订单生成前信息展示)方法.........");
        Map<String,Object> param=new HashMap<>();
        try {
        //1.判断用户是否存在(从浏览器获取token，redis获取用户信息)
           String token=request.getHeader("token");
            System.out.println("token:"+token);
        //2.通过酒店id获取酒店名称
        //3.剩余房间数量
        param.put("roomId",validateRoomStoreVO.getRoomId());
        param.put("startTime",validateRoomStoreVO.getCheckInDate());
        param.put("endTime",validateRoomStoreVO.getCheckOutDate());
        List<StoreVO> storeVOList=itripHotelTempStoreService.queryRoomStore(param);
        //4.房费运算
        //5.综合RoomStoreVO
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

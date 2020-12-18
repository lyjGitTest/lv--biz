package com.controller;


import com.po.Dto;
import com.serivce.ItripHotelTempStoreService;
import com.util.vo.RoomStoreVO;
import com.util.vo.StoreVO;
import com.util.vo.ValidateRoomStoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/hotelorder/")
public class ItripHotelOrderController {
@Autowired
private ItripHotelTempStoreService itripHotelTempStoreService;
@RequestMapping(value = "/getpreorderinfo")
    public Dto<RoomStoreVO> getpreorderinfo(HttpServletRequest request, HttpServletResponse response, @PathVariable ValidateRoomStoreVO validateRoomStoreVO){
    System.out.println("生成订单前,获取预订信息方法进入。。。");
    String tocken=request.getHeader("tocken");
    System.out.println("tocken==="+tocken);
    Map<String,Object> map=new HashMap<>();
    return null;
}



/*@RequestMapping(value = "/getpersonalorderlist")
    public Dto getpersonalorderlist(@RequestBody ItripSearchOrderVO itripSearchOrderVO){
    System.out.println("查询个人订单列表，分页显示方法进入。。。。");
    return null;
}
@RequestMapping(value = "/getpersonalorderinfo/{orderId}")
    public Dto getpersonalorderinfo(@PathVariable Long orderId){
    System.out.println("根据订单ID查看个人订单详情方法进入。。。。。");
    List<TtripHotelOrder> orderList=itripHotelOrderSerivce.finddingdanAll(orderId);
    return DtoUtil.returnDataSuccess(orderList);
}
@RequestMapping(value = "/getpersonalorderroominfo/{orderId}")
    public Dto getpersonalorderroominfo(@PathVariable Long orderId){
    System.out.println("根据订单ID查看个人订单详情-房型相关信息方法进入。。。。。");
    return null;
}*/
}

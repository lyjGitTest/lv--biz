package com.controller;


import com.po.Dto;
import com.po.TtripHotelOrder;
import com.serivce.ItripHotelOrderSerivce;
import com.util.DtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/hotelorder")
public class ItripHotelOrderController {
    @Autowired
    private ItripHotelOrderSerivce itripHotelOrderSerivce;

/*@RequestMapping(value = "/getpersonalorderlist")
    public Dto getpersonalorderlist(@RequestBody ItripSearchOrderVO itripSearchOrderVO){
    System.out.println("查询个人订单列表，分页显示方法进入。。。。");
    return null;
}*/
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
}
}

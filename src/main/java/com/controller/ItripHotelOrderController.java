package com.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.po.*;
import com.serivce.ItripHotelRoomSerivce;
import com.serivce.ItripHotelSerivce;
import com.serivce.ItripHotelTempStoreService;
import com.util.DateUtil;
import com.util.DtoUtil;
import com.util.EmptyUtils;
import com.util.vo.ItripAddHotelOrderVO;
import com.util.vo.RoomStoreVO;
import com.util.vo.StoreVO;
import com.util.vo.ValidateRoomStoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(value = "/api/hotelorder/")
public class ItripHotelOrderController {
    @Autowired
    private ItripHotelRoomSerivce itripHotelRoomSerivce;
    @Autowired
    private ItripHotelSerivce itripHotelSerivce;
    @Autowired
    private ItripHotelTempStoreService itripHotelTempStoreService;
    // 生成订单前,获取预订信息方法
    @RequestMapping(value = "/getpreorderinfo",method = RequestMethod.POST,produces = "application/json")
    public Dto<RoomStoreVO> getpreorderinfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ValidateRoomStoreVO validateRoomStoreVO) {
        System.out.println(" 生成订单前,获取预订信息方法进入。。。。");
        try {
            String token = request.getHeader("token");
            Jedis jedis = new Jedis();
            String usecodetocken = jedis.get(token);
            JSONObject jsonObject=JSONObject.parseObject(usecodetocken.toString());
            ItripUser itripUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
            if (EmptyUtils.isNotEmpty(itripUser)) {
                Map<String, Object> strMap = JSON.parseObject(usecodetocken);
                Object user = strMap.get("usercode");
                System.out.println("user==" + user);
                //酒店id获取酒店名称
                if (EmptyUtils.isNotEmpty(validateRoomStoreVO.getHotelId())) {
                    ItripHotel itripHotel = itripHotelSerivce.getItripHotelById(validateRoomStoreVO.getHotelId());
                    String hotelName = itripHotel.getHotelName();
                    System.out.println("hotelName==" + hotelName);
                    //房费运算
                    if (EmptyUtils.isNotEmpty(validateRoomStoreVO.getRoomId())) {
                        ItripHotelRoom itripRoom = itripHotelRoomSerivce.getItripHotelRoomById(validateRoomStoreVO.getRoomId());
                        BigDecimal price = itripRoom.getRoomPrice();
                        //store
                        Map<String, Object> param = new HashMap<>();
                        param.put("roomId", validateRoomStoreVO.getRoomId());
                        param.put("startTime", validateRoomStoreVO.getCheckInDate());
                        param.put("endTime", validateRoomStoreVO.getCheckOutDate());

                        List<StoreVO> storeVOList = itripHotelTempStoreService.queryRoomStore(param);
                       if(EmptyUtils.isNotEmpty(storeVOList)) {
                           System.out.println("storeVoList.size()===" + storeVOList.size());
                           //RoomStoreVO
                           RoomStoreVO roomStoreVO = new RoomStoreVO();
                           roomStoreVO.setHotelId(validateRoomStoreVO.getHotelId());
                           roomStoreVO.setHotelName(hotelName);
                           roomStoreVO.setRoomId(itripRoom.getId());
                           roomStoreVO.setCheckInDate(validateRoomStoreVO.getCheckInDate());
                           roomStoreVO.setCheckOutDate(validateRoomStoreVO.getCheckOutDate());
                           roomStoreVO.setCount(validateRoomStoreVO.getCount());
                           roomStoreVO.setPrice(price);
                           roomStoreVO.setStore(storeVOList.get(0).getStore());
                           return DtoUtil.returnDataSuccess(roomStoreVO);
                       }else{
                           return DtoUtil.returnFail("暂时无房","100512");
                       }
                    } else {
                        return DtoUtil.returnFail("hotelId不能为空", "100510");
                    }
                } else {
                    return DtoUtil.returnFail("roomId不能为空", "100511");
                }
            } else {
                return DtoUtil.returnFail("token失效，请重登录", "100000");
            }
        } catch (Exception e) {
            e.getMessage();
            return DtoUtil.returnFail("系统异常", "100513");
        }
    }
    //生成订单
    @RequestMapping(value = "/addhotelorder")
    public Dto addhotelorder(HttpServletRequest request,HttpServletResponse response,@RequestBody ItripAddHotelOrderVO itripAddHotelOrderVO){
        System.out.println("生成订单方法进入。。。");
       String tocken=request.getHeader("token");
       Jedis jedis=new Jedis();
       JSONObject jsonObject=JSONObject.parseObject(jedis.get(tocken).toString());
       ItripUser itripUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
       if(EmptyUtils.isNotEmpty(itripUser)){
           //预定天数
           List<Date> days=DateUtil.getBetweenDates(itripAddHotelOrderVO.getCheckInDate(),itripAddHotelOrderVO.getCheckOutDate());
           Integer ds=days.size()-1;

           ItripHotelOrder itripHotelOrder=new ItripHotelOrder();
           itripHotelOrder.setUserId(itripAddHotelOrderVO.getId());
           itripHotelOrder.setHotelId(itripAddHotelOrderVO.getHotelId());
           itripHotelOrder.setCount(itripAddHotelOrderVO.getCount());
           itripHotelOrder.setCheckInDate(itripAddHotelOrderVO.getCheckInDate());
           itripHotelOrder.setCheckOutDate(itripAddHotelOrderVO.getCheckOutDate());
           itripHotelOrder.setHotelName(itripAddHotelOrderVO.getHotelName());
           itripHotelOrder.setIsNeedInvoice(itripAddHotelOrderVO.getIsNeedInvoice());
           itripHotelOrder.setBookingDays(ds);
           itripHotelOrder.setId(itripAddHotelOrderVO.getId());
           itripHotelOrder.setCreatedBy(itripUser.getId());
           itripHotelOrder.setOrderType(itripAddHotelOrderVO.getOrderType());
           itripHotelOrder.setRoomId(itripAddHotelOrderVO.getRoomId());
           itripHotelOrder.setCreationDate(new Date());
           //订单状态
           itripHotelOrder.setOrderStatus(0);
           //订单支付类型
           itripHotelOrder.setPayType(1);
           //是否需要发票
           itripHotelOrder.setIsNeedInvoice(itripAddHotelOrderVO.getIsNeedInvoice());
           itripHotelOrder.setInvoiceHead(itripAddHotelOrderVO.getInvoiceHead());
           itripHotelOrder.setInvoiceType(itripAddHotelOrderVO.getInvoiceType());
           itripHotelOrder.setLinkUserName(itripUser.getUsername());
           itripHotelOrder.setBookType(itripHotelOrder.getBookType());

           itripHotelOrder.setSpecialRequirement(itripHotelOrder.getSpecialRequirement());
           //订单金额
BigDecimal bigDecimal=new BigDecimal(itripAddHotelOrderVO.getCount());
BigDecimal bigDecimal1=new BigDecimal(days.toString());
BigDecimal bigDecimal2=bigDecimal.multiply(bigDecimal1);
itripHotelOrder.setPayAmount(bigDecimal2);

return DtoUtil.returnDataSuccess(itripHotelOrder);
       }else{
           return DtoUtil.returnFail("token失效，请重登录","100000");
       }

    }
}

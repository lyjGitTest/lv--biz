package com.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.po.*;
import com.serivce.ItripHotelOrderService;
import com.serivce.ItripHotelRoomSerivce;
import com.serivce.ItripHotelSerivce;
import com.serivce.ItripHotelTempStoreService;
import com.util.DateUtil;
import com.util.DtoUtil;
import com.util.EmptyUtils;
import com.util.MD5Util;
import com.util.vo.ItripAddHotelOrderVO;
import com.util.vo.RoomStoreVO;
import com.util.vo.StoreVO;
import com.util.vo.ValidateRoomStoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(value = "/api/hotelorder/")
public class ItripHotelOrderController {
    @Autowired
    private ItripHotelOrderService itripHotelOrderService;
    @Autowired
    private ItripHotelRoomSerivce itripHotelRoomSerivce;
    @Autowired
    private ItripHotelSerivce itripHotelSerivce;
    @Autowired
    private ItripHotelTempStoreService itripHotelTempStoreService;
    // 生成订单前,获取预订信息方法
    @RequestMapping(value = "/getpreorderinfo")
    public Dto<RoomStoreVO> getpreorderinfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ValidateRoomStoreVO validateRoomStoreVO) {
        System.out.println(" 生成订单前,获取预订信息方法进入。。。。");
        Map<String, Object> param = new HashMap<>();
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
                    //房费运算
                    if (EmptyUtils.isNotEmpty(validateRoomStoreVO.getRoomId())) {
                        ItripHotelRoom itripRoom = itripHotelRoomSerivce.getItripHotelRoomById(validateRoomStoreVO.getRoomId());
                        BigDecimal price = itripRoom.getRoomPrice();
                        //store
                        param.put("roomId", validateRoomStoreVO.getRoomId());
                        param.put("startTime", validateRoomStoreVO.getCheckInDate());
                        param.put("endTime", validateRoomStoreVO.getCheckOutDate());
                        List<StoreVO> storeVOList = itripHotelTempStoreService.queryRoomStore(param);

                        //RoomStoreVO
                           RoomStoreVO roomStoreVO = new RoomStoreVO();
                           roomStoreVO.setHotelId(validateRoomStoreVO.getHotelId());
                           roomStoreVO.setHotelName(itripHotel.getHotelName());
                           roomStoreVO.setRoomId(itripRoom.getId());
                           roomStoreVO.setCheckInDate(validateRoomStoreVO.getCheckInDate());
                           roomStoreVO.setCheckOutDate(validateRoomStoreVO.getCheckOutDate());
                           roomStoreVO.setCount(validateRoomStoreVO.getCount());
                           roomStoreVO.setPrice(price);
                        if(EmptyUtils.isNotEmpty(storeVOList)) {
                            System.out.println("storeVoList.size()===" + storeVOList.size());
                            roomStoreVO.setStore(storeVOList.get(0).getStore());
                        } else{
                            return DtoUtil.returnFail("暂时无房","100512");
                        }
                           return DtoUtil.returnDataSuccess(roomStoreVO);
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

            return DtoUtil.returnFail("系统异常", "100513");
        }
    }
    //生成订单
    @RequestMapping(value = "/addhotelorder")
    public Dto addhotelorder(HttpServletRequest request,HttpServletResponse response,@RequestBody ItripAddHotelOrderVO itripAddHotelOrderVO) {
        if(EmptyUtils.isEmpty(itripAddHotelOrderVO) || (itripAddHotelOrderVO.getHotelId()==null || itripAddHotelOrderVO.getHotelId()==0) || (itripAddHotelOrderVO.getRoomId()==null || itripAddHotelOrderVO.getRoomId()==0) ){
            return DtoUtil.returnFail("不能提交空，请填写订单信息","100506");
        }
        System.out.println("生成订单方法进入。。。");
        try {
            String tocken = request.getHeader("token");
            Jedis jedis = new Jedis();
            JSONObject jsonObject = JSONObject.parseObject(jedis.get(tocken).toString());
            ItripUser itripUser = JSONObject.toJavaObject(jsonObject, ItripUser.class);
            if (EmptyUtils.isNotEmpty(itripUser)) {
                //预定天数
                List<Date> days = DateUtil.getBetweenDates(itripAddHotelOrderVO.getCheckInDate(), itripAddHotelOrderVO.getCheckOutDate());
                Integer ds = days.size() - 1;

                ItripHotelOrder itripHotelOrder = new ItripHotelOrder();
                itripHotelOrder.setUserId(itripUser.getId());
                itripHotelOrder.setHotelId(itripAddHotelOrderVO.getHotelId());
                itripHotelOrder.setCount(itripAddHotelOrderVO.getCount());
                itripHotelOrder.setCheckInDate(itripAddHotelOrderVO.getCheckInDate());
                itripHotelOrder.setCheckOutDate(itripAddHotelOrderVO.getCheckOutDate());
                itripHotelOrder.setHotelName(itripAddHotelOrderVO.getHotelName());
                itripHotelOrder.setIsNeedInvoice(itripAddHotelOrderVO.getIsNeedInvoice());
                itripHotelOrder.setNoticePhone(itripAddHotelOrderVO.getNoticePhone());
                itripHotelOrder.setNoticeEmail(itripAddHotelOrderVO.getNoticeEmail());
                //天数
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
                //判断预定类型
                if (tocken.startsWith("token:PC")) {
                    itripHotelOrder.setBookType(0);
                }
                itripHotelOrder.setSpecialRequirement(itripHotelOrder.getSpecialRequirement());
                //相关联系人
                List<ItripUserLinkUser> linkUserList = itripAddHotelOrderVO.getLinkUser();
                if (EmptyUtils.isNotEmpty(linkUserList)) {
                    StringBuffer stringBuffer = new StringBuffer("");
                    int flag = 1;
                    for (int i = 0; i < linkUserList.size(); i++) {
                        if (i != flag) {
                            stringBuffer.append(linkUserList.get(i).getLinkUserName() + ",");
                        } else {
                            stringBuffer.append(linkUserList.get(i).getLinkUserName());
                            flag++;
                        }
                    }
                    itripHotelOrder.setLinkUserName(stringBuffer.toString());
                }
                //订单order加密
                StringBuilder stringBuilder=new StringBuilder();
                stringBuilder.append(itripAddHotelOrderVO.getHotelId());
                stringBuilder.append(itripAddHotelOrderVO.getRoomId());
                stringBuilder.append(System.currentTimeMillis());
                stringBuilder.append(Math.random()*1000000);
                String md5=MD5Util.getMd5(stringBuilder.toString(),6);
                StringBuilder orderNo=new StringBuilder();
                orderNo.append("D1000001");
                orderNo.append(DateUtil.format(new Date(),"yyyyMMddHHmmss"));
                orderNo.append(md5);
                itripHotelOrder.setOrderNo(orderNo.toString());
                    //订单金额
                    BigDecimal bigDecimal = new BigDecimal(itripAddHotelOrderVO.getCount());
                    BigDecimal bigDecimal1 = new BigDecimal(ds);
                    BigDecimal bigDecimal2 = bigDecimal.multiply(bigDecimal1);
                    itripHotelOrder.setPayAmount(itripHotelRoomSerivce.getItripHotelRoomById(itripAddHotelOrderVO.getRoomId()).getRoomPrice().multiply(bigDecimal2));
                Map<String,Object> param=itripHotelOrderService.insertItripHotelOrder(itripHotelOrder);
                if(EmptyUtils.isNotEmpty(param)){
                return DtoUtil.returnDataSuccess(param);
                }
                else{
                    return DtoUtil.returnFail("生成订单失败","100505");
                }
                } else {
                    return DtoUtil.returnFail("token失效，请重登录", "100000");
                }
        } catch (Exception e) {
            return DtoUtil.returnFail("系统异常","100517");
        }

    }
    //根据订单ID获取订单信息
    @RequestMapping(value = "/queryOrderById/{orderId}",method = RequestMethod.GET)
    public Dto queryOrderById(@PathVariable Long orderId){
        System.out.println("根据订单ID获取订单信息方法进入。。。。");

        return null;

    }
    //根据订单ID查看个人订单详情
    @RequestMapping(value = "/getpersonalorderinfo/{orderId}",method = RequestMethod.GET)
    public Dto getpersonalorderinfo(HttpServletRequest request,@PathVariable Long orderId){
        System.out.println("根据订单ID查看个人订单详情方法进入。。。。");
       String token=request.getHeader("token");
       Jedis jedis=new Jedis();
       JSONObject jsonObject=JSONObject.parseObject(jedis.get(token));
       ItripUser itripUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
       if(EmptyUtils.isNotEmpty(itripUser)){

       }else{
           return DtoUtil.returnFail("token失效，请重登录","100000");
       }
        return null;
    }
    //根据订单ID查看个人订单详情-房型相关信息
    @RequestMapping(value = "/getpersonalorderroominfo/{orderId}",method = RequestMethod.GET)
    public Dto getpersonalorderroominfo(HttpServletRequest request,@PathVariable Long orderId){
        System.out.println("根据订单ID查看个人订单详情-房型相关信息方法进入.....");
        String token=request.getHeader("token");
        Jedis jedis=new Jedis();
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token));
        ItripUser itripUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if(EmptyUtils.isNotEmpty(itripUser)){

        }else{
            return DtoUtil.returnFail("token失效，请重登录","100000");
        }
        return null;
    }
        }

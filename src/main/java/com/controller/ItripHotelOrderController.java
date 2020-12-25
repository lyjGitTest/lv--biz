package com.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.po.*;
import com.serivce.*;
import com.util.*;
import com.util.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
  @Autowired
  private ItripOrderLinkUserService itripOrderLinkUserService;
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
            System.out.println("itripUser=="+itripUser.toString());
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
                        System.out.println("store=="+storeVOList.get(0).getStore());
                        //RoomStoreVO
                           RoomStoreVO roomStoreVO = new RoomStoreVO();
                           roomStoreVO.setHotelId(validateRoomStoreVO.getHotelId());
                           roomStoreVO.setHotelName(itripHotel.getHotelName());
                           roomStoreVO.setRoomId(itripRoom.getId());
                           roomStoreVO.setCheckInDate(validateRoomStoreVO.getCheckInDate());
                           roomStoreVO.setCheckOutDate(validateRoomStoreVO.getCheckOutDate());
                           roomStoreVO.setCount(validateRoomStoreVO.getCount());
                           roomStoreVO.setPrice(price);
                        if(EmptyUtils.isNotEmpty(storeVOList) && storeVOList.get(0).getStore()!=0){
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
    @RequestMapping(value = "/queryOrderById/{orderId}")
    public Dto queryOrderById(@PathVariable Long orderId){
        System.out.println("根据订单ID获取订单信息方法进入。。。。");
        if(EmptyUtils.isNotEmpty(orderId)) {
            try {
                ItripHotelOrder itripHotelOrder = itripHotelOrderService.getItripHotelOrderById(orderId);
              SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                String strDate = sdf.format(itripHotelOrder.getCheckInDate()); //格式化成yyyy-MM-dd格式的时间字符串
                Date newDate = sdf.parse(strDate);
                java.sql.Date resultDate = new java.sql.Date(newDate.getTime());
              itripHotelOrder.setCheckInDate(resultDate);

                String strDate1 = sdf.format(itripHotelOrder.getCheckOutDate()); //格式化成yyyy-MM-dd格式的时间字符串
                Date newDate1 = sdf.parse(strDate1);
                java.sql.Date resultDate1 = new java.sql.Date(newDate1.getTime());
                itripHotelOrder.setCheckOutDate(resultDate1);

                String strDate2 = sdf.format(itripHotelOrder.getCreationDate()); //格式化成yyyy-MM-dd格式的时间字符串
                Date newDate2 = sdf.parse(strDate2);
                java.sql.Date resultDate2 = new java.sql.Date(newDate2.getTime());
                itripHotelOrder.setCreationDate(resultDate2);

                System.out.println("Order===" + itripHotelOrder.toString());
                ItripModifyHotelOrderVO itripModifyHotelOrderVO=new ItripModifyHotelOrderVO();
                BeanUtils.copyProperties(itripHotelOrder,itripModifyHotelOrderVO);
                Map<String, Object> map = new HashMap<>();
                map.put("orderId", orderId);
                List<ItripOrderLinkUserVO> itripOrderLinkUserVOS= itripOrderLinkUserService.getItripOrderLinkUserListByMap(map);
               itripModifyHotelOrderVO.setItripOrderLinkUserList(itripOrderLinkUserVOS);
                return DtoUtil.returnDataSuccess(itripModifyHotelOrderVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            return DtoUtil.returnFail("请传递参数：orderId","100525");
        }
        return null;

    }
    //根据个人订单列表，并分页显示
    @RequestMapping(value = "/getpersonalorderlist")
    public Dto getpersonalorderlist(HttpServletRequest request,@RequestBody ItripSearchOrderVO itripSearchOrderVO) throws ParseException {
        System.out.println("根据个人订单列表，并分页显示方法进入。。。");
        String token=request.getHeader("token");
        Jedis jedis=new Jedis();
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token));
        ItripUser itripUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if(EmptyUtils.isNotEmpty(itripUser)) {
            Integer orderStatus=itripSearchOrderVO.getOrderStatus();
            if(orderStatus!=-1) {
            return DtoUtil.returnFail("修改订单状态","111111");
            }
            System.out.println("itripSearchOrderVO==="+itripSearchOrderVO.toString());
                Map<String, Object> map = new HashMap<>();
                map.put("userId",itripUser.getId());
                map.put("orderNo",itripSearchOrderVO.getOrderNo());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if(itripSearchOrderVO.getEndDate()==null){
                    map.put("endDate1", null);
                }else {
                    String strDate = sdf.format(itripSearchOrderVO.getEndDate1()); //格式化成yyyy-MM-dd格式的时间字符串
                    Date newDate = sdf.parse(strDate);
                    java.sql.Date resultDate = new java.sql.Date(newDate.getTime());
                    System.out.println("resultdate==="+resultDate);
                    map.put("endDate1", resultDate);
                }
                map.put("pageNo",itripSearchOrderVO.getPageNo());
                map.put("pageSize",itripSearchOrderVO.getPageSize());
                map.put("linkUserName",itripSearchOrderVO.getLinkUserName());
                if(itripSearchOrderVO.getStartDate()==null) {
                    map.put("startDate", null);
                }else {
                    String strDate = sdf.format(itripSearchOrderVO.getStartDate()); //格式化成yyyy-MM-dd格式的时间字符串
                    Date newDate = sdf.parse(strDate);
                    java.sql.Date resultDate = new java.sql.Date(newDate.getTime());
                    map.put("startDate", resultDate);
                }
                if(EmptyUtils.isEmpty(itripSearchOrderVO.getOrderStatus())){
                    return DtoUtil.returnFail("请传递参数：orderStatus","100502");
                }else {
                    map.put("orderStatus", itripSearchOrderVO.getOrderStatus()== -1? null : itripSearchOrderVO.getOrderStatus());
                }
                if(EmptyUtils.isEmpty(itripSearchOrderVO.getOrderType())){
                    return DtoUtil.returnFail("请传递参数：orderType","100501");
                }else {
                    map.put("orderType", itripSearchOrderVO.getOrderType()==-1?null:itripSearchOrderVO.getOrderType());
                }
                try {
                    Page page = itripHotelOrderService.getOrderListByMap(map, itripSearchOrderVO.getPageNo(), itripSearchOrderVO.getPageSize());
               if(EmptyUtils.isEmpty(page)){
                   return DtoUtil.returnFail("获取个人订单列表错误","100503");
               }else {
                   return DtoUtil.returnDataSuccess(page);
               }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }else{
            return  DtoUtil.returnFail("token失效，请重登录","100000");
        }
        return null;
    }
    //根据订单ID查看个人订单详情
    @RequestMapping(value = "/getpersonalorderinfo/{orderId}")
    public Dto getpersonalorderinfo(HttpServletRequest request,@PathVariable Long orderId){
        System.out.println("根据订单ID查看个人订单详情方法进入。。。。");
       String token=request.getHeader("token");
       Jedis jedis=new Jedis();
       JSONObject jsonObject=JSONObject.parseObject(jedis.get(token));
       ItripUser itripUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
       if(EmptyUtils.isNotEmpty(itripUser)){
           try {
               ItripHotelOrder itripHotelOrder=itripHotelOrderService.getItripHotelOrderById(orderId);
              Integer orderStatus=itripHotelOrder.getOrderStatus();
               ItripPersonalHotelOrderVO itripPersonalHotelOrderVO=new ItripPersonalHotelOrderVO();
             itripPersonalHotelOrderVO.setId(itripHotelOrder.getId());
             itripPersonalHotelOrderVO.setOrderNo(itripHotelOrder.getOrderNo());
               //预定方式（0:WEB端 1:手机端 2:其他客户端）
             itripPersonalHotelOrderVO.setBookType(itripHotelOrder.getBookType());
             itripPersonalHotelOrderVO.setNoticePhone(itripHotelOrder.getNoticePhone());
             itripPersonalHotelOrderVO.setPayAmount(itripHotelOrder.getPayAmount());
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               String strDate = sdf.format(itripHotelOrder.getCreationDate()); //格式化成yyyy-MM-dd格式的时间字符串
               Date newDate = sdf.parse(strDate);
               java.sql.Date resultDate = new java.sql.Date(newDate.getTime());
             itripPersonalHotelOrderVO.setCreationDate(resultDate);
               //支付方式:1:支付宝 2:微信 3:到店付
             itripPersonalHotelOrderVO.setPayType(itripHotelOrder.getPayType());
               //房间支持的支付方式 {"1":"在线付","2":"线下付","3":"不限"}
               ItripHotelRoom itripHotelRoom=itripHotelRoomSerivce.getItripHotelRoomById(itripHotelOrder.getRoomId());
             itripPersonalHotelOrderVO.setRoomPayType(itripHotelRoom.getPayType());
               /**
                * 订单流程:
                * 1、待付款、待评价（已消费）、未出行（支付成功）
                * 流程: 已提交-->待支付-->支付成功-->已入住-->已点评
                * 2、已取消
                * 流程: 已提交-->待支付-->已取消
                */
               Object process=null;
               if(orderStatus==0){
                   //待支付
                   itripPersonalHotelOrderVO.setProcessNode("1");
               }else if(orderStatus==2){
                   itripPersonalHotelOrderVO.setProcessNode("3");
               }else if(orderStatus==3){
                   itripPersonalHotelOrderVO.setProcessNode("5");
               }else if(orderStatus==4){
                   itripPersonalHotelOrderVO.setProcessNode("6");
               }else {
                   itripPersonalHotelOrderVO.setProcessNode(null);
               }
             itripPersonalHotelOrderVO.setOrderProcess(process);
               System.out.println("personalVo==="+itripPersonalHotelOrderVO.toString());
               return DtoUtil.returnDataSuccess(itripPersonalHotelOrderVO);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }else{
           return DtoUtil.returnFail("token失效，请重登录","100000");
       }
        return null;
    }
    //根据订单ID查看个人订单详情-房型相关信息
    @RequestMapping(value = "/getpersonalorderroominfo/{orderId}")
    public Dto getpersonalorderroominfo(HttpServletRequest request,@PathVariable Long orderId){
        System.out.println("根据订单ID查看个人订单详情-房型相关信息方法进入.....");
        String token=request.getHeader("token");
        Jedis jedis=new Jedis();
        JSONObject jsonObject=JSONObject.parseObject(jedis.get(token));
        ItripUser itripUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if(EmptyUtils.isNotEmpty(itripUser)){
         try {
        ItripPersonalOrderRoomVO itripPersonalOrderRoomVO=itripHotelOrderService.getItripHotelOrderRoomInfoById(orderId);
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             String strDate = sdf.format( itripPersonalOrderRoomVO.getCheckInDate()); //格式化成yyyy-MM-dd格式的时间字符串
             Date newDate = sdf.parse(strDate);
             java.sql.Date resultDate = new java.sql.Date(newDate.getTime());
           itripPersonalOrderRoomVO.setCheckInDate(resultDate);

             String strDate1 = sdf.format(itripPersonalOrderRoomVO.getCheckOutDate()); //格式化成yyyy-MM-dd格式的时间字符串
             Date newDate1 = sdf.parse(strDate1);
             java.sql.Date resultDate1 = new java.sql.Date(newDate1.getTime());
             itripPersonalOrderRoomVO.setCheckOutDate(resultDate1);

             System.out.println("itripPersonalOrderRoomVO===="+itripPersonalOrderRoomVO.toString());
         return DtoUtil.returnDataSuccess(itripPersonalOrderRoomVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            return DtoUtil.returnFail("token失效，请重登录","100000");
        }
        return null;
    }
    //修改订单的支付方式和状态
    @RequestMapping(value = "/updateorderstatusandpaytype")
    public Dto updateorderstatusandpaytype(){
        System.out.println("修改订单的支付方式和状态方法进入。。。。");
        return null;
    }
        }

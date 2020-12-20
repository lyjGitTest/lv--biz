package com.controller;

import com.alibaba.fastjson.JSON;
import com.po.Dto;
import com.po.ItripHotel;
import com.po.ItripHotelRoom;
import com.serivce.ItripHotelRoomSerivce;
import com.serivce.ItripHotelSerivce;
import com.serivce.ItripHotelTempStoreService;
import com.util.DtoUtil;
import com.util.EmptyUtils;
import com.util.vo.RoomStoreVO;
import com.util.vo.StoreVO;
import com.util.vo.ValidateRoomStoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/hotelorder/")
public class ItripHotelOrderController {
    @Autowired
    private ItripHotelRoomSerivce itripHotelRoomSerivce;
    @Autowired
    private ItripHotelSerivce itripHotelSerivce;
    @Autowired
    private ItripHotelTempStoreService itripHotelTempStoreService;

    /**
     * 通过酒店id和房间Id加预定时间查询相关信息(订单生成前信息展示)
     **/
    @RequestMapping(value = "/getpreorderinfo")
    public Dto<RoomStoreVO> getpreorderinfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ValidateRoomStoreVO validateRoomStoreVO) {
        System.out.println(" 生成订单前,获取预订信息方法进入。。。。");
        try {
            String token = request.getHeader("token");
            Jedis jedis = new Jedis();
            String usecodetocken = jedis.get(token);
            if (EmptyUtils.isNotEmpty(usecodetocken)) {
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
                            //RoomStoreVO
                            RoomStoreVO roomStoreVO = new RoomStoreVO();
                            roomStoreVO.setHotelId(validateRoomStoreVO.getHotelId());
                            roomStoreVO.setHotelName(hotelName);
                            roomStoreVO.setRoomId(itripRoom.getId());
                            roomStoreVO.setCheckInDate(validateRoomStoreVO.getCheckInDate());
                            roomStoreVO.setCheckOutDate(validateRoomStoreVO.getCheckOutDate());
                            roomStoreVO.setCount(validateRoomStoreVO.getCount());
                            roomStoreVO.setPrice(price);
                        //store
                        Map<String, Object> param = new HashMap<>();
                        param.put("roomId", validateRoomStoreVO.getRoomId());
                        param.put("startTime", validateRoomStoreVO.getCheckInDate());
                        param.put("endTime", validateRoomStoreVO.getCheckOutDate());
                       List<StoreVO> storeVOList = itripHotelTempStoreService.queryRoomStore(param);
                        System.out.println("size=="+storeVOList.size());

                        roomStoreVO.setStore(storeVOList.get(0).getStore());
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
}

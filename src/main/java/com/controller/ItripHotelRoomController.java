package com.controller;

import com.po.Dto;
import com.serivce.ItripHotelRoomSerivce;
import com.serivce.ItripImageService;
import com.serivce.ItripLabelDicSerivce;
import com.util.DtoUtil;
import com.util.EmptyUtils;
import com.util.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/hotelroom")
public class ItripHotelRoomController {
    @Autowired
    private ItripHotelRoomSerivce itripHotelRoomSerivce;
    @Autowired
    private ItripLabelDicSerivce itripLabelDicSerivce;
    @Autowired
    private ItripImageService itripImageService;
    @RequestMapping(value = "/queryhotelroombyhotel")
    public Dto queryhotelroombyhotel(@RequestBody SearchHotelRoomVO searchHotelRoomVO){
        System.out.println("查询酒店房间列表方法进入。。。");
        if(searchHotelRoomVO.getStartDate().getTime()>searchHotelRoomVO.getEndDate().getTime() && EmptyUtils.isNotEmpty(searchHotelRoomVO.getHotelId()) && EmptyUtils.isNotEmpty(searchHotelRoomVO.getEndDate()) && EmptyUtils.isNotEmpty(searchHotelRoomVO.getStartDate())){
            return DtoUtil.returnFail("酒店id不能为空,酒店入住及退房时间不能为空,入住时间不能大于退房时间","100303");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("hotelId",searchHotelRoomVO.getHotelId());
        map.put("isBook",searchHotelRoomVO.getIsBook());
        map.put("isCancel",searchHotelRoomVO.getIsCancel());
       map.put("isHavingBreakfast",searchHotelRoomVO.getIsHavingBreakfast());
       map.put("isTimelyResponse",searchHotelRoomVO.getIsTimelyResponse());
       map.put("roomBedTypeId",searchHotelRoomVO.getRoomBedTypeId());
        if(EmptyUtils.isEmpty(searchHotelRoomVO.getPayType())||searchHotelRoomVO.getPayType()==3){
            map.put("payType",null);
        }else{
            map.put("payType",searchHotelRoomVO.getPayType());
        }
      try {
          List<ItripHotelRoomVO> itripHotelRoomVOList=itripHotelRoomSerivce.getItripHotelRoomListByMap(map);
       List<List<ItripHotelRoomVO>> lists=new ArrayList<>();
       for(ItripHotelRoomVO itripHotelRoomVO:itripHotelRoomVOList){
           List<ItripHotelRoomVO> list=new ArrayList<>();
           list.add(itripHotelRoomVO);
           lists.add(list);
       }
       return DtoUtil.returnDataSuccess(lists);
        } catch (Exception e) {
            return DtoUtil.returnFail("系统异常","100304");
        }
    }
@RequestMapping(value = "/getimg/{targetId}")
    public Dto getimg(@PathVariable Long targetId){
    System.out.println("根据酒店房型ID查询酒店房型图片方法进入。。。。");
    if (EmptyUtils.isNotEmpty(targetId)) {
    try {
        Map<String,Object> map=new HashMap<>();
        map.put("type",1);
        map.put("targetId",targetId);
            List<ItripImageVO> itripImageVOList = itripImageService.getItripImageListByMap(map);
            return DtoUtil.returnDataSuccess(itripImageVOList);
        } catch(Exception e){
            return DtoUtil.returnFail("获取酒店房型图片失败","100301");
        }
    }else{
        return DtoUtil.returnFail("酒店房型id不能为空","100302");
    }
}
@RequestMapping(value = "/queryhotelroombed")
    public Dto queryhotelroombed(){
    System.out.println("查询酒店房间床型列表方法进入。。。。");
    try {
        List<ItripLabelDicVO> itripAreaDicVOS=itripLabelDicSerivce.getItripLabelDicById(new Long(1));
     return DtoUtil.returnDataSuccess(itripAreaDicVOS);
    } catch (Exception e) {
        return DtoUtil.returnFail("获取酒店房间床型失败","100305");
    }
}
}

package com.controller;

import com.po.Dto;
import com.po.ItripAreaDic;
import com.po.ItripLabelDic;
import com.serivce.Impl.ItripAreaDicSerivceImpl;
import com.serivce.ItripAreaDicSerivce;
import com.serivce.ItripHotelSerivce;
import com.serivce.ItripLabelDicSerivce;
import com.util.DtoUtil;
import com.util.EmptyUtils;
import com.util.vo.*;
import org.springframework.beans.BeanUtils;
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
@RequestMapping(value = "/api/hotel")
public class ItripHotelCityController {
    @Autowired
    private ItripAreaDicSerivce itripAreaDicSerivce;
    @Autowired
    private ItripLabelDicSerivce itripLabelDicSerivce;
    @Autowired
    private ItripHotelSerivce itripHotelSerivce;
    @RequestMapping(value = "/queryhotcity/{type}")
    public Dto queryhotcity(@PathVariable Integer type){
        System.out.println("查询热门城市方法进入。。。。。");
        System.out.println("type====="+type);
        if(EmptyUtils.isNotEmpty(type)){
            Map map=new HashMap();
            map.put("isHot",1);
            map.put("isChina",type);
            try {
                List<ItripAreaDic> itripAreaDicList=itripAreaDicSerivce.getItripAreaDicListByMap(map);
           if(EmptyUtils.isNotEmpty(itripAreaDicList)){
               List<ItripAreaDicVO> areaDicVOList=new ArrayList<>();
               for(ItripAreaDic itripAreaDic : itripAreaDicList){
                   ItripAreaDicVO itripAreaDicVO=new ItripAreaDicVO();
                   System.out.println("itripAreaDic"+itripAreaDic.getName());
                   BeanUtils.copyProperties(itripAreaDic,itripAreaDicVO);
                   areaDicVOList.add(itripAreaDicVO);
               }
               return DtoUtil.returnDataSuccess(areaDicVOList);
           }else{
               return DtoUtil.returnFail("没有找到热门城市","10200");
           }
            } catch (Exception e) {
                return DtoUtil.returnFail(e.getMessage(),"10202");
            }
        }else{
            return DtoUtil.returnFail("hotelId不能为空","10201");
        }

    }
    @RequestMapping(value = "querytradearea/{cityId}")
    public Dto querytradearea(@PathVariable Integer cityId){
        System.out.println("查询商圈方法进入。。。");
        if(EmptyUtils.isNotEmpty(cityId)){
        Map<String,Object> map=new HashMap<>();
           map.put("parent",cityId);
           map.put("isTradingArea",1);
            try {
                List<ItripAreaDic> itripLabelDics=itripAreaDicSerivce.getItripAreaDicListByMap(map);
                System.out.println("itripLabelDics======"+itripLabelDics);
           if(EmptyUtils.isNotEmpty(itripLabelDics)){
               List<ItripAreaDicVO> itripAreaDicVOS=new ArrayList<>();
               for(ItripAreaDic areaDic : itripLabelDics){
                   ItripAreaDicVO itripAreaDicVO=new ItripAreaDicVO();
                   BeanUtils.copyProperties(areaDic,itripAreaDicVO);
                   itripAreaDicVOS.add(itripAreaDicVO);
               }
               return DtoUtil.returnDataSuccess(itripAreaDicVOS);
           }else{
               return DtoUtil.returnFail("itripLabelDics为空","11111");
           }
            } catch (Exception e) {
                return DtoUtil.returnFail(e.getMessage(),"10204");
            }
        }else{
            return DtoUtil.returnFail("cityId不能为空","10203");
        }
    }
    @RequestMapping(value = "/queryhoteldetails/{hotelId}")
    public Dto queryHotelDetails(@PathVariable Long hotelId){
        System.out.println("酒店介绍方法进入。。。。");
        if(EmptyUtils.isNotEmpty(hotelId)) {
            try {
                List<ItripSearchDetailsHotelVO> itripSearchDetailsHotelVOList = itripHotelSerivce.queryHotelDetails(hotelId);
                return DtoUtil.returnDataSuccess(itripSearchDetailsHotelVOList);
            } catch (Exception e) {
               return DtoUtil.returnFail(e.getMessage(),"10211");
            }
        }else{
            return DtoUtil.returnFail("酒店id不能为空","10210");
        }
    }
    @RequestMapping(value = "/queryhotelfeature")
    public Dto queryhotelfeature(){
        System.out.println("查询酒店特色列表方法进入。。。。");
        try {
            List<ItripLabelDicVO> itripLabelDicVOS=itripLabelDicSerivce.getItripLabelDicById(16L);
        return DtoUtil.returnDataSuccess(itripLabelDicVOS);
        } catch (Exception e) {
            return DtoUtil.returnFail(e.getMessage(),"10205");
        }
    }
    @RequestMapping(value = "/queryhotelpolicy/{id}")
    public Dto queryhotelpolicy(@PathVariable Long id){
        System.out.println("根据酒店id查询酒店政策方法进入。。。");
        if(EmptyUtils.isNotEmpty(id)){
            try {
                ItripSearchPolicyHotelVO itripSearchPolicyHotelVO=itripHotelSerivce.queryHotelPolicy(id);
            return DtoUtil.returnDataSuccess(itripSearchPolicyHotelVO.getHotelPolicy());
            } catch (Exception e) {
                return DtoUtil.returnFail(e.getMessage(),"10209");
            }
        }else{
            return DtoUtil.returnFail("酒店id不能为空","10208");
        }

    }
    @RequestMapping(value = "/getimg/{targetId}")
    public Dto getimg(@PathVariable String targetId){
        System.out.println("查询酒店图片方法进入。。。");
        if(EmptyUtils.isNotEmpty(targetId)){

        }else{
            return DtoUtil.returnFail("targetId不能为空","100213");
        }
        return  null;
    }
    @RequestMapping(value = "/getvideodesc/{hotelId}")
    public Dto getvideodesc(@PathVariable long hotelId){
        System.out.println("查询酒店特色，商圈，名称方法进入。。。。");
        if(EmptyUtils.isNotEmpty(hotelId)){
            try {
                HotelVideoDescVO hotelVideoDescVO=itripHotelSerivce.queryWenzimianshu(hotelId);
           return DtoUtil.returnDataSuccess(hotelVideoDescVO);
            } catch (Exception e) {
                return DtoUtil.returnFail(e.getMessage(),"100214");
            }
        }else{
            return DtoUtil.returnFail("hotelId不能为空","100215 ");
        }

    }
    @RequestMapping(value = "/queryhotelfacilities/{id}")
    public Dto queryhotelfacilities(@PathVariable Long id){
        System.out.println("查询酒店设施方法进入。。。。");
        if(EmptyUtils.isNotEmpty(id)){
            try {
                ItripSearchFacilitiesHotelVO searchFacilitiesHotelVO=itripHotelSerivce.getItripHotelFacilitiesById(id);
            return  DtoUtil.returnDataSuccess(searchFacilitiesHotelVO.getFacilities());
            } catch (Exception e) {
                return DtoUtil.returnFail(e.getMessage(),"10207");
            }
        }else{
            return DtoUtil.returnFail("酒店id不能为空","10206");
        }

    }

}

package com.controller;

import com.po.Dto;
import com.po.ItripAreaDic;
import com.serivce.Impl.ItripAreaDicSerivceImpl;
import com.serivce.ItripAreaDicSerivce;
import com.util.DtoUtil;
import com.util.EmptyUtils;
import com.util.vo.ItripAreaDicVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    public ItripAreaDicSerivce getItripAreaDicSerivce() {
        return itripAreaDicSerivce;
    }

    public void setItripAreaDicSerivce(ItripAreaDicSerivce itripAreaDicSerivce) {
        this.itripAreaDicSerivce = itripAreaDicSerivce;
    }
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

        }
        return null;
    }
}

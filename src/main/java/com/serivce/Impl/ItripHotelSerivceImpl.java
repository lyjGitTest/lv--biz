package com.serivce.Impl;

import com.mapper.ItripHotelMapper;
import com.po.ItripAreaDic;
import com.po.ItripHotel;
import com.po.ItripLabelDic;
import com.serivce.ItripHotelSerivce;
import com.util.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class ItripHotelSerivceImpl implements ItripHotelSerivce {
    @Autowired
    private ItripHotelMapper itripHotelMapper;
    @Override
    public List<ItripSearchDetailsHotelVO> queryHotelDetails(Long id) throws Exception {
        return itripHotelMapper.queryHotelDetails(id);
    }

    @Override
    public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(Long id) throws Exception {
        return itripHotelMapper.getItripHotelFacilitiesById(id);
    }

    @Override
    public ItripSearchPolicyHotelVO queryHotelPolicy(Long id) throws Exception {
        return itripHotelMapper.queryHotelPolicy(id);
    }

    @Override
    public HotelVideoDescVO queryWenzimianshu(Long id) throws Exception {
        HotelVideoDescVO hotelVideoDescVO=new HotelVideoDescVO();
        //酒店名称
        ItripHotel itripHotel=itripHotelMapper.getItripHotelById(id);
        hotelVideoDescVO.setHotelName(itripHotel.getHotelName());
        //获得商圈
      List<ItripAreaDic> itripAreaDicList=itripHotelMapper.getHotelAreaByHotelId(id);
       List<String> sharr=new ArrayList<>();
        for(ItripAreaDic itripAreaDic:itripAreaDicList){
            sharr.add(itripAreaDic.getName());
        }
       hotelVideoDescVO.setTradingAreaNameList(sharr);
      //获得特色
        List<ItripLabelDic> itripLabelDics=itripHotelMapper.getHotelFeatureByHotelId(id);
        List<String> tsarr=new ArrayList<>();
       for(ItripLabelDic itripLabelDic:itripLabelDics) {
          tsarr.add(itripLabelDic.getName());
       }
       hotelVideoDescVO.setHotelFeatureList(tsarr);
        return hotelVideoDescVO;
    }
}

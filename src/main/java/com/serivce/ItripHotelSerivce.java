package com.serivce;

import com.po.ItripHotel;
import com.util.vo.HotelVideoDescVO;
import com.util.vo.ItripSearchDetailsHotelVO;
import com.util.vo.ItripSearchFacilitiesHotelVO;
import com.util.vo.ItripSearchPolicyHotelVO;


import java.util.List;

public interface ItripHotelSerivce {
    //通过酒店id
    public ItripHotel getItripHotelById( Long id)throws Exception;

    /*酒店介绍*/
public List<ItripSearchDetailsHotelVO> queryHotelDetails(Long id) throws Exception;
/*查询设施*/
    public ItripSearchFacilitiesHotelVO getItripHotelFacilitiesById(Long id)throws Exception;
    /*查询政策*/
    public ItripSearchPolicyHotelVO queryHotelPolicy(Long id)throws Exception;
    /*查询文字*/
    public HotelVideoDescVO queryWenzimianshu(Long id)throws Exception;
}

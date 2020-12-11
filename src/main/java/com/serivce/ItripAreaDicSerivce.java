package com.serivce;

import com.po.ItripAreaDic;

import java.util.List;
import java.util.Map;

public interface ItripAreaDicSerivce {
    //查询区域字典
    public List<ItripAreaDic>  getItripAreaDicListByMap(Map<String,Object> param) throws Exception;
}

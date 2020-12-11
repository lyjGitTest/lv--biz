package com.serivce.Impl;

import com.mapper.ItripAreaDicMapper;
import com.po.ItripAreaDic;
import com.serivce.ItripAreaDicSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
@Service
@Transactional
public class ItripAreaDicSerivceImpl implements ItripAreaDicSerivce {
    @Autowired
    private ItripAreaDicMapper itripAreaDicMapper;

    public ItripAreaDicMapper getItripAreaDicMapper() {
        return itripAreaDicMapper;
    }

    public void setItripAreaDicMapper(ItripAreaDicMapper itripAreaDicMapper) {
        this.itripAreaDicMapper = itripAreaDicMapper;
    }

    @Override
    public List<ItripAreaDic> getItripAreaDicListByMap(Map<String, Object> param) throws Exception {
        return itripAreaDicMapper.getItripAreaDicListByMap(param);
    }
}

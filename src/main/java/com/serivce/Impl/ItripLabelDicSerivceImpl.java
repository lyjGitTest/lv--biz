package com.serivce.Impl;

import com.mapper.ItripLabelDicMapper;
import com.po.ItripAreaDic;
import com.po.ItripLabelDic;
import com.serivce.ItripAreaDicSerivce;
import com.serivce.ItripLabelDicSerivce;
import com.util.vo.ItripAreaDicVO;
import com.util.vo.ItripLabelDicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ItripLabelDicSerivceImpl implements ItripLabelDicSerivce {
    @Autowired
    private ItripLabelDicMapper itripLabelDicMapper;
    @Override
    public List<ItripLabelDicVO> getItripLabelDicById(Long parentId) throws  Exception {
        return itripLabelDicMapper.getItripLabelDicByParentId(parentId);
    }
}

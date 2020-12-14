package com.serivce;

import com.po.ItripLabelDic;
import com.util.vo.ItripLabelDicVO;

import java.util.List;

public interface ItripLabelDicSerivce {
public List<ItripLabelDicVO> getItripLabelDicById(Long parentId) throws  Exception;
}

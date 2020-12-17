package com.serivce;

import com.util.vo.ItripImageVO;

import java.util.List;
import java.util.Map;

public interface ItripImageService {
    /**通过评论Id和图片类型查询评论图片**/
    public List<ItripImageVO> getItripImageListByMap(Map<String, Object> param)throws Exception;
}

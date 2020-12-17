package com.serivce;

import com.po.ItripComment;
import com.util.Page;
import com.util.vo.ItripImageVO;
import com.util.vo.ItripListCommentVO;
import com.util.vo.ItripScoreCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItripCommentSerivce {
    //通过酒店id查询各类评分
    public ItripScoreCommentVO getCommentAvgScore(Long hotelId) throws Exception;
    //通过map查询各类评论数量
    public Integer getItripCommentCountByMap (Map<String,Object> param) throws Exception;
    //查询评论展示
    public Page<ItripListCommentVO> getItripCommentListByMap(Map<String,Object> param,Integer pageNo,Integer pageSize) throws Exception;
//根据targetId查询评论照片(type=2)
    public List<ItripImageVO> getimg(Long targetId)throws Exception;
}

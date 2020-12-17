package com.serivce.Impl;

import com.mapper.ItripCommentMapper;
import com.po.ItripComment;
import com.serivce.ItripCommentSerivce;
import com.util.Constants;
import com.util.DtoUtil;
import com.util.EmptyUtils;
import com.util.Page;
import com.util.vo.ItripImageVO;
import com.util.vo.ItripListCommentVO;
import com.util.vo.ItripScoreCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItripCommentSerivceImpl implements ItripCommentSerivce  {
    @Autowired
    private ItripCommentMapper itripCommentMapper;
    @Override
    public ItripScoreCommentVO getCommentAvgScore(Long hotelId) throws  Exception{
        return itripCommentMapper.getCommentAvgScore(hotelId);
    }
    @Override
    public Integer getItripCommentCountByMap(Map<String, Object> param) throws Exception {
        return itripCommentMapper.getItripCommentCountByMap(param);
    }

    @Override
    public Page<ItripListCommentVO> getItripCommentListByMap(Map<String, Object> param,Integer pageNo,Integer pageSize) throws Exception {
       Integer num=itripCommentMapper.getItripCommentCountByMap(param);
        pageNo= EmptyUtils.isEmpty(pageNo)?Constants.DEFAULT_PAGE_NO:pageNo;
        pageSize=EmptyUtils.isEmpty(pageSize)?Constants.DEFAULT_PAGE_SIZE:pageSize;
        Page p=new Page(pageNo,pageSize,num);
        //起始位置
        param.put("beginPos",p.getBeginPos());
        //页面容量
        param.put("pageSize",p.getPageSize());
       List<ItripListCommentVO> itripListCommentVOList=itripCommentMapper.getItripCommentListByMap(param);
       if(EmptyUtils.isNotEmpty(itripListCommentVOList)){
            p.setRows(itripListCommentVOList);
        }
       return p;
    }
    @Override
    public  List<ItripImageVO> getimg(Long targetId) throws Exception {
        List<ItripImageVO> itripImageVOS=itripCommentMapper.getimg(targetId);
        return itripImageVOS;
    }
}

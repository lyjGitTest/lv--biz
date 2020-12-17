package com.controller;

import com.po.Dto;
import com.po.ItripComment;
import com.po.ItripImage;
import com.serivce.ItripCommentSerivce;
import com.util.DtoUtil;
import com.util.EmptyUtils;
import com.util.Page;
import com.util.vo.ItripImageVO;
import com.util.vo.ItripScoreCommentVO;
import com.util.vo.ItripSearchCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/comment")
public class ItripCommentController {
    @Autowired
    private ItripCommentSerivce itripCommentSerivce;

    @RequestMapping("/gethotelscore/{hotelId}")
    public Dto gethotelscore(@PathVariable Long hotelId) {
        System.out.println("根据酒店id查询酒店平均分方法进入。。。。");
        try {
            if (EmptyUtils.isNotEmpty(hotelId)) {
                ItripScoreCommentVO itripScoreCommentVO = itripCommentSerivce.getCommentAvgScore(hotelId);
                if (EmptyUtils.isNotEmpty(itripScoreCommentVO)) {
                    return DtoUtil.returnDataSuccess(itripScoreCommentVO);
                } else {
                    return DtoUtil.returnFail("获取评分失败", "100001");
                }
            } else {
                return DtoUtil.returnFail("hotelId不能为空", "100002");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/getcount/{hotelId}")
    public Dto getcount(@PathVariable Long hotelId) {
        System.out.println("根据酒店id查询评论数量方法进入。。。");
        Map<String, Object> map = new HashMap<>();
        int count = 0;
        try {
            if (EmptyUtils.isNotEmpty(hotelId)) {
                Map<String, Object> param = new HashMap<>();
                param.put("hotelId", hotelId);
                //全部评论
                count = itripCommentSerivce.getItripCommentCountByMap(param);
                System.out.println("count1===" + count);
                if (EmptyUtils.isNotEmpty(count)) {
                    map.put("allcomment", count);
                    param.put("isOk", 1);
                } else {
                    return DtoUtil.returnFail("获取酒店总评论数失败", "100014");
                }
                //值得推荐
                count = itripCommentSerivce.getItripCommentCountByMap(param);
                System.out.println("count2===" + count);
                if (EmptyUtils.isNotEmpty(count)) {
                    map.put("isok", count);
                    param.put("isOk", 0);
                } else {
                    return DtoUtil.returnFail("获取酒店有待评论数失败", "100016");
                }
                //有待改善
                count = itripCommentSerivce.getItripCommentCountByMap(param);
                System.out.println("count3===" + count);
                if (EmptyUtils.isNotEmpty(count)) {
                    map.put("improve", count);
                    param.put("isHavingImg", 1);
                    param.put("isOk", null);
                } else {
                    return DtoUtil.returnFail("获取酒店值得推荐评论数失败", "100017");
                }
                //有图片
                count = itripCommentSerivce.getItripCommentCountByMap(param);
                System.out.println("count4===" + count);
                if (EmptyUtils.isNotEmpty(count)) {
                    map.put("havingimg", count);
                    return DtoUtil.returnDataSuccess(map);
                } else {
                    return DtoUtil.returnFail("获取酒店有图片评论数失败", "100015");
                }
            } else {
                return DtoUtil.returnFail("参数hotelId为空", "100018");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/getcommentlist")
    public Dto getcommentlist(@RequestBody ItripSearchCommentVO itripSearchCommentVO) {
        System.out.println("查询评论列表方法进入。。。。");
        Map<String, Object> map = new HashMap<>();
        if (itripSearchCommentVO.getIsOk() == -1) {
            itripSearchCommentVO.setIsOk(null);
        }
        if (itripSearchCommentVO.getIsHavingImg() == -1) {
            itripSearchCommentVO.setIsHavingImg(null);
        }
        map.put("hotelId", itripSearchCommentVO.getHotelId());
        map.put("isOK", itripSearchCommentVO.getIsOk());
        map.put("isHavingImg", itripSearchCommentVO.getIsHavingImg());
        try {
            Page page = itripCommentSerivce.getItripCommentListByMap(map, itripSearchCommentVO.getPageNo(), itripSearchCommentVO.getPageSize());
            System.out.println("getBeginPos" + page.getBeginPos());
            System.out.println("getPageSize" + page.getPageSize());
            return DtoUtil.returnDataSuccess(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "getimg/{targetId}")
    public Dto getimg(@PathVariable Long targetId) {
        System.out.println("根据targetId查询评论照片方法进入....");
        if (EmptyUtils.isNotEmpty(targetId)) {
            try {
                List<ItripImageVO> itripImageVO1 = itripCommentSerivce.getimg(targetId);
                return DtoUtil.returnDataSuccess(itripImageVO1);
            } catch (Exception e) {
                return DtoUtil.returnFail("获取评论图片失败","100012");
            }
        } else {
            return DtoUtil.returnFail("评论id不能为空", "100013");
        }

    }
}






package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.mapper.ItripUserLinkUserMapper;
import com.po.Dto;
import com.po.ItripUser;
import com.po.ItripUserLinkUser;
import com.serivce.ItripUserLinkUserSerivce;
import com.util.DtoUtil;
import com.util.EmptyUtils;
import com.util.vo.ItripAddUserLinkUserVO;
import com.util.vo.ItripModifyUserLinkUserVO;
import com.util.vo.ItripSearchUserLinkUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/userinfo")
public class ItripUserLinkUserController {
    private Jedis jedis=new Jedis("127.0.0.1",6379);
    @Autowired
    private ItripUserLinkUserSerivce itripUserLinkUserSerivce;

    public ItripUserLinkUserSerivce getItripUserLinkUserSerivce() {
        return itripUserLinkUserSerivce;
    }

    public void setItripUserLinkUserSerivce(ItripUserLinkUserSerivce itripUserLinkUserSerivce) {
        this.itripUserLinkUserSerivce = itripUserLinkUserSerivce;
    }

    @RequestMapping(value = "/adduserlinkuser")
    public Dto adduserlinkuser(HttpServletRequest request, HttpServletResponse response, @RequestBody ItripAddUserLinkUserVO itripAddUserLinkUserVO){
        System.out.println("进入添加方法。。。");
        String token=request.getHeader("token");
        String tokenJson=jedis.get(token);
        JSONObject jsonObject=JSONObject.parseObject(tokenJson);
        ItripUser itripUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if(EmptyUtils.isNotEmpty(itripUser)){
            ItripUserLinkUser linkUser=new ItripUserLinkUser();
            linkUser.setLinkIdCard(itripAddUserLinkUserVO.getLinkIdCard());
            linkUser.setLinkPhone(itripAddUserLinkUserVO.getLinkPhone());
            linkUser.setLinkIdCardType(itripAddUserLinkUserVO.getLinkIdCardType());
            linkUser.setLinkUserName(itripAddUserLinkUserVO.getLinkUserName());
            linkUser.setUserId(itripUser.getId());
            linkUser.setCreatedBy(itripUser.getId());
            linkUser.setCreationDate(new Date(System.currentTimeMillis()));
            try {
                int num=itripUserLinkUserSerivce.insertItripUserLinkUser(linkUser);
           if (num>0){
               return DtoUtil.returnSuccess("添加成功");
           }else{
               return DtoUtil.returnFail("添加失败","100411");
           }
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("系统异常","100411");
            }
        }else {
            return DtoUtil.returnFail("tocken失效，重新登陆","100000");
        }
    }

@RequestMapping(value = "/deluserlinkuser")
    public Dto deluserlinkuser(HttpServletRequest request,HttpServletResponse response,Long[] ids) {
    System.out.println("删除方法进入。。。");
    String token = request.getHeader("token");
    String tokenjson = jedis.get(token);
    JSONObject jsonObject = JSONObject.parseObject(tokenjson);
    ItripUser itripUser = JSONObject.toJavaObject(jsonObject, ItripUser.class);
    if (EmptyUtils.isNotEmpty(itripUser)) {
        if (EmptyUtils.isNotEmpty(ids)) {
            try {
                int num = itripUserLinkUserSerivce.deleteItripUserLinkUserByIds(ids);
                if (num > 0) {
                    return DtoUtil.returnSuccess("删除成功");
                } else {
                    return DtoUtil.returnFail("删除失败", "100432");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("系统异常", "100411");
            }
        } else {
            return DtoUtil.returnFail("请选择用户", "100433");
        }
    } else {
        return DtoUtil.returnFail("token失效，重新登陆", "100000");
    }
}
@RequestMapping(value = "/queryuserlinkuser")
    public Dto queryuserlinkuser(HttpServletRequest request, HttpServletResponse response,@RequestBody ItripSearchUserLinkUserVO itripSearchUserLinkUserVO){
    System.out.println("进入查询方法。。。");
    String token=request.getHeader("token");
    String tokenjson=jedis.get(token);
    JSONObject jsonObject=JSONObject.parseObject(tokenjson);
    ItripUser itripUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
    if(EmptyUtils.isNotEmpty(itripUser)){

        String name=(itripSearchUserLinkUserVO==null)?null:itripSearchUserLinkUserVO.getLinkUserName();
        Map<String,Object> map=new HashMap<>();
        map.put("userId",itripUser.getId());
        map.put("linkUserName",name);
        try {
            List<ItripUserLinkUser> userLinkUserList=itripUserLinkUserSerivce.getItripUserLinkUserListByMap(map);
            System.out.println(userLinkUserList);
            return  DtoUtil.returnSuccess("查询成功",userLinkUserList);
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("系统异常","100401");
        }
    }else {
        return DtoUtil.returnFail("tocken失效，重新登陆","100000");
    }

}
    @RequestMapping(value = "/modifyuserlinkuser")
    public Dto modifyuserlinkuser(HttpServletRequest request, HttpServletResponse response, @RequestBody ItripModifyUserLinkUserVO itripModifyUserLinkUserVO){
        System.out.println("修改方法进入。。。");
        String token=request.getHeader("token");
        String tokenjson=jedis.get(token);
        JSONObject jsonObject=JSONObject.parseObject(tokenjson);
        ItripUser itripUser=JSONObject.toJavaObject(jsonObject,ItripUser.class);
        if(EmptyUtils.isNotEmpty(itripUser)){
            if (EmptyUtils.isNotEmpty(itripModifyUserLinkUserVO)){
                ItripUserLinkUser linkUser=new ItripUserLinkUser();
                linkUser.setId(itripModifyUserLinkUserVO.getId());
                linkUser.setLinkIdCardType(itripModifyUserLinkUserVO.getLinkIdCardType());
                linkUser.setLinkIdCard(itripModifyUserLinkUserVO.getLinkIdCard());
                linkUser.setLinkUserName(itripModifyUserLinkUserVO.getLinkUserName());
                linkUser.setLinkPhone(itripModifyUserLinkUserVO.getLinkPhone());
                linkUser.setUserId(itripUser.getId());
                linkUser.setModifiedBy(itripUser.getId());
                linkUser.setModifyDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
                try {
                    int num=itripUserLinkUserSerivce.updateItripUserLinkUser(linkUser);
                if (num>0){
                    return DtoUtil.returnSuccess("修改成功");
                }else{
                    return DtoUtil.returnFail("修改失败","100421");
                }
                } catch (Exception e) {
                    e.printStackTrace();
                    return DtoUtil.returnFail("系统异常","100411");
                }
            }else {
                return DtoUtil.returnFail("不能提交为空，请填写","100422");
            }
        }else{
            return DtoUtil.returnFail("token失效，重新登陆","100000");
        }

    }
}

package com.serivce.Impl;

import com.mapper.ItripImageMapper;
import com.serivce.ItripImageService;
import com.util.vo.ItripImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
@Transactional
public class ItripImageServiceImpl implements ItripImageService {
    @Autowired
    private ItripImageMapper itripImageMapper;
    @Override
    public List<ItripImageVO> getItripImageListByMap(Map<String, Object> param) throws Exception {
        return itripImageMapper.getItripImageListByMap(param);
    }
}

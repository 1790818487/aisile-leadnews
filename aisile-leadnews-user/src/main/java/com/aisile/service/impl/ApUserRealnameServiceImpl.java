package com.aisile.service.impl;


import com.aisile.common.exception.CustomExceptionCatch;
import com.aisile.mapper.ApUserRealnameMapper;
import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.PageResponseResult;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.model.user.pojos.ApUserRealname;
import com.aisile.service.IApUserRealnameService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * APP实名认证信息表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-02
 */
@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements IApUserRealnameService {



    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseResult showRealName(PageRequestDto dto, int status) {
        dto.checkParam();

        LambdaQueryWrapper<ApUserRealname> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(
                status == 1 || status == 0 || status == 2 || status == 9,
                ApUserRealname::getStatus, status);
        IPage<ApUserRealname> page = this.page(new Page<>(dto.getPage(), dto.getSize()), wrapper);
        PageResponseResult result = new PageResponseResult((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal());
        result.setData(page.getRecords());
        return result;
    }

    @Override
    public ResponseResult updateStatus(int status, int id) {
        if (status != 0 && status != 1 && status != 2 && status != 9)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.PARAM_INVALID);
        ApUserRealname realname = new ApUserRealname();
        realname.setId(id);
        realname.setStatus((short) status);
        this.updateById(realname);
        return ResponseResult.errorResult(AppHttpCodeEnum.SUCCESS);
    }

    //用户实名认证信息提交后,调用这个方法,把用户的id插入到消息队列中
    @Override
    public ResponseResult userUealName(ApUserRealname realname) {
        if (realname == null)
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        realname.setSubmitedTime(new Date());
        realname.setStatus((short) 1);
        this.save(realname);
        rabbitTemplate.convertAndSend("queen.real", realname.getId());
        return ResponseResult.errorResult(AppHttpCodeEnum.SUCCESS);
    }

}

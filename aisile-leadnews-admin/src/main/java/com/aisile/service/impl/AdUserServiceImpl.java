package com.aisile.service.impl;


import com.aisile.common.exception.CustomExceptionCatch;
import com.aisile.mapper.AdUserMapper;
import com.aisile.model.admin.dtos.AdUserDto;
import com.aisile.model.admin.pojos.AdUser;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.service.IAdUserService;
import com.aisile.utils.common.AppJwtUtil;
import com.aisile.utils.common.BCrypt;
import com.aisile.utils.threadlocal.AdminThreadLocalUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.awt.AWTUtilities;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.Date;
import java.util.HashMap;

/**
 * <p>
 * 管理员用户信息表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-10-31
 */
@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements IAdUserService {

    @Override
    public ResponseResult login(AdUserDto dto) {
        //判断空
        if (dto == null || "".equals(dto.getName()) || "".equals(dto.getPassword()))
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.PARAM_REQUIRE);
        //构造查询的条件，通过账号查询到信息
        QueryWrapper<AdUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(AdUser::getName, dto.getName());
        AdUser adUser = this.getOne(wrapper);
        if (adUser == null)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        if (adUser.getStatus()==1)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.USERNAME_DISABLED);
        if (BCrypt.checkpw(dto.getPassword(), adUser.getPassword())) {
            //更新登录时间
            adUser.setLoginTime(new Date());
            this.updateById(adUser);
            //拿到一个token
            String token = AppJwtUtil.getToken((long) adUser.getId());
            HashMap<String, Object> hashMap = new HashMap<>();
            adUser.setPassword("********");
            hashMap.put("token", token);
            hashMap.put("adUser", adUser);
//            AdminThreadLocalUtils.setUser(adUser);
            return ResponseResult.okResult(hashMap);
        } else
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
    }
}

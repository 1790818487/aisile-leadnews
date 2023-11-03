package com.aisile.service;

import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.user.pojos.ApUserRealname;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * <p>
 * APP实名认证信息表 服务类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-02
 */
public interface IApUserRealnameService extends IService<ApUserRealname> {
    ResponseResult showRealName(PageRequestDto dto,int status);
    ResponseResult updateStatus(int status,int id);
}

package com.aisile.service.impl;


import com.aisile.common.exception.CustomExceptionCatch;
import com.aisile.mapper.ApUserRealnameMapper;
import com.aisile.model.common.dtos.PageRequestDto;
import com.aisile.model.common.dtos.PageResponseResult;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.model.user.pojos.ApUserRealname;
import com.aisile.service.IApUserRealnameService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

    @Override
    public ResponseResult showRealName(PageRequestDto dto, int status) {
        dto.checkParam();

        LambdaQueryWrapper<ApUserRealname> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(
                status == 1 || status == 0 || status == 2 || status == 9,
                ApUserRealname::getStatus, status);
        IPage<ApUserRealname> page = this.page(new Page<>(dto.getPage(), dto.getSize()),wrapper);
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

}

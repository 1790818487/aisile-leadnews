package com.aisile.service.impl;

import com.aisile.common.exception.CustomException;
import com.aisile.common.exception.CustomExceptionCatch;
import com.aisile.mapper.WmNewsMaterialMapper;
import com.aisile.model.common.dtos.PageResponseResult;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.model.media.dtos.WmMaterialDto;
import com.aisile.model.media.dtos.WmNewsPageReqDto;
import com.aisile.model.media.pojos.WmMaterial;
import com.aisile.mapper.WmMaterialMapper;
import com.aisile.model.media.pojos.WmNews;
import com.aisile.model.media.pojos.WmNewsMaterial;
import com.aisile.service.IWmMaterialService;
import com.aisile.utils.threadlocal.WmThreadLocalUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 自媒体图文素材信息表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-10
 */
@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements IWmMaterialService {

    @Override
    //自媒体人的素材管理,需要请求头中存在user_id
    public ResponseResult showAllMaterial(WmMaterialDto dto) {
        if (WmThreadLocalUtils.getUser().getApUserId() == null)
            return ResponseResult.errorResult(AppHttpCodeEnum.UNKNOWN_USER);
        if (dto == null)
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        dto.checkParam();
        Page<WmMaterial> page = new Page<>(dto.getPage(), dto.getSize());

        if (dto.getIsCollection() == null || dto.getIsCollection() != 1)
            dto.setIsCollection((short) 0);
        LambdaQueryWrapper<WmMaterial> wrapper = Wrappers.lambdaQuery(new WmMaterial())
                .eq(WmMaterial::getUserId, WmThreadLocalUtils.getUser().getApUserId())
                .eq(dto.getIsCollection() == 1, WmMaterial::getIsCollection, dto.getIsCollection());

        IPage<WmMaterial> page1 = this.page(page, wrapper);


        PageResponseResult result = new PageResponseResult(
                (int) page1.getCurrent(),
                (int) page1.getSize(),
                (int) page1.getTotal());
        result.setData(page1.getRecords());
        return result;
    }

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    @Override
    public ResponseResult delMaterialById(int id) {
        List<WmNewsMaterial> li = wmNewsMaterialMapper.selectList(
                Wrappers.lambdaQuery(new WmNewsMaterial())
                        .eq(WmNewsMaterial::getMaterialId, id)
        );
        if (li.size() != 0)
            return ResponseResult.errorResult(AppHttpCodeEnum.PICTURE_NO_DEL);
        else
            this.removeById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

}

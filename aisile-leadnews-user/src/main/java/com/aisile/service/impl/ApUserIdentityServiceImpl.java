package com.aisile.service.impl;

import com.aisile.common.exception.CustomExceptionCatch;
import com.aisile.feign.ArticleOpenFeign;
import com.aisile.feign.WemediaOpenFeign;
import com.aisile.mapper.ApUserMapper;
import com.aisile.mapper.ApUserRealnameMapper;
import com.aisile.model.article.pojos.ApAuthor;
import com.aisile.model.article.pojos.WmUserAndToken;
import com.aisile.model.common.dtos.ResponseResult;
import com.aisile.model.common.enums.AppHttpCodeEnum;
import com.aisile.model.media.pojos.WmUser;
import com.aisile.model.user.dtos.AuthDto;
import com.aisile.model.user.pojos.ApUser;
import com.aisile.model.user.pojos.ApUserIdentity;
import com.aisile.mapper.ApUserIdentityMapper;
import com.aisile.model.user.pojos.ApUserRealname;
import com.aisile.service.IApUserIdentityService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * APP身份认证信息表 服务实现类
 * </p>
 *
 * @author 黎明
 * @since 2023-11-06
 */
@Service
public class ApUserIdentityServiceImpl extends ServiceImpl<ApUserIdentityMapper, ApUserIdentity> implements IApUserIdentityService {

    @Autowired
    private ApUserRealnameMapper realnameMapper;

    @Autowired
    private WemediaOpenFeign wemediaOpenFeign;

    @Autowired
    private ArticleOpenFeign articleOpenFeign;

    @Autowired
    protected ApUserMapper apUserMapper;

    @Override
    public ResponseResult userIdentity(AuthDto dto, short type) {
        //先判断空
        if (dto == null || type != 0 && type != 1 && type != 2)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.PARAM_INVALID);

        //从数据库查询出来此用户
        ApUserIdentity identity = this.getOne(
                Wrappers.lambdaQuery(new ApUserIdentity())
                        .eq(ApUserIdentity::getUserId, dto.getId())
        );

        //查询实名认证的表信息
        ApUserRealname apUserRealname = realnameMapper.selectOne(
                Wrappers.lambdaQuery(new ApUserRealname())
                        .eq(ApUserRealname::getUserId, identity.getUserId())
        );
        if (apUserRealname == null)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        //判断实名认证的状态
        if (apUserRealname.getStatus() == 9)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.USER_NONE_IDENTITY);

        //用户以上都通过.开始对身份认证进行审核
        ApUserIdentity userIdentity = this.getOne(
                Wrappers.lambdaQuery(new ApUserIdentity())
                        .eq(ApUserIdentity::getUserId, identity.getUserId())
        );
        //如果状态未9或者2,不用重复审核,直接返回
        if (userIdentity.getStatus() == 9 || userIdentity.getStatus() == 2)
            CustomExceptionCatch.catchsApp(AppHttpCodeEnum.USER_ALREADY_IDENTITY);
        //在这里修改状态,审核通过,并且生成账号和密码,并告知用户成功了

        return this.createdArticleAndWe(userIdentity, type);
    }

    //创建自媒体账号
    @Transactional
    public ResponseResult createdArticleAndWe(ApUserIdentity identity, short type) {
        ApUser apUser = apUserMapper.selectOne(
                Wrappers.lambdaQuery(new ApUser())
                        .eq(ApUser::getId, identity.getUserId())
        );
        apUser.setFlag(type);
        identity.setStatus((short) 9);
        identity.setUpdatedTime(LocalDateTime.now());
        this.updateById(identity);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = attributes.getRequest().getHeader("token");
        WmUser wmUser = wemediaOpenFeign.findByUserId(identity.getUserId(), token);
        if (wmUser == null) {
            wmUser = new WmUser();
            wmUser.setApUserId(apUser.getId());
            wmUser.setName(apUser.getId().toString());
            wmUser.setPassword(apUser.getPassword());
            wmUser.setNickname(apUser.getName());
            wmUser.setStatus(0);
            wemediaOpenFeign.addWmUser(wmUser, token);
        }

        Integer id = this.createdArticle(wmUser, token).getId();
        wmUser.setApAuthorId(id);
        return wemediaOpenFeign.updateById(wmUser, token);
    }

    //创建作者信息

    public ApAuthor createdArticle(WmUser wmUser, String token) {
        ApAuthor apAuthor = articleOpenFeign.findByUserId(wmUser.getApUserId(), token);
        if (apAuthor == null) {
            apAuthor = new ApAuthor();
            apAuthor.setUserId(wmUser.getApUserId());
            apAuthor.setCreatedTime(LocalDateTime.now());
            apAuthor.setType(Short.parseShort(wmUser.getType().toString()));
            apAuthor.setWmUserId(wmUser.getId());
            articleOpenFeign.saveArticle(apAuthor, token);
        }
        return apAuthor;
    }
}

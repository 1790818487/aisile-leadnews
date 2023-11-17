package com.aisile.model.article.pojos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * APP已发布文章配置表
 * </p>
 *
 * @author 黎明
 * @since 2023-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ap_article_config")
public class ArticleConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 是否可评论
     */
    private Boolean isComment;

    /**
     * 是否转发
     */
    private Boolean isForward;

    /**
     * 是否下架
     */
    private Boolean isDown;

    /**
     * 是否已删除
     */
    private Boolean isDelete;


}

package com.aisile.model.article.pojos;

import com.aisile.model.media.pojos.WmUser;
import lombok.Data;

@Data
public class WmUserAndToken {
    private WmUser wmUser;
    private String token;
} 
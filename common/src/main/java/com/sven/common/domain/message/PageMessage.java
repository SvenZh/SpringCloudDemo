package com.sven.common.domain.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageMessage {

    private int pageSize = 10;
    
    private int pageNo = 1;
}

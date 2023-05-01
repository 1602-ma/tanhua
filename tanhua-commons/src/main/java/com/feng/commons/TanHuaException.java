package com.feng.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author f
 * @date 2023/5/1 10:47
 */
@Data
@NoArgsConstructor
public class TanHuaException extends RuntimeException{

    private Object errData;

    public TanHuaException(String errMessage) {
        super(errMessage);
    }

    public TanHuaException(Object data) {
        super();
        this.errData = data;
    }
}

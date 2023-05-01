package com.feng.server.exception;

import com.feng.commons.TanHuaException;
import com.feng.domain.vo.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author f
 * @date 2023/5/1 10:58
 */
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    /**
     * 处理自定义的业务异常
     * @param ex ex
     * @return   res
     */
    public ResponseEntity handleTanHuaException(TanHuaException ex) {
        if (null != ex.getErrData()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getErrData());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResult.error("000009", ex.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleException(Exception ex) {
        log.error("网络错误，请稍后再试", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResult.error());
    }


}

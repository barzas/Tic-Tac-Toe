package com.example.tictactoe.tictactoe.dto.response;

import com.example.tictactoe.tictactoe.dto.ErrorCode;

public abstract class BaseResponse {
    private final boolean success;
    private final ErrorCode errorCode;

    protected BaseResponse(boolean success, ErrorCode errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() { return success; }
    public ErrorCode getErrorCode() { return errorCode; }
    public String getError() { return errorCode != null ? errorCode.getMessage() : null; }
}

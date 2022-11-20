package springboot.shoppingmall.api.util;

import lombok.Data;

@Data
public class ApiResult<T> {

    private String returnCode;
    private String message;
    private T body;

    public static ApiResult build(){
        return new ApiResult<>();
    }

    public ApiResult body(T body){
        this.body = body;
        return this;
    }

    public ApiResult message(String message){
        this.message = message;
        return this;
    }

    public ApiResult returnCode(String returnCode){
        this.returnCode = returnCode;
        return this;
    }
}

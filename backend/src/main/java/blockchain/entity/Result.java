package blockchain.entity;

import lombok.Data;

//表示web请求的返回，包含关于请求的信息（是否成功，失败代码，失败信息等）
@Data
public class Result {
    private boolean success;
    private String message;
    private String status;
    public Result(){}
    public Result(boolean success,String message,String status)
    {
        this.message = message;
        this.success = success;
        this.status = status;
    }
}

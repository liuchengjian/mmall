package liucj.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.List;

/**
 * 公共的返回结果Response
 *
 * @param <T>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候,如果是null的对象,key也会消失
public class ServerResponse<T> implements Serializable {
    private int status;
    private boolean success;
    private String msg;
    private T data;
    private List<T> list;
    private int total;


    /**
     * 私有构造器
     *
     * @param status
     */
    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, List<T> list, int total) {
        this.status = status;
        this.list = list;
        this.total = total;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> ServerResponse<T> createByListSuccess(List<T> list, int total) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), list, total);
    }

    /**
     * 错误
     *
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<T>(errorCode, errorMessage);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    @JsonIgnore
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

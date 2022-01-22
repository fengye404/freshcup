package sast.freshcup.common.enums;

/**
 * @author: 風楪fy
 * @create: 2022-01-15 17:18
 **/
public enum ErrorEnum {

    COMMON_ERROR(5000, "错误"),

    TOKEN_ERROR(5001,"TOKEN错误"),

    NO_LOGIN(5002,"没有登录"),

    NO_USER(6000, "没有用户"),

    NO_PROBLEM(7000, "没有问题"),

    NO_CONTEST(8000, "没有比赛"),
    DATE_ERROR(8001, "时间设置错误")
    ;


    private Integer errCode;
    private String errMsg;

    ErrorEnum(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }
}

package ir.controller;

import io.micrometer.core.instrument.util.StringUtils;
import ir.common.Constant;
import ir.common.Message;

public class BaseController {
  /**
   * 构建Response.
   *
   * @param success 是否成功
   * @param errorCode 错误状态码
   * @param info 信息
   * @param data 数据
   */
  protected Message buildRestResult(boolean success, Integer errorCode, String info,
      Object data) {
    info = StringUtils.isNotBlank(info) ? info : success ? "操作成功" : "操作失败";
    errorCode =
        errorCode != null
            ? errorCode
            : success ? Constant.ERROR_CODE_SUCCESS : Constant.ERROR_CODE_FAIL;
    return Message.builder().success(success ? true : false).errorCode(errorCode).info(info).data(data).build();
  }

  /**
   * 构建Response.
   *
   * @param data 数据
   */
  public Message buildRestResult(Object data) {
    boolean success = data != null;
    return this.buildRestResult(success, null, success ? "操作成功" : "操作失败", data);
  }
}

package ir.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class Message {

  private String info;
  private Object data;
  private Boolean success;
  private Integer errorCode;

}

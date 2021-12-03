package ir.utils;


public class GetLongTime {

  public static long getLongTime(String t) {
    if (t.indexOf("-") < 0) {
      return 0;
    } else {
      long t0;
      String[] t1 = t.split(" ");
      String t2 = t1[0].replace("-", "");
      t0 = !t2.equals("") ? Long.parseLong(t2) : 0;
      return t0;
    }
  }
}

package pers.solid.mishang.uc.arrp;

import net.devtech.arrp.json.models.JTextures;

public class FasterJTextures extends JTextures implements Cloneable /* BRRP 存在没有让 JTextures 实现 Cloneable 的问题 */ {
  public FasterJTextures varP(String name, String val) {
    var(name, "mishanguc:block/" + val);
    return this;
  }

  public FasterJTextures top(String val) {
    return varP("top", val);
  }

  public FasterJTextures side(String val) {
    return varP("side", val);
  }

  public FasterJTextures bottom(String val) {
    return varP("bottom", val);
  }

  public FasterJTextures base(String val) {
    return varP("base", val);
  }

  public FasterJTextures line(String val) {
    return varP("line", val);
  }

  public FasterJTextures particle(String val) {
    return varP("particle", val);
  }

  public FasterJTextures lineSide(String val) {
    return varP("line_side", val);
  }

  public FasterJTextures lineSide2(String val) {
    return varP("line_side2", val);
  }

  public FasterJTextures lineTop(String val) {
    return varP("line_top", val);
  }

  public static FasterJTextures ofP(String key1, String value1) {
    return new FasterJTextures().varP(key1, value1);
  }

  public static FasterJTextures ofP(String key1, String value1, String key2, String value2) {
    return new FasterJTextures().varP(key1, value1).varP(key2, value2);
  }

  public static FasterJTextures ofP(String key1, String value1, String key2, String value2, String key3, String value3) {
    return new FasterJTextures().varP(key1, value1).varP(key2, value2).varP(key3, value3);
  }

  @Override
  public FasterJTextures clone() {
    return (FasterJTextures) super.clone();
  }
}

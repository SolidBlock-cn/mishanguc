package pers.solid.mishang.uc.arrp;

import net.devtech.arrp.json.models.JTextures;

public class FasterJTextures extends JTextures {
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

  public static FasterJTextures ofP(String... values) {
    final FasterJTextures r = new FasterJTextures();
    for (int i = 0; i < values.length; i++) {
      if (i + 1 >= values.length) break;
      r.varP(values[i], values[i + 1]);
      i++;
    }
    return r;
  }
}

package pers.solid.mishang.uc.arrp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MiningLevel {
  Tool value() default Tool.PICKAXE;

  Level level() default Level.NONE;

  enum Tool {
    PICKAXE, SHOVEL, AXE, HOE, NONE
  }

  enum Level {
    NONE, STONE, IRON, DIAMOND
  }
}

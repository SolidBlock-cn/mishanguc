package pers.solid.mishang.uc.arrp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定方块各部分材质的注解。<br>
 * {@link ARRPMain} 可能会使用到这些值。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Texture {
  /**
   * 告示牌主体部分的材质。若为空，则默认使用字段的 baseBlock 对应的材质，但这种情况是直接根据基础方块的 ID 进行推断的，有时候会出现一些问题。
   */
  String texture() default "";

  /**
   * 告示牌发光部分的材质。
   */
  String glowTexture() default "";
}

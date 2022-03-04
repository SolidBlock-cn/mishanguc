package pers.solid.mishang.uc.mixin;

import net.devtech.arrp.json.blockstate.JWhen;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@SuppressWarnings({
    "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc",
    "AlibabaClassNamingShouldBeCamel",
    "AlibabaLowerCamelCaseVariableNaming"
})
@Mixin(JWhen.class)
public interface JWhenAccessor {
  @Accessor
  List<Pair<String, String[]>> getOR();
}

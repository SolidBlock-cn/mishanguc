package pers.solid.mishang.uc.arrp;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.devtech.arrp.json.blockstate.JWhen;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.mixin.JWhenAccessor;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * 修复有多个属性时，产生的JSON格式不正确的问题。<br>
 *
 * @deprecated 未来版本的 ARRP 模组将会修复这个问题，届时一段时间后，本模组不再进行本地修复，同时对 ARRP 的版本提出要求。
 */
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "0.1.6")
public class FixedWhen extends JWhen {
  public static class Serializer implements JsonSerializer<FixedWhen> {

    @Override
    public JsonElement serialize(FixedWhen src, Type typeOfSrc, JsonSerializationContext context) {
      final List<Pair<String, String[]>> OR = ((JWhenAccessor) src).getOR();
      if (OR.size() == 1) {
        return new JWhen.Serializer().serialize(src, typeOfSrc, context);
      } else {
        JsonObject json = new JsonObject();
        for (Pair<String, String[]> val : OR) {
          json.addProperty(val.getLeft(), String.join("|", Arrays.asList(val.getRight())));
        }
        return json;
      }
    }
  }
}

import net.devtech.arrp.impl.RuntimeResourcePackImpl;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.minecraft.util.Identifier;

public class Main {
  public static void main(String[] args) {
    final JState state = new JState().add(
        new JVariant()
            .put("", new JBlockModel(new Identifier("test_id")))
    ).add(new JVariant()
        .put("", new JBlockModel(new Identifier("text_id"))));
    System.out.println(RuntimeResourcePackImpl.GSON.toJson(state));
  }
}

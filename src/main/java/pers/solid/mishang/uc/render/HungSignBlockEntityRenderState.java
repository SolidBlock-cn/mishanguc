package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class HungSignBlockEntityRenderState extends BlockEntityRenderState {
  public @Unmodifiable Map<Direction, @Unmodifiable List<TextContext>> texts;
  public @Unmodifiable Set<Direction> glowing;
  public float height;
  public Direction.Axis axis;
}

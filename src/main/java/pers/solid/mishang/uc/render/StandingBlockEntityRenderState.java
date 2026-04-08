package pers.solid.mishang.uc.render;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.booleans.BooleanSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;

@Environment(EnvType.CLIENT)
public class StandingBlockEntityRenderState extends BlockEntityRenderState {
  public BooleanSet glowing;
  public List<TextContext> frontTexts = ImmutableList.of();
  public List<TextContext> backTexts = ImmutableList.of();
  public float height;
}

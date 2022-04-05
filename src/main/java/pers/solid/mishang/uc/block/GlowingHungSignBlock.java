package pers.solid.mishang.uc.block;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;

public class GlowingHungSignBlock extends HungSignBlock {
  /**
   * 由基础方块映射到对应的发光悬挂告示牌的方块。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public static final Reference2ReferenceMap<Block, HungSignBlock> BASE_TO_GLOWING_HUNG_SIGN = new Reference2ReferenceOpenHashMap<>();
  @ApiStatus.AvailableSince("0.1.7")
  protected static final String DEFAULT_GLOW_TEXTURE = "mishanguc:block/white_light";
  public String glowTexture;

  public GlowingHungSignBlock(@Nullable Block baseBlock, FabricBlockSettings settings) {
    super(baseBlock, settings.luminance(15));
    this.glowTexture = DEFAULT_GLOW_TEXTURE;
  }

  @ApiStatus.AvailableSince("0.1.7")
  public GlowingHungSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock).luminance(15));
  }

  @Override
  protected void putToMap() {
    BASE_TO_GLOWING_HUNG_SIGN.put(baseBlock, this);
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) {
      return new TranslatableText("block.mishanguc.glowing_hung_sign", baseBlock.getName());
    }
    return super.getName();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final Identifier id = getBlockModelIdentifier();
    final String texture = getBaseTexture();
    final JTextures textures = new JTextures().var("texture", texture).var("glow", glowTexture).var("bar", barTexture).var("texture_top", textureTop);
    pack.addModel(
        JModel.model(new Identifier("mishanguc", "block/glowing_hung_sign"))
            .textures(textures),
        new Identifier(id.getNamespace(), id.getPath()));
    pack.addModel(
        JModel.model(new Identifier("mishanguc", "block/glowing_hung_sign_body"))
            .textures(textures),
        MishangUtils.identifierSuffix(id, "_body"));
    pack.addModel(
        JModel.model(new Identifier("mishanguc", "block/hung_sign_top_bar"))
            .textures(textures),
        MishangUtils.identifierSuffix(id, "_top_bar"));
    pack.addModel(
        JModel.model(new Identifier("mishanguc", "block/hung_sign_top_bar_edge"))
            .textures(textures),
        MishangUtils.identifierSuffix(id, "_top_bar_edge"));
  }
}

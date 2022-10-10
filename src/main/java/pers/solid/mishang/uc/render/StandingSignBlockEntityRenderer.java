package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;

@ApiStatus.AvailableSince("1.0.2")
@Environment(EnvType.CLIENT)
public record StandingSignBlockEntityRenderer<T extends StandingSignBlockEntity>(BlockEntityRendererFactory.Context ctx)implements BlockEntityRenderer<T> {

@Override
public void render(T entity,float tickDelta,MatrixStack matrices,VertexConsumerProvider vertexConsumers,int light,int overlay){
    matrices.translate(0.5,0.75,0.5);
final BlockState state=entity.getCachedState();
final int rotation=state.get(StandingSignBlock.ROTATION);
    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-rotation*22.5f));
    matrices.scale(1/16f,-1/16f,1/16f);

    matrices.push();
    matrices.translate(0,0,0.5125);
    for(TextContext textContext:entity.frontTexts){
    textContext.draw(ctx.getTextRenderer(),matrices,vertexConsumers,light,16,entity.getHeight());
    }
    matrices.pop();
    matrices.push();
    matrices.translate(0,0,-0.5125);
    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
    for(TextContext textContext:entity.backTexts){
    textContext.draw(ctx.getTextRenderer(),matrices,vertexConsumers,light,16,entity.getHeight());
    }
    matrices.pop();
    }
    }

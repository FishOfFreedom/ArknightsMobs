package com.freefish.arknightsmobs.client.render.model.tools;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.*;
import software.bernie.geckolib3.geo.render.built.GeoBone;

/**
 * @author Paul Fulham
 */
public final class MathUtils {

    public static final float TAU = (float) (2 * StrictMath.PI);
    public static final float PI = (float) StrictMath.PI;

    public static void matrixStackFromModel(MatrixStack matrixStack, GeoBone geoBone) {
        GeoBone parent = geoBone.parent;
        if (parent != null) matrixStackFromModel(matrixStack, parent);
        translateRotateGeckolib(geoBone, matrixStack);
    }

    public static Vector3d getWorldPosFromModel(Entity entity, float entityYaw, GeoBone geoBone) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(entity.getPosX(), entity.getPosY(), entity.getPosZ());
        matrixStack.rotate(new Quaternion(0,   -entityYaw - 180, 180, true));
        matrixStack.scale(-1, -1, 1);
        matrixStackFromModel(matrixStack, geoBone);
        MatrixStack.Entry matrixEntry = matrixStack.getLast();
        Matrix4f matrix4f = matrixEntry.getMatrix();

        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix4f);
        return new Vector3d(vec.getX(), vec.getY(), vec.getZ());
    }

    public static void translateRotateGeckolib(GeoBone bone, MatrixStack matrixStackIn) {
        if(bone.parent != null)
            matrixStackIn.translate((double)((bone.rotationPointX-bone.parent.rotationPointX + bone.getPositionX())/ 16.0F),
                    (double)((bone.rotationPointY-bone.parent.rotationPointY + bone.getPositionY()) / 16.0F),
                    (double)((bone.rotationPointZ-bone.parent.rotationPointZ + bone.getPositionZ()) / 16.0F));
        else
            matrixStackIn.translate((double)(bone.rotationPointX / 16.0F), (double)(bone.rotationPointY / 16.0F), (double)(bone.rotationPointZ / 16.0F));

        if (bone.getRotationZ() != 0.0F) {
            matrixStackIn.rotate(Vector3f.ZP.rotation(bone.getRotationZ()));
        }

        if (bone.getRotationY() != 0.0F) {
            matrixStackIn.rotate(Vector3f.YP.rotation(bone.getRotationY()));
        }

        if (bone.getRotationX() != 0.0F) {
            matrixStackIn.rotate(Vector3f.XP.rotation(bone.getRotationX()));
        }
        matrixStackIn.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
    }

    public static double linearTransformd(double x, double domainMin, double domainMax, double rangeMin, double rangeMax) {
        x = x < domainMin ? domainMin : x > domainMax ? domainMax : x;
        return (rangeMax - rangeMin) * (x - domainMin) / (domainMax - domainMin) + rangeMin;
    }

    public static float fade(float i){
        i=i/2 + 0.5f;
        return (6*i*i*i*i*i-15*i*i*i*i+10*i*i*i - 0.5f)*2;
    }

    public static float sampleNoise3D(int x, int y, int z, float simplexSampleRate) {
        return (float) ((ACSimplexNoise.noise((x + simplexSampleRate) / simplexSampleRate, (y + simplexSampleRate) / simplexSampleRate, (z + simplexSampleRate) / simplexSampleRate)));
    }

    public static float cosFromSin(float sin, float angle) {
        float PI_f = (float) java.lang.Math.PI;
        float PIHalf_f = (float) (Math.PI / 2);
        float PI2_f = PI_f * 2.0f;
        float cos = (float) Math.sqrt(1.0f - sin * sin);
        float a = angle + PIHalf_f;
        float b = a - (int)(a / PI2_f) * PI2_f;
        if (b < 0.0)
            b = PI2_f + b;
        if (b >= PI_f)
            return -cos;
        return cos;
    }

    public static class ABGR32 {
        public static int alpha(int p_267257_) {
            return p_267257_ >>> 24;
        }

        public static int red(int p_267160_) {
            return p_267160_ & 255;
        }

        public static int green(int p_266784_) {
            return p_266784_ >> 8 & 255;
        }

        public static int blue(int p_267087_) {
            return p_267087_ >> 16 & 255;
        }

        public static int transparent(int p_267248_) {
            return p_267248_ & 16777215;
        }

        public static int opaque(int p_268288_) {
            return p_268288_ | -16777216;
        }

        public static int color(int p_267196_, int p_266895_, int p_266779_, int p_267206_) {
            return p_267196_ << 24 | p_266895_ << 16 | p_266779_ << 8 | p_267206_;
        }

        public static int color(int p_267230_, int p_266708_) {
            return p_267230_ << 24 | p_266708_ & 16777215;
        }
    }
}

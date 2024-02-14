package com.freefish.arknightsmobs.client.particle.util;

import com.freefish.arknightsmobs.client.particle.ParticleRibbon;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.vector.Vector3d;

public class RibbonComponent extends ParticleComponent {
    int length;
    ParticleType<? extends RibbonParticleData> ribbon;
    double yaw, pitch, roll, scale, r, g, b, a;
    boolean faceCamera;
    boolean emissive;
    ParticleComponent[] components;

    public RibbonComponent(ParticleType<? extends RibbonParticleData> particle, int length, double yaw, double pitch, double roll, double scale, double r, double g, double b, double a, boolean faceCamera, boolean emissive, ParticleComponent[] components) {
        this.length = length;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.scale = scale;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.emissive = emissive;
        this.faceCamera = faceCamera;
        this.components = components;
        this.ribbon = particle;
    }

    @Override
    public void init(AdvancedParticleBase particle) {
        super.init(particle);
        if (particle != null) {

            ParticleComponent[] newComponents = new ParticleComponent[components.length + 2];
            System.arraycopy(components, 0, newComponents, 0, components.length);
            newComponents[components.length] = new AttachToParticle(particle);
            newComponents[components.length + 1] = new Trail();

            ParticleRibbon.spawnRibbon(particle.getWorld(), ribbon, length, particle.getPosX(), particle.getPosY(), particle.getPosZ(), 0, 0, 0, faceCamera, yaw, pitch, roll, scale, r, g, b, a, 0, particle.getMaxAge() + length, emissive, newComponents);
        }
    }

    private static class AttachToParticle extends ParticleComponent {
        AdvancedParticleBase attachedParticle;

        public AttachToParticle(AdvancedParticleBase attachedParticle) {
            this.attachedParticle = attachedParticle;
        }

        @Override
        public void init(AdvancedParticleBase particle) {
            super.init(particle);
            attachedParticle.ribbon = (ParticleRibbon) particle;
        }
    }

    public static class PropertyOverLength extends ParticleComponent {
        public enum EnumRibbonProperty {
            RED, GREEN, BLUE, ALPHA,
            SCALE
        }
        private final AnimData animData;
        private final EnumRibbonProperty property;

        public PropertyOverLength(EnumRibbonProperty property, AnimData animData) {
            this.animData = animData;
            this.property = property;
        }

        public float evaluate(float t) {
            return animData.evaluate(t);
        }

        public EnumRibbonProperty getProperty() {
            return property;
        }
    }

    public static class Trail extends ParticleComponent {
        @Override
        public void postUpdate(AdvancedParticleBase particle) {
            if (particle instanceof ParticleRibbon) {
                ParticleRibbon ribbon = (ParticleRibbon) particle;
                for (int i = ribbon.positions.length - 1; i > 0; i--) {
                    ribbon.positions[i] = ribbon.positions[i - 1];
                    ribbon.prevPositions[i] = ribbon.prevPositions[i - 1];
                }
                ribbon.positions[0] = new Vector3d(ribbon.getPosX(), ribbon.getPosY(), ribbon.getPosZ());
                ribbon.prevPositions[0] = ribbon.getPrevPos();
            }
        }
    }

    public static class BeamPinning extends ParticleComponent {
        private final Vector3d[] startLocation;
        private final Vector3d[] endLocation;

        public BeamPinning(Vector3d[] startLocation, Vector3d[] endLocation) {
            this.startLocation = startLocation;
            this.endLocation = endLocation;
        }

        @Override
        public void postUpdate(AdvancedParticleBase particle) {
            if (particle instanceof ParticleRibbon && validateLocation(startLocation) && validateLocation(endLocation)) {
                ParticleRibbon ribbon = (ParticleRibbon) particle;
                ribbon.setPosition(startLocation[0].getX(), startLocation[0].getY(), startLocation[0].getZ());

                Vector3d increment = endLocation[0].subtract(startLocation[0]).scale(1.0f / (float) (ribbon.positions.length - 1));
                for (int i = 0; i < ribbon.positions.length; i++) {
                    Vector3d newPos = startLocation[0].add(increment.scale(i));
                    ribbon.prevPositions[i] = ribbon.positions[i] == null ? newPos : ribbon.positions[i];
                    ribbon.positions[i] = newPos;
                }
            }
        }

        private boolean validateLocation(Vector3d[] location) {
            return location != null && location.length >= 1 && location[0] != null;
        }
    }

    public static class PanTexture extends ParticleComponent {
        float startOffset = 0;
        float speed = 1;

        public PanTexture(float startOffset, float speed) {
            this.startOffset = startOffset;
            this.speed = speed;
        }

        @Override
        public void preRender(AdvancedParticleBase particle, float partialTicks) {
            if (particle instanceof ParticleRibbon) {
                ParticleRibbon ribbon = (ParticleRibbon) particle;
                float time = (ribbon.getAge() - 1 + partialTicks) / (ribbon.getMaxAge());
                float t = (startOffset + time * speed) % 1.0f;
                ribbon.texPanOffset = (ribbon.getMaxUPublic() - ribbon.getMinUPublic()) / 2 * t;
            }
        }
    }
}

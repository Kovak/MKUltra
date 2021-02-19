package com.chaosbuffalo.mkultra.abilities.green_knight;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.abilities.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.entities.projectiles.SpiritBombProjectileEntity;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpiritBombAbility extends MKAbility {
    public static final SpiritBombAbility INSTANCE = new SpiritBombAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final FloatAttribute baseDamage = new FloatAttribute("baseDamage", 4.0f);
    protected final FloatAttribute scaleDamage = new FloatAttribute("scaleDamage", 4.0f);
    protected final FloatAttribute projectileSpeed = new FloatAttribute("projectileSpeed", 1.25f);
    protected final FloatAttribute projectileInaccuracy = new FloatAttribute("projectileInaccuracy", 0.2f);

    private SpiritBombAbility() {
        super(MKUltra.MODID, "ability.spirit_bomb");
        setCooldownSeconds(16);
        setCastTime(GameConstants.TICKS_PER_SECOND + (GameConstants.TICKS_PER_SECOND / 4));
        setManaCost(4);
        addAttributes(baseDamage, scaleDamage, projectileSpeed, projectileInaccuracy);
        addSkillAttribute(MKAttributes.EVOCATION);
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }


    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_holy;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_magic_whoosh_1;
    }

    public FloatAttribute getBaseDamage() {
        return baseDamage;
    }

    public FloatAttribute getScaleDamage() {
        return scaleDamage;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int level = getSkillLevel(entity, MKAttributes.EVOCATION);
        SpiritBombProjectileEntity proj = new SpiritBombProjectileEntity(entity.world);
        proj.setShooter(entity);
        proj.setAmplifier(level);
        shootProjectile(proj, projectileSpeed.getValue(), projectileInaccuracy.getValue(), entity, context);
        entity.world.addEntity(proj);
    }
}

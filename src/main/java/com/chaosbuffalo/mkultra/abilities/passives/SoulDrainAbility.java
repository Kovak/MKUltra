package com.chaosbuffalo.mkultra.abilities.passives;

import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.abilities.MKPassiveAbility;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.SoulDrainEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoulDrainAbility extends MKPassiveAbility {

    public static final SoulDrainAbility INSTANCE = new SoulDrainAbility();

    protected final FloatAttribute base = new FloatAttribute("base", 4.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 4.0f);

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    public float getDrainValue(LivingEntity entity) {
        int skillLevel = MKAbility.getSkillLevel(entity, MKAttributes.EVOCATION);
        return base.value() + scale.value() * skillLevel;
    }


    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.EVOCATION);
        float value = base.value() + level * scale.value();
        return new TranslationTextComponent(getDescriptionTranslationKey(), value);
    }

    public SoulDrainAbility() {
        super(new ResourceLocation(MKUltra.MODID, "ability.soul_drain"));
        addSkillAttribute(MKAttributes.EVOCATION);
        addAttributes(base, scale);
    }

    @Override
    public MKEffect getPassiveEffect() {
        return SoulDrainEffect.INSTANCE;
    }
}

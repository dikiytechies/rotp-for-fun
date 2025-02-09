package com.dikiytechies.privaterotp.mixin;

import com.dikiytechies.privaterotp.capability.PrivateUtilCap;
import com.dikiytechies.privaterotp.capability.PrivateUtilCapProvider;
import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.init.power.non_stand.hamon.ModHamonSkills;
import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonData;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.skill.AbstractHamonSkill;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.skill.BaseHamonSkill;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HamonData.class)
public abstract class HamonMultiplierMixin extends TypeSpecificData {
    @Shadow(remap = false) public abstract boolean isSkillLearned(AbstractHamonSkill skill);
    @Shadow(remap = false) public abstract void setHamonStatPoints(BaseHamonSkill.HamonStat stat, int points, boolean ignoreTraining, boolean allowLesserValue);
    @Shadow(remap = false)
    protected abstract int getStatPoints(BaseHamonSkill.HamonStat stat);

    @Shadow(remap = false) @Final
    private static final float ENERGY_PER_POINT = 750F;
    @Shadow(remap = false)
    private float pointsIncFrac = 0.0F;
    /**
     * @author dikiytechies
     * @reason wasn't able to leave a variable smoothly
     */
    @Overwrite(remap = false)
    public void hamonPointsFromAction(BaseHamonSkill.HamonStat stat, float energyCost) {
        energyCost *= power.getUser().getCapability(PrivateUtilCapProvider.CAPABILITY).map(PrivateUtilCap::getPointsMultiplier).isPresent()?
                power.getUser().getCapability(PrivateUtilCapProvider.CAPABILITY).map(PrivateUtilCap::getPointsMultiplier).get():1.0F;
        if (isSkillLearned(ModHamonSkills.NATURAL_TALENT.get())) {
            energyCost *= 2;
        }
        energyCost *= JojoModConfig.getCommonConfigInstance(false).hamonPointsMultiplier.get().floatValue();
        int points = (int) (energyCost / ENERGY_PER_POINT);
        pointsIncFrac += (energyCost % ENERGY_PER_POINT) / ENERGY_PER_POINT;
        if (pointsIncFrac >= 1) {
            points++;
            pointsIncFrac--;
        }
        setHamonStatPoints(stat, getStatPoints(stat) + points, false, false);
    }
}

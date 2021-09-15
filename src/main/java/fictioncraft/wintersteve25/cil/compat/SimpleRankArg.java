package fictioncraft.wintersteve25.cil.compat;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects.SimpleEffectArg;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.SimpleObjProvider;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import net.minecraft.potion.Effect;
import net.minecraft.util.Tuple;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.common.rank.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleRankArg extends SimpleArgProvider {

    private final int tier;
    private final int defaultColor;
    private final int numAffixes;
    private final int growthFactor;
    private final float chance;
    private final List<SimpleEffectArg> effects;
    private final List<SimpleObjProvider> affixes;

    public SimpleRankArg(int tier, int defaultColor, int numAffixes, int growthFactor, float chance, List<SimpleEffectArg> effects, List<SimpleObjProvider> affixes) {
        super("Rank");
        this.tier = tier;
        this.defaultColor = defaultColor;
        this.numAffixes = numAffixes;
        this.growthFactor = growthFactor;
        this.chance = chance;
        this.effects = effects;
        this.affixes = affixes;
    }

    public int getTier() {
        return tier;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public int getNumAffixes() {
        return numAffixes;
    }

    public int getGrowthFactor() {
        return growthFactor;
    }

    public float getChance() {
        return chance;
    }

    public List<SimpleEffectArg> getEffects() {
        return effects;
    }

    public List<SimpleObjProvider> getAffixes() {
        return affixes;
    }

    public static Effect getEffectFromProvider(SimpleEffectArg jsonIn) {
        return JsonSerializer.EffectSerializer.getEffectFromJson(jsonIn);
    }

    public static Tuple<Effect, Integer> getTupleFromConfig(SimpleEffectArg effectArgIn) {
        return new Tuple<>(getEffectFromProvider(effectArgIn), effectArgIn.getEffectLevel());
    }

    public static Optional<IAffix> getAffixFromProvider(SimpleObjProvider affixProvider) {
        return Champions.API.getAffix(affixProvider.getName());
    }

    public static Rank getRankFromProvider(SimpleRankArg jsonRank) {
        List<Tuple<Effect, Integer>> effectList = new ArrayList<>();
        List<IAffix> affixList = new ArrayList<>();

        if (jsonRank.getEffects() == null) return null;
        if (jsonRank.getAffixes() == null) return null;

        for (SimpleEffectArg tupleIn : jsonRank.getEffects()) {
            effectList.add(getTupleFromConfig(tupleIn));
        }

        for (SimpleObjProvider affixIn : jsonRank.getAffixes()) {
            Optional<IAffix> affixOptional = getAffixFromProvider(affixIn);
            affixOptional.ifPresent(affixList::add);
        }

        return new Rank(jsonRank.getTier(), jsonRank.getNumAffixes(), jsonRank.getGrowthFactor(), jsonRank.getChance(), jsonRank.getDefaultColor(), effectList, affixList);
    }

    public Rank getRank() {
        return getRankFromProvider(this);
    }
}

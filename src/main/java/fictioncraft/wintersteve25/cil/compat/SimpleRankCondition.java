package fictioncraft.wintersteve25.cil.compat;

import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.template.effects.SimpleEffectArg;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.SimpleObjProvider;

import java.util.List;

public class SimpleRankCondition extends SimpleRankArg{
    public SimpleRankCondition(int tier, int defaultColor, int numAffixes, int growthFactor, float chance, List<SimpleEffectArg> effects, List<SimpleObjProvider> affixes) {
        super(tier, defaultColor, numAffixes, growthFactor, chance, effects, affixes);
    }
}

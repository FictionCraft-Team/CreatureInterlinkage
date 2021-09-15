package fictioncraft.wintersteve25.cil.events;

import fictioncraft.wintersteve25.cil.compat.SimpleRankArg;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.ArgProviderType;
import fictioncraft.wintersteve25.fclib.common.helper.ModListHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.util.ChampionBuilder;

public class ArgRegistration {
    public static void commonSetUp(FMLCommonSetupEvent event) {
        if (ModListHelper.isModLoaded("champions")) {
            ArgProviderType.registerProvider("E_Rank", "champions", SimpleRankArg.class, (player, entity, arg) -> {
                if (!entity.getEntityWorld().isRemote()) {
                    if (entity instanceof LivingEntity && arg instanceof SimpleRankArg) {
                        LivingEntity livingEntity = (LivingEntity) entity;
                        SimpleRankArg rankArg = (SimpleRankArg) arg;

                        ChampionCapability.getCapability(livingEntity).ifPresent((champion) -> {
                            IChampion.Server serverChampion = champion.getServer();
                            if (serverChampion.getRank().isPresent()) {
                                if (serverChampion.getRank().get().getTier() == 0) {
                                    if (!ChampionsConfig.championSpawners) {
                                        serverChampion.setRank(rankArg.getRank());
                                    } else {
                                        ChampionBuilder.spawn(champion);
                                    }
                                }
                            }
                        });
                    }
                }
                return true;
            });
//            ArgProviderType.registerProvider("C_Rank", "champions", SimpleRankCondition.class, (player, entity, arg) -> {
//                AtomicBoolean returnType = new AtomicBoolean(false);
//
//                if (!entity.getEntityWorld().isRemote()) {
//                    if (entity instanceof LivingEntity && arg instanceof SimpleRankArg) {
//                        LivingEntity livingEntity = (LivingEntity) entity;
//                        SimpleRankArg rankArg = (SimpleRankArg) arg;
//
//                        ChampionCapability.getCapability(livingEntity).ifPresent((champion) -> {
//                            IChampion.Server serverChampion = champion.getServer();
//                            if (serverChampion.getRank().isPresent()) {
//                                Rank currentRank = serverChampion.getRank().get();
//                                Rank conditionalRank = rankArg.getRank();
//                                returnType.set(currentRank == rankArg.getRank());
//                            }
//                        });
//                    }
//                }
//
//                return returnType.get();
//            });
        }
    }
}

package fictioncraft.wintersteve25.cil.events;

import fictioncraft.wintersteve25.cil.CILMod;
import fictioncraft.wintersteve25.cil.configs.MainConfig;
import fictioncraft.wintersteve25.cil.configs.objects.SimpleCILConfigObject;
import fictioncraft.wintersteve25.cil.configs.objects.SimpleCILMap;
import fictioncraft.wintersteve25.fclib.api.events.JsonConfigEvent;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.arg.SimpleArgProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.SimpleObjProvider;
import fictioncraft.wintersteve25.fclib.api.json.objects.providers.obj.templates.SimpleEntityProvider;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer.Args;
import fictioncraft.wintersteve25.fclib.api.json.utils.JsonSerializer.EntitySerialization;
import fictioncraft.wintersteve25.fclib.common.helper.MiscHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

public class ServerEventsHandler {
    public static MainConfig CONFIG;
    public static SimpleCILMap configMap;
    public static List<SimpleCILConfigObject> leftClick;
    public static List<SimpleCILConfigObject> rightClick;

    public static void onJsonReload(JsonConfigEvent.Post event) {
        if (event.getStage() == JsonConfigEvent.JsonConfigLoadStages.READ) {
            configMap = CONFIG.configData;
            if (configMap == null || !MiscHelper.isMapValid(configMap.getConfigurations())) return;
            leftClick = configMap.getConfigurations().get(CILMod.LEFT_CLICK);
            rightClick = configMap.getConfigurations().get(CILMod.RIGHT_CLICK);
        }
    }

    public static void registerConfig(JsonConfigEvent.Registration event) {
        CONFIG = new MainConfig();
        event.getManager().registerConfig(CONFIG);
    }

    public static void entityLeftClick(AttackEntityEvent event) {
        if (configMap == null || !MiscHelper.isMapValid(configMap.getConfigurations())) return;
        leftClick = configMap.getConfigurations().get(CILMod.LEFT_CLICK);

        PlayerEntity player = event.getPlayer();
        if (basicChecks(leftClick, player)) {
            Entity target = event.getTarget();
            for (SimpleCILConfigObject configObject : leftClick) {
                SimpleObjProvider objProvider = configObject.getTarget();
                if (objProvider instanceof SimpleEntityProvider) {
                    SimpleEntityProvider entityProvider = (SimpleEntityProvider) objProvider;
                    if (EntitySerialization.doesEntitiesMatch(target, entityProvider)) {
                        if (!MiscHelper.isListValid(configObject.getArgs())) return;
                        if (Args.executeConditions(player, target, configObject.getConditions(), CONFIG)) {
                            for (SimpleArgProvider args : configObject.getArgs()) {
                                Args.executeFromEntity(player, target, args, CONFIG);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void entityRightClickSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        if (configMap == null || !MiscHelper.isMapValid(configMap.getConfigurations())) return;
        rightClick = configMap.getConfigurations().get(CILMod.RIGHT_CLICK);

        PlayerEntity player = event.getPlayer();
        if (basicChecks(rightClick, player)) {
            if (event.getHand() == Hand.MAIN_HAND) {
                Entity target = event.getTarget();
                entityRightClickResult(player, target);
            }
        }
    }

    private static void entityRightClickResult(PlayerEntity player, Entity target) {
        for (SimpleCILConfigObject configObject : rightClick) {
            SimpleObjProvider objProvider = configObject.getTarget();
            if (objProvider instanceof SimpleEntityProvider) {
                SimpleEntityProvider entityProvider = (SimpleEntityProvider) objProvider;
                if (EntitySerialization.doesEntitiesMatch(target, entityProvider)) {
                    if (!MiscHelper.isListValid(configObject.getArgs())) return;
                    if (Args.executeConditions(player, target, configObject.getConditions(), CONFIG)) {
                        for (SimpleArgProvider args : configObject.getArgs()) {
                            Args.executeFromEntity(player, target, args, CONFIG);
                        }
                    }
                }
            }
        }
    }

    private static boolean basicChecks(List<?> configList, Entity entity) {
        if (!MiscHelper.isListValid(configList)) return false;
        if (entity == null) return false;
        World world = entity.getEntityWorld();
        return !world.isRemote();
    }
}

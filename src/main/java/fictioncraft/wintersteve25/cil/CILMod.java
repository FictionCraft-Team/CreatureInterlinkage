package fictioncraft.wintersteve25.cil;

import fictioncraft.wintersteve25.cil.events.ArgRegistration;
import fictioncraft.wintersteve25.cil.events.ServerEventsHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CILMod.MODID)
public class CILMod {
    public static final String MODID = "cil";
    public static final Logger LOGGER = LogManager.getLogger("CIL");

    public static final String LEFT_CLICK = "left_click";
    public static final String RIGHT_CLICK = "right_click";

    public CILMod() {
        MinecraftForge.EVENT_BUS.addListener(ServerEventsHandler::registerConfig);
        MinecraftForge.EVENT_BUS.addListener(ServerEventsHandler::onJsonReload);
        MinecraftForge.EVENT_BUS.addListener(ServerEventsHandler::entityLeftClick);
//        MinecraftForge.EVENT_BUS.addListener(ServerEventsHandler::entityRightClick);
        MinecraftForge.EVENT_BUS.addListener(ServerEventsHandler::entityRightClickSpecific);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ArgRegistration::commonSetUp);
    }
}

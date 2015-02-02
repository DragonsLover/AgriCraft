package com.InfinityRaider.AgriCraft.utility;


import com.InfinityRaider.AgriCraft.reference.Constants;
import com.InfinityRaider.AgriCraft.renderers.RenderChannel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RenderLogger {

    private long timer = 0;

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if (!Constants.LOG_RENDER_CALLS)
            return;

        timer++;
        if (timer % 40 == 0) {
            timer = 0;

            int callsPerSecond = RenderChannel.renderCallCounter.getAndSet(0);
            LogHelper.info("Calls since previous output: " + callsPerSecond);
        }
    }
}

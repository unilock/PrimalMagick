package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.AttunementIndexPage;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI button to view the grimoire attunement index page.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class AttunementIndexButton extends AbstractTopicButton {
    public AttunementIndexButton(int widthIn, int heightIn, Component text, GrimoireScreen screen) {
        super(widthIn, heightIn, 123, 12, text, screen, new Handler());
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof AttunementIndexButton) {
                AttunementIndexButton gab = (AttunementIndexButton)button;
                
                // Push the current grimoire topic onto the history stack
                GrimoireScreen.HISTORY.add(gab.getScreen().getMenu().getTopic());
                
                // Set the new grimoire topic and open a new screen for it
                gab.getScreen().getMenu().setTopic(AttunementIndexPage.TOPIC);
                gab.getScreen().getMinecraft().setScreen(new GrimoireScreen(
                    gab.getScreen().getMenu(),
                    gab.getScreen().getPlayerInventory(),
                    gab.getScreen().getTitle()
                ));
            }
        }
    }
}

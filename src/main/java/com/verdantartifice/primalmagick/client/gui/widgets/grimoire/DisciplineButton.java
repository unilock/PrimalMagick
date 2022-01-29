package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.topics.DisciplineResearchTopic;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire page for a given research discipline.
 * 
 * @author Daedalus4096
 */
public class DisciplineButton extends AbstractTopicButton {
    protected final ResearchDiscipline discipline;
    protected final boolean showIcon;
    protected final boolean enlarge;

    public DisciplineButton(int widthIn, int heightIn, Component text, GrimoireScreen screen, ResearchDiscipline discipline, boolean showIcon, boolean enlarge) {
        super(widthIn, heightIn, 123, enlarge ? 18 : 12, text, screen, GenericIndexIcon.of(discipline.getIconLocation(), true), new Handler());
        this.discipline = discipline;
        this.showIcon = showIcon;
        this.enlarge = enlarge;
    }
    
    public ResearchDiscipline getDiscipline() {
        return this.discipline;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof DisciplineButton gdb) {
                // Push the current grimoire topic onto the history stack
                gdb.getScreen().pushCurrentHistoryTopic();
                
                // Set the new grimoire topic and open a new screen for it
                gdb.getScreen().getMenu().setTopic(new DisciplineResearchTopic(gdb.getDiscipline(), 0));
                gdb.getScreen().getMinecraft().setScreen(new GrimoireScreen(
                    gdb.getScreen().getMenu(),
                    gdb.getScreen().getPlayerInventory(),
                    gdb.getScreen().getTitle()
                ));
            }
        }
    }
}

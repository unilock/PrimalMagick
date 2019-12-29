package com.verdantartifice.primalmagic.common.spells.vehicles;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellImpactPacket;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.mods.BlastSpellMod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SelfSpellVehicle extends AbstractSpellVehicle {
    public static final String TYPE = "self";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_SORCERY"));
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }

    @Override
    public void execute(SpellPackage spell, World world, PlayerEntity caster) {
        if (spell.getPayload() != null) {
            RayTraceResult result = new EntityRayTraceResult(caster);
            BlastSpellMod blastMod = spell.getMod(BlastSpellMod.class, "power");
            if (!world.isRemote) {
                Vec3d hitVec = caster.getEyePosition(1.0F);
                int radius = blastMod == null ? 1 : blastMod.getModdedPropertyValue("power", spell);
                PacketHandler.sendToAllAround(
                        new SpellImpactPacket(hitVec.x, hitVec.y, hitVec.z, radius, spell.getPayload().getSource().getColor()), 
                        world.getDimension().getType(), 
                        caster.getPosition(), 
                        64.0D);
            }
            
            if (blastMod != null) {
                for (RayTraceResult target : blastMod.getBlastTargets(result, spell, world)) {
                    spell.getPayload().execute(target, result.getHitVec(), spell, world, caster);
                }
            } else {
                spell.getPayload().execute(result, null, spell, world, caster);
            }
        }
    }
}

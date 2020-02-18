package com.verdantartifice.primalmagic.common.attunements;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

/**
 * Definition of an attunement-linked attribute modifier.  Used to modify entity attributes when
 * crossing certain attunement thresholds.
 * 
 * @author Daedalus4096
 */
public class AttunementAttributeModifier {
    protected final Source source;
    protected final int threshold;
    protected final IAttribute attribute;
    protected final AttributeModifier modifier;
    
    public AttunementAttributeModifier(@Nonnull Source source, int threshold, @Nonnull IAttribute attribute, @Nonnull String uuidStr, double modValue, @Nonnull AttributeModifier.Operation modOperation) {
        this.source = source;
        this.threshold = threshold;
        this.attribute = attribute;
        this.modifier = new AttributeModifier(UUID.fromString(uuidStr), this::getModifierName, modValue, modOperation);
    }
    
    @Nonnull
    public String getModifierName() {
        return this.source.getTag() + Integer.toString(this.threshold);
    }
    
    @Nonnull
    public Source getSource() {
        return this.source;
    }
    
    public int getThreshold() {
        return this.threshold;
    }
    
    @Nonnull
    public IAttribute getAttribute() {
        return this.attribute;
    }
    
    @Nonnull
    public AttributeModifier getModifier() {
        return this.modifier;
    }
    
    public void applyToEntity(@Nullable LivingEntity entity) {
        if (entity != null && !entity.world.isRemote) {
            IAttributeInstance instance = entity.getAttributes().getAttributeInstance(this.getAttribute());
            if (instance != null) {
                instance.removeModifier(this.getModifier());
                instance.applyModifier(this.getModifier());
            }
        }
    }
    
    public void removeFromEntity(@Nullable LivingEntity entity) {
        if (entity != null && !entity.world.isRemote) {
            IAttributeInstance instance = entity.getAttributes().getAttributeInstance(this.getAttribute());
            if (instance != null) {
                instance.removeModifier(this.getModifier());
            }
        }
    }
}
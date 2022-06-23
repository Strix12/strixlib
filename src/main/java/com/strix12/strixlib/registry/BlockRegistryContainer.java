package com.strix12.strixlib.registry;

import com.strix12.strixlib.registry.annotations.NoBlockItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;

public interface BlockRegistryContainer extends RegistryContainer<Block> {
    default Registry<Block> getRegistry() {
        return Registry.BLOCK;
    }
    default Class<Block> getTargetFieldType() {
        return Block.class;
    }

    @Override
    default void postFieldProcess(Block value, String namespace, String id, Field field) {
        if (field.isAnnotationPresent(NoBlockItem.class)) return;
        Registry.register(Registry.ITEM, new Identifier(namespace, id), createBlockItem(value));
    }

    default BlockItem createBlockItem(Block block) {
        return new BlockItem(block, new FabricItemSettings());
    }
}

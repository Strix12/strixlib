package com.strix12.strixlib.registry;

import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public interface ItemRegistryContainer extends RegistryContainer<Item> {
    default Registry<Item> getRegistry() {
        return Registry.ITEM;
    }

    default Class<Item> getTargetFieldType() {
        return Item.class;
    }
}

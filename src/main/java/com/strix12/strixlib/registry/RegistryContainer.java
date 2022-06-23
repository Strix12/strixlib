package com.strix12.strixlib.registry;

import com.strix12.strixlib.registry.annotations.SkipIteration;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;

public interface RegistryContainer<T> {
    Registry<T> getRegistry();
    Class<T> getTargetFieldType();

    default void postProcess() {}
    default void postFieldProcess(T value, String namespace, String id, Field field) {}
    default boolean notShouldProcess(T value, String id, Field field) {
        return field.isAnnotationPresent(SkipIteration.class) && !getTargetFieldType().isAssignableFrom(field.getClass());
    }
    default void process(T value, String namespace, String id, Field field) {
        if (notShouldProcess(value, id, field)) return;
        Registry.register(getRegistry(), new Identifier(namespace, id), value);
        postFieldProcess(value, namespace, id, field);
    }
}

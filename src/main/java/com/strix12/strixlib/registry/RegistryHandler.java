package com.strix12.strixlib.registry;

import com.strix12.strixlib.registry.annotations.AssignedName;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Locale;

public class RegistryHandler {
    @SuppressWarnings("unchecked")
    public static <T> void register(Class<? extends RegistryContainer<T>> clazz, String namespace, boolean recurse) {
        RegistryContainer<T> container = instantiateContainer(clazz);

        iterateFields(clazz, (fieldValue, id, field) -> {
            container.process(fieldValue, namespace, id, field);
            container.postFieldProcess(fieldValue, namespace, id, field);
        }, container);

        if (recurse) {
            for (Class<?> subclass : clazz.getDeclaredClasses()) {
                if (!RegistryContainer.class.isAssignableFrom(subclass)) continue;
                register((Class<? extends RegistryContainer<T>>) subclass, namespace, true);
            }
        }

        container.postProcess();
    }

    private static <T> RegistryContainer<T> instantiateContainer(Class<? extends RegistryContainer<T>> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format(
                    e instanceof NoSuchMethodException ? "No zero-args constructor found on class %s" : "Could not instantiate class %s",
                    clazz.getName()));
        }
    }
    @SuppressWarnings("unchecked")
    private static <C, F> void iterateFields(Class<C> clazz, FieldProcessor<F> fieldProcessor, RegistryContainer<F> container) {
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) continue;

            F value;
            try {
                value = (F) field.get(null);
            } catch (IllegalAccessException e) {
                continue;
            }

            if (container.notShouldProcess(value, getFieldName(field), field)) continue;
            fieldProcessor.process(value, getFieldName(field), field);
        }
    }
    private static String getFieldName(Field field) {
        return (field.isAnnotationPresent(AssignedName.class) ? field.getAnnotation(AssignedName.class).name() : field.getName().toLowerCase(Locale.ROOT));
    }

    @FunctionalInterface
    private interface FieldProcessor<F> {
        void process(F value, String name, Field field);
    }
}

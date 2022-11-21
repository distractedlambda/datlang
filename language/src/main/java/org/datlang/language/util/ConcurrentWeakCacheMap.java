package org.datlang.language.util;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class ConcurrentWeakCacheMap<K, V> {
    private final ConcurrentHashMap<K, ValueReference<K, V>> storage = new ConcurrentHashMap<>();
    private final ReferenceQueue<V> queue = new ReferenceQueue<>();

    @SuppressWarnings("unchecked")
    private void prune() {
        ValueReference<K, V> ref;
        while ((ref = (ValueReference<K, V>)queue.poll()) != null) {
            storage.remove(ref.key, ref);
        }
    }

    public @NotNull V getOrCompute(@NotNull K key, @NotNull Function<@NotNull K, @NotNull V> computeValue) {
        prune();

        var existingRef = storage.get(key);

        if (existingRef != null) {
            var existing = existingRef.get();
            if (existing != null) {
                return existing;
            }
        }

        var computedValue = computeValue.apply(key);
        var newRef = new ValueReference<>(key, computedValue, queue);

        for (;;) {
            if (existingRef == null) {
                if ((existingRef = storage.putIfAbsent(key, newRef)) == null) {
                    return computedValue;
                }

                var existing = existingRef.get();

                if (existing != null) {
                    return existing;
                }
            }

            if (storage.replace(key, existingRef, newRef)) {
                return computedValue;
            }

            existingRef = storage.get(key);
        }
    }

    private static final class ValueReference<K, V> extends WeakReference<V> {
        private final @NotNull K key;

        private ValueReference(@NotNull K key, @NotNull V value, @NotNull ReferenceQueue<V> queue) {
            super(value, queue);
            this.key = key;
        }
    }
}

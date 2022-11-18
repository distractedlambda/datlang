package org.datlang.language.util;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentWeakCacheSet<E> {
    private final WeakHashMap<E, WeakReference<E>> storage = new WeakHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public @NotNull E intern(@NotNull E element) {
        lock.readLock().lock();
        try {
            var existingRef = storage.get(element);
            if (existingRef != null) {
                var existing = existingRef.get();
                if (existing != null) {
                    return existing;
                }
            }
        }
        finally {
            lock.readLock().unlock();
        }

        lock.writeLock().lock();
        try {
            var newRef = new WeakReference<>(element);
            var existingRef = storage.putIfAbsent(element, newRef);
            if (existingRef != null) {
                var existing = existingRef.get();
                if (existing != null) {
                    return existing;
                }
                else {
                    storage.put(element, newRef);
                }
            }

            return element;
        }
        finally {
            lock.writeLock().unlock();
        }
    }
}

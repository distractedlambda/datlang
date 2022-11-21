package org.datlang.language.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("unchecked")
public final class ConcurrentWeakCacheSet<E> {
    private final ReferenceQueue<E> queue = new ReferenceQueue<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private @Nullable Entry<E> @NotNull[] table = (Entry<E>[]) new Entry[16];
    private int overestimatedLoad;

    private void pruneEntry(@NotNull Entry<E> entry) {
        var table = this.table;
        var tableIndex = entry.elementHashCode % table.length;
        var tableEntry = table[tableIndex];

        assert tableEntry != null;

        if (tableEntry == entry) {
            table[tableIndex] = tableEntry.next;
            return;
        }

        while (tableEntry.next != entry) {
            tableEntry = tableEntry.next;
            assert tableEntry != null;
        }

        tableEntry.next = tableEntry.next.next;
    }

    private void prune() {
        Entry<E> entry;
        while ((entry = (Entry<E>) queue.poll()) != null) {
            lock.writeLock().lock();
            try {
                pruneEntry(entry);
                overestimatedLoad--;
            }
            finally {
                lock.writeLock().unlock();
            }
        }
    }

    private @Nullable E find(@NotNull E element, int elementHashCode) {
        lock.readLock().lock();
        try {
            var table = this.table;
            var tableIndex = elementHashCode % table.length;

            for (var entry = table[tableIndex]; entry != null; entry = entry.next) {
                if (entry.elementHashCode == elementHashCode) {
                    var entryElement = entry.get();
                    if (element.equals(entryElement)) {
                        return entryElement;
                    }
                }
            }

            return null;
        }
        finally {
            lock.readLock().unlock();
        }
    }

    private void growTable(@Nullable Entry<E> @NotNull[] oldTable) {
        var newTable = (Entry<E>[]) new Entry[oldTable.length * 2];

        for (var entry : oldTable) {
            while (entry != null) {
                var nextEntry = entry.next;

                var newTableIndex = entry.elementHashCode % newTable.length;
                entry.next = newTable[newTableIndex];
                newTable[newTableIndex] = entry;

                entry = nextEntry;
            }
        }

        this.table = newTable;
    }

    private @NotNull E findOrAdd(@NotNull E element, int elementHashCode) {
        lock.writeLock().lock();
        try {
            var table = this.table;

            if (overestimatedLoad * LOAD_FACTOR_DENOMINATOR >= table.length * LOAD_FACTOR_NUMERATOR) {
                growTable(table);
                table = this.table;
            }

            var tableIndex = elementHashCode % table.length;

            for (var entry = table[tableIndex]; entry != null; entry = entry.next) {
                if (entry.elementHashCode == elementHashCode) {
                    var entryElement = entry.get();
                    if (element.equals(entryElement)) {
                        return entryElement;
                    }
                }
            }

            var newEntry = new Entry<>(element, elementHashCode, queue);
            newEntry.next = table[tableIndex];
            table[tableIndex] = newEntry;
            overestimatedLoad++;

            return element;
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    public @NotNull E intern(@NotNull E element) {
        prune();

        var elementHashCode = redistributeHashCode(element.hashCode());

        var existing = find(element, elementHashCode);
        if (existing != null) {
            return existing;
        }

        return findOrAdd(element, elementHashCode);
    }

    private static final class Entry<E> extends WeakReference<E> {
        private final int elementHashCode;
        private @Nullable Entry<E> next;

        private Entry(@NotNull E element, int elementHashCode, @NotNull ReferenceQueue<E> queue) {
            super(element, queue);
            this.elementHashCode = elementHashCode;
        }
    }

    /**
     * <a href="https://github.com/skeeto/hash-prospector">Source</a>
     */
    private static int redistributeHashCode(int code) {
        code ^= code >>> 16;
        code *= 0x21f0aaad;
        code ^= code >>> 15;
        code *= 0xd35a2d97;
        code ^= code >>> 15;
        return code;
    }

    private static final long LOAD_FACTOR_NUMERATOR = 3;

    private static final long LOAD_FACTOR_DENOMINATOR = 4;
}

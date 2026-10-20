package buildcraft.lib.misc;

import java.util.function.Supplier;

/** Replacement for removed {@code net.minecraft.util.LazyLoadedValue}. */
public final class LazyLoadedValue<T> {
    private final Supplier<T> supplier;
    private T value;
    private boolean initialized;

    public LazyLoadedValue(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        if (!initialized) {
            value = supplier.get();
            initialized = true;
        }
        return value;
    }
}

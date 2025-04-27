package fr.univtln.m1im.png.generation;

import java.util.HashMap;
import java.util.function.Function;

/**
 * A utility class that caches the results of a function to avoid redundant
 * computations.
 * It uses a {@link HashMap} to store the mapping between input and output
 * values.
 *
 * @param <T> The type of the input to the function.
 * @param <R> The type of the result of the function.
 */
class HashCacheFn<T, R> implements Function<T, R> {
    private Function<T, R> f;
    private HashMap<T, R> h;

    private HashCacheFn(Function<T, R> f) {
        this.f = f;
        this.h = new HashMap<>();
    }

    public static <T, R> HashCacheFn<T, R> of(Function<T, R> f) {
        return new HashCacheFn<T, R>(f);
    }

    public R apply(T x) {
        if (h.containsKey(x)) {
            return h.get(x);
        } else {
            var y = f.apply(x);
            h.put(x, y);
            return y;
        }
    }

    public HashMap<T, R> getCache() {
        return h;
    }
}

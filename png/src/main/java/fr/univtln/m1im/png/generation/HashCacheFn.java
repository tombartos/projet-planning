package fr.univtln.m1im.png.generation;

import java.util.HashMap;
import java.util.function.Function;

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

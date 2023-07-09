/*
 * Copyright (c) 2014-2023 Chris Kroells
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.coobird.gui.simpleimageviewer4j.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class Cache<K, V> {
    private final Object lock = new Object();
    private final Map<K, SoftReference<V>> cache = new HashMap<K, SoftReference<V>>();

    private V computeAndSet(K key, Callable<V> computation) throws Exception {
        V result = computation.call();
        cache.put(key, new SoftReference<V>(result));
        return result;
    }

    public V computeIfAbsent(K key, Callable<V> computation) throws Exception {
        // While this is correct, we're effectively making processing single-threaded.
        synchronized (lock) {
            if (!cache.containsKey(key)) {
                return computeAndSet(key, computation);
            }

            V value = cache.get(key).get();
            if (value == null) {
                return computeAndSet(key, computation);
            }
            return value;
        }
    }
}

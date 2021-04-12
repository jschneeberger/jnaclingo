/*
 * Copyright 2017 Andrej Petras.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lorislab.clingo4j.util;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import org.bridj.NativeList;
import org.bridj.Pointer;

/**
 *
 * @author Andrej Petras
 * @param <T>
 * @param <K>
 */
public class PointerList<T, K> implements UnmodifiableList<T> {

    private  final NativeList<K> list;
    
    private Function<K, T> fn;
    
    public PointerList(Function<K, T> fn, Pointer<K> pointer, long size) {
        this(pointer, size);
        this.fn = fn;
    }
    
    public PointerList(Pointer<K> pointer, long size) {
        this(new UnmodifiableNativeList<>(pointer, size));
    }

    public PointerList(NativeList<K> list) {
        this.list = list;
    }
    
    public Pointer<K> getPointer() {
        return (Pointer<K>) list.getPointer();
    }
    
    protected T getItem(K p) {
        if (fn != null) {
            return fn.apply(p);
        }
        return (T) p;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        final Iterator<K> iter = list.iterator();
        return new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public T next() {
                return getItem(iter.next());
            }
        };
    }

    @Override
    public T get(int index) {
        K tmp = list.get(index);
        return getItem(tmp);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        final ListIterator<K> iter = list.listIterator();
        return new ListIterator<T>() {

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public T next() {
                return getItem(iter.next());
            }

            @Override
            public boolean hasPrevious() {
                return iter.hasPrevious();
            }

            @Override
            public T previous() {
                return getItem(iter.previous());
            }

            @Override
            public int nextIndex() {
                return iter.nextIndex();
            }

            @Override
            public int previousIndex() {
                return iter.previousIndex();
            }

            @Override
            public void remove() {
                iter.remove();
            }

            @Override
            public void set(T e) {
                throw new UnsupportedOperationException("This list is unmodifiable");
            }

            @Override
            public void add(T e) {
                throw new UnsupportedOperationException("This list is unmodifiable");
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean comma = false;
        for (int i=0; i<size(); i++) {
            if (comma) {
                sb.append(", ");
            } else {
                sb.append(" ");
            }
            sb.append(get(i));
            comma = true;
        }
        sb.append(" }");
        return sb.toString();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        final ListIterator<K> iter = list.listIterator(index);
        return new ListIterator<T>() {

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public T next() {
                return getItem(iter.next());
            }

            @Override
            public boolean hasPrevious() {
                return iter.hasPrevious();
            }

            @Override
            public T previous() {
                return getItem(iter.previous());
            }

            @Override
            public int nextIndex() {
                return iter.nextIndex();
            }

            @Override
            public int previousIndex() {
                return iter.previousIndex();
            }

            @Override
            public void remove() {
                iter.remove();
            }

            @Override
            public void set(T e) {
                throw new UnsupportedOperationException("This list is unmodifiable");
            }

            @Override
            public void add(T e) {
                throw new UnsupportedOperationException("This list is unmodifiable");
            }
        };
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new PointerList<>((NativeList<K>)list.subList(fromIndex, toIndex));
    }

}

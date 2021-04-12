/*
 * Copyright 2018 Andrej Petras.
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

import java.util.AbstractList;
import java.util.Collection;
import java.util.RandomAccess;
import org.bridj.NativeList;
import org.bridj.Pointer;
import static org.bridj.Pointer.allocate;

/**
 *
 * @author Andrej Petras
 */
public class UnmodifiableNativeList<T> extends AbstractList<T> implements NativeList<T>, RandomAccess {

    private volatile Pointer<T> pointer;
    
    private volatile long size;

    /**
     * Create a native list that uses the provided storage and implementation
     * strategy
     *
     * @param pointer
     * @param size
     */
    public UnmodifiableNativeList(Pointer<T> pointer, long size) {
        if (pointer == null ) {
            throw new IllegalArgumentException("Cannot build a FixedNativeList for pointer " + pointer + ".");
        }
        this.size = size;
        this.pointer = pointer;
    }

    @Override
    public Pointer<T> getPointer() {
        return pointer;
    }
    
    @Override
    public int size() {
        return (int) size;
    }

    @Override
    public T get(int i) {
        if (i >= size || i < 0) {
            throw new IndexOutOfBoundsException("Invalid index : " + i + " (list has size " + size + ")");
        }
        return pointer.get(i);
    }

    @Override
    public int indexOf(Object o) {
        Pointer<T> tmp = allocate(pointer.getIO());
        tmp.set((T) o);
        Pointer<T> occurrence = pointer.find(tmp);
        if (occurrence == null) {
            return -1;
        }
        return (int) (occurrence.getPeer() - pointer.getPeer());        
    }

    @Override
    public int lastIndexOf(Object o) {
        Pointer<T> tmp = allocate(pointer.getIO());
        tmp.set((T) o);
        Pointer<T> occurrence = pointer.findLast(tmp);
        if (occurrence == null) {
            return -1;
        }
        return (int) (occurrence.getPeer() - pointer.getPeer());        
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Object[] toArray() {
        return pointer.validElements(size).toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return pointer.validElements(size).toArray(ts);
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }
    
    @Override
    public T set(int i, T e) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    @Override
    public void add(int i, T e) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    @Override
    public T remove(int i) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }
    
    @Override
    public boolean addAll(int i, Collection<? extends T> clctn) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }
    
}

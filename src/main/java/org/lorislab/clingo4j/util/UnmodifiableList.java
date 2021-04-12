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

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * The unmodifiable list interface.
 *
 * @param <T> the type of elements in this list
 * 
 * @author Andrej Petras
 */
public interface UnmodifiableList<T> extends List<T> {

    /**
     * {@inheritDoc }
     */
    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default boolean contains(Object o) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default Object[] toArray() {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default boolean add(T e) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default boolean remove(Object o) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default void clear() {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default T set(int index, T element) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default void add(int index, T element) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default T remove(int index) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default int indexOf(Object o) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    default List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("This list is unmodifiable");
    }

}

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

import java.util.List;
import org.bridj.NativeList;
import org.bridj.Pointer;

/**
 *
 * @author Andrej Petras
 * @param <T>
 */
public class Struct<T> {

    protected final T structObject;

    public Struct(T structObject) {
        this.structObject = structObject;
    }

    public T getStructObject() {
        return structObject;
    }

    public static <T, E extends Struct<T>> Pointer<T> array(List<E> data, Class<T> clazz) {
        if (data != null && !data.isEmpty()) {
            NativeList<T> tmp = Pointer.allocateList(clazz, data.size());
            int size = data.size();
            for (int i = 0; i < size; i++) {
                tmp.add(data.get(i).structObject);
            }
            return (Pointer<T>) tmp.getPointer();
        }
        return null;
    }
}

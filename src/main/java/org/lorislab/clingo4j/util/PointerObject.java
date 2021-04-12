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

import java.util.List;
import org.bridj.NativeList;
import org.bridj.Pointer;

/**
 *
 * @author Andrej Petras
 * @param <T>
 */
public abstract class PointerObject<T> {
    
    protected final Pointer<T> pointer;

    public PointerObject(Pointer<T> pointer) {
        this.pointer = pointer;
    }

    public Pointer<T> getPointer() {
        return pointer;
    }
    
    public T getPointerValue() {
        return pointer.get();
    }
    
    public boolean isNull() {
        return pointer == null || pointer.get() == null;
    }

    public static NativeList<Integer> toNativeList(List<Integer> list) {
        if (list == null) {
            return null;
        }
        if (list instanceof NativeList) {
            return (NativeList<Integer>) list;
        }
        if (list.isEmpty()) {
            return null;
        }
        int size = size(list);
        Pointer<Integer> tmp = array(list, Integer.class);
        return Pointer.allocateList(tmp.getIO(), size);
    }    
    
    public static <T> Pointer<T> array(List<T> data, Class<T> clazz) {
        if (data != null && !data.isEmpty()) {
            NativeList<T> tmp = Pointer.allocateList(clazz, data.size());
            int size = data.size();
            for (int i=0; i<size; i++) {
                tmp.add(data.get(i));
            }
            return (Pointer<T>) tmp.getPointer();
        }
        return null;
    }    
    
    protected static int size(List data) {
        if (data != null) {
            return data.size();
        }
        return 0;
    }
    
}

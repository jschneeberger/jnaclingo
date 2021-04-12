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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.bridj.NativeList;
import org.bridj.NativeObject;
import org.bridj.Pointer;

/**
 *
 * @author Andrej Petras
 * @param <T>
 */
public interface ASTObject<T extends NativeObject> {

    public abstract T create();

    default Pointer<T> createPointer() {
        return Pointer.getPointer(create());
    }

    default Class<T> getNativeClass() {
        Type[] genericInterfaces = getClass().getGenericInterfaces();
        for (int i=0; i<genericInterfaces.length; i++) {
            Type t = genericInterfaces[i];
            if (t instanceof ParameterizedType) {
                Type rt = ((ParameterizedType) t).getRawType();
                if (rt.equals(ASTObject.class)) {
                    return (Class<T>) ((ParameterizedType) t).getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }

    public static <T, R> Optional<R> optional(Function<T, R> fn, Pointer<T> val) {
        return Optional.ofNullable(create(fn, val));
    }
    
    public static <T, R> R create(Function<T, R> fn, Pointer<T> val) {
        if (val != null && val.get() != null) {
            return fn.apply(val.get());
        }
        return null;
    }
    
    public static <K extends NativeObject> K optional(Optional<? extends ASTObject<K>> item) {
        if (item.isPresent()) {
            return item.get().create();
        }
        return null;
    }

    public static <K extends NativeObject> Pointer<K> optionalPointer(Optional<? extends ASTObject<K>> item) {
        if (item.isPresent()) {
            return item.get().createPointer();
        }
        return null;
    }

    public static <T, K> List<T> pointerList(Function<K, T> fn, Pointer<K> pointer, long size) {
        if (pointer == null) {
            return null;
        }
        return new PointerList<>(fn, pointer, size);
    }
    
    public static List<String> listString(Pointer<Pointer<Byte>> data, long size) {
        if (data == null) {
            return null;
        }
        return new PointerList<String, Pointer<Byte>>(data, size) {
            @Override
            protected String getItem(Pointer<Byte> p) {
                return p.getCString();
            }
        };
    }
    
    public static <T extends NativeObject, E extends ASTObject<T>> Pointer<T> array(List<E> data) {
        if (data != null && !data.isEmpty()) {
            E tmp = data.get(0);
            return ASTObject.array(data, tmp.getNativeClass());
        }
        return null;
    }

    public static <T extends NativeObject, E extends ASTObject<T>> Pointer<T> array(List<E> data, Class<T> clazz) {
        if (data != null && !data.isEmpty()) {
            NativeList<T> tmp = Pointer.allocateList(clazz, data.size());
            for (int i=0; i<data.size(); i++) {
                tmp.add(data.get(i).create());
            }
            return (Pointer<T>) tmp.getPointer();
        }
        return null;
    }

    public static int size(List data) {
        if (data != null) {
            return data.size();
        }
        return 0;
    }
    
    public static String print(List list, String prefix, String separator, String suffix, boolean empty) {
        StringBuilder sb = new StringBuilder();
        if (list != null && !list.isEmpty()) {
            sb.append(prefix);
            sb.append(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                sb.append(separator);
                sb.append(list.get(i));
            }
            sb.append(suffix);
        } else if (empty) {
            sb.append(prefix).append(suffix);
        }
        return sb.toString();
    }  
    
    public static String printBody(List list) {
        return printBody(list, " : ");
    }

    public static String printBody(List list, String pre) {
        String tmp = "";
        if (list != null && !list.isEmpty()) {
            tmp = pre;
        }
        return print(list, tmp, "; ", ".", true);
    }    

    public static Pointer<Pointer<Byte>> stringArray(List<String> data) {
        Pointer<Pointer<Byte>> result = null;
        if (data != null && !data.isEmpty()) {
            result = Pointer.pointerToCStrings(data.toArray(new String[data.size()]));
        }
        return result;
    }    
}

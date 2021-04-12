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

import org.bridj.ValuedEnum;

/**
 *
 * @author Andrej Petras
 * @param <T>
 */
public interface EnumValue<T extends ValuedEnum> {

    public T getValue();

    default int getInt() {
        return (int) getValue().value();
    }

    public static <T extends Enum & EnumValue> T valueOfInt(Class<T> clazz, int value) {
        T[] items = clazz.getEnumConstants();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getInt() == value) {
                return items[i];
            }
        }
        return null;
    }
 
}

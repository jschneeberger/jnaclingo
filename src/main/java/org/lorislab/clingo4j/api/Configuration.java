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
package org.lorislab.clingo4j.api;

import org.lorislab.clingo4j.api.enums.ConfigurationType;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_configuration;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.util.PointerObject;

/**
 *
 * @author Andrej Petras
 */
public class Configuration extends PointerObject<clingo_configuration> {

    protected final int key;

    public Configuration(Pointer<clingo_configuration> pointer, int key) {
        super(pointer);
        this.key = key;
    }

    public ConfigurationMap toMap() {
        return new ConfigurationMap(pointer, key);
    }

    public ConfigurationList toList() {
        return new ConfigurationList(pointer, key);
    }

    public int getType() throws ClingoException {
        Pointer<Integer> tmp = Pointer.allocateInt();
        handleError(LIB.clingo_configuration_type(pointer, key, tmp), "Error reading the configuration type!");
        return tmp.getInt();
    }

    public boolean isValue() throws ClingoException {
        return (getType() & ConfigurationType.VALUE.getInt()) != 0;
    }

    public boolean isMap() throws ClingoException {
        return (getType() & ConfigurationType.MAP.getInt()) != 0;
    }

    public boolean isList() throws ClingoException {
        return (getType() & ConfigurationType.ARRAY.getInt()) != 0;
    }

    public boolean isAssigned() throws ClingoException {
        Pointer<Boolean> result = Pointer.allocateBoolean();
        handleError(LIB.clingo_configuration_value_is_assigned(pointer, key, result), "Error reading the configureation assigned.");
        return result.getBoolean();
    }

    public String getValue() throws ClingoException {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleError(LIB.clingo_configuration_value_get_size(pointer, key, size), "Error reading the configuration value size!");
        Pointer<Byte> value = Pointer.allocateBytes(size.getInt());
        handleError(LIB.clingo_configuration_value_get(pointer, key, value, size.getInt()), "Error reading the configuration value!");
        return value.getCString();
    }

    public void setValue(String value) throws ClingoException {
        handleError(LIB.clingo_configuration_value_set(pointer, key, Pointer.pointerToCString(value)), "Error settings the configuration value " + value + "!");
    }

    public String getDescription() throws ClingoException {
        Pointer<Pointer<Byte>> description = Pointer.allocatePointer(Byte.class);
        handleError(LIB.clingo_configuration_description(pointer, key, description), "Error reading the configuration description!");
        return description.get().getCString();
    }

    public String toDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(description(this));
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString(this));
        return sb.toString();
    }

    protected static String description(Configuration conf) {
        StringBuilder sb = new StringBuilder();
        try {
            if (conf.isValue() && conf.isAssigned()) {
                sb.append(conf.getDescription());
            } else if (conf.isMap()) {
                sb.append(conf.toMap().toDescription());
            } else if (conf.isList()) {
                sb.append(conf.toList().toDescription());
            }
        } catch (ClingoException ex) {
            Clingo.handleRuntimeError(ex);
        }
        return sb.toString();
    }

    protected static String toString(Configuration conf) {
        StringBuilder sb = new StringBuilder();
        try {
            if (conf.isValue() && conf.isAssigned()) {
                sb.append('"').append(conf.getValue()).append('"');
            } else if (conf.isMap()) {
                sb.append(conf.toMap());
            } else if (conf.isList()) {
                sb.append(conf.toList());
            }
        } catch (ClingoException ex) {
            Clingo.handleRuntimeError(ex);
        }
        return sb.toString();
    }
}

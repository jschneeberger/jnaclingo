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

import org.lorislab.clingo4j.api.enums.StatisticsType;
import org.bridj.Pointer;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_statistic;
import org.lorislab.clingo4j.util.PointerObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author Andrej Petras
 */
public class Statistics extends PointerObject<clingo_statistic> {

    protected final long key;

    public Statistics(Pointer<clingo_statistic> pointer, long key) {
        super(pointer);
        this.key = key;
    }

    public StatisticsList toList() {
        return new StatisticsList(pointer, key);
    }

    public StatisticsMap toMap() {
        return new StatisticsMap(pointer, key);
    }

    public double getValue() throws ClingoException {
        Pointer<Double> value = Pointer.allocateDouble();
        handleError(LIB.clingo_statistics_value_get(pointer, key, value), "Error reading the statistic value!");
        return value.getDouble();
    }

    public StatisticsType getType() throws ClingoException {
        Pointer<Integer> value = Pointer.allocateInt();
        handleError(LIB.clingo_statistics_type(pointer, key, value), "Error reading the statistric type!");
        return EnumValue.valueOfInt(StatisticsType.class, value.getInt());
    }

    public Pointer<clingo_statistic> getPointer() {
        return pointer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString(this));
        return sb.toString();
    }

    protected static String toString(Statistics statistics) {
        StringBuilder sb = new StringBuilder();
        try {
            StatisticsType type = statistics.getType();
            switch (type) {
                case VALUE:
                    sb.append('"').append(statistics.getValue()).append('"');
                    break;
                case ARRAY:
                    sb.append(statistics.toList());
                    break;
                case MAP:
                    sb.append(statistics.toMap());
                    break;
                case EMPTY:
                    break;
            }
        } catch (ClingoException ex) {
            Clingo.handleRuntimeError(ex);
        }
        return sb.toString();
    }    
}

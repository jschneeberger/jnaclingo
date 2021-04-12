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

import org.lorislab.clingo4j.api.c.clingo_symbolic_literal;

/**
 *
 * @author Andrej Petras
 */
public class SymbolicLiteral {

    private final Symbol symbol;

    private final boolean positive;

    public SymbolicLiteral(Symbol symbol, boolean positive) {
        this.symbol = symbol;
        this.positive = positive;
    }

    public boolean isPositive() {
        return positive;
    }

    public boolean isNegative() {
        return !positive;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public clingo_symbolic_literal toSymbolicLiteral() {
        clingo_symbolic_literal ret = new clingo_symbolic_literal();
        ret.symbol(symbol.getStructObject());
        ret.positive(positive);
        return ret;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isNegative()) {
            sb.append("~");
        }
        sb.append(getSymbol());
        return sb.toString();
    }

    public boolean isEqual(SymbolicLiteral b) {
        return positive == b.positive && symbol.isEqual(b.symbol);
    }

    public boolean isNotEqual(SymbolicLiteral b) {
        return !isEqual(b);
    }

    public boolean isLessThan(SymbolicLiteral b) {
        if (positive != b.positive) {
            return positive && !b.positive;
        }
        return symbol.isLessThan(b.symbol);
    }

    public boolean isLessEqualThan(SymbolicLiteral b) {
        return !b.isLessThan(this);
    }

    public boolean isMoreThan(SymbolicLiteral b) {
        return b.isLessThan(this);
    }

    public boolean isMoreEqualThan(SymbolicLiteral b) {
        return !isLessThan(b);
    }

}

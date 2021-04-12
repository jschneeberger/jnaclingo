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

import org.lorislab.clingo4j.api.enums.ErrorCode;

/**
 *
 * @author Andrej Petras
 */
public class ClingoException extends Exception {
    
    private static final long serialVersionUID = -5124979156179474046L;
    
    private final ErrorCode errorCode;
    
    private final String errorMessage;

    public ClingoException() {
        super();
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    public ClingoException(ErrorCode errorCode, String errorMessage, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Clingo error code: " + errorCode + " message: " + errorMessage;
    }
    
    
    
}

/*
 *
 *  * Copyright (c) 2016. David Sowerby
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations under the License.
 *
 */

package uk.q3c.util.data;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.ClassUtils;

import static com.google.common.base.Preconditions.*;

/**
 * Converts an Enum in full - that is, the class and constant as a single String.
 * <p>
 * Created by David Sowerby on 27/06/15.
 */
public class EnumConverter implements DataItemConverter<Enum> {
    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings("EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS")
    @Override
    public Enum convertToModel(String value) throws ConversionException {
        checkNotNull(value);
        //slight mis-use of ClassUtils - it is just about the structure of the String
        String constantName = ClassUtils.getShortClassName(value);
        String className = ClassUtils.getPackageName(value);

        try {
            @SuppressWarnings("unchecked") final Class<? extends Enum> clazz = (Class<? extends Enum>) Class.forName(className);
            return Enum.valueOf(clazz, constantName);
        } catch (Exception e) {
            String msg = "Failed to convert String '" + value + "' to Enum";
            throw new ConversionException(msg, e);
        }
    }


    @Override
    public String convertToString(Enum value) {
        checkNotNull(value);
        return value.getClass()
                .getName() + '.' + value.name();

    }
}

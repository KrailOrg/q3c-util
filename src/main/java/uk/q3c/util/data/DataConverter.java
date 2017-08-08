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

import javax.annotation.Nonnull;

/**
 * Utility to convert a list of values to and from String.  These are not considered suitable for presentation to the user
 * as conversions use a fixed Locale.
 * <p>
 * Created by David Sowerby on 27/06/15.
 */
public interface DataConverter {


    /**
     * Converts the supplied {@code value} to String
     *
     * @param value the value to be converted
     * @param <V>   the value type
     * @return String for persistence, null if value is null
     * @throws ConverterException  if no converter is available for the type of V
     * @throws ConversionException if the conversion itself fails
     */
    <V> String convertValueToString(V value);

    /**
     * Returns a value converted from the String.
     *
     * @param elementClass the class of the element to be converted
     * @param valueString  the String representation of the value
     * @throws ConverterException  if no converter is available for the type of V
     * @throws ConversionException if the conversion itself fails
     */
    @Nonnull
    <V> V convertStringToValue(@Nonnull Class<V> elementClass, @Nonnull String valueString);
}

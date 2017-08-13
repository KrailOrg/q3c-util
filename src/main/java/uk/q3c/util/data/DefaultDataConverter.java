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

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.inject.Inject;
import uk.q3c.util.DataListConverter;
import uk.q3c.util.clazz.ClassNameUtils;
import uk.q3c.util.data.collection.DataList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;


/**
 * Default implementation for {@link DataConverter}.
 * <p>
 * Created by David Sowerby on 27/06/15.
 */
public class DefaultDataConverter implements DataConverter {


    private final Map<Class<?>, DataItemConverter> customConverters;
    private final ClassNameUtils classNameUtils;


    @Inject
    public DefaultDataConverter(Map<Class<?>, DataItemConverter> customConverters, ClassNameUtils classNameUtils) {
        this.customConverters = customConverters;
        this.classNameUtils = classNameUtils;
    }


    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override

    public String convertValueToString(Object value) {
        Class<?> modelType = classNameUtils.classWithEnhanceRemoved(value.getClass());
        if (modelType == String.class) {
            return ((String) value);
        } else if (modelType.isAssignableFrom(DataList.class)) {
            return new DataListConverter(this).convertToString((DataList) (value));
        } else if (modelType == Integer.class) {
            return Ints.stringConverter()
                    .reverse()
                    .convert((Integer) value);
        } else if (modelType == Long.class) {
            return Longs.stringConverter()
                    .reverse()
                    .convert((Long) value);

        } else if (modelType == Boolean.class) {
            return value.toString();
        } else if (modelType == Locale.class) {
            return ((Locale) value).toLanguageTag();
        } else if (modelType == LocalDateTime.class) {
            return ((LocalDateTime) value).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else if (modelType.isEnum()) {
            return new EnumConverter().convertToString((Enum) value);
        } else if (modelType == BigDecimal.class) {
            return value.toString();

        } else if (customConverters.containsKey(modelType)) {
            return customConverters.get(modelType).convertToString(value);
        }
        String msg = "Conversion of data type of '" + value.getClass() + "' is not supported";
        throw new ConverterException(msg);
    }


    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override

    public <V> V convertStringToValue(Class<V> elementClass, String valueString) {
        if (elementClass == String.class) {
            return ((V) valueString);
        } else if (elementClass == Integer.class) {
            return (V) Ints.stringConverter()
                    .convert(valueString);
        } else if (elementClass == Long.class) {
            return (V) Longs.stringConverter()
                    .convert(valueString);
        } else if (elementClass == Boolean.class) {
            return (V) Boolean.valueOf(valueString);
        } else if (elementClass == Locale.class) {
            return (V) Locale.forLanguageTag(valueString);
        } else if (elementClass == LocalDateTime.class) {
            return (V) LocalDateTime.parse(valueString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        } else if (Enum.class.isAssignableFrom(elementClass)) {
            return (V) new EnumConverter().convertToModel(valueString);
        } else if (elementClass == BigDecimal.class) {
            return (V) new BigDecimal(valueString);
        } else if (customConverters.containsKey(elementClass)) {
            return (V) customConverters.get(elementClass).convertToModel(valueString);
        }
        String msg = "Conversion to data type of '" + elementClass + "' is not supported";
        throw new ConverterException(msg);
    }

    public <C, E> DataList<E> convertStringToCollection(Class<C> collectionClass, Class<E> elementClass, String stringValue) {
        return convertStringToCollection(collectionClass, elementClass, stringValue, ",");
    }

    @Override
    public <C, E> DataList<E> convertStringToCollection(Class<C> collectionClass, Class<E> elementClass, String stringValue, String separator) {
        if (collectionClass.isAssignableFrom(DataList.class)) {
            DataListConverter<E> converter = new DataListConverter<>(this);
            return converter.convertToModel(elementClass, stringValue, separator);
        } else {
            String msg = "Conversion to collection type of '" + collectionClass + "' is not supported";
            throw new ConverterException(msg);
        }
    }


}

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

package uk.q3c.util.data

import com.google.common.collect.ImmutableList
import com.google.inject.Guice
import com.google.inject.Injector
import spock.lang.Specification
import uk.q3c.util.UtilModule
import uk.q3c.util.dag.DynamicDAG
import uk.q3c.util.data.collection.AnnotationList
import uk.q3c.util.data.collection.DataList

import java.time.LocalDateTime

/**
 * Created by David Sowerby on 21 Jan 2016
 */
class DefaultDataConverterTest extends Specification {
    static enum TestEnum {
        MAY, THE, FORCE
    }

    DataConverter converter
    Injector injector

    def setup() {
        injector = Guice.createInjector(new UtilModule())
        converter = injector.getInstance(DataConverter)
    }

    def "from other to String"() {
        expect:
        converter.convertValueToString('ab').equals("ab")
        converter.convertValueToString(3).equals("3")
        converter.convertValueToString(33L).equals("33")
        converter.convertValueToString(true).equals('true')
        converter.convertValueToString(Locale.UK).equals('en-GB')
        converter.convertValueToString(LocalDateTime.of(2014, 07, 13, 12, 15)).equals('2014-07-13T12:15:00')
        converter.convertValueToString(TestEnum.THE).equals('uk.q3c.util.data.DefaultDataConverterTest$TestEnum.THE')
        converter.convertValueToString(new AnnotationList(Override, Deprecated)).equals('java.lang.Override~~java.lang.Deprecated')
        converter.convertValueToString(BigDecimal.valueOf(433)).equals('433')
//        converter.convertValueToString(Service.State.FAILED).equals('uk.q3c.krail.core.services.Service$State.FAILED')

//        converter.convertValueToString(new OptionList<>(Integer.class, 1, 3)).equals('1~~3')

    }

    def "from String to other"() {
        expect:
        converter.convertStringToValue(String.class, 'ab').equals("ab")
        converter.convertStringToValue(Integer.class, '3').equals(3)
        converter.convertStringToValue(Long, '33').equals(33L)
        converter.convertStringToValue(Boolean, 'true').equals(true)
        converter.convertStringToValue(Locale, 'en-GB').equals(Locale.UK)
        converter.convertStringToValue(LocalDateTime, '2014-07-13T12:15:00').equals(LocalDateTime.of(2014, 07, 13, 12, 15))
//        converter.convertStringToValue(I18NKey, 'uk.q3c.krail.core.i18n.LabelKey.Yes').equals(LabelKey.Yes)
        converter.convertStringToValue(Enum, 'uk.q3c.util.data.DefaultDataConverterTest$TestEnum.MAY').equals(TestEnum.MAY)
        converter.convertStringToValue(BigDecimal, '433').equals(BigDecimal.valueOf(433))
        converter.convertStringToValue(AnnotationList, 'java.lang.Override~~java.lang.Deprecated').equals(new AnnotationList(Override, Deprecated))
    }

    def "convertToString, unknown converter type throws ConverterException "() {
        when:
        converter.convertValueToString(new DynamicDAG())

        then:
        thrown(ConverterException)
    }

    def "convertToModel, unknown converter type throws ConverterException "() {
        when:
        converter.convertStringToValue(DynamicDAG.class, "")

        then:
        thrown(ConverterException)
    }

    def "DataList round trip"() {
        given:
        DataList<Integer> dataList = new DataList<>(Integer.class, ImmutableList.of(1, 2, 3))

        when:
        String stringValue = converter.convertValueToString(dataList)

        then:
        stringValue == "1,2,3"

        when:
        DataList<Integer> dataList2 = converter.convertStringToCollection(DataList.class, Integer.class, stringValue, ",")

        then:
        dataList == dataList2
    }
}

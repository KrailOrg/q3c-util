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

package uk.q3c.util.data.collection

import com.google.common.collect.ImmutableList
import com.google.inject.Inject
import spock.lang.Specification

/**
 *
 * Created by David Sowerby on 08/08/15.
 */
class AnnotationListTest extends Specification {

    AnnotationList list

    def "create empty list"() {
        when:
        list = new AnnotationList()

        then:

        list.isEmpty()
    }

    def "create with elements"() {
        when:
        list = new AnnotationList(Override)

        then:
        list.size() == 1
        list.getList().contains(Override)
    }

    def "create with list"() {
        when:
        list = new AnnotationList(ImmutableList.of(Override))

        then:
        list.size() == 1
        list.getList().contains(Override)
    }

    def "create with empty list"() {
        when:
        list = new AnnotationList(ImmutableList.of())

        then:

        list.isEmpty()
    }

    def "equals and hashcode"() {
        given:
        AnnotationList ref = new AnnotationList(Override, Inject)
        AnnotationList refEmpty = new AnnotationList()
        AnnotationList different = new AnnotationList(Inject)
        AnnotationList empty = new AnnotationList()
        AnnotationList same = new AnnotationList(Override, Inject)

        expect:
        ref.equals(same)
        ref.hashCode() == same.hashCode()

        refEmpty.equals(empty)
        refEmpty.hashCode() == empty.hashCode()

        !different.equals(ref)
        different.hashCode() != ref.hashCode()

        !empty.equals(ref)
        empty.hashCode() != ref.hashCode()
    }
}

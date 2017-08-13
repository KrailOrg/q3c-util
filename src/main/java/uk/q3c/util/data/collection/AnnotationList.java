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

package uk.q3c.util.data.collection;

import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.ListUtils;

import javax.annotation.concurrent.Immutable;
import java.lang.annotation.Annotation;
import java.util.List;


/**
 * The syntax of getting the Generics right to create a list of Class<? extends Annotation> is clumsy and prone to errors - this class just makes it clearer and
 * simpler
 * <p>
 * Created by David Sowerby on 07/08/15.
 */
@Immutable
public class AnnotationList {

    protected final ImmutableList<Class<? extends Annotation>> list;

    /**
     * Construct with List containing required values - may be empty
     *
     * @param list containing required values - may be empty
     */
    public AnnotationList(List<Class<? extends Annotation>> list) {
        super();
        this.list = ImmutableList.copyOf(list);
    }

    /**
     * Construct with elements containing required values - may be empty
     *
     * @param elements containing required values - may be empty
     */
    @SafeVarargs
    public AnnotationList(Class<? extends Annotation>... elements) {
        super();
        this.list = (elements == null) ? ImmutableList.of() : ImmutableList.copyOf(elements);
    }


    public ImmutableList<Class<? extends Annotation>> getList() {
        return list;
    }

    public int size() {
        return list.size();
    }


    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotationList that = (AnnotationList) o;

        return ListUtils.isEqualList(this.list, that.list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }
}

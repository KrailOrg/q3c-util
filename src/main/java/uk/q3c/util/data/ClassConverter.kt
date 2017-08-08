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

import com.google.common.base.Preconditions.checkNotNull
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.apache.commons.lang3.ClassUtils

/**
 * Converts an Class to a String representation and back.
 *
 *
 * Created by David Sowerby on 27/06/15.
 */
class ClassConverter : DataItemConverter<Class<*>> {
    /**
     * {@inheritDoc}
     */
    @SuppressFBWarnings("EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS")
    override fun convertToModel(value: String): Class<*> {
        checkNotNull(value)
        try {
            return ClassUtils.getClass(this.javaClass
                    .classLoader, value)
        } catch (e: Exception) {
            val msg = ("Failed to convert String '$value' to Class")
            throw ConversionException(msg, e)
        }

    }


    override fun convertToString(value: Class<*>): String {
        checkNotNull(value)
        return value.canonicalName
    }
}

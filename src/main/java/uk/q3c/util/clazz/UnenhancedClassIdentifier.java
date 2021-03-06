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

package uk.q3c.util.clazz;

import java.io.Serializable;

/**
 * Returns the un-enhanced original class from a byte enhanced instance.  Often needed when the instance is subject to reflection to identify field / class annotations.
 * <p>
 * If {@code target} is not enhanced, returns target.getClass()
 * <p>
 * <p>
 * Created by David Sowerby on 10/05/15.
 */
public interface UnenhancedClassIdentifier extends Serializable {
    Class<?> getOriginalClassFor(Object target);
}

package uk.q3c.util.guice

import com.google.common.reflect.TypeToken
import com.google.inject.Key
import java.io.Serializable

/**
 *
 * A serializable proxy for Guice [Key]
 *
 * Created by David Sowerby on 23 Apr 2018
 */
class GuiceKeyProxy(key: Key<*>) : Serializable {

    val annotation: Annotation? = key.annotation
    val annotationType: Class<out Annotation>? = key.annotationType
    val typeToken: TypeToken<*> = TypeToken.of(key.typeLiteral.type)

    val key: Key<*>
        get() {
            if (annotation == null && annotationType == null) {
                return Key.get(typeToken.type)
            }
            if (annotation != null) {
                return Key.get(typeToken.type, annotation)
            }
            return Key.get(typeToken.type, annotationType)
        }

}
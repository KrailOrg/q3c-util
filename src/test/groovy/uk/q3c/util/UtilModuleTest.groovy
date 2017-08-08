package uk.q3c.util

import com.google.inject.Guice
import com.google.inject.Inject
import com.google.inject.Injector
import spock.lang.Specification
import uk.q3c.util.clazz.ClassNameUtils
import uk.q3c.util.clazz.DefaultClassNameUtils
import uk.q3c.util.clazz.DefaultUnenhancedClassIdentifier
import uk.q3c.util.clazz.UnenhancedClassIdentifier
import uk.q3c.util.data.DataItemConverter
import uk.q3c.util.data.collection.AnnotationList
import uk.q3c.util.data.collection.AnnotationListConverter

/**
 * Created by David Sowerby on 08 Aug 2017
 */
class UtilModuleTest extends Specification {


    static class TestClass {
        Map<Class<?>, DataItemConverter> customConverters

        @Inject
        TestClass(Map<Class<?>, DataItemConverter> customConverters) {
            this.customConverters = customConverters
        }
    }

    Injector injector

    def setup() {
        injector = Guice.createInjector(new UtilModule())
    }

    def "DataItemConverter registration is valid"() {

        when: "A class using custom converters is constructed"
        TestClass testClass = injector.getInstance(TestClass)


        then: "custom converters include AnnotationList converter"
        testClass.customConverters.get(AnnotationList.class) instanceof AnnotationListConverter
    }

    def "contains correct bindings"() {
        expect:
        injector.getInstance(ClassNameUtils) instanceof DefaultClassNameUtils
        injector.getInstance(UnenhancedClassIdentifier) instanceof DefaultUnenhancedClassIdentifier
    }
}

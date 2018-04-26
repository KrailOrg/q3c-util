package uk.q3c.util.guice

import com.google.inject.Key
import com.google.inject.TypeLiteral
import org.amshove.kluent.shouldEqual
import org.apache.commons.lang3.SerializationUtils
import org.junit.Test
import java.util.*


/**
 * Created by David Sowerby on 23 Apr 2018
 */
class GuiceKeyProxyTest {


    @Test
    fun roundTripClassOnly() {

        // given
        val key = Key.get(Locale::class.java)
        val proxy = GuiceKeyProxy(key)


        //when
        val output = SerializationUtils.serialize(proxy)
        val result: GuiceKeyProxy = SerializationUtils.deserialize<GuiceKeyProxy>(output)

        // then
        result.key.shouldEqual(key)

    }

    @Test
    fun roundTripClassAndAnnotationClass() {

        // given
        val key = Key.get(Locale::class.java, TestAnnotation1::class.java)
        val proxy = GuiceKeyProxy(key)


        //when
        val output = SerializationUtils.serialize(proxy)
        val result: GuiceKeyProxy = SerializationUtils.deserialize<GuiceKeyProxy>(output)

        // then
        result.key.shouldEqual(key)

    }

    @Test
    fun roundTripClassAndAnnotationWithoutParams() {

        // given
        val fields = AnnotatedLocaleHolder::class.java.declaredFields
        val annotation = fields[0].declaredAnnotations[0]
        val key = Key.get(Locale::class.java, annotation)
        val proxy = GuiceKeyProxy(key)

        //when
        val output = SerializationUtils.serialize(proxy)
        val result: GuiceKeyProxy = SerializationUtils.deserialize<GuiceKeyProxy>(output)

        // then
        result.key.shouldEqual(key)

    }

    @Test
    fun roundTripClassAndAnnotationWithParams() {

        // given
        val fields = AnnotatedLocaleHolder::class.java.declaredFields
        val annotation = fields[1].declaredAnnotations[0]
        val key = Key.get(Locale::class.java, annotation)
        val proxy = GuiceKeyProxy(key)

        //when
        val output = SerializationUtils.serialize(proxy)
        val result: GuiceKeyProxy = SerializationUtils.deserialize<GuiceKeyProxy>(output)

        // then
        result.key.shouldEqual(key)

    }

    @Test
    fun roundTripClassWithParamAndAnnotation() {

        // given
        val eventBusLiteral = object : TypeLiteral<List<String>>() {}

        val key = Key.get(eventBusLiteral, TestAnnotation::class.java)
        val proxy = GuiceKeyProxy(key)

        //when
        val output = SerializationUtils.serialize(proxy)
        val result: GuiceKeyProxy = SerializationUtils.deserialize<GuiceKeyProxy>(output)

        // then
        result.key.shouldEqual(key)

    }
}

class AnnotatedLocaleHolder {
    @TestAnnotation1
    val locale: Locale = Locale.CANADA_FRENCH

    @TestAnnotation("because")
    val testLocale: Locale = Locale.CHINESE
}



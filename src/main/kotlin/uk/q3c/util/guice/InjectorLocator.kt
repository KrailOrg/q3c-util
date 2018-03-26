package uk.q3c.util.guice

import com.google.inject.Injector
import java.io.Serializable

/**
 * Created by David Sowerby on 26 Mar 2018
 */
interface InjectorLocator : Serializable {
    fun get(): Injector
    fun put(injector: Injector)
}
package uk.q3c.util.data.collection

import com.google.common.collect.ImmutableList

/**
 * Created by David Sowerby on 13 Aug 2017
 */

data class DataList<E> @JvmOverloads constructor(val entryClass: Class<E>, private val entryList: List<E>, val separator: String = ",") {
    val entries: List<E>

    init {
        entries = ImmutableList.copyOf(entryList)
    }

    fun isEmpty(): Boolean {
        return entries.isEmpty()
    }

}
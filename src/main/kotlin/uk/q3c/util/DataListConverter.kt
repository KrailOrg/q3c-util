package uk.q3c.util

import com.google.common.base.Splitter
import com.google.common.collect.ImmutableList
import com.google.inject.Inject
import org.apache.commons.lang3.StringEscapeUtils
import uk.q3c.util.data.DataConverter
import uk.q3c.util.data.collection.DataList
import java.util.*

/**
 * Created by David Sowerby on 13 Aug 2017
 *
 * @param <E> the type of the entries in the [DataList] to be converted
 */
class DataListConverter<E> @Inject constructor(val dataConverter: DataConverter) {

    @JvmOverloads
    fun convertToModel(elementClass: Class<E>, value: String, separator: String = ","): DataList<E> {
        if (value.isEmpty()) {
            return DataList(elementClass, ImmutableList.of(), separator)
        }
        val strings = Splitter.on(separator)
                .splitToList(value)
        val elementList = ArrayList<E>()
        strings.forEach { s ->
            val unescaped = StringEscapeUtils.unescapeCsv(s)
            val element = dataConverter.convertStringToValue(elementClass, unescaped)
            elementList.add(element)
        }
        return DataList(elementClass, elementList, separator)
    }

    fun convertToString(model: DataList<E>): String {
        if (model.isEmpty()) {
            return ""
        }
        val buf = StringBuilder()
        val modelList = model.entries
        var first = true
        for (e in modelList) {
            if (!first) {
                buf.append(model.separator)
            } else {
                first = false
            }
            buf.append(StringEscapeUtils.escapeCsv(dataConverter.convertValueToString(e)))
        }
        return buf.toString()
    }
}
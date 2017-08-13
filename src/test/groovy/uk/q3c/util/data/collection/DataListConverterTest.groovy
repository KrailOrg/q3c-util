package uk.q3c.util.data.collection

import com.google.common.collect.ImmutableList
import org.apache.commons.collections15.ListUtils
import spock.lang.Specification
import uk.q3c.util.DataListConverter
import uk.q3c.util.clazz.DefaultClassNameUtils
import uk.q3c.util.data.DataItemConverter
import uk.q3c.util.data.DefaultDataConverter

/**
 * Created by David Sowerby on 13 Aug 2017
 */
class DataListConverterTest extends Specification {

    DataList<Integer> dataListInt
    DataListConverter<Integer> converterInt
    Map<Class<?>, DataItemConverter> customConverters = new HashMap<>()

    def setup() {

    }

    def "round trip integer"() {
        given:
        converterInt = new DataListConverter<>(new DefaultDataConverter(customConverters, new DefaultClassNameUtils()))
        dataListInt = new DataList<>(Integer.class, ImmutableList.of(3, 5, 7))

        when:
        String string = converterInt.convertToString(dataListInt)

        then:
        string == "3,5,7"

        when:
        DataList<Integer> integerDataList = converterInt.convertToModel(Integer.class, string)

        then:
        ListUtils.isEqualList(integerDataList.entries, ImmutableList.of(3, 5, 7))
        integerDataList == dataListInt
    }
}

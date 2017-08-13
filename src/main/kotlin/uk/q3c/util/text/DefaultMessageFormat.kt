package uk.q3c.util.text

import org.slf4j.LoggerFactory
import org.slf4j.helpers.MessageFormatter
import uk.q3c.util.text.MessageFormatMode.STRICT
import uk.q3c.util.text.MessageFormatMode.STRICT_EXCEPTION
import java.util.*


class DefaultMessageFormat : MessageFormat2 {
    private val log = LoggerFactory.getLogger(DefaultMessageFormat::class.java)

    override fun format(pattern: String, vararg arguments: Any): String {
        return format(STRICT, pattern, *arguments)
    }

    override fun format(mode: MessageFormatMode, pattern: String, vararg arguments: Any): String {
        val parameters = ArrayList<Int>()
        val strippedPattern = scanForParameters(pattern, parameters)
        val argsList = Arrays.asList(*arguments)

        when (mode) {
            STRICT -> if (!argsMatchParams(parameters, argsList)) {
                log.warn(strictFailureMessage(parameters, arguments, pattern))
                return pattern
            }
            STRICT_EXCEPTION -> if (!argsMatchParams(parameters, argsList)) {
                throw MessageFormatException(strictFailureMessage(parameters, arguments, pattern))
            }
            else -> {
                // not strict, no checks
            }
        }

        val sortedArguments = sortArguments(parameters, arguments)
        return MessageFormatter.arrayFormat(strippedPattern, sortedArguments)
                .message

    }

    private fun strictFailureMessage(parameters: ArrayList<Int>, arguments: Array<out Any>, pattern: String): String {
        return "Message pattern and arguments do not match, there are ${parameters.size} parameters in the pattern, and ${arguments.size} arguments. The pattern is: '$pattern'.  LENIENT mode would allow this"
    }

    private fun scanForParameters(pattern: String, parameters: MutableList<Int>): String {
        var i = 0
        val strippedPattern = StringBuilder()
        while (i < pattern.length) {
            var c = pattern[i]
            // if the '{' has been escaped this moves the scan beyond it, thereby ignoring it
            if (c == '\\') {
                i++
                c = pattern[i]
                if (c == '{') {
                    strippedPattern.append('{')
                    i++
                    c = pattern[i]
                    strippedPattern.append(c)
                }
            } else {
                strippedPattern.append(c)
            }
            // find an opening brace
            if (c == '{') {
                // find the closing '}' and extract
                val placeholder = StringBuilder()
                var done = false

                while (!done) {
                    i++
                    c = pattern[i]

                    if (c == '}') {
                        parameters.add(Integer.valueOf(placeholder.toString()))
                        strippedPattern.append(c)
                        done = true
                    } else {
                        placeholder.append(c)
                    }
                }
            }

            i++
        }
        return strippedPattern.toString()
    }

    private fun argsMatchParams(parameters: List<Int>, arguments: List<Any>): Boolean {
        // strict requires matching numbers of params and args
        if (parameters.size != arguments.size) {
            return false
        }
        // strict requires that param indexes are a complete sequence
        for (i in 0..parameters.size - 1) {
            if (!parameters.contains(i)) {
                return false
            }
        }
        return true
    }


    private fun sortArguments(parameters: List<Int>, arguments: Array<out Any>): Array<Any> {
        val sortedArguments = ArrayList<Any>()
        for (i in parameters) {
            val argument = if (i < arguments.size) {
                arguments[i]
            } else {
                "{??}"
            }
            sortedArguments.add(argument)
        }
        return sortedArguments.toTypedArray()
    }
}



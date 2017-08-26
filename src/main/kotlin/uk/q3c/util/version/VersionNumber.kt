package uk.q3c.util.version


/**
 * Created by David Sowerby on 24 Aug 2017
 */

data class VersionNumber @JvmOverloads constructor(
        val major: Int = 0,
        val minor: Int = 0,
        val patch: Int = 0,
        val nonFunctional: Int = 0,
        val qualifier: String = "",
        val buildMetaData: String = "",
        val scheme: Scheme = Scheme()) : Comparable<VersionNumber> {

    /**
     * The [major],[minor].[patch].[nonFunctional] version elements are compared to a depth determined by [Scheme.depth].
     * The [qualifier] element is ignored
     * The [buildMetaData] element is ignored
     */
    fun isSameBaseVersionAs(other: VersionNumber): Boolean {
        return compareTo(other) == 0
    }

    fun isLaterBaseVersionThan(other: VersionNumber): Boolean {
        return compareVersion(other, false) > 0
    }


    /**
     * The [major],[minor].[patch].[nonFunctional] version elements are compared to a depth determined by [Scheme.depth].
     * The [qualifier] elements are lexically compared (case insensitive)
     * The [buildMetaData] element is ignored
     */
    fun isSameVersionAs(other: VersionNumber): Boolean {
        return (compareTo(other) == 0) && (qualifier.compareTo(other.qualifier, true) == 0)
    }

    fun isLaterVersionThan(other: VersionNumber): Boolean {
        return compareVersion(other, false) > 0 || (compareTo(other) == 0 && (qualifier.compareTo(other.qualifier, true) < 0))
    }

    /**
     * The [major],[minor].[patch].[nonFunctional] version elements are compared to a depth determined by [Scheme.depth].
     * The [qualifier] element is lexically compared (case insensitive)
     * The [buildMetaData] element is lexically compared (case insensitive)
     */
    fun isSameBuildAs(other: VersionNumber): Boolean {
        return (compareTo(other) == 0) && (qualifier.compareTo(other.qualifier, true) == 0) && (buildMetaData.compareTo(other.buildMetaData) == 0)
    }

    fun isLaterBuildThan(other: VersionNumber): Boolean {
        return isLaterVersionThan(other) || (isSameVersionAs(other) && (buildMetaData.compareTo(other.buildMetaData) > 0))
    }

    /**
     * The [major],[minor].[patch] version elements are compared to a depth determined by [Scheme.depth].
     * The [nonFunctional] element is ignored
     * The [qualifier] element is ignored
     * The [buildMetaData] is ignored
     */
    fun isSameFunctionalVersionAs(other: VersionNumber): Boolean {
        return (compareVersion(other, true) == 0) && (qualifier.compareTo(other.qualifier, true) == 0)
    }

    fun isLaterFunctionalVersionThan(other: VersionNumber): Boolean {
        return compareVersion(other, true) > 0 || (compareTo(other) == 0 && (qualifier.compareTo(other.qualifier, true) < 0))
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive integer
     * as this object is less than, equal to, or greater than the specified object.
     *
     *
     * The depth to which the version is compared is set by [Scheme.depth].
     * The [qualifier] element is lexically compared (case insensitive) using [String.compareTo]
     * The [buildMetaData] element is ignored by this method
     */
    override fun compareTo(other: VersionNumber): Int {
        return compareVersion(other, false)
    }

    fun compareVersion(other: VersionNumber, ignoreNonfunctional: Boolean): Int {
        val depth = if (ignoreNonfunctional && scheme.depth == 4) {
            3
        } else {
            scheme.depth
        }
        if (major != other.major) {
            return major - other.major
        }
        if (depth == 1) {
            return 0
        }
        if (minor != other.minor) {
            return minor - other.minor
        }
        if (depth == 2) {
            return 0
        }
        if (patch != other.patch) {
            return patch - other.patch
        }
        if (depth == 3) {
            return 0
        }
        if (nonFunctional != other.nonFunctional) {
            return nonFunctional - other.nonFunctional
        }
        return 0
    }

    /**
     * Returns a new instance with [major] incremented, and ALL other elements reset to 0 (empty String for [buildMetaData] and [qualifier])
     */
    fun incMajor(): VersionNumber {
        return VersionNumber(major = major + 1)
    }

    /**
     * Returns a new instance with [minor] incremented, and all lower order elements reset to 0 (empty String for [buildMetaData] and [qualifier])
     */
    fun incMinor(): VersionNumber {
        return VersionNumber(major = major, minor = minor + 1)
    }

    /**
     * Returns a new instance with [patch] incremented, and all lower order elements reset to 0 (empty String for [buildMetaData] and [qualifier])
     */
    fun incPatch(): VersionNumber {
        return VersionNumber(major = major, minor = minor, patch = patch + 1)
    }

    /**
     * Returns a new instance with [nonFunctional] incremented, [buildMetaData] and [qualifier] rest to empty String
     */
    fun incNonFunctional(): VersionNumber {
        return VersionNumber(major = major, minor = minor, patch = patch, nonFunctional = nonFunctional + 1)
    }

    override fun toString(): String {
        val buf = StringBuilder()
        buf.append(major)
        if (scheme.depth > 1) {
            buf.append(".")
            buf.append(minor)
        }
        if (scheme.depth > 2) {
            buf.append(".")
            buf.append(patch)
        }
        if (scheme.depth > 3) {
            buf.append(".")
            buf.append(nonFunctional)
        }
        if (qualifier.isNotBlank()) {
            buf.append(scheme.qualifierSeparator)
            buf.append(qualifier)
        }
        if (buildMetaData.isNotBlank()) {
            buf.append(scheme.buildMetaDataSeparator)
            buf.append(buildMetaData)
        }
        return buf.toString()
    }


}


/**
 * Parses a full version number, that is one which contains all the elements, including qualifier and build meta data, in one string
 * The format of that string must comply with the selected [scheme]
 *
 * @return a [VersionNumber] constructed from the input
 */
@JvmOverloads
fun parseFullVersionNumber(input: String, scheme: Scheme = Scheme()): VersionNumber {
    val buildSplit = input.split(scheme.buildMetaDataSeparator)
    var buildMeta = ""
    // if there is a buildMeta element
    if (buildSplit.size > 1) {
        buildMeta = buildSplit[1]
    }
    val qualifierSplit = buildSplit[0].split(scheme.qualifierSeparator)
    var qualifier = ""
    if (qualifierSplit.size > 1) {
        qualifier = qualifierSplit[1]
    }
    val baseVersion = qualifierSplit[0]
    val bv = parseBaseVersion(baseVersion)
    return VersionNumber(bv.major, bv.minor, bv.patch, bv.nonFunctional, qualifier, buildMeta, scheme)
}

/**
 * Parses a version number, from  its 3 constituent parst, [baseVersion] (for example 1.2.3.4), [qualifier] and [buildMetaData]
 *
 * The format of that string must comply with the selected [scheme]
 *
 * @return a [VersionNumber] constructed from the input
 */
@JvmOverloads
fun parseVersion(baseVersion: String, qualifier: String = "", buildMetaData: String = "", scheme: Scheme = Scheme()): VersionNumber {
    val bv = parseBaseVersion(baseVersion)
    return VersionNumber(bv.major, bv.minor, bv.patch, bv.nonFunctional, qualifier, buildMetaData, scheme)
}

/**
 * Parses just the base version identifier (for example 1.2.3.4) into a simple data object containing the elements in numeric form
 */
fun parseBaseVersion(baseVersion: String): BaseVersion {
    val versionSplit = baseVersion.split(".")
    var index = 0
    val bv = BaseVersion()
    for (element in versionSplit) {
        when (index) {
            0 -> bv.major = element.toInt()
            1 -> bv.minor = element.toInt()
            2 -> bv.patch = element.toInt()
            3 -> bv.nonFunctional = element.toInt()

        }
        index++
    }
    return bv
}


data class Scheme @JvmOverloads constructor(val depth: Int = 4, val qualifierSeparator: String = "-", val buildMetaDataSeparator: String = "+") {
    init {
        if (depth < 1 || depth > 4) {
            throw SchemeException("Scheme depth must be in the range 1 - 4 inclusive")
        }
    }
}

data class BaseVersion(var major: Int = 0,
                       var minor: Int = 0,
                       var patch: Int = 0,
                       var nonFunctional: Int = 0)

class SchemeException(msg: String) : RuntimeException(msg)
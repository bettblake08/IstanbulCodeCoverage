package com.bettblake08.istanbulcc

/**
 * This class represents an Istanbul code coverage report.
 *
 * @property coverageMap A map of file paths to file coverage data.
 */
class IstanbulCoverageReport(private val coverageMap: Map<String, FileCoverage>) {

    /**
     * This method returns the file coverage data for a given file path.
     *
     * @param filePath The path of the file to get coverage data for.
     * @return The coverage data for the specified file path, or null if the file is not in the report.
     */
    fun getFileCoverage(filePath: String): FileCoverage? {
        return coverageMap[filePath]
    }

    fun getCoverageMap(): Map<String, FileCoverage> {
        return coverageMap
    }

    /**
     * This class represents the coverage data for a single file.
     *
     * @property path The path of the file.
     * @property statementCoverage The statement coverage percentage for the file.
     * @property branchCoverage The branch coverage percentage for the file.
     * @property functionCoverage The function coverage percentage for the file.
     */
    data class FileCoverage(
        val _coverageSchema: String? = null,
        val b: Map<Int, IntArray>,
        val branchCoverage: Double,
        val branchMap: Map<Int, Map<String, Any>>,
        val f: Map<Int, Int>,
        val fnMap: Map<Int, Map<String, Any>>,
        val functionCoverage: Double,
        val hash: String? = null,
        val path: String,
        val s: Map<Int, Int>,
        val statementCoverage: Double,
        val statementMap: Map<Int, Map<String, Any>>
    )
}

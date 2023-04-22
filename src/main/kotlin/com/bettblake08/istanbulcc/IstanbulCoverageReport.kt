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
     * The FileCoverage data class represents the coverage information for a single file. It contains various coverage
     * metrics and mappings that can be used to determine the test coverage of the file.
     *
     * @property _coverageSchema The coverage schema version. Default is null.
     * @property b A mapping of branch indexes to an array of execution counts for each branch.
     * @property branchCoverage The branch coverage percentage for the file.
     * @property branchMap A mapping of branch indexes to branch metadata.
     * @property f A mapping of function indexes to their execution count.
     * @property fnMap A mapping of function indexes to function metadata.
     * @property functionCoverage The function coverage percentage for the file.
     * @property hash A hash value representing the file. Default is null.
     * @property path The path to the file.
     * @property s A mapping of statement indexes to their execution count.
     * @property statementCoverage The statement coverage percentage for the file.
     * @property statementMap A mapping of statement indexes to statement metadata.
     */
    data class FileCoverage(
        val _coverageSchema: String? = null,
        val b: Map<Int, IntArray>,
        val branchCoverage: Double,
        val branchMap: Map<Int, BranchMap>,
        val f: Map<Int, Int>,
        val fnMap: Map<Int, FunctionMap>,
        val functionCoverage: Double,
        val hash: String? = null,
        val path: String,
        val s: Map<Int, Int>,
        val statementCoverage: Double,
        val statementMap: Map<Int, CodeLocation>
    )

    /**
     * Data class representing a function map in an Istanbul coverage report.
     *
     * @property name The name of the function.
     * @property decl The start and end positions of the function declaration.
     * @property loc The start and end positions of the function block.
     * @property line The line number of the function.
     */
    data class FunctionMap(
        val name: String,
        val decl: CodeLocation,
        val loc: CodeLocation,
        val line: Int
    )

    /**
     * Data class representing a source code location, with a start and end position.
     *
     * @property start The starting position of the code location.
     * @property end The ending position of the code location.
     */
    class CodeLocation(val start: CodePosition, val end: CodePosition)

    /**
     * Data class representing a position in a source code file, with a line and column number.
     *
     * @property line The line number.
     * @property column The column number.
     */
    data class CodePosition(val line: Int, val column: Int)

    /**
     * Data class representing branching code coverage
     *
     * @property loc Primary location of the branch starting point
     * @property type Type of branch
     * @property locations List of branch code locations
     * @property line Primary line location of the branching statement
     */
    data class BranchMap(
        val loc: CodeLocation,
        val type: String,
        val locations: List<CodeLocation>,
        val line: Int
    )
}

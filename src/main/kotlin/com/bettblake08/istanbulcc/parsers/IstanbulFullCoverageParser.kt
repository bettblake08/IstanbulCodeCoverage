package com.bettblake08.istanbulcc.parsers

import com.bettblake08.istanbulcc.IstanbulCoverageReport
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.intellij.openapi.diagnostic.Logger
import java.io.File

/**
 * This class is responsible for parsing an Istanbul coverage report and returning the relevant coverage data.
 *
 * @property reportFile The file containing the Istanbul coverage report.
 */
class IstanbulFullCoverageParser(private val reportFile: File) {
    private val logger = Logger.getInstance(IstanbulFullCoverageParser::class.java)

    /**
     * This method parses the Istanbul coverage report and returns the relevant coverage data.
     *
     * @return An IstanbulCoverageReport object representing the coverage data in the report.
     */
    fun parseReport(): IstanbulCoverageReport {
        val objectMapper = ObjectMapper().registerKotlinModule()
        val rootNode: JsonNode = objectMapper.readTree(reportFile)
        val coverageMap = parseCoverageMap(rootNode, objectMapper)
        val coverage = coverageMap.map { (path, fileCoverageData) ->
            mapOf(path to IstanbulCoverageReport.FileCoverage(
                path = path,
                statementCoverage = fileCoverageData.s.values.filterIsInstance<Int>().average(),
                branchCoverage = fileCoverageData.b.values.filterIsInstance<Int>().average(),
                functionCoverage = fileCoverageData.f.values.filterIsInstance<Int>().average(),
                statementMap = fileCoverageData.statementMap,
                fnMap = fileCoverageData.fnMap,
                branchMap = fileCoverageData.branchMap,
                s = fileCoverageData.s,
                b = fileCoverageData.b,
                f = fileCoverageData.f
            ))
        }.fold(mapOf<String, IstanbulCoverageReport.FileCoverage>()) { acc, map -> acc.plus(map) }

        logger.debug("Successfully parsed code coverage file")
        return IstanbulCoverageReport(coverage)
    }

    private fun parseCoverageMap(rootNode: JsonNode, objectMapper: ObjectMapper): Map<String, IstanbulCoverageReport.FileCoverage> {
        val typeFactory = TypeFactory.defaultInstance()
        val mapType = typeFactory.constructMapType(HashMap::class.java, String::class.java, IstanbulCoverageReport.FileCoverage::class.java)
        return objectMapper.convertValue(rootNode, mapType)
    }
}

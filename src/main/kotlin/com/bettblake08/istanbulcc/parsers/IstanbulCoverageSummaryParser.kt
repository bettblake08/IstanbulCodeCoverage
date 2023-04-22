package com.bettblake08.istanbulcc.parsers

import com.bettblake08.istanbulcc.CoverageSummary
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.MapType
import com.fasterxml.jackson.databind.type.TypeFactory
import java.io.File

class IstanbulCoverageSummaryParser(private val reportFile: File) {
    /**
     * Parses the Istanbul coverage summary report and returns a [CoverageSummary] object.
     */
    fun parseReport(): List<CoverageSummary.FileSummary> {
        val objectMapper = ObjectMapper()
        val rootNode = objectMapper.readTree(reportFile)
        val summaryMap = parseSummaryMap(rootNode, objectMapper)
        return summaryMap.map { (filePath, summaryMetric) ->
            val lines = summaryMetric.lines
            val statements = summaryMetric.statements
            val functions = summaryMetric.functions
            val branches = summaryMetric.branches
            CoverageSummary.FileSummary(filePath, lines, statements, functions, branches)
        }
    }

    /**
     * Parses the summary data from the JSON tree and returns a [Map] of summary metrics.
     */
    private fun parseSummaryMap(
        rootNode: JsonNode,
        objectMapper: ObjectMapper
    ): Map<String, CoverageSummary.FileSummary> {
        val typeFactory = TypeFactory.defaultInstance()
        val mapType: MapType = typeFactory.constructMapType(
            HashMap::class.java,
            String::class.java,
            CoverageSummary.FileSummary::class.java
        )
        return objectMapper.convertValue(rootNode, mapType)
    }
}

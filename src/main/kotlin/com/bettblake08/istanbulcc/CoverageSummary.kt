package com.bettblake08.istanbulcc

/**
 * Represents the summary data from the Istanbul coverage summary report.
 */
class CoverageSummary(private val summaryMap: Map<String, SummaryMetric>) {

    /**
     * Returns the total line coverage percentage.
     */
    fun getTotalLineCoverage(): Double = summaryMap["lines"]?.coveragePercentage ?: 0.0

    /**
     * Returns the total branch coverage percentage.
     */
    fun getTotalBranchCoverage(): Double = summaryMap["branches"]?.coveragePercentage ?: 0.0

    data class FileSummary(
        val filePath: String,
        val lines: SummaryMetric,
        val statements: SummaryMetric,
        val functions: SummaryMetric,
        val branches: SummaryMetric
    )

    /**
     * Represents a summary metric from the Istanbul coverage summary report.
     */
    data class SummaryMetric(
        val total: Int,
        val covered: Int,
        val skipped: Int,
        val coveragePercentage: Double
    )
}

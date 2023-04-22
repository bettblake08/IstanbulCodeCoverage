import com.bettblake08.istanbulcc.IstanbulCoverageReport
import com.bettblake08.istanbulcc.parsers.IstanbulFullCoverageParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.File

class IstanbulFullCoverageParserTest {

    private lateinit var parser: IstanbulFullCoverageParser
    private lateinit var coverageFile: File

    private val validFullCoverageFileResourcePath: String = "fixtures/validFullCoverageReport.json"

    private fun testCodeLocation(
        codeLocation: IstanbulCoverageReport.CodeLocation,
        startLine: Int,
        startColumn: Int,
        endLine: Int,
        endColumn: Int
    ) = run {
        assertEquals(startLine, codeLocation.start.line)
        assertEquals(startColumn, codeLocation.start.column)
        assertEquals(endLine, codeLocation.end.line)
        assertEquals(endColumn, codeLocation.end.column)
    }

    @Test
    fun `parseReport should return correct coverage data`() {
        coverageFile = File(javaClass.getResource(validFullCoverageFileResourcePath)!!.toURI())
        parser = IstanbulFullCoverageParser(coverageFile)

        val testPath = "api.js"
        val result = parser.parseReport()
        val coverageMap = result.getFileCoverage(testPath)

        assertNotNull(coverageMap)

        if (coverageMap != null) {
            assertEquals(testPath, coverageMap.path)

            assertEquals(48, coverageMap.statementMap.keys.size)
            coverageMap.statementMap.get(0)?.let { testCodeLocation(it, 5, 38, 5, 79) }

            assertEquals(5, coverageMap.fnMap.keys.size)

            val functionMap = coverageMap.fnMap.get(0)!!

            assertEquals("function", functionMap.name)
            assertEquals(23, functionMap.line)
            testCodeLocation(functionMap.decl, 23, 15, 23, 46)
            testCodeLocation(functionMap.loc, 23, 55, 57, 1)

            assertEquals(12, coverageMap.branchMap.keys.size)

            val branchMap = coverageMap.branchMap.get(0)!!
            assertEquals("switch", branchMap.type)
            assertEquals(33, branchMap.line)
            testCodeLocation(branchMap.locations[0], 34, 4, 35, 18)
            testCodeLocation(branchMap.loc, 33, 2, 56, 3)
        }
    }
}


import com.bettblake08.istanbulcc.CodeHighlighter
import com.bettblake08.istanbulcc.IstanbulCoverageReport
import com.bettblake08.istanbulcc.parsers.IstanbulFullCoverageParser
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.TextAttributes
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.io.File

class CodeHighlighterTest {
    private lateinit var editor: Editor
    private lateinit var document: Document
    private lateinit var fileCoverage: IstanbulCoverageReport.FileCoverage

    private val markupModel = mockk<com.intellij.openapi.editor.markup.MarkupModel>()
    private val rangeHighlighter = mockk<com.intellij.openapi.editor.markup.RangeHighlighter>()

    private val validFullCoverageFileResourcePath: String = "fixtures/validFullCoverageReport.json"
    private val testPath: String = "api.js"

    @Before
    fun setUp() {
        editor = mockk()
        document = mockk()

        val coverageFile = File(javaClass.getResource(validFullCoverageFileResourcePath)!!.toURI())
        fileCoverage = IstanbulFullCoverageParser(coverageFile).parseReport().getFileCoverage(testPath)!!

        every { editor.markupModel } returns markupModel
    }

    @Test
    fun `highlightCoverage should add a range highlighter for a covered statement`() {
        every { document.lineCount } returns 1
        every { document.getLineStartOffset(any()) } returns 0
        every { document.getLineEndOffset(any()) } returns 10

        val textAttributes = TextAttributes()
        textAttributes.backgroundColor = CodeHighlighter.COVERAGE_COLOR.STATEMENT.color

        every {
            markupModel.addRangeHighlighter(
                any(),
                any(),
                HighlighterLayer.SYNTAX,
                textAttributes,
                HighlighterTargetArea.EXACT_RANGE
            )
        } returns rangeHighlighter

        CodeHighlighter(editor, document, fileCoverage).highlightCoverage()

        verify {
            markupModel.addRangeHighlighter(
                0,
                10,
                HighlighterLayer.SYNTAX,
                textAttributes,
                HighlighterTargetArea.EXACT_RANGE
            )
        }
    }

    @Test
    fun `highlightCoverage should add a range highlighter for a covered function`() {
        every { document.lineCount } returns 1
        every { document.getLineStartOffset(any()) } returns 0
        every { document.getLineEndOffset(any()) } returns 10

        val textAttributes = TextAttributes()
        textAttributes.backgroundColor = CodeHighlighter.COVERAGE_COLOR.STATEMENT.color

        every {
            markupModel.addRangeHighlighter(
                any(),
                any(),
                HighlighterLayer.SYNTAX,
                textAttributes,
                HighlighterTargetArea.EXACT_RANGE
            )
        } returns rangeHighlighter

        CodeHighlighter(editor, document, fileCoverage).highlightCoverage()

        verify {
            markupModel.addRangeHighlighter(
                0,
                10,
                HighlighterLayer.SYNTAX,
                textAttributes,
                HighlighterTargetArea.EXACT_RANGE
            )
        }
    }

    @Test
    fun `highlightCoverage should add a range highlighter for an uncovered statement`() {
        every { document.lineCount } returns 1
        every { document.getLineStartOffset(any()) } returns 0
        every { document.getLineEndOffset(any()) } returns 10

        val textAttributes = TextAttributes()
        textAttributes.backgroundColor = CodeHighlighter.COVERAGE_COLOR.UNCOVERED.color

        every {
            markupModel.addRangeHighlighter(
                any(),
                any(),
                HighlighterLayer.SYNTAX,
                textAttributes,
                HighlighterTargetArea.EXACT_RANGE
            )
        } returns rangeHighlighter

        CodeHighlighter(editor, document, fileCoverage).highlightCoverage()

        verify {
            markupModel.addRangeHighlighter(
                0,
                10,
                HighlighterLayer.SYNTAX,
                textAttributes,
                HighlighterTargetArea.EXACT_RANGE
            )
        }
    }
}

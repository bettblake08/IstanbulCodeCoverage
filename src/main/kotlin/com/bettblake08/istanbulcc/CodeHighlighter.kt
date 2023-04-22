
import com.bettblake08.istanbulcc.IstanbulCoverageReport
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.ui.JBColor

/**
 * This class represents the coverage highlighting logic for a single file in IntelliJ.
 *
 * @property editor The editor instance for the file.
 * @property document The document instance for the file.
 * @property fileCoverage The FileCoverage object containing coverage data for the file.
 */
class CodeHighlighter(
    private val editor: Editor,
    private val document: Document,
    private val fileCoverage: IstanbulCoverageReport.FileCoverage
) {
    /**
     * This method highlights the lines of code in the file based on the coverage data.
     * If a line, branch, or statement is covered, highlight the code with a green background color.
     * If a line, branch, or statement is not covered, highlight the code with a red background color.
     */
    fun highlightCoverage() {
        val markupModel = editor.markupModel
        val textAttributes = TextAttributes()
        val statementColor = JBColor(0x72c872, 0x398639)
        val branchColor = JBColor(0x94b8ff, 0x3c4f9e)
        val uncoveredColor = JBColor(0xff6666, 0xff6666)

        for (lineNumber in 1..document.lineCount) {
            val statementHits = fileCoverage.s[lineNumber]
            val functionHits = fileCoverage.f[lineNumber]
            val branchHits = fileCoverage.b[lineNumber]

            val lineStartOffset = document.getLineStartOffset(lineNumber - 1)
            val lineEndOffset = document.getLineEndOffset(lineNumber - 1)

            if (statementHits != null && statementHits > 0) {
                textAttributes.backgroundColor = statementColor
                markupModel.addRangeHighlighter(
                    lineStartOffset,
                    lineEndOffset,
                    HighlighterLayer.SYNTAX,
                    textAttributes,
                    HighlighterTargetArea.EXACT_RANGE
                )
            } else if (functionHits != null && functionHits > 0) {
                textAttributes.backgroundColor = statementColor
                markupModel.addRangeHighlighter(
                    lineStartOffset,
                    lineEndOffset,
                    HighlighterLayer.SYNTAX,
                    textAttributes,
                    HighlighterTargetArea.EXACT_RANGE
                )
            } else if (branchHits != null && branchHits.isNotEmpty()) {
                for (i in branchHits.indices) {
                    if (branchHits[i] > 0) {
                        textAttributes.backgroundColor = branchColor
                        markupModel.addRangeHighlighter(
                            lineStartOffset,
                            lineEndOffset,
                            HighlighterLayer.SYNTAX,
                            textAttributes,
                            HighlighterTargetArea.EXACT_RANGE
                        )
                        break
                    }
                }
            } else {
                textAttributes.backgroundColor = uncoveredColor
                markupModel.addRangeHighlighter(
                    lineStartOffset,
                    lineEndOffset,
                    HighlighterLayer.SYNTAX,
                    textAttributes,
                    HighlighterTargetArea.EXACT_RANGE
                )
            }
        }
    }
}

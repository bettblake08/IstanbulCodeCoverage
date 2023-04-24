package com.bettblake08.istanbulcc

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

        for (lineCount in 1..document.lineCount) {
            val lineIndex = lineCount - 1
            val statementHits = fileCoverage.s[lineIndex]
            val functionHits = fileCoverage.f[lineIndex]
            val branchHits = fileCoverage.b[lineIndex]

            val lineStartOffset = document.getLineStartOffset(lineIndex)
            val lineEndOffset = document.getLineEndOffset(lineIndex)

            if ((statementHits != null && statementHits > 0) || (functionHits != null && functionHits > 0)) {
                textAttributes.backgroundColor = COVERAGE_COLOR.STATEMENT.color
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
                        textAttributes.backgroundColor = COVERAGE_COLOR.BRANCH.color
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
                textAttributes.backgroundColor = COVERAGE_COLOR.UNCOVERED.color
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

    enum class COVERAGE_COLOR(val color: JBColor) {
        STATEMENT(JBColor(0x72c872, 0x398639)),
        BRANCH(JBColor(0x72c872, 0x398639)),
        UNCOVERED(JBColor(0x72c872, 0x398639))
    }
}

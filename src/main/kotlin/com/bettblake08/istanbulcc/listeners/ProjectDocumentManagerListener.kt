package com.bettblake08.istanbulcc.listeners

import com.bettblake08.istanbulcc.CodeHighlighter
import com.bettblake08.istanbulcc.parsers.IstanbulFullCoverageParser
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import javax.swing.SwingUtilities

class ProjectDocumentManagerListener : FileDocumentManagerListener {
    private val logger = Logger.getInstance(ProjectDocumentManagerListener::class.java)

    private val coverageFinalFile = "coverage/coverage-final.json"

    override fun fileContentLoaded(file: VirtualFile, document: Document) {
        logger.info("Loading Istanbul Code Coverage lens over file. File: ${file.path}");

        SwingUtilities.invokeLater {
            executeCodeCoverageLens(file, document)
        }
    }

    private fun executeCodeCoverageLens(file: VirtualFile, document: Document) {
        val project = ProjectManager.getInstance().openProjects.firstOrNull { project -> project.isOpen }

        if (project === null || project.basePath == null) {
            logger.error("Unable to load an active project");
            return
        }

        logger.debug("Confirmed the base path to be ${project.basePath}")

        val path = "${project.basePath}/${coverageFinalFile}"
        logger.info("Loading code coverage file from $path")

        // Read the coverage file and perform any necessary processing
        val coverageReport = IstanbulFullCoverageParser(File(path)).parseReport()

        // Get the editor for the loaded file
        val fileEditorManager = FileEditorManager.getInstance(project)
        val coverageMap = coverageReport.getCoverageMap()

        if (fileEditorManager.selectedTextEditor != null) {
            coverageMap[file.path]?.let {
                CodeHighlighter(
                    fileEditorManager.selectedTextEditor!!,
                    document,
                    it
                ).highlightCoverage()
            }
        }
    }
}

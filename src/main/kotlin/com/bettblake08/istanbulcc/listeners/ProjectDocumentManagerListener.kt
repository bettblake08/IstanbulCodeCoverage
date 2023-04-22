package com.bettblake08.istanbulcc.listeners

import CodeHighlighter
import com.bettblake08.istanbulcc.parsers.IstanbulFullCoverageParser
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

class ProjectDocumentManagerListener : FileDocumentManagerListener {
    private val logger = Logger.getInstance(ProjectDocumentManagerListener::class.java)

    private val coverageFinalFile = "coverage/coverage-final.json"

    override fun fileContentLoaded(file: VirtualFile, document: com.intellij.openapi.editor.Document) {
        logger.info("Loading Istanbul Code Coverage lens over file. File: ${file.path}");

        val projects: Array<Project> = ProjectManager.getInstance().openProjects
        for (project in projects) {
            if (project.basePath != null) {
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
    }
}

package com.github.valentine456.synonymsplugin.actions

import com.github.valentine456.synonymsplugin.services.setUpThesaurusService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class ShowSynonymAction : AnAction() {
    @Override
    override fun actionPerformed(event: AnActionEvent) {
        val editor: Editor = event.getRequiredData(CommonDataKeys.EDITOR)
        val project: Project = event.getRequiredData(CommonDataKeys.PROJECT)
        val document = editor.document
        val selectionModel = editor.selectionModel

        val selectedText = selectionModel.selectedText ?: return
        val identifier = selectedText.lowercase()

        val myService = setUpThesaurusService(project)
        val synonyms = arrayListOf(identifier) + myService.getSynonyms(identifier)

        if (synonyms.isNotEmpty()) {
            val chosenItem = Messages.showEditableChooseDialog(
                "Select an option:",
                "Choose Synonym",
                Messages.getInformationIcon(),
                synonyms.toTypedArray(),
                synonyms.first(),
                null
            )

            chosenItem?.let {
                WriteCommandAction.runWriteCommandAction(project) {
                    document.replaceString(selectionModel.selectionStart, selectionModel.selectionEnd, it)
                }
                selectionModel.removeSelection()
            }
        }

    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val editor = event.getData(CommonDataKeys.EDITOR)

        event.presentation.isEnabledAndVisible =
            project != null && editor != null && editor.selectionModel.hasSelection()
    }
}
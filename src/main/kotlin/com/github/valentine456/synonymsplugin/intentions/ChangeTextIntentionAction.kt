package com.github.valentine456.synonymsplugin.intentions

import com.github.valentine456.synonymsplugin.services.setUpThesaurusService
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile
import org.jetbrains.annotations.Nls


class ChangeTextIntentionAction : IntentionAction {

    override fun getText(): @Nls(capitalization = Nls.Capitalization.Sentence) String {
        return "Change for Synonym" // Displayed text for the intention action
    }

    override fun getFamilyName(): @Nls(capitalization = Nls.Capitalization.Sentence) String {
        return "Text Actions" // Grouping label for similar intention actions
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        if (editor != null && file != null) {
            return editor.selectionModel.hasSelection()
        }
        return false
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile?) {
        val selectedText = editor.selectionModel.selectedText ?: return

        val myService = setUpThesaurusService(editor.project!!)
        val identifier = selectedText.lowercase()
        val synonyms = arrayListOf(identifier) + myService.getSynonyms(identifier)

        ApplicationManager.getApplication().invokeLater {
            checkAndPerformAction(editor, project, synonyms)
        }
    }

    private fun checkAndPerformAction(editor: Editor, project: Project, synonyms: List<String>) {
        val chosenItem = Messages.showEditableChooseDialog(
            "Select an option:",
            "Choose Option",
            Messages.getInformationIcon(),
            synonyms.toTypedArray(),
            synonyms.first(),
            null
        )

        chosenItem?.let {
            WriteCommandAction.runWriteCommandAction(project) {
                editor.document.replaceString(
                    editor.selectionModel.selectionStart, editor.selectionModel.selectionEnd, chosenItem
                )
            }
        }
    }

    override fun startInWriteAction(): Boolean {
        return true // Set to true if your action requires write access to PSI elements
    }
}

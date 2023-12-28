package com.github.valentine456.synonymsplugin.intentions

import com.github.valentine456.synonymsplugin.services.*
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
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
        val document = editor.document
        val selectionModel = editor.selectionModel
        val selectedText = editor.selectionModel.selectedText ?: return

        val myService = setUpThesaurusService(project)
        val identifier = selectedText.lowercase()
        val synonyms = arrayListOf(identifier) + myService.getSynonyms(identifier)
        val chosenItem = Messages.showEditableChooseDialog(
            "Select an option:",
            "Choose Option",
            Messages.getInformationIcon(),
            synonyms.toTypedArray(),
            synonyms.first(),
            null
        )
        chosenItem?.let {
            editor.document.replaceString(selectionModel.selectionStart, selectionModel.selectionEnd, chosenItem)
        }
    }

    override fun startInWriteAction(): Boolean {
        return true // Set to true if your action requires write access to PSI elements
    }

    private fun setUpThesaurusService(project: Project): ThesaurusService {
        val myService: ThesaurusService = ServiceManager.getService(project, FileThesaurusService::class.java)
        val myService2: ThesaurusService = ServiceManager.getService(project, BigHugeThesaurusService::class.java)
        val myService3: ThesaurusService = ServiceManager.getService(project, ApiNinjaThesaurusService::class.java)
        val myService4: ThesaurusService = ServiceManager.getService(project, MockThesaurusService::class.java)

        myService.nextService = myService2
        myService2.nextService = myService3
        myService3.nextService = myService4

        return myService
    }
}

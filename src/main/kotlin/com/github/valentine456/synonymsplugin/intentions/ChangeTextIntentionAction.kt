package com.github.valentine456.synonymsplugin.intentions

import com.github.valentine456.synonymsplugin.services.ApiNinjaThesaurusService
import com.github.valentine456.synonymsplugin.services.BigHugeThesaurusService
import com.github.valentine456.synonymsplugin.services.MockThesaurusService
import com.github.valentine456.synonymsplugin.services.ThesaurusService
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.components.ServiceManager
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
        return editor != null && file != null // Enable the intention action when there's an editor and file
    }

    override fun invoke(project: Project, editor: Editor, file: PsiFile?) {
        val document = editor.document
        val selectionModel = editor.selectionModel

        if (selectionModel.hasSelection()) {
            val selectedText = selectionModel.selectedText
            val myService = setUpThesaurusService(project)

            val chosenItem = Messages.showEditableChooseDialog(
                "Select an option:",
                "Choose Option",
                Messages.getInformationIcon(),
                myService.getSynonyms(selectedText!!).toTypedArray(),
                myService.getSynonyms(selectedText).firstOrNull(),
                null
            )
            // Replace with your desired fixed string
            document.replaceString(selectionModel.selectionStart, selectionModel.selectionEnd, chosenItem!!)
        }
    }

    override fun startInWriteAction(): Boolean {
        return true // Set to true if your action requires write access to PSI elements
    }

    private fun setUpThesaurusService(project: Project): ThesaurusService {
        val myService: ThesaurusService = ServiceManager.getService(project, BigHugeThesaurusService::class.java)
        val myService2 = ApiNinjaThesaurusService()
        val myService3 = MockThesaurusService()

        myService.nextService = myService2
        myService2.nextService = myService3

        return myService
    }
}

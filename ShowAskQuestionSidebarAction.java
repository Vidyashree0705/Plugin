package com.example.ncode;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.actionSystem.CommonDataKeys;

public class ShowAskQuestionSidebarAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // Get the current project
        Project project = e.getProject();
        if (project != null) {
            // Get the active editor using CommonDataKeys.EDITOR
            Editor editor = e.getData(CommonDataKeys.EDITOR); // Corrected line
            if (editor != null) {
                // Get the selected text
                SelectionModel selectionModel = editor.getSelectionModel();
                String selectedText = selectionModel.getSelectedText();

                // Open the tool window with the selected text
                AskQuestionToolWindow.openToolWindow(project, selectedText != null ? selectedText : "No text selected");
            }
        }
    }
}

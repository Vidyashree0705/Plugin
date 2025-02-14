package com.example.ncode;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AskQuestionToolWindow {

    private static JTextArea selectedTextArea;
    private static JLabel responseLabel;
    private static JTextArea questionTextArea;
    private static String currentSelectedText = "";

    public static void openToolWindow(@NotNull Project project, String selectedText) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            ToolWindow toolWindow = toolWindowManager.getToolWindow("AskQuestionToolWindow");

            if (toolWindow == null) {
                toolWindow = toolWindowManager.registerToolWindow(
                        "AskQuestionToolWindow",
                        true,
                        ToolWindowAnchor.LEFT,
                        true
                );

                JPanel sidebarPanel = createSidebarPanel(selectedText);
                Content content = ContentFactory.SERVICE.getInstance().createContent(sidebarPanel, "", false);
                toolWindow.getContentManager().addContent(content);
            } else {
                // Update the selected text dynamically
                updateSelectedText(selectedText);
            }

            toolWindow.show();
        });
    }

    private static JPanel createSidebarPanel(String selectedText) {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebarPanel.setBackground(Color.BLACK);

        // Selected Text Title
        JLabel selectedTextTitle = new JLabel("Selected Code:");
        selectedTextTitle.setFont(new Font("Arial",Font.PLAIN, 12));  // Reduced font size
        selectedTextTitle.setForeground(Color.WHITE);
        selectedTextTitle.setAlignmentX(Component.LEFT_ALIGNMENT);  // Left align
        sidebarPanel.add(selectedTextTitle);
        sidebarPanel.add(Box.createVerticalStrut(10));

        // Selected Text Box (Now further reduced in height)
        selectedTextArea = new JTextArea(selectedText);
        selectedTextArea.setEditable(false);
        selectedTextArea.setWrapStyleWord(true);
        selectedTextArea.setLineWrap(true);
        selectedTextArea.setFont(new Font("Arial", Font.PLAIN, 12));  // Smaller font
        selectedTextArea.setBackground(Color.DARK_GRAY);
        selectedTextArea.setForeground(Color.WHITE);

        JScrollPane selectedTextScrollPane = new JScrollPane(selectedTextArea);
        selectedTextScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        selectedTextScrollPane.setPreferredSize(new Dimension(280, 30));  // Reduced height to 30

        sidebarPanel.add(selectedTextScrollPane);

        // Small vertical space
        sidebarPanel.add(Box.createVerticalStrut(5));

        // Question Text Area (flexible height)
        questionTextArea = new JTextArea(3, 20);  // Initial height set to 3 rows
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setLineWrap(true);
        questionTextArea.setFont(new Font("Arial", Font.PLAIN, 12));  // Smaller font
        questionTextArea.setBackground(Color.BLACK);
        questionTextArea.setForeground(Color.WHITE);

        // Set the placeholder text
        String placeholderText = "Type your question here...";
        questionTextArea.setText(placeholderText);
        questionTextArea.setForeground(Color.GRAY);

        // Add focus listener to remove placeholder text when focus is gained
        questionTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (questionTextArea.getText().equals(placeholderText)) {
                    questionTextArea.setText("");
                    questionTextArea.setForeground(Color.WHITE);  // Set text color to white when typing
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (questionTextArea.getText().trim().isEmpty()) {
                    questionTextArea.setText(placeholderText);
                    questionTextArea.setForeground(Color.GRAY);  // Reset text color to gray for placeholder
                }
            }
        });

        JScrollPane questionTextScrollPane = new JScrollPane(questionTextArea);
        questionTextScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        questionTextScrollPane.setPreferredSize(new Dimension(280, 60));  // Keep fixed height for initial rows

        sidebarPanel.add(questionTextScrollPane);

        // More space between question box and submit button
        sidebarPanel.add(Box.createVerticalStrut(15)); // Increased vertical space

        // Submit Button (no fixed size, let it shrink to content)
        JButton enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Arial", Font.PLAIN, 12));  // Smaller font
        enterButton.setBackground(Color.DARK_GRAY);
        enterButton.setForeground(Color.WHITE);
        enterButton.setAlignmentX(Component.LEFT_ALIGNMENT);  // Left align
        enterButton.addActionListener(e -> {
            String userQuestion = questionTextArea.getText().trim();
            if (userQuestion.isEmpty() || userQuestion.equals(placeholderText)) {
                JOptionPane.showMessageDialog(null, "Please type a question.", "Warning", JOptionPane.WARNING_MESSAGE);
                responseLabel.setText("");
            } else {
                String response = generateAIResponse(selectedText, userQuestion);
                responseLabel.setText("<html>" + response.replaceAll("\n", "<br>") + "</html>");
            }
        });

        sidebarPanel.add(enterButton);

        // More space before response label
        sidebarPanel.add(Box.createVerticalStrut(10));

        // Response Label (flexible height)
        responseLabel = new JLabel("Your answer will appear here...");
        responseLabel.setForeground(Color.WHITE);
        responseLabel.setFont(new Font("Arial", Font.PLAIN, 12));  // Smaller font
        sidebarPanel.add(responseLabel);

        return sidebarPanel;
    }

    private static String generateAIResponse(String selectedCode, String userQuestion) {
        return "This is a sample response for the question: " + userQuestion;
    }

    // Update the selected text in the sidebar when new lines are selected
    private static void updateSelectedText(String newSelectedText) {
        if (newSelectedText != null && !newSelectedText.equals(currentSelectedText)) {
            currentSelectedText = newSelectedText;
            if (selectedTextArea != null) {
                selectedTextArea.setText(currentSelectedText);
            }
        }
    }
}

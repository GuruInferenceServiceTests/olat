package org.olat.test.util.selenium.olatapi.course.editor;

import com.thoughtworks.selenium.Selenium;

public class QuestionnaireElementEditor extends CourseElementEditor {

    public QuestionnaireElementEditor(Selenium selenium) {
        super(selenium);
        // Check that we're on the right place
        if (!selenium.isElementPresent("ui=courseEditor::content_bbQuestionnaire_tabQuestionnaireConfiguration()")) {
            throw new IllegalStateException("This is not the - Questionnaire configuration - page");
        }
    }

    private void selectTabQuestionnaireConfiguration() {
        if (selenium.isElementPresent("ui=courseEditor::content_bbQuestionnaire_tabQuestionnaireConfiguration()")) {
            selenium.click("ui=courseEditor::content_bbQuestionnaire_tabQuestionnaireConfiguration()");
            selenium.waitForPageToLoad("30000");
        }
    }

    public void chooseMyFile(String testTitle) {
        selectTabQuestionnaireConfiguration();

        selenium.click("ui=courseEditor::content_bbQuestionnaire_chooseFile()");
        selenium.waitForPageToLoad("30000");
        selenium.click("ui=courseEditor::commons_chooseLr_myEntries()");
        selenium.waitForPageToLoad("30000");
        selenium.click("ui=courseEditor::commons_chooseLr_chooseQuestionnaire(nameOfQuestionnaire=" + testTitle + ")");
        selenium.waitForPageToLoad("30000");
    }

    public void configureQuestionnaireLayout(Boolean allowMenuNavigation, Boolean showMenuNavigation, Boolean allowCancel, Boolean allowSuspend) {
        selectTabQuestionnaireConfiguration();

        // TODO:LD: the XPATHs for test/survey/questionnaire are simply unnecessary replicated, remove the copies
        if (showMenuNavigation != null && showMenuNavigation && !selenium.isChecked("ui=courseEditor::content_bbTest_layoutParameters_showMenuNavigation()")) {
            selenium.click("ui=courseEditor::content_bbTest_layoutParameters_showMenuNavigation()");
        } else if (showMenuNavigation != null && !showMenuNavigation && selenium.isChecked("ui=courseEditor::content_bbTest_layoutParameters_showMenuNavigation()")) {
            selenium.uncheck("ui=courseEditor::content_bbTest_layoutParameters_showMenuNavigation()");
        }

        if (allowMenuNavigation != null && allowMenuNavigation && !selenium.isChecked("ui=courseEditor::content_bbTest_layoutParameters_allowMenuNavigation()")) {
            // check
            selenium.click("ui=courseEditor::content_bbTest_layoutParameters_allowMenuNavigation()");
        } else if (allowMenuNavigation != null && !allowMenuNavigation && selenium.isChecked("ui=courseEditor::content_bbTest_layoutParameters_allowMenuNavigation()")) {
            // uncheck
            selenium.uncheck("ui=courseEditor::content_bbTest_layoutParameters_allowMenuNavigation()");
        }

        if (allowCancel != null && allowCancel && !selenium.isChecked("ui=courseEditor::content_bbTest_layoutParameters_allowCancel()")) {
            selenium.click("ui=courseEditor::content_bbTest_layoutParameters_allowCancel()");
        } else if (allowCancel != null && !allowCancel && selenium.isChecked("ui=courseEditor::content_bbTest_layoutParameters_allowCancel()")) {
            selenium.uncheck("ui=courseEditor::content_bbTest_layoutParameters_allowCancel()");
        }

        if (allowSuspend != null && allowSuspend && !selenium.isChecked("ui=courseEditor::content_bbTest_layoutParameters_allowSuspend()")) {
            selenium.click("ui=courseEditor::content_bbTest_layoutParameters_allowSuspend()");
        } else if (allowSuspend != null && !allowSuspend && selenium.isChecked("ui=courseEditor::content_bbTest_layoutParameters_allowSuspend()")) {
            selenium.uncheck("ui=courseEditor::content_bbTest_layoutParameters_allowSuspend()");
        }
        selenium.click("ui=commons::flexiForm_saveButton()");
    }
}

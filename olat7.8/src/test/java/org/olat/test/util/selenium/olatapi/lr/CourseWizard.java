package org.olat.test.util.selenium.olatapi.lr;

import org.olat.test.util.selenium.olatapi.OLATSeleniumWrapper;
import org.olat.test.util.selenium.olatapi.course.run.CourseRun;

import com.thoughtworks.selenium.Selenium;

public class CourseWizard extends OLATSeleniumWrapper {

    /**
     * Default constructor
     * 
     * @param selenium
     */

    public CourseWizard(Selenium selenium) {
        super(selenium);
    }

    /**
     * Select course elements that should be created. Possible elements are
     * 
     * @param singlePage
     *            A Single Page with some text
     * @param enrollment
     *            An Enrollment
     * @param downloadFolder
     *            A download Folder
     * @param forum
     *            a forum
     * @param contactForm
     *            a contact form
     */

    public void selectCourseElements(boolean singlePage, boolean enrollment, boolean downloadFolder, boolean forum, boolean contactForm) {
        if (singlePage) {
            selenium.click("ui=learningResources::courseWizard_createSinglePage()");
        }
        if (enrollment) {
            selenium.click("ui=learningResources::courseWizard_createEnrollment()");
        }
        if (downloadFolder) {
            selenium.click("ui=learningResources::courseWizard_createDownloadFolder()");
        }
        if (forum) {
            selenium.click("ui=learningResources::courseWizard_createForum()");
        }
        if (contactForm) {
            selenium.click("ui=learningResources::courseWizard_createContactForm()");
        }
    }

    /**
     * Edit the enrollment properties. Select all CBBs that should be affected by this changes
     * 
     * @param accessLimit
     *            whether the selected CBBs should be hidden if not enrolled
     * @param singlePage
     *            whether the single page should be hidden
     * @param downloadFolder
     *            whether the download folder should be hidden
     * @param forum
     *            whether the forum should be hidden
     * @param contactForm
     *            whether the contact form should be hidden
     */

    public void editEnrollment(boolean accessLimit, boolean singlePage, boolean downloadFolder, boolean forum, boolean contactForm) {
        selenium.click("ui=learningResources::courseWizard_editEnrollmentLink()");
        selenium.waitForPageToLoad("30000");
        if (accessLimit) {
            selenium.click("ui=learningResources::courseWizard_editEnrollment_accessLimit()");
            if (singlePage) {
                selenium.click("ui=learningResources::courseWizard_editEnrollment_selectSP()");
            }
            if (downloadFolder) {
                selenium.click("ui=learningResources::courseWizard_editEnrollment_selectBC()");
            }
            if (forum) {
                selenium.click("ui=learningResources::courseWizard_editEnrollment_selectFO()");
            }
            if (contactForm) {
                selenium.click("ui=learningResources::courseWizard_editEnrollment_selectCO()");
            }
        }
        selenium.click("ui=commons::flexiForm_saveButton()");
    }

    /**
     * Create the course and run it, providing access with @param publishLabel
     * 
     * @param publishLabel
     *            The label to use for access
     * @return A CourseRun from the created course
     */

    public CourseRun createCourseAndRun(String publishLabel) {
        selenium.click("ui=courseEditor::publishDialog_next()");
        selenium.waitForPageToLoad("30000");
        // skip catalog entry
        selenium.click("ui=learningResources::courseWizard_catalogRoot()");
        selenium.click("ui=courseEditor::publishDialog_next()");
        selenium.waitForPageToLoad("30000");
        selenium.select("ui=courseEditor::publishDialog_courseAccessDropDown()", "label=" + publishLabel);
        // TODO: LD: add here check if next selectable, else finish
        if (selenium.isElementPresent("ui=courseEditor::publishDialog_next()")) {
            selenium.click("ui=courseEditor::publishDialog_next()");
            selenium.waitForPageToLoad("30000");
        }
        selenium.click("ui=courseEditor::publishDialog_finish()");
        selenium.waitForPageToLoad("30000");
        return new CourseRun(selenium);

    }

    /**
     * Create the course and run it, providing access with default ("All registered OLAT users") publish Label
     * 
     * @return The resulting CourseRun object
     */

    public CourseRun createCourseAndRun() {
        return createCourseAndRun("All registered OLAT users");
    }

}

package org.olat.test.functional.codepoints.cluster;

import org.junit.Ignore;
import org.olat.test.util.selenium.BaseSeleneseTestCase;
import org.olat.test.util.setup.OlatLoginInfos;
import org.olat.test.util.setup.SetupType;
import org.olat.test.util.setup.context.Context;
import org.olat.testutils.codepoints.client.CodepointClient;
import org.olat.testutils.codepoints.client.CodepointRef;

@Ignore
public class MultiBrowserClusterNewLearningAreaTstDisabled3440 extends BaseSeleneseTestCase {

    protected com.thoughtworks.selenium.Selenium selenium1;
    protected com.thoughtworks.selenium.Selenium selenium2;

    public void testMultiBrowserClusterNewLearningArea() throws Exception {
        Context context = Context.setupContext(getFullName(), SetupType.TWO_NODE_CLUSTER);

        OlatLoginInfos user1 = context.createuserIfNotExists(1, "mbcnla1", "passwd08", true, true, true, true, true);
        OlatLoginInfos user2 = context.createuserIfNotExists(2, "mbcnla2", "passwd08", true, true, true, true, true);

        {
            System.out.println("logging in browser 1...");
            selenium1 = context.createSeleniumAndLogin(user1);
            selenium1.click("ui=tabs::learningResources()");
            selenium1.waitForPageToLoad("30000");
            selenium1.click("ui=learningResources::menu_searchForm()");
            selenium1.waitForPageToLoad("30000");
            selenium1.type("ui=learningResources::content_searchForm_titleField()", "Demo course wiki");
            selenium1.click("ui=learningResources::content_searchForm_search()");
            selenium1.waitForPageToLoad("30000");
            selenium1.click("ui=learningResources::content_clickCourseEntry(nameOfCourse=Demo course wiki)");
            selenium1.waitForPageToLoad("30000");
            selenium1.click("ui=learningResources::content_showContent()");
            selenium1.waitForPageToLoad("30000");

            selenium1.click("ui=course::toolbox_courseTools_groupManagement()");
            selenium1.waitForPageToLoad("30000");

            // make sure the learning area does not exist yet - delete otherwise
            selenium1.click("ui=groupManagement::menu_allLearningAreas()");
            selenium1.waitForPageToLoad("30000");
            if (selenium1.isElementPresent("ui=groupManagement::content_learningAreaTable_deleteLearningArea(nameOfLearningArea=multibrowserclusterlearningarea)")) {
                selenium1.click("ui=groupManagement::content_learningAreaTable_deleteLearningArea(nameOfLearningArea=multibrowserclusterlearningarea)");
                selenium1.waitForPageToLoad("30000");
                selenium1.click("ui=dialog::Yes()");
                selenium1.waitForPageToLoad("30000");
            }

            selenium1.click("ui=groupManagement::toolbox_groupManagement_newLearningArea()");
            selenium1.waitForPageToLoad("30000");

            assertTrue(selenium1.isTextPresent("Create a new learning area"));

            selenium1.type("ui=groupManagement::toolbox_groupManagement_formNewLearningArea_name()", "multibrowserclusterlearningarea");
            selenium1.type("ui=groupManagement::toolbox_groupManagement_formNewLearningArea_description()", "egal oder?");
        }

        {
            System.out.println("logging in browser 2...");
            selenium2 = context.createSeleniumAndLogin(user2);
            selenium2.click("ui=tabs::learningResources()");
            selenium2.waitForPageToLoad("30000");
            selenium2.click("ui=learningResources::menu_searchForm()");
            selenium2.waitForPageToLoad("30000");
            selenium2.type("ui=learningResources::content_searchForm_titleField()", "Demo course wiki");
            selenium2.click("ui=learningResources::content_searchForm_search()");
            selenium2.waitForPageToLoad("30000");
            selenium2.click("ui=learningResources::content_clickCourseEntry(nameOfCourse=Demo course wiki)");
            selenium2.waitForPageToLoad("30000");
            selenium2.click("ui=learningResources::content_showContent()");
            selenium2.waitForPageToLoad("30000");

            selenium2.click("ui=course::toolbox_courseTools_groupManagement()");
            selenium2.waitForPageToLoad("30000");

            selenium2.click("ui=groupManagement::toolbox_groupManagement_newLearningArea()");
            selenium2.waitForPageToLoad("30000");

            assertTrue(selenium2.isTextPresent("Create a new learning area"));
            selenium2.type("ui=groupManagement::toolbox_groupManagement_formNewLearningArea_name()", "multibrowserclusterlearningarea");
            selenium2.type("ui=groupManagement::toolbox_groupManagement_formNewLearningArea_description()", "egal oder?");
        }

        CodepointClient codepointClientA = context.createCodepointClient(1);
        CodepointRef createAreaCpA = codepointClientA.getCodepoint("org.olat.lms.group.ui.NewAreaController.createArea");
        createAreaCpA.setHitCount(0);
        createAreaCpA.enableBreakpoint();

        CodepointClient codepointClientB = context.createCodepointClient(2);
        CodepointRef createAreaCpB = codepointClientB.getCodepoint("org.olat.lms.group.ui.NewAreaController.createArea");
        createAreaCpB.setHitCount(0);
        createAreaCpB.enableBreakpoint();

        selenium1.click("ui=groupManagement::toolbox_groupManagement_formNewLearningArea_save()");
        selenium2.click("ui=groupManagement::toolbox_groupManagement_formNewLearningArea_save()");

        createAreaCpA.assertBreakpointReached(1, 10000);
        createAreaCpB.assertBreakpointReached(1, 10000);

        createAreaCpA.disableBreakpoint(true);
        createAreaCpB.disableBreakpoint(true);

        selenium1.waitForPageToLoad("30000");
        selenium2.waitForPageToLoad("30000");

        assertTrue("Not found in selenium 1: Edit learning area multibrowserclusterlearningarea",
                selenium1.isTextPresent("Edit learning area multibrowserclusterlearningarea"));
        assertTrue("Not found in selenium 2: The name of this learning area is already used in this group management, please select another.",
                selenium2.isTextPresent("The name of this learning area is already used in this group management, please select another."));
    }
}

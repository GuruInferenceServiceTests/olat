package org.olat.test.functional.usermanagement;

import org.olat.test.util.selenium.BaseSeleneseTestCase;
import org.olat.test.util.selenium.OlatLoginHelper;
import org.olat.test.util.selenium.olatapi.OLATWorkflowHelper;
import org.olat.test.util.selenium.olatapi.user.UserManagement;
import org.olat.test.util.setup.SetupType;
import org.olat.test.util.setup.context.Context;

/**
 * 
 * Test if deleted user cannot login anymore. <br/>
 * Test setup: <br/>
 * -
 * <p>
 * Test case: <br/>
 * 1. Administrator opens user management. <br/>
 * 2. Administrator creates user USER_NAME. <br/>
 * 3. User USER_NAME logs in and out. <br/>
 * 4. Administrator deletes user USER_NAME and check that user cannot be found anymore. <br/>
 * 5. USER_NAME asserts that he cannot login anymore. <br/>
 * 
 * @author sandra
 * 
 */

public class DeleteUserTest extends BaseSeleneseTestCase {

    private final String USER_NAME = "usertodelete" + System.currentTimeMillis();
    private final String USER_FNAME = "First";
    private final String USER_LNAME = "Last";
    private final String USER_EMAIL = System.currentTimeMillis() + "@user.com";
    private final String USER_PW = "olat_3NEW"; // must be at least 8 chars long

    public void testDeleteUser() throws Exception {
        Context context = Context.setupContext(getFullName(), SetupType.SINGLE_VM);

        OLATWorkflowHelper olatWorkflowAdmin = context.getOLATWorkflowHelper(context.getStandardAdminOlatLoginInfos());
        UserManagement userManagement = olatWorkflowAdmin.getUserManagement();

        // create user
        userManagement.createUser(USER_NAME, USER_FNAME, USER_LNAME, USER_EMAIL, USER_PW);

        // log in user
        OLATWorkflowHelper olatWorkflowUserToDelete = context.getOLATWorkflowHelper(context.getOlatLoginInfo(1, USER_NAME, USER_PW));
        olatWorkflowUserToDelete.getHome();
        olatWorkflowUserToDelete.logout();

        // admin deletes user and check that user cannot be found anymore
        assertTrue(userManagement.searchUser(USER_NAME));
        boolean userDeleted = userManagement.deleteUserImmediately(USER_NAME);
        Thread.sleep(5000);
        if (userDeleted) {
            assertFalse("Asserts that no user is found with this username: " + USER_NAME, userManagement.searchUser(USER_NAME));

            // check that user cannot login any more
            assertTrue("Asserts that the USER_NAME cannot login anymore!", OlatLoginHelper.loginExpectingError(1, USER_NAME, USER_PW));
        } else {
            System.out.println("Could not delete user since the delete user workflow is locked!");// e.g. if running against OLATNG
        }
        olatWorkflowAdmin.logout();
    }
}

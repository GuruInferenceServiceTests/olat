/**
 * OLAT - Online Learning and Training<br>
 * http://www.olat.org
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Copyright (c) 1999-2007 at Multimedia- & E-Learning Services (MELS),<br>
 * University of Zurich, Switzerland.
 * <p>
 */
package org.olat.test.util.selenium.olatapi.course.run;

import org.olat.test.util.selenium.olatapi.OLATSeleniumWrapper;

import com.thoughtworks.selenium.Selenium;

/**
 * This is the SCORM run page.
 * 
 * @author Lavinia Dumitrescu
 * 
 */
public class SCORM extends OLATSeleniumWrapper {

    /**
     * @param selenium
     */
    public SCORM(Selenium selenium) {
        super(selenium);

        // Check that we're on the right place
        if (!selenium.isElementPresent("ui=course::content_scorm_scormPreview()")) {
            throw new IllegalStateException("This is not the - SCORM run - page");
        }
    }

    public void showSCORMLearningContent() {
        selenium.click("ui=course::content_scorm_scormPreview()");
        selenium.waitForPageToLoad("30000");
    }

    public CourseRun back() throws Exception {
        selenium.click("ui=course::content_scorm_back()");
        Thread.sleep(500);
        selenium.waitForPageToLoad("30000");
        return new CourseRun(selenium);
    }
}

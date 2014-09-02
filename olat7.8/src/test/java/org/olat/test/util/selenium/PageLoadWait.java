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
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Copyright (c) since 2004 at Multimedia- & E-Learning Services (MELS),<br>
 * University of Zurich, Switzerland.
 * <p>
 */
package org.olat.test.util.selenium;

/**
 * Defines different page load waiting times in ms and allows nowait time. The facility "Not waiting" for page load was introduced in conjunction with codepoints. See the
 * following example
 * 
 * <pre>
 * // trigger &quot;Activation Content&quot; which in turn loads each of its children for displaying the Previews -&gt; Changed behavior of test!
 * StructureElement selectActivation = courseRun.selectAnyButGetToRoot(&quot;Activation Interaction&quot;, PageLoadWait.NO_WAIT);
 * selenium = selectActivation.getSelenium();
 * 
 * // ASSERTION check if codepoint reached, if yes continue
 * beforeSyncCp_A.assertBreakpointReached(1, 10000);
 * System.out.println(&quot;beforeSyncCp_A.assertBreakpointReached&quot;);
 * TemporaryPausedThread[] threadsA = beforeSyncCp_A.getPausedThreads();
 * threadsA[0].continueThread();
 * 
 * doInSyncCp_A.assertBreakpointReached(1, 10000);
 * System.out.println(&quot;doInSyncCp_A.assertBreakpointReached&quot;);
 * threadsA = doInSyncCp_A.getPausedThreads(); // overwrite threadsA
 * threadsA[0].continueThread();
 * 
 * // activate actual &quot;Forum&quot; content for proceeding
 * selenium.waitForPageToLoad(&quot;30000&quot;);// wait for previous NO_WAITED Action
 * courseRun.selectForum(CourseEditor.FORUM_COURSE_ELEM_TITLE);
 * </pre>
 * <P>
 * Initial Date: Apr 29, 2011 <br>
 * 
 * @author patrick
 */
public enum PageLoadWait {

    LONG("60000"), DEFAULT("30000"), SHORT("5000"), NO_WAIT("0");

    // ms are specified via String to Selenium
    private String ms;

    PageLoadWait(String msValue) {
        this.ms = msValue;
    }

    public String getMs() {
        return ms;
    }

}

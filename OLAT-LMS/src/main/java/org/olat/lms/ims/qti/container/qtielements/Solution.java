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

package org.olat.lms.ims.qti.container.qtielements;

import org.dom4j.Element;
import org.olat.system.exception.AssertException;

/**
 * Initial Date: 24.11.2004
 * 
 * @author Mike Stock
 */
public class Solution extends GenericQTIElement {

    /**
     * Comment for <code>xmlClass</code>
     */
    public static final String xmlClass = "solution";

    /**
     * Comment for <code>FEEDBACKSTYLE_COMPLETE</code>
     */
    public static final int FEEDBACKSTYLE_COMPLETE = 0;
    /**
     * Comment for <code>FEEDBACKSTYLE_INCREMENTAL</code>
     */
    public static final int FEEDBACKSTYLE_INCREMENTAL = 1;
    /**
     * Comment for <code>FEEDBACKSTYLE_MULTILEVEL</code>
     */
    public static final int FEEDBACKSTYLE_MULTILEVEL = 2;

    private int style = FEEDBACKSTYLE_COMPLETE;

    /**
     * @param el_solution
     */
    public Solution(final Element el_solution) {
        super(el_solution);
        final String fbstyle = el_solution.attributeValue("feedbackstyle");
        if (fbstyle != null) {
            if (fbstyle.equals("Complete")) {
                style = FEEDBACKSTYLE_COMPLETE;
            } else if (fbstyle.equals("Incremental")) {
                style = FEEDBACKSTYLE_INCREMENTAL;
            } else if (fbstyle.equals("Multilevel")) {
                style = FEEDBACKSTYLE_MULTILEVEL;
            } else {
                throw new AssertException("Invalid feedbackstyle in solution element " + getIdent() + ": " + fbstyle);
            }
        }
    }

    /**
     * @return feedbackstyle
     */
    public int getFeedbackstyle() {
        return style;
    }

}

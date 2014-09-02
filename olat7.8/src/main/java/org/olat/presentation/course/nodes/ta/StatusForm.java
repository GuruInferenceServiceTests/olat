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

package org.olat.presentation.course.nodes.ta;

import org.olat.lms.course.nodes.ta.StatusManager;
import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.components.form.flexible.FormItemContainer;
import org.olat.presentation.framework.core.components.form.flexible.elements.SingleSelection;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormBasicController;
import org.olat.presentation.framework.core.control.Controller;
import org.olat.presentation.framework.core.control.WindowControl;
import org.olat.system.event.Event;

/**
 * Task status form. A tutor can define the status (ok,not ok, working on) of task.
 * 
 * @author Christian Guretzki
 */
public class StatusForm extends FormBasicController {

    protected static final String FORMNAME = "statusform";

    /** Name of the form-element SingleSelectionElement. */
    protected static final String STATUS_SELECTION = "status_selection";

    // This status values will be stored in the db.
    // Do not change it, if you have already persistent data.
    private static final String STATUS_VALUE_NOT_OK = "not_ok";
    private static final String STATUS_VALUE_OK = "ok";
    private static final String STATUS_VALUE_WORKING_ON = "working_on";

    // Keys to access Locale.properties file
    public static final String STATUS_LOCALE_PROPERTY_PREFIX = "status.";
    private static final String PROPERTY_KEY_NOT_OK = STATUS_LOCALE_PROPERTY_PREFIX + STATUS_VALUE_NOT_OK;
    private static final String PROPERTY_KEY_OK = STATUS_LOCALE_PROPERTY_PREFIX + STATUS_VALUE_OK;
    private static final String PROPERTY_KEY_WORKING_ON = STATUS_LOCALE_PROPERTY_PREFIX + STATUS_VALUE_WORKING_ON;
    public static final String PROPERTY_KEY_UNDEFINED = STATUS_LOCALE_PROPERTY_PREFIX + StatusManager.STATUS_VALUE_UNDEFINED;

    /** Initial status value for a new course. */

    private SingleSelection statusRadio;
    private final String keys[], values[];

    /**
     * @param name
     *            form name
     * @param locale
     *            locale of the request
     */
    public StatusForm(final UserRequest ureq, final WindowControl wControl) {
        super(ureq, wControl);
        values = new String[] { translate(PROPERTY_KEY_UNDEFINED), translate(PROPERTY_KEY_NOT_OK), translate(PROPERTY_KEY_OK), translate(PROPERTY_KEY_WORKING_ON) };
        keys = new String[] { StatusManager.STATUS_VALUE_UNDEFINED, STATUS_VALUE_NOT_OK, STATUS_VALUE_OK, STATUS_VALUE_WORKING_ON };
        initForm(ureq);
    }

    public String getSelectedStatus() {
        return statusRadio.getSelectedKey();
    }

    public void setSelectedStatus(final String value) {
        statusRadio.select(value, true);
    }

    @Override
    protected void formOK(final UserRequest ureq) {
        fireEvent(ureq, Event.DONE_EVENT);
    }

    @Override
    protected void initForm(final FormItemContainer formLayout, final Controller listener, final UserRequest ureq) {
        statusRadio = uifactory.addRadiosVertical("status", "form.status.selection", formLayout, keys, values);
        statusRadio.select(StatusManager.STATUS_VALUE_UNDEFINED, true);
        uifactory.addFormSubmitButton("save", formLayout);
    }

    @Override
    protected void doDispose() {
        //
    }

}
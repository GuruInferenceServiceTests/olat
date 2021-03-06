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

import org.olat.lms.commons.ModuleConfiguration;
import org.olat.lms.course.nodes.TACourseNode;
import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.components.form.flexible.FormItem;
import org.olat.presentation.framework.core.components.form.flexible.FormItemContainer;
import org.olat.presentation.framework.core.components.form.flexible.elements.SelectionElement;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormBasicController;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormEvent;
import org.olat.presentation.framework.core.control.Controller;
import org.olat.presentation.framework.core.control.WindowControl;
import org.olat.system.event.Event;

/**
 * Initial Date: 30.08.2004
 * 
 * @author Mike Stock
 */

public class ModulesForm extends FormBasicController {

    private final ModuleConfiguration config;
    private SelectionElement task, dropbox, returnbox, scoring, solution;

    /**
     * Modules selection form.
     * 
     * @param name
     * @param config
     */
    public ModulesForm(final UserRequest ureq, final WindowControl wControl, final ModuleConfiguration config) {
        super(ureq, wControl);
        this.config = config;
        initForm(ureq);
    }

    @Override
    protected void formOK(final UserRequest ureq) {
        //
    }

    @Override
    protected void formInnerEvent(final UserRequest ureq, final FormItem source, final FormEvent event) {
        if (source instanceof SelectionElement) {
            if (atLeastOneIsChecked((SelectionElement) source)) {
                fireEvent(ureq, new Event(source.getName() + ":" + ((SelectionElement) source).isSelected(0)));
            }
        }
    }

    private boolean atLeastOneIsChecked(final SelectionElement cb) {

        setFormInfo(null);

        if (task.isSelected(0) || dropbox.isSelected(0) || returnbox.isSelected(0) || scoring.isSelected(0) || solution.isSelected(0)) {
            return true;
        }

        setFormInfo("chelp.sel2");
        cb.select("xx", true);
        return false;
    }

    @Override
    protected void initForm(final FormItemContainer formLayout, final Controller listener, final UserRequest ureq) {
        setFormTitle("form.modules.title");
        setFormContextHelp("org.olat.presentation.course.nodes.ta", "ced-ta-select.html", "help.hover.ta-module");

        task = uifactory.addCheckboxesVertical("task", "form.modules.task", formLayout, new String[] { "xx" }, new String[] { "" }, null, 1);
        dropbox = uifactory.addCheckboxesVertical("dropbox", "form.modules.dropbox", formLayout, new String[] { "xx" }, new String[] { "" }, null, 1);
        returnbox = uifactory.addCheckboxesVertical("returnbox", "form.modules.returnbox", formLayout, new String[] { "xx" }, new String[] { "" }, null, 1);
        scoring = uifactory.addCheckboxesVertical("scoring", "form.modules.scoring", formLayout, new String[] { "xx" }, new String[] { "" }, null, 1);
        solution = uifactory.addCheckboxesVertical("solution", "form.modules.sample", formLayout, new String[] { "xx" }, new String[] { "" }, null, 1);

        Boolean cv;

        cv = (Boolean) config.get(TACourseNode.CONF_TASK_ENABLED);
        task.select("xx", cv == null ? false : cv.booleanValue());

        cv = (Boolean) config.get(TACourseNode.CONF_DROPBOX_ENABLED);
        dropbox.select("xx", cv == null ? false : cv.booleanValue());

        cv = (Boolean) config.get(TACourseNode.CONF_RETURNBOX_ENABLED);
        returnbox.select("xx", cv == null ? false : cv.booleanValue());

        cv = (Boolean) config.get(TACourseNode.CONF_SCORING_ENABLED);
        scoring.select("xx", cv == null ? false : cv.booleanValue());

        cv = (Boolean) config.get(TACourseNode.CONF_SOLUTION_ENABLED);
        solution.select("xx", cv == null ? false : cv.booleanValue());

        task.addActionListener(this, FormEvent.ONCLICK);
        dropbox.addActionListener(this, FormEvent.ONCLICK);
        returnbox.addActionListener(this, FormEvent.ONCLICK);
        scoring.addActionListener(this, FormEvent.ONCLICK);
        solution.addActionListener(this, FormEvent.ONCLICK);
    }

    @Override
    protected void doDispose() {
        //
    }

}

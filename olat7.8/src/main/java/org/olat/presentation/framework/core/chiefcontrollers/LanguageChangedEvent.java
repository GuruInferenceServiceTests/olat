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
 * Copyright (c) 1999-2007 at Multimedia- & E-Learning Services (MELS),<br>
 * University of Zurich, Switzerland.
 * <p>
 */
package org.olat.presentation.framework.core.chiefcontrollers;

import java.util.Locale;

import org.olat.presentation.framework.core.UserRequest;
import org.olat.system.event.MultiUserEvent;

/**
 * Description:<br>
 * inform about a changed language within a usersession, this MultiUserEvent.
 * <P>
 * Initial Date: 30.01.2008 <br>
 * 
 * @author patrickb
 */
public class LanguageChangedEvent extends MultiUserEvent {

    private Locale newLocale;
    private transient UserRequest ureq;

    public LanguageChangedEvent(Locale newLocale, UserRequest ureq) {
        super("LANG_CHANGED");
        this.newLocale = newLocale;
        // a language change event only occurs in ureq click
        // the "multiuserevent" is restricted on one user on the same olat instance
        // it is save to carry around the userrequest here.
        // but you normally should never see the ureq beeing hold as instance variable!
        this.ureq = ureq;
    }

    public Locale getNewLocale() {
        return newLocale;
    }

    public UserRequest getCurrentUreq() {
        return ureq;
    }
}

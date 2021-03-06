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

package org.olat.presentation.group.securitygroup;

import java.util.List;

import org.olat.data.basesecurity.Identity;
import org.olat.presentation.events.MultiIdentityChosenEvent;

/**
 * Description:<BR>
 * Event fired when identities should be moved from a security group to another
 * <P>
 * Initial Date: 03.01.2007
 * 
 * @author Ch.Guretzki
 */
public class IdentitiesMoveEvent extends MultiIdentityChosenEvent {

    private List<Identity> notMovedIdentities;
    private List<Identity> movedIdentities;

    /**
     * @param identity
     *            the removed identity
     */
    public IdentitiesMoveEvent(final List<Identity> identities) {
        super(identities, "identities_transfer");
    }

    /**
     * @return List of not moved identities.
     */
    public List<Identity> getNotMovedIdentities() {
        return this.notMovedIdentities;
    }

    /**
     * @param notMovedIdentities
     *            New list of NOT moved identities.
     */
    public void setNotMovedIdentities(final List<Identity> notMovedIdentities) {
        this.notMovedIdentities = notMovedIdentities;
    }

    /**
     * @return List of moved identities.
     */
    public List<Identity> getMovedIdentities() {
        return movedIdentities;
    }

    /**
     * @param movedIdentities
     *            New list of moved identities.
     */
    public void setMovedIdentities(final List<Identity> movedIdentities) {
        this.movedIdentities = movedIdentities;
    }

}

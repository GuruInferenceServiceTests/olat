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
 * Copyright (c) 1999-2006 at Multimedia- & E-Learning Services (MELS),<br>
 * University of Zurich, Switzerland.
 * <p>
 */

package org.olat.data.notifications;

import java.util.Date;

import org.olat.data.basesecurity.Identity;
import org.olat.data.commons.database.CreateInfo;
import org.olat.data.commons.database.ModifiedInfo;
import org.olat.data.commons.database.Persistable;

/**
 * Description: <br>
 * TODO: Felix Jost Class Description for Subscriber
 * <P>
 * Initial Date: 21.10.2004 <br>
 * 
 * @author Felix Jost
 */
public interface Subscriber extends Persistable, CreateInfo, ModifiedInfo {
    /**
     * @return the identity
     */
    public abstract Identity getIdentity();

    /**
     * @param identity
     */
    public abstract void setIdentity(Identity identity);

    /**
     * @return the latest date the user got an email concering this subscription here
     */
    public abstract Date getLatestEmailed();

    /**
     * @param latestEmailed
     */
    public abstract void setLatestEmailed(Date latestEmailed);

    /**
     * @return the publisher
     */
    public abstract Publisher getPublisher();

    /**
     * @param publisher
     */
    public abstract void setPublisher(Publisher publisher);
}

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
 * Copyright (c) 1999-2009 at Multimedia- & E-Learning Services (MELS),<br>
 * University of Zurich, Switzerland.
 * <p>
 */

package org.olat.data.course.statistic.homeorg;

import org.olat.data.commons.database.PersistentObject;

/**
 * Hibernate object representing an entry in the o_stat_homeorg table.
 * <P>
 * Initial Date: 12.02.2010 <br>
 * 
 * @author Stefan
 */
public class HomeOrgStat extends PersistentObject {

    private String businessPath;
    private String homeOrg;
    private int value;
    private long resId;

    public HomeOrgStat() {
        // for hibernate
    }

    public final long getResId() {
        return resId;
    }

    public void setResId(final long resId) {
        this.resId = resId;
    }

    public final String getBusinessPath() {
        return businessPath;
    }

    public final void setBusinessPath(final String businessPath) {
        this.businessPath = businessPath;
    }

    public final String getHomeOrg() {
        return homeOrg;
    }

    public final void setHomeOrg(final String homeOrg) {
        this.homeOrg = homeOrg;
    }

    public final int getValue() {
        return value;
    }

    public final void setValue(final int value) {
        this.value = value;
    }

}

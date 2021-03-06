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

package org.olat.lms.commons.validation;

import java.util.logging.Level;

public interface ValidationStatus {

    public final static Level ERROR = Level.SEVERE;
    public final static Level WARNING = Level.WARNING;
    public final static Level INFO = Level.INFO;
    final static Level NOERROR = Level.OFF;

    /**
     * @return getLevel() == ERROR
     */
    public boolean isError();

    /**
     * @return getLevel() == WARNING
     */
    public boolean isWarning();

    /**
     * @return getLevel() == INFO
     */
    public boolean isInfo();

    public Level getLevel();

    public ValidationAction getAction();

}

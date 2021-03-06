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

package org.olat.data.commons.vfs;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Description:<br>
 * VFSLeaf: A file that can be written to, read from
 * <P>
 * Initial Date: 23.06.2005 <br>
 * 
 * @author Felix Jost
 */
public interface VFSLeaf extends VFSItem {

    /**
     * @return is or null (if e.g. the underlying file no longer exists)
     */
    public InputStream getInputStream();

    /**
     * @return size
     */
    public long getSize();

    /**
     * @param append
     * @return os
     */
    public OutputStream getOutputStream(boolean append);

}

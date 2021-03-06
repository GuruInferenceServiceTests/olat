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
package org.olat.presentation.framework.core.components.form.flexible.elements;

/**
 * Description:<br>
 * TODO: patrickb Class Description for IntegerElement
 * <P>
 * Initial Date: 22.06.2007 <br>
 * 
 * @author patrickb
 */
public interface IntegerElement extends TextElement {
    /**
     * @return int value validated by element
     */
    public int getIntValue();

    /**
     * set the int value.
     * 
     * @param value
     */
    public void setIntValue(int value);

    /**
     * Implementors note: an integer element implementation has to provide a generic int value check anyway with a generic "must be a number" error message. But the user
     * of an int element can provide its own error key.
     * 
     * @param errorKey
     */
    public void setIntValueCheck(String errorKey);

    /**
     * @param otherValue
     * @param errorKey
     */
    public void setIsEqualCheck(int otherValue, String errorKey);

    /**
     * The value must be bigger or equal minValue.
     * 
     * @param minValue
     * @param errorKey
     */
    public void setMinValueCheck(int minValue, String errorKey);

    /**
     * The value must be less or equal to maxValue
     * 
     * @param maxValue
     * @param errorKey
     */
    public void setMaxValueCheck(int maxValue, String errorKey);

}

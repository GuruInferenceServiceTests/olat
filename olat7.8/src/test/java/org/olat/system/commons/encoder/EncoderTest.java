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
package org.olat.system.commons.encoder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Initial Date: 10.03.2014 <br>
 * 
 * @author lavinia
 */
public class EncoderTest {

    @BeforeClass
    public static void setup() {
        //
    }

    @Test
    public void testBCryptEncode_OK() {
        String plaintext = "aBc_12£_X";
        String encodedPassword = Encoder.bCryptEncode(plaintext);
        // System.out.println("testBCryptEncode - encodedPassword: " + encodedPassword);
        boolean matches = Encoder.matches(plaintext, encodedPassword);
        assertTrue(matches);
    }

    /**
     * ERROR: password match check failed because of: Invalid salt version.
     */
    @Test
    public void testBCryptEncode_wrongPasswordHash() {
        String plaintext = "aBc_123_X";
        String humbugPasswordHash = "e8136d63483adcd7760a1d50a2a8978b";
        boolean matches = Encoder.matches(plaintext, humbugPasswordHash);
        assertFalse(matches);
    }

    @Test
    public void testBCryptEncode_wrongPassword() {
        String plaintext = "aBc_123_X";
        String encodedPassword = Encoder.bCryptEncode(plaintext);
        // System.out.println("testBCryptEncode - encodedPassword: " + encodedPassword);
        boolean matches = Encoder.matches(plaintext.toLowerCase(), encodedPassword);
        assertFalse(matches);
    }
}

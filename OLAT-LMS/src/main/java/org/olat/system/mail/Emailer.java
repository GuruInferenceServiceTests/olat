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

package org.olat.system.mail;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.olat.system.commons.StringHelper;
import org.olat.system.security.OLATPrincipal;

/**
 * Helper class for sending emails. All mails sent by this class will have a footer that includes a link to this OLAT installation
 * This Helper Class has a system emailing and a user dependent emailing modus, depending on the constructor used.
 * 
 * 
 * Initial Date: Feb 10, 2005
 * 
 * @author Sabina Jeger
 */
public class Emailer {
	String mailfrom;
	String footer; // footer appended to the end of the mail

	private MailPackageStaticDependenciesWrapper webappAndMailHelper = null;

    /**
     * Constructs an Emailer which derives its <b>mail from address </b> from the mailfrom configuration in OLATContext. The <b>mail host </b> used to send the email is
     * taken from the <code>OLATContext</code>
     * 
	 * @param locale locale that should be used for message localization
     */
    public Emailer(MailTemplate mailTemplate) {
		this(mailTemplate, new DefaultStaticDependenciesDelegator());
	}

	/**
	 * constructor to be used from testing for providing the facadeToWebappAndMailHelper mock
	 * @param mailTemplate
	 * @param facadeToWebappAndMailhelper
	 */
	Emailer (MailTemplate mailTemplate, MailPackageStaticDependenciesWrapper facadeToWebappAndMailhelper){
		this.webappAndMailHelper = facadeToWebappAndMailhelper;

		this.mailfrom = facadeToWebappAndMailhelper.getSystemEmailAddress();
        footer = mailTemplate.getFooterTemplate();
    }

    /**
     * Constructs an Emailer which derives its <b>mail from address </b> from the given <code>Identity</code>. The <b>mail host </b> used to send the email is taken from
     * the <code>OLATContext</code>
     * <p>
     * The <code>boolean</code> parameter determines which email address is used.
     * <UL>
     * <LI><b>true: </b> <br>
     * <UL>
     * <LI>try first the Institutional email</LI>
     * <LI>if not defined, take user defined email</LI>
     * </UL>
     * </LI>
     * <LI><b>false: </b> <br>
     * <UL>
     * <LI>take user defined email</LI>
     * </UL>
     * </LI>
     * </UL>
     * 
	 * @param mailFromIdentity <b>not null </b>, containing the senders e-mail
	 * @param tryInstitutionalEmail specifies email address priority order
     */
    public Emailer(OLATPrincipal senderPrincipal, boolean tryInstitutionalEmail, MailTemplate mailTemplate) {
		this(senderPrincipal, tryInstitutionalEmail, mailTemplate, new DefaultStaticDependenciesDelegator());
	}
	
	/**
	 * for testing purpose
	 * @param senderPrincipal
	 * @param tryInstitutionalEmail
	 * @param mailTemplate
	 * @param facadeToWebappAndMailhelper
	 */
	Emailer (OLATPrincipal senderPrincipal, boolean tryInstitutionalEmail, MailTemplate mailTemplate, MailPackageStaticDependenciesWrapper facadeToWebappAndMailhelper){
		this.webappAndMailHelper = facadeToWebappAndMailhelper;
		
        String myMailfrom = senderPrincipal.getAttributes().getEmail();
        if (tryInstitutionalEmail) {
            String tmpFrom = senderPrincipal.getAttributes().getInstitutionalEmail();
            if (StringHelper.containsNonWhitespace(tmpFrom)) {
                myMailfrom = tmpFrom;
            }
        }
        this.mailfrom = myMailfrom;
        // initialize the mail footer with infos about this OLAT installation and the user who sent the mail

        footer = mailTemplate.getFooterTemplate();
    }

    /**
     * Creates a e-mail message with the given subject and body. The sender is taken from the value which was given to the constructor. The recipient fields TO: and BCC:
     * are generated in the follwing manner:
     * <ul>
     * <li>each ContactList-Name from the listOfContactLists is used as (empty group) in the TO: field</li>
     * <li>all ContactList-Members are added to the BCC: field</li>
     * </ul>
     * 
     * @param listOfContactLists
     * @param subject
     * @param body
     * @return
     * @throws MessagingException
     * @throws AddressException
     * @throws MessagingException
     */
    public boolean sendEmail(List<ContactList> listOfContactLists, String subject, String body) throws AddressException, MessagingException {
        return sendEmail(listOfContactLists, subject, body, null);
    }

    public boolean sendEmail(List<ContactList> listOfContactLists, String subject, String body, List<File> attachments) throws AddressException, MessagingException {

		if (webappAndMailHelper.isEmailFunctionalityDisabled()) return false;

		InternetAddress mailFromAddress = new InternetAddress(mailfrom);		
		File[] attachmentsArray = attachmentListToArry(attachments);
        MailerResult result = new MailerResult();
		
		MimeMessage msg = webappAndMailHelper.createMessage(mailFromAddress, listOfContactLists, body + footer, subject, attachmentsArray, result);
		webappAndMailHelper.send(msg, result);
		
        return result.getReturnCode() == MailerResult.OK;
    }

	/**
	 * @param attachments
	 * @return
	 */
	private File[] attachmentListToArry(List<File> attachments) {
        File[] attachmentsArray = null;
        if (attachments != null && !attachments.isEmpty()) {
            attachmentsArray = attachments.toArray(new File[attachments.size()]);
        }
		return attachmentsArray;
	}

	/**
	 * 
	 * @param cc a string containing comma separated list of email addresses
	 * @param subject
	 * @param body
	 * @param attachments
	 * @return
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public boolean sendEmailCC(String cc, String subject, String body, List<File> attachments) throws AddressException, MessagingException {

		if (webappAndMailHelper.isEmailFunctionalityDisabled()) return false;
		
		InternetAddress mailFromAddress = new InternetAddress(mailfrom);
		InternetAddress[] recipientsCC = InternetAddress.parse(cc);
		File[] attachmentsArray = attachmentListToArry(attachments);
		MailerResult result = new MailerResult();
		
		MimeMessage msg = webappAndMailHelper.createMessage(mailFromAddress, null, recipientsCC, null, body + footer, subject, attachmentsArray, result);
		webappAndMailHelper.send(msg, result);
		//TODO:discuss why is the MailerResult not used here?
        return true;
    }

    /**
     * @param mailto
     * @param subject
     * @param body
     * @return
     * @throws AddressException
     * @throws SendFailedException
	 * @throws MessagingException TODO:gs handle exceptions internally and may return some error codes or so to get rid of dependecy of mail/activatoin jars in olat
     */
    public boolean sendEmail(String mailto, String subject, String body) throws AddressException, SendFailedException, MessagingException {
        return sendEmail(mailfrom, mailto, subject, body);
    }

    private boolean sendEmail(String from, String mailto, String subject, String body) throws AddressException, SendFailedException, MessagingException {

		if (webappAndMailHelper.isEmailFunctionalityDisabled()) return false;
		
		InternetAddress mailFromAddress = new InternetAddress(from);
		InternetAddress mailToAddress[] = InternetAddress.parse(mailto);
        MailerResult result = new MailerResult();
		
		MimeMessage msg = webappAndMailHelper.createMessage(mailFromAddress, mailToAddress, null, null, body + footer, subject, null, result);
		webappAndMailHelper.send(msg, result);
		//TODO:discuss why is the MailerResult not used here?
        return true;
    }

}

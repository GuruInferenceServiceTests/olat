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
 * Copyright (c) frentix GmbH<br>
 * http://www.frentix.com<br>
 * <p>
 */

package org.olat.lms.portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.olat.data.basesecurity.BaseSecurity;
import org.olat.data.basesecurity.Constants;
import org.olat.data.basesecurity.Identity;
import org.olat.data.basesecurity.SecurityGroup;
import org.olat.data.commons.database.DB;
import org.olat.data.portfolio.artefact.AbstractArtefact;
import org.olat.data.portfolio.structure.EPDefaultMap;
import org.olat.data.portfolio.structure.EPPage;
import org.olat.data.portfolio.structure.EPStructureElement;
import org.olat.data.portfolio.structure.EPStructuredMap;
import org.olat.data.portfolio.structure.EPStructuredMapTemplate;
import org.olat.data.portfolio.structure.PortfolioStructure;
import org.olat.data.portfolio.structure.PortfolioStructureDao;
import org.olat.data.portfolio.structure.PortfolioStructureMap;
import org.olat.data.portfolio.structure.StructureStatusEnum;
import org.olat.data.repository.RepositoryEntry;
import org.olat.data.resource.OLATResource;
import org.olat.data.resource.OLATResourceManager;
import org.olat.lms.repository.RepositoryService;
import org.olat.test.JunitTestHelper;
import org.olat.test.OlatTestCase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:<br>
 * This an integration test for the frontend manager. The goal of this test is to check the hibernate mapping and the constraints on the database too and not only the
 * manager. This will only test methods, which are really implemented in the frontendManager and not the ones which mainly pass through artefact- or structure- manager
 * <P>
 * Initial Date: 28 jun. 2010 <br>
 * 
 * @author srosse, stephane.rosse@frentix.com, http.//www.frentix.com
 */
public class EPFrontendManagerITCase extends OlatTestCase {

    private static Identity ident1, ident2, ident3;
    private static boolean isInitialized = false;

    @Autowired
    private DB dbInstance;

    @Autowired
    private EPFrontendManager epFrontendManager;

    @Autowired
    private PortfolioStructureDao epStructureManager;

    @Autowired
    private BaseSecurity securityManager;

    @Autowired
    private RepositoryService repositoryManager;

    @Autowired
    private OLATResourceManager resourceManager;

    @Before
    public void setUp() {
        if (!isInitialized) {
            ident1 = JunitTestHelper.createAndPersistIdentityAsUser("frtuse-1");
            ident2 = JunitTestHelper.createAndPersistIdentityAsUser("frtuse-2");
            ident3 = JunitTestHelper.createAndPersistIdentityAsUser("frtuse-3");
        }
    }

    @After
    public void tearDown() {
        dbInstance.commitAndCloseSession();
    }

    @Test
    public void testManagers() {
        assertNotNull(dbInstance);
        assertNotNull(epFrontendManager);
        assertNotNull(securityManager);
    }

    @Test
    public void testAssignMapTemplateToUser() {
        // create a template
        // test save parent and child
        final PortfolioStructure templateEl = epStructureManager.createPortfolioMapTemplate(ident1, "template-1", "map-template-1");
        epStructureManager.savePortfolioStructure(templateEl);
        // first page
        final PortfolioStructure page1 = epFrontendManager.createAndPersistPortfolioPage(templateEl, "template-page-1", "template-page-1");
        // structure element 1 from page 1
        final PortfolioStructure struct11 = epFrontendManager.createAndPersistPortfolioStructureElement(page1, "template-structure-1.1", "template-structure-1.1");
        epStructureManager.addCollectRestriction(struct11, "Forum", "minimum", 2);
        epStructureManager.savePortfolioStructure(struct11);
        // structure element 2 from page 1
        final PortfolioStructure struct12 = epFrontendManager.createAndPersistPortfolioStructureElement(page1, "template-structure-1.2", "template-structure-1.2");
        // first page
        final PortfolioStructure page2 = epFrontendManager.createAndPersistPortfolioPage(templateEl, "template-page-2", "template-page-2");
        // structure element 1 from page 2
        final PortfolioStructure struct21 = epFrontendManager.createAndPersistPortfolioStructureElement(page2, "template-structure-2.1", "template-structure-2.1");
        epStructureManager.addCollectRestriction(struct21, "bc", "maximum", 4);
        epStructureManager.savePortfolioStructure(struct21);
        // save the template
        dbInstance.commitAndCloseSession();

        // make the copy
        final PortfolioStructureMap map = epFrontendManager.assignStructuredMapToUser(ident2, (EPStructuredMapTemplate) templateEl, null, null, null, null);
        dbInstance.commitAndCloseSession();
        assertNotNull(map);

        // check the copy
        final PortfolioStructure retrievedMap = epFrontendManager.loadPortfolioStructureByKey(map.getKey());
        assertNotNull(retrievedMap);
        assertTrue(retrievedMap instanceof EPStructuredMap);
        assertNotNull(((EPStructuredMap) retrievedMap).getStructuredMapSource());
        assertEquals(templateEl.getKey(), ((EPStructuredMap) retrievedMap).getStructuredMapSource().getKey());

        // check pages of the copied map
        final List<PortfolioStructure> pages = epFrontendManager.loadStructureChildren(retrievedMap);
        assertNotNull(pages);
        assertEquals(2, pages.size());
        assertTrue(pages.get(0) instanceof EPPage);
        assertTrue(pages.get(1) instanceof EPPage);
        assertEquals("template-page-1", ((EPStructureElement) pages.get(0)).getTitle());
        assertEquals("template-page-2", ((EPStructureElement) pages.get(1)).getTitle());
        // check root
        assertNotNull(((EPStructureElement) pages.get(0)).getRoot());
        assertEquals(retrievedMap.getKey(), ((EPStructureElement) pages.get(0)).getRoot().getKey());

        // check children of the pages
        final List<PortfolioStructure> structs1 = epFrontendManager.loadStructureChildren(pages.get(0));
        assertNotNull(structs1);
        assertEquals(2, structs1.size());
        assertTrue(structs1.get(0) instanceof EPStructureElement);
        assertTrue(structs1.get(1) instanceof EPStructureElement);
        final EPStructureElement struct11El = (EPStructureElement) structs1.get(0);
        assertEquals("template-structure-1.1", struct11El.getTitle());
        assertEquals("template-structure-1.2", ((EPStructureElement) structs1.get(1)).getTitle());
        // check root
        assertNotNull(((EPStructureElement) structs1.get(0)).getRoot());
        assertEquals(retrievedMap.getKey(), ((EPStructureElement) structs1.get(0)).getRoot().getKey());
        assertNotNull(((EPStructureElement) structs1.get(1)).getRoot());
        assertEquals(retrievedMap.getKey(), ((EPStructureElement) structs1.get(1)).getRoot().getKey());
        // check collect restriction
        assertNotNull(struct11El.getCollectRestrictions());
        assertEquals("Forum", struct11El.getCollectRestrictions().get(0).getArtefactType());
        assertEquals("minimum", struct11El.getCollectRestrictions().get(0).getRestriction());
        assertEquals(2, struct11El.getCollectRestrictions().get(0).getAmount());

        final List<PortfolioStructure> structs2 = epFrontendManager.loadStructureChildren(pages.get(1));
        assertNotNull(structs2);
        assertEquals(1, structs2.size());
        assertTrue(structs2.get(0) instanceof EPStructureElement);
        final EPStructureElement struct21El = (EPStructureElement) structs2.get(0);
        assertEquals("template-structure-2.1", struct21El.getTitle());
        // check root
        assertNotNull(struct21El.getRoot());
        assertEquals(retrievedMap.getKey(), struct21El.getRoot().getKey());
        // check collect restriction
        assertNotNull(struct21El.getCollectRestrictions());
        assertEquals("bc", struct21El.getCollectRestrictions().get(0).getArtefactType());
        assertEquals("maximum", struct21El.getCollectRestrictions().get(0).getRestriction());
        assertEquals(4, struct21El.getCollectRestrictions().get(0).getAmount());
    }

    @Test
    public void testSyncMapTemplateToUserMap() {
        // //////////////////////
        // start create a template
        final PortfolioStructure templateEl = epStructureManager.createPortfolioMapTemplate(ident1, "sync-template-1", "symc-map-template-1");
        epStructureManager.savePortfolioStructure(templateEl);
        // create five pages
        final List<PortfolioStructure> pageRefs = new ArrayList<PortfolioStructure>();
        final List<PortfolioStructure> elementRefs = new ArrayList<PortfolioStructure>();

        for (int i = 0; i < 5; i++) {
            final PortfolioStructure page = epFrontendManager.createAndPersistPortfolioPage(templateEl, "sync-template-page-" + i, "sync-template-page-" + i);
            pageRefs.add(page);

            for (int j = 0; j < 5; j++) {
                final PortfolioStructure struct = epFrontendManager.createAndPersistPortfolioStructureElement(page, "template-structure-" + i + "." + j,
                        "template-structure-" + i + "." + j);
                epStructureManager.addCollectRestriction(struct, "Forum", "minimum", 2);
                epStructureManager.savePortfolioStructure(struct);
                elementRefs.add(struct);
            }
        }
        // save the template
        dbInstance.commitAndCloseSession();
        // end create template
        // ////////////////////

        // make the copy
        final PortfolioStructureMap map = epFrontendManager.assignStructuredMapToUser(ident2, (EPStructuredMapTemplate) templateEl, null, null, null, null);
        dbInstance.commitAndCloseSession();
        assertNotNull(map);

        // ////////////////////////////////
        // shuffle the pages and delete one
        final PortfolioStructure retrievedTemplateEl = epFrontendManager.loadPortfolioStructureByKey(templateEl.getKey());
        final List<PortfolioStructure> pages = epFrontendManager.loadStructureChildren(retrievedTemplateEl);
        epStructureManager.moveUp(retrievedTemplateEl, pages.get(1));
        epStructureManager.moveDown(retrievedTemplateEl, pages.get(2));
        epStructureManager.removeStructure(retrievedTemplateEl, pages.get(3));
        epStructureManager.savePortfolioStructure(retrievedTemplateEl);
        // shuffle a page
        final List<PortfolioStructure> page1Children = epFrontendManager.loadStructureChildren(pages.get(1));
        epStructureManager.moveUp(pages.get(1), page1Children.get(3));
        epStructureManager.moveUp(pages.get(1), page1Children.get(2));
        epStructureManager.moveUp(pages.get(1), page1Children.get(4));
        // and add an element and sub-elements
        final PortfolioStructure newStruct = epFrontendManager.createAndPersistPortfolioStructureElement(pages.get(1), "new-template-structure-1.6",
                "template-structure-1.6");
        epStructureManager.addCollectRestriction(pages.get(1), "Forum", "minimum", 2);
        epStructureManager.savePortfolioStructure(newStruct);
        epStructureManager.savePortfolioStructure(pages.get(1));
        for (int k = 0; k < 5; k++) {
            final PortfolioStructure struct = epFrontendManager.createAndPersistPortfolioStructureElement(newStruct, "new-template-structure-2." + k,
                    "template-structure-2." + k);
            epStructureManager.addCollectRestriction(struct, "bc", "minimum", 2);
            epStructureManager.savePortfolioStructure(struct);
            elementRefs.add(struct);
        }
        dbInstance.commitAndCloseSession();
        // end shuffle the pages
        // ////////////////////////////////

        // //////////////////
        // check the template
        final PortfolioStructure retrievedTemplate2El = epFrontendManager.loadPortfolioStructureByKey(templateEl.getKey());
        assertNotNull(retrievedTemplate2El);
        assertTrue(retrievedTemplate2El instanceof EPStructuredMapTemplate);
        final List<PortfolioStructure> retrievedPages2 = epFrontendManager.loadStructureChildren(retrievedTemplate2El);
        assertEquals(4, retrievedPages2.size());
        assertEquals(4, ((EPStructuredMapTemplate) retrievedTemplate2El).getInternalChildren().size());
        // check the shuffled pages
        assertEquals(pageRefs.get(1).getKey(), retrievedPages2.get(0).getKey());
        assertEquals(pageRefs.get(0).getKey(), retrievedPages2.get(1).getKey());
        assertEquals(pageRefs.get(2).getKey(), retrievedPages2.get(2).getKey());
        assertEquals(pageRefs.get(4).getKey(), retrievedPages2.get(3).getKey());
        // check added element
        final List<PortfolioStructure> retrievedChildren2 = epFrontendManager.loadStructureChildren(retrievedPages2.get(0));
        assertEquals(6, retrievedChildren2.size());

        dbInstance.commitAndCloseSession();
        // check the template
        // //////////////////

        // sync the map
        epFrontendManager.synchronizeStructuredMapToUserCopy(map);
        dbInstance.commitAndCloseSession();

        // ///////////////
        // check the sync
        final PortfolioStructure synchedMap = epFrontendManager.loadPortfolioStructureByKey(map.getKey());
        assertNotNull(synchedMap);
        assertTrue(synchedMap instanceof EPStructuredMap);
        final List<PortfolioStructure> synchedPages = epFrontendManager.loadStructureChildren(synchedMap);
        assertNotNull(synchedPages);
        assertEquals(4, synchedPages.size());
        assertEquals(((EPStructureElement) pageRefs.get(1)).getTitle(), ((EPStructureElement) synchedPages.get(0)).getTitle());
        assertEquals(((EPStructureElement) pageRefs.get(0)).getTitle(), ((EPStructureElement) synchedPages.get(1)).getTitle());
        assertEquals(((EPStructureElement) pageRefs.get(2)).getTitle(), ((EPStructureElement) synchedPages.get(2)).getTitle());
        assertEquals(((EPStructureElement) pageRefs.get(4)).getTitle(), ((EPStructureElement) synchedPages.get(3)).getTitle());

        // check synched key
        assertEquals(((EPStructureElement) pageRefs.get(1)).getKey(), ((EPStructureElement) synchedPages.get(0)).getStructureElSource());
        assertEquals(((EPStructureElement) pageRefs.get(0)).getKey(), ((EPStructureElement) synchedPages.get(1)).getStructureElSource());
        assertEquals(((EPStructureElement) pageRefs.get(2)).getKey(), ((EPStructureElement) synchedPages.get(2)).getStructureElSource());
        assertEquals(((EPStructureElement) pageRefs.get(4)).getKey(), ((EPStructureElement) synchedPages.get(3)).getStructureElSource());

        // check the new elements
        final List<PortfolioStructure> retrievedPage1Children = epFrontendManager.loadStructureChildren(synchedPages.get(0));
        assertEquals(6, retrievedPage1Children.size());
        final PortfolioStructure retrievedNewStruct = retrievedPage1Children.get(5);
        assertEquals("new-template-structure-1.6", ((EPStructureElement) retrievedNewStruct).getTitle());
        final List<PortfolioStructure> retrievedNewStructChildren = epFrontendManager.loadStructureChildren(retrievedNewStruct);
        assertNotNull(retrievedNewStructChildren);
        assertEquals(5, retrievedNewStructChildren.size());
        for (int k = 0; k < 5; k++) {
            assertEquals("new-template-structure-2." + k, ((EPStructureElement) retrievedNewStructChildren.get(k)).getTitle());
        }
        // end check the sync
        // //////////////////
    }

    @Test
    public void testTaggingOfArtefacts() {
        final AbstractArtefact artefact = epFrontendManager.createAndPersistArtefact(ident1, "Forum");
        dbInstance.commitAndCloseSession();
        assertNotNull(artefact);

        epFrontendManager.setArtefactTag(ident1, artefact, "Hello");
        epFrontendManager.setArtefactTag(ident2, artefact, "Hello");
        epFrontendManager.setArtefactTag(ident2, artefact, "Tchao");
        dbInstance.commitAndCloseSession();

        final List<String> tags = (List<String>) epFrontendManager.getArtefactTags(artefact);
        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertTrue(tags.get(0).equals("Hello") || tags.get(1).equals("Hello"));
        assertTrue(tags.get(0).equals("Tchao") || tags.get(1).equals("Tchao"));
    }

    @Test
    public void testCopyMap() {
        // create two artefacts
        final AbstractArtefact artefact1 = epFrontendManager.createAndPersistArtefact(ident1, "text");
        assertNotNull(artefact1);

        final AbstractArtefact artefact2 = epFrontendManager.createAndPersistArtefact(ident1, "bc");
        assertNotNull(artefact2);

        dbInstance.commitAndCloseSession();

        // create a map with a page and the page has two artefacts
        final PortfolioStructureMap originalMap = epFrontendManager.createAndPersistPortfolioDefaultMap(ident1, "Title", "Description");
        final PortfolioStructure newPage = epFrontendManager.createAndPersistPortfolioPage(originalMap, "Page title", "Page description");
        final boolean successfullLink1 = epFrontendManager.addArtefactToStructure(ident1, artefact1, newPage);
        assertTrue(successfullLink1);

        final boolean successfullLink2 = epFrontendManager.addArtefactToStructure(ident1, artefact2, newPage);
        assertTrue(successfullLink2);

        dbInstance.commitAndCloseSession();

        // 1 test: copy the map one shoot
        final PortfolioStructureMap copyMap = epFrontendManager.createAndPersistPortfolioDefaultMap(ident1, "Title copy", "Description copy");
        epFrontendManager.copyStructureRecursively(originalMap, copyMap, true);
        assertNotNull(copyMap.getKey());

        dbInstance.commitAndCloseSession();

        // 2 test: copy the map two shoota
        final PortfolioStructureMap copyMap2 = epFrontendManager.createAndPersistPortfolioDefaultMap(ident1, "Title copy 2", "Description copy 2");
        dbInstance.commitAndCloseSession();
        assertNotNull(copyMap2.getKey());

        epFrontendManager.copyStructureRecursively(originalMap, copyMap2, true);
        dbInstance.commitAndCloseSession();
    }

    @Test
    public void closedArtefacts() {
        // create two artefacts
        final AbstractArtefact artefact1 = epFrontendManager.createAndPersistArtefact(ident1, "text");
        assertNotNull(artefact1);

        final AbstractArtefact artefact2 = epFrontendManager.createAndPersistArtefact(ident1, "bc");
        assertNotNull(artefact2);

        dbInstance.commitAndCloseSession();

        // create a map with a page and the page has two artefacts
        final PortfolioStructureMap originalMap = epFrontendManager.createAndPersistPortfolioDefaultMap(ident1, "Title", "Description");
        final PortfolioStructure newPage = epFrontendManager.createAndPersistPortfolioPage(originalMap, "Page title", "Page description");

        final boolean successfullLink1 = epFrontendManager.addArtefactToStructure(ident1, artefact1, newPage);
        assertTrue(successfullLink1);
        final boolean successfullLink2 = epFrontendManager.addArtefactToStructure(ident1, artefact2, newPage);
        assertTrue(successfullLink2);

        dbInstance.commitAndCloseSession();

        // check if the artefact is in a closed map
        assertFalse(epFrontendManager.isArtefactClosed(artefact1));
        assertFalse(epFrontendManager.isArtefactClosed(artefact2));

        // closed the map artificially
        ((EPDefaultMap) originalMap).setStatus(StructureStatusEnum.CLOSED);
        dbInstance.updateObject(originalMap);
        dbInstance.commitAndCloseSession();

        // check if the artefact is in a closed map
        assertTrue(epFrontendManager.isArtefactClosed(artefact1));
        assertTrue(epFrontendManager.isArtefactClosed(artefact2));
    }

    @Test
    public void isArtefactsInStructure() {

        // create two artefacts
        final AbstractArtefact artefact1 = epFrontendManager.createAndPersistArtefact(ident1, "text");
        assertNotNull(artefact1);

        final AbstractArtefact artefact2 = epFrontendManager.createAndPersistArtefact(ident1, "bc");
        assertNotNull(artefact2);

        dbInstance.commitAndCloseSession();

        // create a map with a page and the page has two artefacts
        final PortfolioStructureMap originalMap = epFrontendManager.createAndPersistPortfolioDefaultMap(ident1, "Title", "Description");
        final PortfolioStructure page1 = epFrontendManager.createAndPersistPortfolioPage(originalMap, "Page title", "Page description");
        final PortfolioStructure page2 = epFrontendManager.createAndPersistPortfolioPage(originalMap, "Page title", "Page description");

        final boolean successfullLink1 = epFrontendManager.addArtefactToStructure(ident1, artefact1, page1);
        assertTrue(successfullLink1);
        final boolean successfullLink2 = epFrontendManager.addArtefactToStructure(ident1, artefact2, page2);
        assertTrue(successfullLink2);

        dbInstance.commitAndCloseSession();

        assertTrue(epFrontendManager.isArtefactInStructure(artefact1, page1));
        assertFalse(epFrontendManager.isArtefactInStructure(artefact1, page2));
        assertFalse(epFrontendManager.isArtefactInStructure(artefact2, page1));
        assertTrue(epFrontendManager.isArtefactInStructure(artefact2, page2));
    }

    @Test
    public void isMapOwner() {
        // create a map
        final PortfolioStructureMap originalMap = epFrontendManager.createAndPersistPortfolioDefaultMap(ident1, "Title", "Description");
        final PortfolioStructure page1 = epFrontendManager.createAndPersistPortfolioPage(originalMap, "Page title", "Page description");
        assertNotNull(page1);

        dbInstance.commitAndCloseSession();

        // check if ident1 is owner
        assertTrue(epFrontendManager.isMapOwner(ident1, originalMap.getOlatResource()));
        // check if ident2 is not owner
        assertFalse(epFrontendManager.isMapOwner(ident2, originalMap.getOlatResource()));
    }

    /**
     * Same workflow as the repository. This workflow is pretty critical.
     */
    @Test
    public void isStructuredMapOwner() {
        final OLATResource resource = epStructureManager.createPortfolioMapTemplateResource();

        // create a repository entry
        final RepositoryEntry addedEntry = repositoryManager.createRepositoryEntryInstance(ident1.getName());

        addedEntry.setCanDownload(false);
        addedEntry.setCanLaunch(true);
        addedEntry.setDisplayname("test repo");
        addedEntry.setResourcename("-");
        addedEntry.setAccess(RepositoryEntry.ACC_OWNERS);

        // Set the resource on the repository entry and save the entry.
        final OLATResource ores = resourceManager.findOrPersistResourceable(resource);
        addedEntry.setOlatResource(ores);

        // create security group
        final SecurityGroup newGroup = securityManager.createAndPersistSecurityGroup();
        securityManager.createAndPersistPolicy(newGroup, Constants.PERMISSION_ACCESS, newGroup);
        securityManager.createAndPersistPolicy(newGroup, Constants.PERMISSION_HASROLE, EPFrontendManager.ORES_MAPOWNER);
        securityManager.addIdentityToSecurityGroup(ident1, newGroup);
        addedEntry.setOwnerGroup(newGroup);

        repositoryManager.saveRepositoryEntry(addedEntry);
        dbInstance.commitAndCloseSession();

        // create the template owned by ident1
        final PortfolioStructureMap template = epStructureManager.createAndPersistPortfolioMapTemplateFromEntry(ident1, addedEntry);
        final PortfolioStructure page1 = epFrontendManager.createAndPersistPortfolioPage(template, "Page title", "Page description");
        assertNotNull(page1);

        dbInstance.commitAndCloseSession();

        // assign the template to ident2
        final PortfolioStructureMap map = epFrontendManager.assignStructuredMapToUser(ident2, template, null, null, null, null);
        assertNotNull(map);
        dbInstance.commitAndCloseSession();

        // check if ident2 is owner of the map
        assertTrue(epFrontendManager.isMapOwner(ident2, map.getOlatResource()));
        // check if ident1 is not the owner of the map
        assertFalse(epFrontendManager.isMapOwner(ident1, map.getOlatResource()));
        // check if ident1 is owner of the template
        assertTrue(epFrontendManager.isMapOwner(ident1, template.getOlatResource()));
    }

    @Test
    public void isTemplateInUse() {
        // create the template owned by ident1
        final PortfolioStructureMap template = epStructureManager.createPortfolioMapTemplate(ident1, "Template in user", "Template in use");
        dbInstance.saveObject(template);
        dbInstance.commitAndCloseSession();
        // add a page to it
        final PortfolioStructure page1 = epFrontendManager.createAndPersistPortfolioPage(template, "Page title", "Page description");
        assertNotNull(page1);
        dbInstance.commitAndCloseSession();

        // check: the template is not in use
        assertFalse(epFrontendManager.isTemplateInUse(template, null, null, null));

        // use the template: assign the template to ident2
        final PortfolioStructureMap map = epFrontendManager.assignStructuredMapToUser(ident2, template, null, null, null, null);
        assertNotNull(map);
        dbInstance.commitAndCloseSession();

        // check: the template is in use
        assertTrue(epFrontendManager.isTemplateInUse(template, null, null, null));
    }
}

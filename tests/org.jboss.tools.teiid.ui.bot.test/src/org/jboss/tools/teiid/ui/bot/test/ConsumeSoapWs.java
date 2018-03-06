package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.imports.WsdlImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author mmakovy, skaleta 
 * tested features:
 * - consume SOAP WS (+ Digest access authentication)
 */

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {
    ConnectionProfileConstants.SOAP,
    ConnectionProfileConstants.SOAP_SECURE })
public class ConsumeSoapWs {
	private static final String PROJECT_NAME = "ConsumeSoap";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private ModelExplorer modelExplorer;

	@Before
	public void createProject() {
		modelExplorer = new ModelExplorer();
		modelExplorer.createProject(PROJECT_NAME);
	}

	@After
	public void cleanUp(){
		modelExplorer.deleteAllProjectsSafely();
	}

	@Test
	public void testSoap() {
		String sourceModel = "SoapSource";
		String viewModel = "SoapView";
		WsdlImportWizard.openWizard()
                .setConnectionProfile(ConnectionProfileConstants.SOAP)
				.selectOperations("FullCountryInfo", "FullCountryInfoAllCountries")
				.nextPage()
				.setProject(PROJECT_NAME)
				.setSourceModelName(sourceModel)
				.setViewModelName(viewModel)
				.nextPage()
				.setJndiName("java:/" + sourceModel + "DS")
				.nextPage()
				.nextPage()
				.addRequestElement("FullCountryInfo/sequence/arg0")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/capitalCity")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/continentCode")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/currencyIsoCode")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/isoCode")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/name")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/phoneCode")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/capitalCity")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/continentCode")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/currencyIsoCode")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/isoCode")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/name")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/phoneCode")
				.finish();

		String vdb = "SoapVdb";
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdb)
				.addModel(PROJECT_NAME, viewModel)
				.finish();


		modelExplorer.deployVdb(PROJECT_NAME, vdb);

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdb);
		assertEquals(1, jdbchelper.getNumberOfResults("exec FullCountryInfo('US')"));
		assertEquals(3, jdbchelper.getNumberOfResults("exec FullCountryInfoAllCountries()"));
	}

	@Test
	public void testSoapDigest() {
		String sourceModel = "SoapDigestSource";
		String viewModel = "SoapDigestView";
		WsdlImportWizard.openWizard()
                .setConnectionProfile(ConnectionProfileConstants.SOAP_SECURE)
				.selectOperations("FullCountryInfo", "FullCountryInfoAllCountries")
				.nextPage()
				.setProject(PROJECT_NAME)
				.setSourceModelName(sourceModel)
				.setViewModelName(viewModel)
				.nextPage()
				.setJndiName("java:/" + sourceModel + "DS")
				.nextPage()
				.nextPage()
				.addRequestElement("FullCountryInfo/sequence/arg0")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/capitalCity")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/continentCode")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/currencyIsoCode")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/isoCode")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/name")
				.addResponseElement("FullCountryInfo", "FullCountryInfoResponse/sequence/return/sequence/phoneCode")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/capitalCity")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/continentCode")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/currencyIsoCode")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/isoCode")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/name")
				.addResponseElement("FullCountryInfoAllCountries", "FullCountryInfoAllCountriesResponse/sequence/return/sequence/phoneCode")
				.finish();

		String vdb = "SoapDigestVdb";
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(vdb)
				.addModel(PROJECT_NAME, viewModel)
				.finish();

		VdbEditor.getInstance(vdb).save();

		modelExplorer.deployVdb(PROJECT_NAME, vdb);

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, vdb);
		assertEquals(1, jdbchelper.getNumberOfResults("exec FullCountryInfo('US')"));
		assertEquals(3, jdbchelper.getNumberOfResults("exec FullCountryInfoAllCountries()"));
	}

}

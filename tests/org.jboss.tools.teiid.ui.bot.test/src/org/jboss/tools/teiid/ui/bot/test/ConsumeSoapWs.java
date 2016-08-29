package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileHelper;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.WsdlImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for consuming SOAP WS
 * 
 * @author mmakovy
 * 
 */

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class ConsumeSoapWs {
	private static final String PROJECT_NAME = "SOAP";
	private static final String SOURCE_MODEL_NAME = "Soap_Source";
	private static final String VIEW_MODEL_NAME = "Soap_View";
	private static final String VDB_NAME = "SOAP_VDB";
	private static final String TESTSQL = "exec FullCountryInfo('US')";
	private static final String TESTSQL1 = "exec FullCountryInfoAllCountries()";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	@Before
	public void before() {
		new ModelExplorer().createProject(PROJECT_NAME);
	}
	
	@After
	public void cleanUp(){
		new ModelExplorer().deleteAllProjectsSafely();
	}

	@Test
	public void test() {
		Properties wsdlCP = new Properties();
		wsdlCP.setProperty("wsdl", "http://ws-dvirt.rhcloud.com/dv-test-ws/soap?wsdl");
		wsdlCP.setProperty("endPoint", "Countries");

		new ConnectionProfileHelper().createCpWsdl("SOAP", wsdlCP);

		WsdlImportWizard.openWizard()
				.setConnectionProfile("SOAP")
				.selectOperations("FullCountryInfo","FullCountryInfoAllCountries")
				.nextPage()
				.setProject(PROJECT_NAME)
				.setSourceModelName(SOURCE_MODEL_NAME)
				.setViewModelName(VIEW_MODEL_NAME)
				.nextPage()
				.setJndiName(SOURCE_MODEL_NAME)
				.nextPage()
				.nextPage()
				.addRequestElement("FullCountryInfo/sequence/arg0")
				.addResponseElement("FullCountryInfo","FullCountryInfoResponse/sequence/return/sequence/capitalCity")
				.addResponseElement("FullCountryInfo","FullCountryInfoResponse/sequence/return/sequence/continentCode")
				.addResponseElement("FullCountryInfo","FullCountryInfoResponse/sequence/return/sequence/currencyIsoCode")
				.addResponseElement("FullCountryInfo","FullCountryInfoResponse/sequence/return/sequence/isoCode")
				.addResponseElement("FullCountryInfo","FullCountryInfoResponse/sequence/return/sequence/name")
				.addResponseElement("FullCountryInfo","FullCountryInfoResponse/sequence/return/sequence/phoneCode")
			
				.addResponseElement("FullCountryInfoAllCountries","FullCountryInfoAllCountriesResponse/sequence/return/sequence/capitalCity")
				.addResponseElement("FullCountryInfoAllCountries","FullCountryInfoAllCountriesResponse/sequence/return/sequence/continentCode")
				.addResponseElement("FullCountryInfoAllCountries","FullCountryInfoAllCountriesResponse/sequence/return/sequence/currencyIsoCode")
				.addResponseElement("FullCountryInfoAllCountries","FullCountryInfoAllCountriesResponse/sequence/return/sequence/isoCode")
				.addResponseElement("FullCountryInfoAllCountries","FullCountryInfoAllCountriesResponse/sequence/return/sequence/name")
				.addResponseElement("FullCountryInfoAllCountries","FullCountryInfoAllCountriesResponse/sequence/return/sequence/phoneCode")
				.finish();

		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, SOURCE_MODEL_NAME + ".xmi")
				.addModel(PROJECT_NAME, VIEW_MODEL_NAME + ".xmi")
				.finish();

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers().get(0).getLabel().getName());

		new ModelExplorer().deployVdb(PROJECT_NAME, VDB_NAME);

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);

		assertEquals(1, jdbchelper.getNumberOfResults(TESTSQL));
		assertEquals(3, jdbchelper.getNumberOfResults(TESTSQL1));
	}

}

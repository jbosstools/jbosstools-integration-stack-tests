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
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.WsdlImportWizard;
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
	private static final String SOURCE_MODEL_NAME = "Soap_Source.xmi";
	private static final String VIEW_MODEL_NAME = "Soap_View.xmi";
	private static final String VDB_NAME = "SOAP_VDB";
	private static final String TESTSQL = "exec FullCountryInfo('US')";
	private static final String TESTSQL1 = "exec FullCountryInfoAllCountries()";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	@Before
	public static void before() {
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

		WsdlImportWizard wsdlWizard = new WsdlImportWizard();

		wsdlWizard.setProfile("SOAP");
		wsdlWizard.setSourceModelName(SOURCE_MODEL_NAME);
		wsdlWizard.setViewModelName(VIEW_MODEL_NAME);

		wsdlWizard.addOperation("FullCountryInfo");
		wsdlWizard.addOperation("FullCountryInfoAllCountries");
		wsdlWizard.addRequestElement("FullCountryInfo/sequence/arg0");

		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/capitalCity");
		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/continentCode");
		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/currencyIsoCode");
		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/isoCode");
		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/name");
		wsdlWizard.addResponseElement("FullCountryInfoResponse/sequence/return/sequence/phoneCode");

		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/capitalCity");
		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/continentCode");
		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/currencyIsoCode");
		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/isoCode");
		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/name");
		wsdlWizard.addResponseElement("FullCountryInfoAllCountriesResponse/sequence/return/sequence/phoneCode");
		
		wsdlWizard.setJndiName("SOAPSource");
		wsdlWizard.execute();

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

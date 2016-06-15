package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
import org.jboss.tools.teiid.reddeer.wizard.WsdlImportWizard;
import org.junit.BeforeClass;
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

	@BeforeClass
	public static void before() {
		new ModelExplorerManager().createProject(PROJECT_NAME, false);
	}

	@Test
	public void test() {

		String profile = "SOAP";

		Properties wsdlCP = new Properties();
		wsdlCP.setProperty("wsdl", "http://ws-dvirt.rhcloud.com/dv-test-ws/soap?wsdl");
		wsdlCP.setProperty("endPoint", "Countries");

		new ConnectionProfileManager().createCPWSDL(profile, wsdlCP);

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
		
		VdbWizard vdbWizard = new VdbWizard();
		vdbWizard.open();
		vdbWizard.setLocation(PROJECT_NAME);
		vdbWizard.setName(VDB_NAME);
		vdbWizard.addModel(new String[] { PROJECT_NAME, VIEW_MODEL_NAME });
		vdbWizard.finish();

		new ServersView().open();
		new ServersViewExt().refreshServer(new ServersView().getServers().get(0).getLabel().getName());

		new ModelExplorer().executeVDB(PROJECT_NAME, VDB_NAME + ".vdb");

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);

		assertEquals(1, jdbchelper.getNumberOfResults(TESTSQL));
		assertEquals(3, jdbchelper.getNumberOfResults(TESTSQL1));

	}

}

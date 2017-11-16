# Teiid Designer Bot Tests

## How to run

At first, please read a general documentation about [running bot tests](../../README.md).

To run the tests

    $ mvn clean verify -pl tests/org.jboss.tools.teiidwitchyard.ui.bot.test -am -Dtest=SmokeTests

To run the tests with a configuration

    $ mvn clean verify -pl tests/org.jboss.tools.teiid.ui.bot.test -am -Drd.config=...

The configuration file must follow the [SOA XSD schema](http://www.jboss.org/schema/reddeer/3rdparty/SOARequirements.xsd).

Example:
```
<?xml version="1.0" encoding="UTF-8"?>
<testrun
    xmlns="http://www.jboss.org/NS/Req"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:server="http://www.jboss.org/NS/SOAReq"
    xmlns:teiid="http://www.jboss.org/NS/SOAReq"
    xsi:schemaLocation="http://www.jboss.org/NS/Req	http://www.jboss.org/schema/reddeer/RedDeerSchema.xsd
    http://www.jboss.org/NS/SOAReq	http://www.jboss.org/schema/reddeer/3rdparty/SOARequirements.xsd">

    <requirements>

        <server:server-requirement name="DV-6.2.0.GA">
            <server:eap version="6.1+">
                <server:home>/home/apodhrad/Projects/server-installer/dv-6.2.0.GA/target/jboss-eap-6.4</server:home>
                <server:properties>
                    <adminPassword>dvdvdv0#</adminPassword>
                    <modeshapeUser>modeshapeUser</modeshapeUser>
                    <modeshapePassword>dvdvdv0#</modeshapePassword>
                    <modeshape>dv</modeshape>
                    <teiidUser>teiidUser</teiidUser>
                    <teiidPassword>dvdvdv0#</teiidPassword>
                </server:properties>
            </server:eap>
        </server:server-requirement>

        <teiid:teiid-requirement name="DV-6.2.0.GA-Teiid">
            <server:eap version="6.1+">
                <server:home>/home/apodhrad/Projects/server-installer/dv-6.2.0.GA/target/jboss-eap-6.4</server:home>
                <server:properties>
                    <adminPassword>dvdvdv0#</adminPassword>
                    <modeshapeUser>modeshapeUser</modeshapeUser>
                    <modeshapePassword>dvdvdv0#</modeshapePassword>
                    <modeshape>dv</modeshape>
                    <teiidUser>teiidUser</teiidUser>
                    <teiidPassword>dvdvdv0#</teiidPassword>
                </server:properties>
            </server:eap>

            <teiid:connection-profiles>
                <teiid:connection-profile name="mysql_51">
                    <teiid:hostname>jdbc:mysql://mysql.example.com:3306/test</teiid:hostname>
                    <teiid:vendor>MySQL</teiid:vendor>
                    <teiid:template>MySQL JDBC Driver</teiid:template>
                    <teiid:name>bqt2</teiid:name>
                    <teiid:description>mysql_51</teiid:description>
                    <teiid:jdbc_path>/home/apodhrad/Projects/server-installer/dv-6.2.0.GA/target/drivers/mysql-51/mysql-connector-java-5.1.17-bin.jar</teiid:jdbc_path>
                    <teiid:password>test</teiid:password>
                    <teiid:version>5.1</teiid:version>
                    <teiid:jdbc_class>com.mysql.jdbc.Driver</teiid:jdbc_class>
                    <teiid:port>3306</teiid:port>
                    <teiid:username>test</teiid:username>
                    <teiid:properties>
                        <url>jdbc:mysql://mysql.example.com:3306/test</url>
                        <providerID>org.eclipse.datatools.enablement.mysql.connectionProfile</providerID>
                        <defnType>org.eclipse.datatools.enablement.mysql.5_0.driverTemplate</defnType>
                    </teiid:properties>
                </teiid:connection-profile>

                <teiid:connection-profile name="excel_smalla">
                    <teiid:vendor>Server</teiid:vendor>
                    <teiid:username></teiid:username>
                    <teiid:properties>
                        <path>/home/apodhrad/Projects/server-installer/dv-6.2.0.GA/target</path>
                        <filename>smalla.xlsx</filename>
                    </teiid:properties>
                </teiid:connection-profile>
            </teiid:connection-profiles>


        </teiid:teiid-requirement>
    </requirements>
</testrun>
```

To run the tests from IDE import the following projects as an existing maven project
- org.jboss.tools.common.reddeer
- org.jboss.tools.runtime.reddeer
- org.jboss.tools.teiid.reddeer
- org.jboss.tools.teiid.ui.bot.test

Then, open **Run > Run Configurations...** and find the category **RedDeer Test**. Switch to the tab **Arguments** and add a VM argument **-Drd.config=...** if needed.

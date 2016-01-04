# SwitchYard Bot Tests

## How to run

At first, please read a general documentation about [running bot tests](../../README.md).

To run the tests

    $ mvn clean verify -pl tests/org.jboss.tools.switchyard.ui.bot.test -am -Dtest=SmokeTests

To run the tests with a configuration

    $ mvn clean verify -pl tests/org.jboss.tools.switchyard.ui.bot.test -am -Drd.config=...

The configuration file must follow the [SOA XSD schema](http://www.jboss.org/schema/reddeer/3rdparty/SOARequirements.xsd).

Example:
```
<?xml version="1.0" encoding="UTF-8"?>
<testrun
  xmlns="http://www.jboss.org/NS/Req"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:server="http://www.jboss.org/NS/SOAReq"
  xmlns:switchyard="http://www.jboss.org/NS/SOAReq"
  xsi:schemaLocation="http://www.jboss.org/NS/Req	http://www.jboss.org/schema/reddeer/RedDeerSchema.xsd
                      http://www.jboss.org/NS/SOAReq	http://www.jboss.org/schema/reddeer/3rdparty/SOARequirements.xsd">

  <requirements>
    <server:server-requirement name="JBoss Fuse on EAP 6.2.1.GA">
    <switchyard:switchyard-requirement name="JBoss Fuse on EAP 6.2.1.GA-SwitchYard">
      <switchyard:configurationVersion>2.0</switchyard:configurationVersion>
      <switchyard:targetRuntime>SwitchYard: AS7 Extension 2.0.1.redhat-621084</switchyard:targetRuntime>
      <switchyard:libraryVersion>2.0.1.redhat-621084</switchyard:libraryVersion>
      <server:eap version="6.1+">
        <server:home>/home/apodhrad/Projects/server-installer/fuse-eap-6.2.1.GA/target/jboss-eap-6.4</server:home>
      </server:eap>
    </switchyard:switchyard-requirement>
  </requirements>

</testrun>
```

To run the tests from IDE import the following projects as an existing maven project
- org.jboss.tools.common.reddeer
- org.jboss.tools.runtime.reddeer
- org.jboss.tools.bpel.reddeer
- org.jboss.tools.bpmn2.reddeer
- org.jboss.tools.drools.reddeer
- org.jboss.tools.fuse.reddeer
- org.jboss.tools.switchyard.reddeer
- org.jboss.tools.switchyard.ui.bot.test

Then, open **Run > Run Configurations...** and find the category **RedDeer Test**. Switch to the tab **Arguments** and add a VM argument **-Drd.config=...** if needed.

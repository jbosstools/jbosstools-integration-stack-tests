# ESB Bot Tests

## How to run

At first, please read a general documentation about [running bot tests](../../README.md).

To run the tests

    $ mvn clean verify -pl tests/org.jboss.tools.esb.ui.bot.test -am -Dtest=SmokeTests

To run the tests with a configuration

    $ mvn clean verify -pl tests/org.jboss.tools.esb.ui.bot.test -am -Drd.config=...

The configuration file must follow the [SOA XSD schema](http://www.jboss.org/schema/reddeer/3rdparty/SOARequirements.xsd).

Example:
```
<?xml version="1.0" encoding="UTF-8"?>
<testrun
  xmlns="http://www.jboss.org/NS/Req"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:server="http://www.jboss.org/NS/SOAReq"
  xmlns:runtime="http://www.jboss.org/NS/SOAReq"
  xsi:schemaLocation="http://www.jboss.org/NS/Req	http://www.jboss.org/schema/reddeer/RedDeerSchema.xsd
                      http://www.jboss.org/NS/SOAReq	http://www.jboss.org/schema/reddeer/3rdparty/SOARequirements.xsd">

  <requirements>
    <server:server-requirement name="SOA-5.3.1.GA">
      <server:eap version="5.x">
        <server:home>/home/apodhrad/Projects/server-installer/soa-5.3.1.GA/target/jboss-soa-p-5/jboss-as</server:home>
	<server:jre>/opt/java/jdk1.7.0_79</server:jre>
      </server:eap>
    </server:server-requirement>
    <runtime:runtime-requirement name="ESB-4.11">
      <runtime:esb version="4.11">
	<runtime:home>/home/apodhrad/Projects/server-installer/soa-5.3.1.GA/target/jboss-soa-p-5</runtime:home>
      </runtime:esb>
    </runtime:runtime-requirement>
  </requirements>

</testrun>

```

To run the tests from IDE import the following projects as an existing maven project
- org.jboss.tools.common.reddeer
- org.jboss.tools.runtime.reddeer
- org.jboss.tools.esb.reddeer
- org.jboss.tools.esb.ui.bot.test

Then, open **Run > Run Configurations...** and find the category **RedDeer Test**. Switch to the tab **Arguments** and add a VM argument **-Drd.config=...** if needed.
# BPMN2 Bot Tests

## How to run

At first, please read a general documentation about [running bot tests](../../README.md).

To run the tests

    $ mvn clean verify -pl tests/org.jboss.tools.bpmn2.ui.bot.test -am -Dtest=SmokeTests

You can also specify the jBPM version by

    $ mvn clean verify -pl tests/org.jboss.tools.bpmn2.ui.bot.test -am -Ddroolsjbpm.version=6.3.0.Final

Before 

To run the tests from IDE you have to build **org.jboss.tools.bpmn2.ui.bot.test** from cmd

    $ mvn clean package -pl tests/org.jboss.tools.bpmn2.ui.bot.test -am -DskipTests

which copies all needed libraries to /lib folder. Now, you can import the following projects as an existing maven project
- org.jboss.tools.bpmn2.reddeer
- org.jboss.tools.bpmn2.ui.bot.test

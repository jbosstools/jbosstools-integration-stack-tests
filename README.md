# JBoss Tools Integration Stack Tests

This repository contains test plugins for overall integration testing of JBoss Tools Integration Stack (JBTIS). 

##### Table of Contents
- [Get the code](#getthecode)
- [Build the code](#buildthecode)
- [Run tests from cmd](#runtestsfromcmd)
- [Run tests from IDE](#runtestsfromide)
- [List of components](#listofcomponents)
- [How to contribute](#howtocontribute)

<a name="getthecode"/>
## Get the code

The easiest way to get started with the code is to [create your own fork](http://help.github.com/forking/), 
and then clone your fork:

    $ git clone git@github.com:<you>/jbosstools-integration-stack-tests.git
    $ cd jbosstools-integration-stack-tests
    $ git remote add upstream git://github.com/jbosstools/jbosstools-integration-stack-tests.git
	
At any time, you can pull changes from the upstream and merge them onto your master:

    $ git checkout master               # switches to the 'master' branch
    $ git pull upstream master          # fetches all 'upstream' changes and merges 'upstream/master' onto your 'master' branch
    $ git push origin                   # pushes all the updates to your fork, which should be in-sync with 'upstream'

The general idea is to keep your 'master' branch in-sync with the
'upstream/master'.

<a name="buildthecode"/>
## Build the code

To build _JBoss Tools Integration Stack Tests_ you need
* JDK 1.8
* Maven 3.0.5 or 3.1.1 (not higher!)

You also need to add the following lines into ~/.m2/settings.xml
```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">  
...
  <profiles>  
  ...
    <profile>
      <id>jboss-default</id>
      <repositories>
        <!-- To resolve parent artifact -->
        <repository>
          <id>jboss-public-repository-group</id>
          <name>JBoss Public Repository Group</name>
          <url>http://repository.jboss.org/nexus/content/groups/public/</url>
        </repository>
        <repository>
          <id>jboss-snapshots-repository</id>
          <name>JBoss Snapshots Repository</name>
          <url>https://repository.jboss.org/nexus/content/repositories/snapshots/</url>
        </repository>
      </repositories>
	
      <pluginRepositories>
        <!-- To resolve parent artifact -->
        <pluginRepository>
          <id>jboss-public-repository-group</id>
          <name>JBoss Public Repository Group</name>
          <url>http://repository.jboss.org/nexus/content/groups/public/</url>
        </pluginRepository>
        <pluginRepository>
          <id>jboss-snapshots-repository</id>
          <name>JBoss Snapshots Repository</name>
          <url>https://repository.jboss.org/nexus/content/repositories/snapshots/</url>
        </pluginRepository>
      </pluginRepositories>
    </profile>
  </profiles>

  <activeProfiles>
    <activeProfile>jboss-default</activeProfile>
    ...
  </activeProfiles>
</settings>
```

This command will run the build:

    $ mvn clean package -DskipTests -Dtycho.localArtifacts=ignore

**Note:** _Usage of -Dtycho.localArtifacts=ignore is needed if you have built some components locally (bpel, switchyard, reddeer, swtbot, ...)._

<a name="runtestsfromcmd"/>
## Run tests from cmd

The test execution can be affected by mouse moving/clicking. That's why we recommend to run tests in another display (vnc, xephyr, ...).

To run all smoke tests

    $ mvn clean verify -Dtest=SmokeTests

To run tests for a specific component

    $ mvn clean verify -pl tests/org.jboss.tools.<component>.ui.bot.test -am -Dtest=SmokeTests

**Note:** _The deafult test is AllTests._

For running the tests against an existing instance of Eclipse or JBDS use the following command

    $ mvn clean verify -Dtest.installBase=...

**Note:** _Some tests require a specific configuration (runtime, credentials, ...). Please see the appropriate readme file of a specific [component](#listofcomponents)._

To run a test with a configuration file use

    $ mvn clean verify -Drd.config=...

<a name="runtestsfromide"/>
## Run tests from IDE

The test execution can be affected by mouse moving/clicking. That's why we recommend to run tests in another display (vnc, xephyr, ...).

Prepare your environment as follows

1. Install JBDS 9 (Eclipse Mars for Java EE Developers) and JBDS-IS 9.x (JBTIS 4.3.x)
2. Install RedDeer from http://download.jboss.org/jbosstools/updates/stable/mars/core/reddeer/1.0.0
3. Install SWTBot API (except Nebula Gallery) from http://download.eclipse.org/technology/swtbot/releases/latest
4. Install Tycho plugin from http://repo1.maven.org/maven2/.m2e/connectors/m2eclipse-tycho/0.8.0/N/LATEST
5. Import JBTIS plugins as an existing maven project
6. Each test plugin contains a launcher which is available via Run > Run Configurations...
7. In Run Configuration you can set the display (tab Environment) and additional configurations (tab Arguments)

**Note:** _Some tests require a specific configuration (runtime, credentials, ...). Please see the appropriate readme file of a specific [component](#listofcomponents)._

<a name="listofcomponents"/>
## List of components

- [BPEL](tests/org.jboss.tools.bpel.ui.bot.test/README.md)
- [BPMN2](tests/org.jboss.tools.bpmn2.ui.bot.test/README.md)
- [Drools](tests/org.jboss.tools.drools.ui.bot.test/README.md)
- [ESB](tests/org.jboss.tools.esb.ui.bot.test/README.md)
- [Fuse](tests/org.jboss.tools.fuse.ui.bot.test/README.md)
- [jBPM3](tests/org.jboss.tools.jbpm.ui.bot.test/README.md)
- [ModeShape](tests/org.jboss.tools.modeshape.ui.bot.test/README.md)
- [SwitchYard](tests/org.jboss.tools.switchyard.ui.bot.test/README.md)
- [Teiid Designer](tests/org.jboss.tools.teiid.ui.bot.test/README.md)

<a name="howtocontribute"/>
## How to contribute

_JBoss Tools Integration Stack Tests_ is open source, and we welcome anybody that wants to
participate and contribute!

If you want to fix a bug or make any changes, please do following:

1. Log an issue describing the bug or new feature here on [GitHub](https://github.com/jbosstools/jbosstools-integration-stack-tests/issues/new)
2. Update your local master branch

	```
	$ git checkout master               # switches to the 'master' branch
	$ git pull upstream master          # fetches all 'upstream' changes and merges 'upstream/master'
	                                      onto your 'master' branch
	```

3. Make the changes on a topic branch named with issue number. For example, this command creates a branch for the *#1234* issue:

	```
	$ git checkout -b JBTISTEST-1234
	```

4. After you're happy with your changes and a full build (with unit tests) runs successfully, commit your changes on your topic branch (with good comments):

	```
	$ git add <file>                    # adds new/changed files
	$ git commit                        # makes a commit from all added files
	```

   **Note:** _We recommend making a comment of the commit with name of the issue on GitHub plus *'#1234' (issue number)*. The issue number in the comment cause that your commit is visible in issue's comments._

5. Check for any recent changes that were made in the official repository:

	```
	$ git checkout master               # switches to the 'master' branch
	$ git pull upstream master          # fetches all 'upstream' changes and merges 'upstream/master'
	                                      onto your 'master' branch
	$ git checkout JBTISTEST-1234       # switches to your topic branch
	$ git rebase master                 # reapplies your changes on top of the latest in master
	                                      (i.e., the latest from master will be the new base for your changes)
	```

   **Note:** _If the pull grabbed a lot of changes, you should rerun your build with tests enabled to make sure your changes are still good._

6. Push your topic branch and its changes into your public fork repository:

	```
	$ git push origin JBTISTEST-1234    # pushes your topic branch into your public fork of JBTIS Tests
	```

7. Link your topic branch with the created issue:
   * clone this repository:

	```
	$ git clone https://github.com/apodhrad/simple-github-client
	```

     **Note:** _It contains only one Groovy script that links your topic branch on GitHub with the corresponding issue in JBoss Tools Integration Stack Tests repository._

   * run the Groovy script 

	```
	$ groovy create_pull_request.groovy
	username: <you>                     # username on GitHub
	password: <password>                # password associated with your username on GitHub
	issue: 1234                         # number of the issue
	```

8. Check that your issue on GitHub was moved to [Pull Requests](https://github.com/jbosstools/jbosstools-integration-stack-tests/pulls). Then we can review the proposed changes, comment on them, discuss them with you, and if everything is good merge the changes right into the official repository

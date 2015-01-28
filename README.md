# JBoss Tools Integration Stack Tests

## Summary

JBoss Tools Integration Stack Tests contains test plugins for overall integration testing of JBoss Tools Integration Stack (JBTIS). 

## Install

_JBoss Tools Integration Stack Tests_ is part of [JBoss Tools](http://jboss.org/tools) from
which it can be [downloaded and installed](http://jboss.org/tools/download)
on its own or together with the full JBoss Tools distribution.

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

## Building JBoss Tools Integration Tests

To build _JBoss Tools Integration Stack Tests_ requires specific versions of Java and
Maven. Also, there is some Maven setup. The [How to Build JBoss Tools with Maven 3](https://community.jboss.org/wiki/HowToBuildJBossToolsWithMaven3)
document will guide you through that setup.

This command will run the build:

    $ mvn clean verify

If you just want to check if things compiles/builds you can run:

    $ mvn clean verify -DskipTest=true

But *do not* push changes without having the new and existing unit tests pass!

For running the tests against an existing instance of Eclipse or JBDS use the following

    $ mvn clean verify -Dtest.installPath=...
 
## Contribute fixes and features

_JBoss Tools Integration Stack Tests_ is open source, and we welcome anybody that wants to
participate and contribute!

If you want to fix a bug or make any changes, please do following:

1. log an issue describing the bug or new feature here on [GitHub](https://github.com/jbosstools/jbosstools-integration-stack-tests/issues/new)
2. update your local master branch

	```
	$ git checkout master               # switches to the 'master' branch
	$ git pull upstream master          # fetches all 'upstream' changes and merges 'upstream/master'
	                                      onto your 'master' branch
	```

3. make the changes on a topic branch named with issue number. For example, this command creates a branch for the *#1234* issue:

	```
	$ git checkout -b JBTISTEST-1234
	```

4. after you're happy with your changes and a full build (with unit tests) runs successfully, commit your changes on your topic branch (with good comments):

	```
	$ git add <file>                    # adds new/changed files
	$ git commit                        # makes a commit from all added files
	```

   **Note:** We recommend making a comment of the commit with name of the issue on GitHub plus *'#1234' (issue number)*. The issue number in the comment cause that your commit is visible in issue's comments.

5. check for any recent changes that were made in the official repository:

	```
	$ git checkout master               # switches to the 'master' branch
	$ git pull upstream master          # fetches all 'upstream' changes and merges 'upstream/master'
	                                      onto your 'master' branch
	$ git checkout JBTISTEST-1234       # switches to your topic branch
	$ git rebase master                 # reapplies your changes on top of the latest in master
	                                      (i.e., the latest from master will be the new base for your changes)
	```

   **Note:** If the pull grabbed a lot of changes, you should rerun your build with tests enabled to make sure your changes are still good.

6. push your topic branch and its changes into your public fork repository:

	```
	$ git push origin JBTISTEST-1234    # pushes your topic branch into your public fork of JBTIS Tests
	```

7. link your topic branch with the created issue:
   * clone this repository:

	```
	$ git clone https://github.com/apodhrad/simple-github-client
	```

     **Note:** It contains only one Groovy script that links your topic branch on GitHub with the corresponding issue in JBoss Tools Integration Stack Tests repository.

   * run the Groovy script 

	```
	$ groovy create_pull_request.groovy
	username: <you>                     # username on GitHub
	password: <password>                # password associated with your username on GitHub
	issue: 1234                         # number of the issue
	```

8. check that your issue on GitHub was moved to [Pull Requests](https://github.com/jbosstools/jbosstools-integration-stack-tests/pulls). Then we can review the proposed changes, comment on them, discuss them with you, and if everything is good merge the changes right into the official repository

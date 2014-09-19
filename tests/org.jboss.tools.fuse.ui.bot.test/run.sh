#!/bin/bash

SERVER_CONFIG="./target/server-installer/fuse-6.1.0.GA/target/server-settings.xml"

function clean {
	echo ""
	echo "--------------------------------------------------------------------------------"
	echo "                                  Cleaning ...                                  "
	echo "--------------------------------------------------------------------------------"
	echo ""
	mvn clean
	cd ../../
	mvn clean -pl tests/org.jboss.tools.fuse.ui.bot.test -am
	cd tests/org.jboss.tools.fuse.ui.bot.test
}





function install_fuse_server {
	echo ""
	echo "--------------------------------------------------------------------------------"
	echo "                        Installing JBoss Fuse Server ...                        "
	echo "--------------------------------------------------------------------------------"
	echo ""
	mkdir target
	cd target
	git clone https://github.com/apodhrad/server-installer.git
	cd server-installer/fuse-6.1.0.GA
	mvn clean install
	cd ../../../
}





function execute_tests () {
	echo ""
	echo "--------------------------------------------------------------------------------"
	echo "                              Executing tests ...                               "
	echo "--------------------------------------------------------------------------------"
	echo ""
	cd ../../
	DISPLAY=:2 mvn verify -pl tests/org.jboss.tools.fuse.ui.bot.test -am -Dreddeer.config=$SERVER_CONFIG -Dtest=$TEST -Dmaven.test.failure.ignore=$IGNORE_TEST_FAILURE
}






function test_suite {

	echo ""
	echo "--------------------------------------------------------------------------------"
	echo "List of available suites:"
	echo "1 - Smoke Tests"
	echo "2 - Without Server Tests"
	echo "3 - Server Tests"
	echo "4 - Regression Tests"
	echo "5 - All Tests"
	echo ""

	TEST="0"
	while [ "$TEST" == "0" ]; do
		echo -n "Select suite: "
		read input
		case $input in
			[1] )
				TEST=SmokeTests
				;;
			[2] )
				TEST=WithoutServerTests
				;;
			[3] )
				TEST=ServerTests
				;;
			[4] )
				TEST=RegressionTests
				;;
			[5] )
				TEST=AllTests
				;;
			*)
				echo "Invalid input! Try it again."
				;;
		esac
	done

	clean
	install_fuse_server
	execute_tests $TEST
}




function test_case {
	echo ""
	echo "--------------------------------------------------------------------------------"
	echo "List of available tests:"
	echo "1 - Camel Editor Test (try to use every patern in Camel Editor)"
	echo "2 - Deployment Test (deploy folder and JMX deployment)"
	echo "3 - Fuse Project Test (try to create projects from all available archetypes)"
	echo "4 - JMX Navigator Test (JMX Navigator View)"
	echo "5 - Project Local Run Test (tests in project, deploy to Local Camel Context)"
	echo "6 - Server Test (manipulation with servers)"
	echo "7 - Smoke Test (check views, perspectives, project creation)"
	echo "8 - Regression Test (test resolved issues)"
	echo "9 - Debugger Test (check camel route debugger)"
	echo "10 - JMX Navigator Test (Server side)"
	echo "11 - Remote Route Editing, Tracing"
	echo ""

	TEST="0"
	while [ "$TEST" == "0" ]; do
		echo -n "Select test: "
		read input
		case $input in
			[1] )
				TEST=CamelEditorTest
				;;
			[2] )
				TEST=DeploymentTest
				;;
			[3] )
				TEST=FuseProjectTest
				;;
			[4] )
				TEST=JMXNavigatorTest
				;;
			[5] )
				TEST=ProjectLocalRunTest
				;;
			[6] )
				TEST=ServerTest
				;;
			[7] )
				TEST=SmokeTest
				;;
			[8] )
				TEST=RegressionTest
				;;
			[9] )
				TEST=DebuggerTest
				;;
			10)
				TEST=JMXNavigatorServerTest
				;;
			11)
				TEST=RouteManipulationTest
				;;
			*)
				echo "Invalid input! Try it again."
				;;
		esac
	done

	clean
	install_fuse_server
	execute_tests $TEST
}




echo ""
echo ""
echo "================================================================================"
echo "               Welcome to JBoss Fuse Tooling automated test tool                "
echo "================================================================================"
echo ""
cat description.txt
echo ""
echo "What do you want to do?"
echo "-----------------------"
echo "1 - run a test suite"
echo "2 - run a particular test"
echo ""

correct_input="0"
while [ $correct_input -eq 0 ]; do
	echo -n "type '1' or '2': "
	read suite_or_test
	case $suite_or_test in
		[1] )
			let "correct_input=1"
			;;
		[2] )
			let "correct_input=2"
			;;
		*)
			echo "Invalid input! Try it again."
			;;
	esac
done

case $correct_input in
	[1] )
		test_suite
		;;
	[2] )
		test_case
		;;
esac

#!/bin/bash

SERVER_CONFIG="./target/server-installer/fuse-6.1.0.GA/target/server-settings.xml"
ID="AmazonEC2ID"
PASS="AmazonEC2Password"
EMAIL="AmazonEC2Owner"
IGNORE_TEST_FAILURE="false"

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
	DISPLAY=:2 mvn verify -pl tests/org.jboss.tools.fuse.ui.bot.test -am -Dreddeer.config=$SERVER_CONFIG -Dtest=$TEST -Dec2.id=$ID -Dec2.pass=$PASS -Dec2.email=$EMAIL -Dmaven.test.failure.ignore=$IGNORE_TEST_FAILURE
}





function set_credentials {
	echo ""
	echo "The chosen test/suite required access to Amazon EC2 Cloud. Please provide credentials."
	echo -n "Access Key ID: "
	read ID
	echo -n "Secret Access Key: "
	read PASS
	echo -n "E-mail: "
	read EMAIL
}





function test_suite {

	echo ""
	echo "--------------------------------------------------------------------------------"
	echo "List of available suites:"
	echo "1 - Smoke Tests"
	echo "2 - Without Server Tests"
	echo "3 - Server Tests"
	echo "4 - All Tests"
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
	echo "2 - Deployment Test (deploy folder, JMX and deployment into Fabric profile)"
	echo "3 - Fabric Explorer Test (Fabric Explorer View)"
	echo "4 - Fabric In Cloud Test (test Fabric in Cloud)"
	echo "5 - Fuse Project Test (try to create projects from all available archetypes)"
	echo "6 - JMX Navigator Test (JMX Navigator View)"
	echo "7 - Project Local Run Test (tests in project, deploy to Local Camel Context)"
	echo "8 - Server Test (manipulation with servers)"
	echo "9 - Smoke Test (check views, perspectives, project creation)"
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
				TEST=FabricExplorerTest
				;;
			[4] )
				TEST=FabricInCloudTest
				;;
			[5] )
				TEST=FuseProjectTest
				;;
			[6] )
				TEST=JMXNavigatorTest
				;;
			[7] )
				TEST=ProjectLocalRunTest
				;;
			[8] )
				TEST=ServerTest
				;;
			[9] )
				TEST=SmokeTest
				;;
			*)
				echo "Invalid input! Try it again."
				;;
		esac
	done

	if [ "$input" == "4" ]
		then
			#Ignore test failure - ensures execution of amazon-terminator after test failure
			IGNORE_TEST_FAILURE="true"
			set_credentials
	fi
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

package ${package}

import org.switchyard.Message

global java.lang.String userName

rule "Is of valid age"
	when
		$a : Applicant( age > 17 )
	then
		$a.setValid( true );
		System.out.println("********** " + $a.getName() + " is a valid applicant **********");
end

rule "Is not of valid age"
	when
		$a : Applicant( age < 18 )
	then
		$a.setValid( false );
		System.out.println("***" + $a.getName() + " is not a valid applicant ***");
end

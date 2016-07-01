<?php
	$db_host = getenv("$OPENSHIFT_MYSQL_DB_HOST");
	$db_port = getenv("$OPENSHIFT_MYSQL_DB_PORT");

	$conn = mysqli_connect($db_host, "adminRnBGfIR", "ex5-R_qnF1Cv", "instructortracker", $db_port);
	
	if (!$conn) {
		die("Fail\n".mysqli_connect_error());
	}
?>
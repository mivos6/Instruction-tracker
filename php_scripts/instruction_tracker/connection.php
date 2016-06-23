<?php
	$conn = @mysqli_connect("localhost", "root", "", "instruction_tracker_db", "3306");
	
	if (!$conn) {
		die("Fail\n".mysqli_connect_error());
	}
?>
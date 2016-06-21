<?php
	$conn = @mysqli_connect("localhost", "root", "", "instruction_tracker_db", "3306");
	
	if (!$conn) {
		die("Failed to connect: ".mysqli_connect_error());
	}
?>
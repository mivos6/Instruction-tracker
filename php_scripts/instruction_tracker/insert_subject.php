<?php
	include("connection.php");
	
	$query_str = "INSERT INTO subjects (username, subject_name, subject_tags) ".
			"VALUES ('$_POST[username]', '$_POST[subject_name]', '$_POST[subject_tags]');";
	
	@mysqli_query($conn, $query_str);
	
	if (mysqli_affected_rows($conn) > 0) echo "Success";
	else echo "Fail\n".mysqli_error($conn);
?>
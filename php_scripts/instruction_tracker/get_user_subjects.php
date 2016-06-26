<?php
	include("connection.php");
	
	$query_str = "SELECT * FROM subjects ".
			"WHERE username='$_POST[username]';";
			
	$result = @mysqli_query($conn, $query_str);
	if (!$result) {
		die("Fail\n".mysqli_error($conn));
	}
	else  {
		echo "Success\n";
	}
	
	if (mysqli_affected_rows($conn) > 0) {
		while ($row = mysqli_fetch_array($result)) {
			echo json_encode($row).";";
		}
	}
?>
<?php
	include("connection.php");
	
	$username = $_POST["username"];
	$passw = md5($_POST["passw"]);
	
	$query_str = "SELECT * FROM users ".
		"WHERE username='$username' ".
		"AND passw='$passw';";
		
	$result = @mysqli_query($conn, $query_str);
	if (!$result) {
		die("Fail\n".mysqli_error($conn));
	}
	
	$row = mysqli_fetch_array($result);
	if ($row) {
		echo "Success\n";
		echo json_encode($row);
	}
?>
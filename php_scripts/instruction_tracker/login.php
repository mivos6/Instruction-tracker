<?php
	include("connection.php");
	
	$username = $_POST["username"];
	$passw = md5($_POST["passw"]);
	
	$query_str = "SELECT * FROM users ".
		"WHERE username='$username' ".
		"AND passw='$passw';";
		
	$result = @mysqli_query($conn, $query_str);
	if (!$result) {
		die("Invalid query: ".mysqli_error());
	}
	
	$row = mysqli_fetch_array($result);
	if ($row)
		echo json_encode($row);
?>
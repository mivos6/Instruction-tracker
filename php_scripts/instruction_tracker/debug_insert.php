<?php
	include("connection.php");
	
	$username = "mivos6";
	$passw = "mivos286";
	$name = "Milan Ivošević";
	$email = "mivos6@gmail.com";
	$location = "Vukovar";
	
	$enc = md5($passw);
	echo $enc;
	
	$query_str = "INSERT INTO users ".
		"(username, passw, name, email, location) ".
		"VALUES ('$username', '$enc', '$name', '$email', '$location');";
		
	if (!$res = @mysqli_query($conn, $query_str))
		echo "Invalid query: ".mysqli_error($conn);
?>
<?php
	include("connection.php");
	
	$enc = md5($_POST["passw"]);
	
	$query_str = "INSERT INTO users (username, passw, name, email, phone_num, location, about, img_url)".
		"VALUES ('$_POST[username]', '$enc', '$_POST[name]', '$_POST[email]', '$_POST[phone_num]', '$_POST[location]', '$_POST[about]', '$_POST[img_url]');";
		
	@mysqli_query($conn, $query_str);
	
	if (mysqli_affected_rows($conn) > 0) echo "Success";
	else echo "Fail\n".mysqli_error($conn);
?>
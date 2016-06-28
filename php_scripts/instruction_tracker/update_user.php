<?php
	include("connection.php");
	
	$enc = md5($_POST["passw"]);
	
	$query_str = "UPDATE users ".
			"SET username='$_POST[username]', passw='$enc', name='$_POST[name]', email='$_POST[email]', phone_num='$_POST[phone_num]', location='$_POST[location]', about='$_POST[about]', img_url='$_POST[img_url]' ".
			"WHERE username='$_POST[old_username]';";
		
	@mysqli_query($conn, $query_str);
	
	if (!$error = mysqli_error($conn)) echo "Success";
	else echo "Fail\n".$error;
?>
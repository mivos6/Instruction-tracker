<?php
	include("connection.php");
	
	$query_str = "UPDATE subjects ".
			"SET subject_name='$_POST[subject_name]', subject_tags='$_POST[subject_tags]' ".
			"WHERE subject_id='$_POST[subject_id]';";
	
	@mysqli_query($conn, $query_str);
	
	if (mysqli_affected_rows($conn) > 0) echo "Success";
	else echo "Fail\n".mysqli_error($conn);
?>
<?php
	include("connection.php");
	
	$query_str = "SELECT users.username, users.name, users.email, users.phone_num, users.location, users.about, users.img_url, ".
			"subjects.subject_name, subjects.subject_tags ".
			"FROM users, subjects ".
			"WHERE users.username = subjects.username ";
			
	if (isset($_POST["subject_name"])) {
		$query_str = $query_str."AND CONCAT(subjects.subject_name, ',', subject_tags) LIKE '%$_POST[subject_name]%' ";
	}
	
	if (isset($_POST["location"])) {
		$query_str = $query_str."AND users.location LIKE '%$_POST[location]%' ";
	}
	
	$query_str = $query_str."GROUP BY users.username, subjects.subject_name;";
			
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
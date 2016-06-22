<?php
	if ($_FILES["uploaded_file"]["error"] > 0) {
		echo "Error";
		echo "<br/>".$_FILES["uploaded_file"]["error"]."<br>";
	}
	else {
		$upload_path = "Uploads/".basename($_FILES["uploaded_file"]["name"]);
		if (move_uploaded_file($_FILES["uploaded_file"]["tmp_name"], $upload_path))
			echo "Success";
		else
			echo "Fail";
	}
?>
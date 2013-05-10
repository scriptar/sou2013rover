<?php 
	include 'includes/connection.php';

	$query = "SELECT * FROM storage";

	$result = mysql_query($query);

	while( $store = mysql_fetch_array($result)){
		echo "<h3>" . $store["SCRIPT"] . "</h3>";
	}


 ?>
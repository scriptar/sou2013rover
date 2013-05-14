<?php
	require ("DBconn.class.php");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<link href="templatemo_style.css" type="text/css" rel="stylesheet" /> 
<head></head>
<body>
<div id="request_form">
	<form method="post" name="about" action="requests.php">
		<div class="left">
			<label for="author">Name:</label> <input name="author" type="text" class="input_field" id="author" maxlength="40" />
		</div>
		<div class="right">                           
			<label for="email">Email:</label> <input name="email" type="text" class="input_field" id="email" maxlength="40" />
		</div>
		<div class="clear"></div>
		<label for="text">Message:</label> <textarea id="text" name="message" rows="0" cols="0"></textarea>
		<input type="submit" class="submit_btn float_l" name="submit" id="submit" value="Send" />
	</form>
</div>
</body>
</html>
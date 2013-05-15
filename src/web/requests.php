<?php
	error_reporting(E_ALL);
	ini_set('display_errors', '1');
	require_once "includes/DbConn.class.php";
	$connect = new DBConn();
	$pdo = $connect->getConn();
	if (isset($_POST["didsubmit"])) {
		$cmd = $pdo->prepare("INSERT INTO requests (Name, Email, Message) VALUES (?, ?, ?)");
		$cmd->execute(array(htmlspecialchars($_POST["author"]), htmlspecialchars($_POST["email"]), htmlspecialchars($_POST["message"])));
	}
	$cmd = $pdo->prepare("SELECT * FROM requests ORDER BY id DESC");
	$cmd->execute();
	$cmd->bindColumn(1, $id);
	$cmd->bindColumn(2, $name);
	$cmd->bindColumn(3, $email);
	$cmd->bindColumn(4, $message);
	$list = "";
	while($cmd->fetch(PDO::FETCH_BOUND)){
		$list .= "<tr><td>$name</td><td>$message</td></tr>";
	}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="templatemo_style.css" type="text/css" rel="stylesheet" /> 
<head></head>
<body>
<div id="request_form">
<?php
	if (isset($_POST["didsubmit"])) {
		echo "<p style=\"text-decoration: italic;\">Thank you for submitting your request.</p>";
	} else {
?>
	<p>Got an idea? Let us know.</p>
	<form method="post" name="about" action="requests.php">
		<dl>
			<dt><label for="author">Name:</label></dt>
			<dd><input name="author" type="text" class="input_field" id="author" maxlength="100" /></dd>
			<dt><label for="email">Email:</label></dt>
			<dd><input name="email" type="text" class="input_field" id="email" maxlength="150" /></dd>
			<dt><label for="text">Message:</label></dt>
			<dd><textarea id="text" name="message" rows="0" cols="0"></textarea></dd>
		</dl>
		<input type="submit" class="submit_btn" name="submit" id="submit" value="Send" />
		<input type="hidden" name="didsubmit" value="1" />
	</form>
<?php
	}

	if (strlen($list) > 0) {
?>
	<table id="reqlist" style="width: 100%">
		<tr><th style="width: 20%; text-align: left">Name</th><th style="width: 80%; text-align: left">Message</th></tr>
		<?php echo $list; ?>
	</table>
<?php
	}
?>
</div>
</body>
</html>
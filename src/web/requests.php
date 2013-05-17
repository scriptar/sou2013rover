<?php
	error_reporting(E_ALL);
	ini_set("display_errors", "1");
	require_once "includes/DbConn.class.php";
	$connect = new DBConn();
	$pdo = $connect->getConn();
	if (isset($_POST["didsubmit"])) {
		$cmd = $pdo->prepare("INSERT INTO requests (Name, Email, Message) VALUES (?, ?, ?)");
		//htmlentities(trim($_POST["email"])),
		$cmd->execute(
			array(
				htmlentities(trim($_POST["author"])),
				"",
				htmlentities(trim(preg_replace('/[\r\n]+/', "\n", $_POST["message"])))
			)
		);
	}
	$cmd = $pdo->prepare("SELECT * FROM requests ORDER BY id DESC");
	$cmd->execute();
	$cmd->bindColumn(1, $id);
	$cmd->bindColumn(2, $name);
	$cmd->bindColumn(3, $email);
	$cmd->bindColumn(4, $message);
	$list = "";
	while ($cmd->fetch(PDO::FETCH_BOUND)) {
		$list .= "<tr><td id=\"msg$id\">$name</td><td class=\"msg\">$message</td></tr>";
	}
?>
<!DOCTYPE html>
<html>
<head>
<title>ROGO Requests</title>
<link href="templatemo_style.css" type="text/css" rel="stylesheet" />
<style type="text/css">
.msg {
	white-space: pre;
}
</style>
<script type="text/javascript">
var validate = function (f) {
	var goodToGo = false,
		msg;
	if (f.message.value.length == 0) {
		msg = document.getElementById("msgAlert");
		msg.innerHTML = "<br />Please enter some text.";
	} else
		goodToGo = true;
	return goodToGo;
};
</script>
</head>
<body>
<div id="request_form">
<?php
	if (isset($_POST["didsubmit"])) {
		echo "<p style=\"font-style: italic;\"><strong style=\"color: #FF0;\">Thank you</strong> for your feedback!<br />~The ROGO Team</p>";
	} else {
?>
	<p><em>What could we do that would make ROGO even better? Please give us feedback.</em></p>
	<form method="post" name="about" action="requests.php" onsubmit="return validate(this);">
		<dl>
			<dt><label for="author">Your Name:</label></dt>
			<dd><input name="author" type="text" class="input_field" id="author" maxlength="100" /></dd>
			<!--<dt><label for="email">E-mail Address:</label></dt>
			<dd><input name="email" type="text" class="input_field" id="email" maxlength="150" /> (will be private)</dd>-->
			<dt><label for="text">Comments or Ideas:</label><span id="msgAlert" style="color: #F00;"></span></dt>
			<dd><textarea id="text" name="message" rows="4" cols="60" wrap="hard"></textarea></dd>
		</dl>
		<div style="clear: both; text-align: center;"><input type="submit" class="submit_btn" name="submit" id="submit" value="Send" /></div>
		<input type="hidden" name="didsubmit" value="1" />
	</form>
<?php
	}

	if (strlen($list) > 0) {
?>
	<table id="reqlist" style="width: 100%">
		<tr><th style="width: 20%; text-align: left">Name</th><th style="width: 80%; text-align: left">Comments or Ideas</th></tr>
		<?php echo $list; ?>
	</table>
<?php
	}
?>
</div>
</body>
</html>
<?php
error_reporting(E_ALL);
ini_set("display_errors", true);
require_once "includes/DbConn.class.php";

header("Expires: Sat, 26 Jul 1997 05:00:00 GMT"); 
header("Cache-Control: no-cache");
header("Pragma: no-cache");
header("Content-Type: application/json;charset=UTF-8");

$connect = new DBConn();
if (isset($_POST["src"])) {
	echo json_encode(saveSrc($connect->getConn(), $_POST["src"], $_POST["tags"]));
} elseif (isset($_GET["list"])) {
	echo json_encode(getListAll($connect->getConn()));
} elseif (isset($_GET["tags"])) {
	echo json_encode(getListTags($connect->getConn(), $_GET["tags"]));
} elseif (isset($_GET["hash"])) {
	echo json_encode(getDetails($connect->getConn(), $_GET["hash"]));
} else {
	//nothing to save...
	$host = gethost();
	echo json_encode(array("id" => "", "hash" => "", "host" => $host["dns"]));
}
$connect = null;

function gethost() {
	$remoteAddr = array(
		"ip" => $_SERVER["REMOTE_ADDR"],
		"dns" => ""
	);
	if (strstr($remoteAddr["ip"], ', ')) {
		$tmp = explode(', ', $remoteAddr["ip"]);
		$remoteAddr["ip"] = $tmp[0];
	}
	if (preg_match('/^(?:25[0-5]|2[0-4]\d|1\d\d|[1-9]\d|\d)(?:[.](?:25[0-5]|2[0-4]\d|1\d\d|[1-9]\d|\d)){3}$/', $remoteAddr["ip"])) {
		$remoteAddr["dns"] = gethostbyaddr($remoteAddr["ip"]);
	}
	return $remoteAddr;
}

function saveSrc($pdo, $src, $tags) {
	$hash = md5($src);
	$tagArr = array();
	if (isset($tags)) {
		$tagArr = explode(" ", preg_replace('/\s+/', " ", preg_replace('/[^a-z0-9_\.\-]/i', " ", $tags)));
	}
	$srcid = -1;
	$savetime = -1;
	$cmd = $pdo->prepare("SELECT idsrc, savetime FROM src WHERE hash = ?");
	$cmd->execute(array($hash));
	if ($cmd->rowCount() > 0) {
		$cmd->bindColumn(1, $srcid);
		$cmd->bindColumn(2, $savetime);
		$cmd->fetch(PDO::FETCH_BOUND);
	} else {
		$host = gethost();
		$cmd = $pdo->prepare("INSERT INTO src (hash, source, host, savetime) VALUES (?, ?, ?, NOW())");
		$cmd->execute(
			array(
				$hash,
				$src,
				(strlen($host["dns"]) > 0 ? $host["dns"] : $host["ip"])
			)
		);
		$srcid = $pdo->lastInsertId();
		$cmd = $pdo->prepare("SELECT savetime FROM src WHERE idsrc = ?");
		$cmd->execute(array($srcid));
		$cmd->bindColumn(1, $savetime);
		$cmd->fetch(PDO::FETCH_BOUND);
	}
	if ($srcid != -1) {
		$tagsearchcmd = $pdo->prepare("SELECT idtags FROM tags WHERE tag = ?");
		$taginsertcmd = $pdo->prepare("INSERT INTO tags (tag) VALUES (?)");
		$srctagsearchcmd = $pdo->prepare("SELECT idsrctags FROM srctags WHERE idtags = ? AND idsrc = ?");
		$srctaginsertcmd = $pdo->prepare("INSERT INTO srctags (idtags, idsrc) VALUES (?, ?)");
		if (count($tagArr) > 0) {
			foreach ($tagArr as $tag) {
				$tagid = -1;
				$tagsearchcmd->execute(array($tag));
				if ($tagsearchcmd->rowCount() > 0) {
					$tagsearchcmd->bindColumn(1, $tagid);
					$tagsearchcmd->fetch(PDO::FETCH_BOUND);
				}
				if ($tagid == -1) {
					$taginsertcmd->execute(array($tag));
					$tagid = $pdo->lastInsertId();
				}
				$srctagsearchcmd->execute(array($tagid, $srcid));
				if ($srctagsearchcmd->rowCount() == 0) {
					$srctaginsertcmd->execute(array($tagid, $srcid));
				}
			}
		}
	}
	$pdo = null;
	return array(
		"id" => $srcid,
		"hash" => $hash,
		"savetime" => $savetime
	);
}

function getListAll($pdo) {
	$cmd = $pdo->prepare("SELECT src.idsrc, src.hash, src.savetime, IFNULL(tags.tag, '') AS tag FROM src LEFT OUTER JOIN srctags ON src.idsrc = srctags.idsrc LEFT OUTER JOIN tags ON srctags.idtags = tags.idtags ORDER BY src.savetime DESC");
	$cmd->execute();
	$cmd->bindColumn(1, $id);
	$cmd->bindColumn(2, $hash);
	$cmd->bindColumn(3, $savetime);
	$cmd->bindColumn(4, $tag);
	$list = array();
	while ($cmd->fetch(PDO::FETCH_BOUND)) {
		if (!array_key_exists($hash, $list)) {
			$list{$hash} = array(
				"savetime" => $savetime,
				"tags" => array()
			);
		}
		$list{$hash}{"tags"}[] = $tag;
	}
	$pdo = null;
	return $list;
}

function getListTags($pdo, $tags) {
	$tagArr = array();
	$tagSql = "";
	if (isset($tags)) {
		$tagArr = explode(" ", preg_replace('/\s+/', " ", preg_replace('/[^a-z0-9_\.\-]/i', " ", $tags)));
		$tagSql = "'" . implode("', '", $tagArr) . "'";
	}
	if (strlen($tagSql) == 0) {
		$tagSql = "''";
	}
	$cmd = $pdo->prepare("SELECT src.idsrc, src.hash, src.source, src.savetime, IFNULL(tags.tag, '') AS tag FROM src JOIN srctags ON src.idsrc = srctags.idsrc JOIN tags ON srctags.idtags = tags.idtags WHERE tags.tag IN ($tagSql) ORDER BY src.savetime DESC");
	$cmd->execute();
	$cmd->bindColumn(1, $id);
	$cmd->bindColumn(2, $hash);
	$cmd->bindColumn(3, $source);
	$cmd->bindColumn(4, $savetime);
	$cmd->bindColumn(5, $tag);
	$list = array();
	while ($cmd->fetch(PDO::FETCH_BOUND)) {
		if (!array_key_exists($tag, $list)) {
			$list{$tag} = array();
		}
		$list{$tag}[] = array(
			"hash" => $hash,
			"src" => $source,
			"savetime" => $savetime
		);
	}
	$pdo = null;
	return $list;
}

function getDetails($pdo, $hash) {
	$cmd = $pdo->prepare("SELECT src.idsrc, src.hash, src.source, src.savetime, src.host, IFNULL(tags.tag, '') AS tag FROM src LEFT OUTER JOIN srctags ON src.idsrc = srctags.idsrc LEFT OUTER JOIN tags ON srctags.idtags = tags.idtags WHERE src.hash = ?");
	$cmd->execute(array($hash));
	$cmd->bindColumn(1, $id);
	$cmd->bindColumn(2, $hash);
	$cmd->bindColumn(3, $src);
	$cmd->bindColumn(4, $savetime);
	$cmd->bindColumn(5, $host);
	$cmd->bindColumn(6, $tag);
	$details = array();
	while ($cmd->fetch(PDO::FETCH_BOUND)) {
		if (!array_key_exists("id", $details)) {
			$details{"id"} = $id;
			$details{"src"} = $src;
			$details{"savetime"} = $savetime;
			$details{"host"} = $host;
			$details{"tags"} = array();
		}
		$details{"tags"}[] = $tag;
	}
	$pdo = null;
	return $details;
}

<?php
/*
ConfigManager.class.php
Makes available configuratin data from XML file.
Usage:
	require_once("ConfigManager.class.php");
	$cfg = new ConfigManager();
	echo "Login Hash: " . $cfg->loginHash;
*/
class ConfigManager {
	private $configFile = 'rogoConfig.xml';
	private $doc = null;
	
	public function __construct($config = '') {
		$files = array(
			$config,
			(isset($_SERVER['DOCUMENT_ROOT']) ? $_SERVER['DOCUMENT_ROOT'] : '/home/SOU$/rogo/public_html') . '/includes/' . $this->configFile,
			$this->configFile
		);
		foreach ($files as $file) {
			if (file_exists($file)) {
				$this->configFile = $file;
				$this->doc = simplexml_load_file($this->configFile);
				break;
			}
		}
	}
	
	public function __get($key) {
		return (isset($this->doc) ? $this->doc->{$key} : null);
	}
	
	public function __destruct() {
		$this->doc = null;
	}
}
?>
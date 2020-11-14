<?php
require_once 'core/init.php';
// check db connection
$db = db::getInstance();
if($db->isConnected()){
	echo "connected!</br>";
}else{
	echo "not connected!</br>";
}
?>
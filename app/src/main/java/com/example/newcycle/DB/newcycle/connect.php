<?php
require_once 'core/init.php';
if($db->isConnected()){
	$result['success'] = "1";
	$result['message'] = "connect_success";
}else{
	$result['success'] = "0";
	$result['message'] = "connect_error";
}
echo json_encode($result);
?>
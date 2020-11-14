<?php
require_once 'classes/restapi.php';
require_once 'functions/function.php';
$rest = new RestAPI();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$data = filter($_POST, array('user_id'));
	$results = array();

	if(!isset($_POST['user_id'])){
		$results['success'] = '0';
		$results['message'] = 'undefined_user_id';
	}else{
		$rest->table('`order`');
		$where = getId($data, 'product_id');
		$where['user_id'] = $_POST['user_id'];
		$results = $rest->delete($where);
	}

	echo json_encode($results);
	
}else{
	header( 'HTTP/1.0 403 Forbidden', TRUE, 403 );
	die( header( 'location: 404.php' ) );
}


?>
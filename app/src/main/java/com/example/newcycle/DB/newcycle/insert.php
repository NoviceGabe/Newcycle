<?php
require_once 'classes/restapi.php';
require_once 'functions/function.php';
$rest = new RestAPI();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$data = filter($_POST, array('table'));
	$table = null;

	if(isset($_POST['table'])){
		$table = $_POST['table'];
	}

	$rest->table($_POST['table']);
	$result = $rest->insert($data);
	
	echo json_encode($result);
	
}else{
	header( 'HTTP/1.0 403 Forbidden', TRUE, 403 );
	die( header( 'location: 404.php' ) );
}
?>
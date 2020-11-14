<?php
require_once 'classes/restapi.php';
require_once 'functions/function.php';
$rest = new RestAPI();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$data = filter($_POST, array('table','param'));
	$param = null;
	$table = null;
	
	if(isset($_POST['param'])){
		$param = $_POST['param'];
	}
	if(isset($_POST['table'])){
		$table = $_POST['table'];
	}

	$where = map($data);
	$rest->table($_POST['table']);
	$results = $rest->get(function() use($rest, $param, $where){
		if(isset($param)){
			$rest->db()->param($param);
			$rest->db()->get()->where($where);
		}else{
			$rest->db()->getAll()->where($where);
		}
		
	});
	echo json_encode($results[0]);
	
}else{
	header( 'HTTP/1.0 403 Forbidden', TRUE, 403 );
	die( header( 'location: 404.php' ) );
}
?>
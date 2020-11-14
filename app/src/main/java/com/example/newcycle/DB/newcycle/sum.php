<?php
require_once 'classes/restapi.php';
$rest = new RestAPI();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$data = filter($_POST, array('table','sum'));
	$key = null;
	$values = array();

	if(isset($_POST['sum'])){
		$separator = strpos($_POST['sum'], ":");
		$values = json_decode(substr($_POST['sum'], $separator+1));
		$key = substr($_POST['sum'] , 0, $separator);
	}
	
	$rest->table($_POST['table']);
	$where = map($data);
	$results = $rest->execute(function() use($rest, $key, $values, $where){
		$rest->db()->sum($key, $values)->where($where);
	});

	echo json_encode($results);
	
}else{
	header( 'HTTP/1.0 403 Forbidden', TRUE, 403 );
	die( header( 'location: 404.php' ) );
}
?>
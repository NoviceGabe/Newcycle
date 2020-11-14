<?php
require_once 'classes/restapi.php';
require_once 'functions/function.php';
$rest = new RestAPI();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$data = filter($_POST, array('table','limit', 'order'));
	$table = null;
	$limit = null;
	$order = null;


	if(isset($_POST['table'])){
		$table = $_POST['table'];
	}
	if(isset($_POST['limit'])){
		$limit = $_POST['limit'];
	}
	if(isset($_POST['order'])){
		$order = $_POST['order'];
	}

	$where = map($data);

	$rest->table('cart AS c');
	$results = $rest->get(function() use($rest, $where, $order, $limit){
		$rest->db()->param('p.*');
		$rest->db()->get()->join('product AS p','c.product_id','=','  p.id')->condition($where, $order, $limit);
	});

	echo json_encode($results);
	
}else{
	header( 'HTTP/1.0 403 Forbidden', TRUE, 403 );
	die( header( 'location: 404.php' ) );
}

?>
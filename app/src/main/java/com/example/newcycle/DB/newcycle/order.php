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

	$rest->table('`order` AS o');
	$results = $rest->get(function() use($rest, $where, $order, $limit){
		$rest->db()->param('p.id, p.name, p.price, p.shipping_fee, p.rating, p.description, p.stock, p.image, o.quantity AS quantity, o.code, o.payment_method,  o.order_status, o.date_ordered, o.date_shipped, o.date_received');
		$rest->db()->get()->join('product AS p','o.product_id','=','  p.id')->condition($where, $order, $limit);
	});

	echo json_encode($results);
	
}else{
	header( 'HTTP/1.0 403 Forbidden', TRUE, 403 );
	die( header( 'location: 404.php' ) );
}

?>
<?php
require_once 'classes/restapi.php';
require_once 'functions/function.php';
$rest = new RestAPI();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$results = array();
	$data = file_get_contents("php://input");
	$decode = json_decode($data, true);

	if(!isset($decode)){
		$results['success'] = "0";
		$results['message'] = "post_error";
		echo json_encode($results);
	}else{
		$order =$decode['order'];
		$field = array();
		$x = 0;	
		
		foreach ($order as $value) {
			$field[$x]['product_id'] = $value['product_id']; 
			$field[$x]['user_id'] = $value['user_id'];
			$field[$x]['quantity'] =  $value['quantity']; 
			$field[$x]['payment_method'] = $value['payment_method'];
			$field[$x]['code'] = generate_order_no().$x;
			$field[$x]['date_ordered'] = strtotime(get_date());
			 $x++;
		}
		$rest->table('`order`');
		$results = $rest->insert($field);
	}
	echo json_encode($results);
	
}else{
	header( 'HTTP/1.0 403 Forbidden', TRUE, 403 );
	die( header( 'location: 404.php' ) );
}

?>
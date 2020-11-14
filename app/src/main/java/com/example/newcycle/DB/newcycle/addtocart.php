<?php
require_once 'classes/restapi.php';
require_once 'functions/function.php';
$rest = new RestAPI();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$data = filter($_POST, array());

	$rest->table('cart');

	$cart['product_id'] = $data['product_id'];
	$cart['user_id'] = $data['user_id'];

	$results = $rest->get(function() use($rest, $cart){

		$rest->db()->param("COUNT(*) AS count");
		$rest->db()->get()->where(map($cart));
	});
	// no data
	if(isset($results[0]->count) && $results[0]->count == 0){
		$results = $rest->insert($data);
	}else{
		$results = array();
		$results['success'] = '0';
		$results['message'] = 'non_unique_data_error';
	}

	echo json_encode($results);
	
}else{
	header( 'HTTP/1.0 403 Forbidden', TRUE, 403 );
	die( header( 'location: 404.php' ) );
}
?>
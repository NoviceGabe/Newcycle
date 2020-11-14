<?php
require_once 'core/init.php';
require_once 'functions/function.php';
define('ADMIN', 1);

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$result =array();
	if(!$db->isConnected()){
		$result['success'] = "0";
		$result['message'] = "connect_error";
	}else{

		$data = filter_post($_POST);

		if(!count($data) || !isset($data['id']) || !isset($data['group'])){
			$result['success'] = "0";
			$result['message'] = "post_error";
		}else if($data['group'] != ADMIN){
			$result['success'] = "0";
			$result['message'] = "forbidden_access";
		}else{
			
			$db->table($data['table']);
			// check if table exists
			if($db->error()){
				$result['success'] = "0";
				$result['message'] = "no_table";
			}else{
			
				$db->delete()->where(array(
					'id', '=', $data['id']
				));
		
				if($db->execute()->error()){
					$result['success'] = "0";
					$result['message'] = "query_error";
				}else if(!$db->count()){
					$result['success'] = "0";
					$result['message'] = "delete_error";
				}else{
					$result['success'] = "1";
					$result['message'] = "delete_success";	
				}
			}
		}
	}
	echo json_encode($result);
}else{
	header( 'HTTP/1.0 403 Forbidden', TRUE, 403 );
	die( header( 'location: 404.php' ) );
}



?>
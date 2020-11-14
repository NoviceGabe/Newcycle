<?php
require_once 'core/init.php';
require_once 'functions/function.php';
$db = db::getInstance();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$result =array();
	if(!$db->isConnected()){
		$result['success'] = "0";
		$result['message'] = "connect_error";
	}else{

		$data = filter($_POST, array('table'));

		if(!count($data) || !isset($data['id'])){
			$result['success'] = "0";
			$result['message'] = "post_error";
		}else{

			$db->table($_POST['table']);	

			if($db->error()){
				$result['success'] = "0";
				$result['message'] = "no_table";
			}else{
				$id = array_remove($data, 'id');
				$db->update($data)->where(array(
					'id', '=', $id
				));
		
				if($db->execute()->error()){
					$result['success'] = "0";
					$result['message'] = "query_error";
				}else if(!$db->count()){
					$result['success'] = "0";
					$result['message'] = "update_error";
				}else{
					$result['success'] = "1";
					$result['message'] = "update_success";	
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
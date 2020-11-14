<?php
require_once 'core/init.php';
require_once 'functions/function.php';
$db = db::getInstance();

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$result = array();
	if(!$db->isConnected()){
		$result['success'] = "0";
		$result['message'] = "connect_error";
	}else{

		$data = filter($_POST, array('table'));

		if(!count($data) || !isset($data['email']) || !isset($data['password'])
			|| !isset($data['first_name']) || !isset($data['last_name'])){
			$result['success'] = "0";
			$result['message'] = "post_error";
		}else{

			$db->table($_POST['table']);
			// check if table exists
			if($db->error()){
				$result['success'] = "0";
				$result['message'] = "no_table";
			}else{
				// check if user already exists to prevent data duplication
				$db->get()->where(array(
					'email', '=', $data['email']
				));

				if($db->execute()->error()){
					$result['success'] = "0";
					$result['message'] = "query_error";
				}else if($db->count()){
					$result['success'] = "0";
					$result['message'] = "non_unique_data_error";
				}else{
					$hash = password_hash($data['password'], PASSWORD_DEFAULT);
					$data['password'] = $hash;

					date_default_timezone_set('Asia/Manila');
					$date = date('m/d/Y h:i:s a', time());
				
					$data['joined'] = strtotime($date);
					
				 	if($db->insert($data)->error()){
						$result['success'] = "0";
						$result['message'] = "insert_error";
								
					}else{
						$result['success'] = "1";
						$result['message'] = "insert_success";
					}
				}
			}
		}
		echo json_encode($result);
	}
}else{
	header( 'HTTP/1.0 403 Forbidden', TRUE, 403 );
	die( header( 'location: 404.php' ) );
}

?>
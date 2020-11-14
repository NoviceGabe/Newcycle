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

		if(!count($data) || !isset($data['email']) || !isset($data['password'])){
			$result['success'] = "0";
			$result['message'] = "post_error";
		}else{
			
			$db->table($_POST['table']);
			// check if table exists
			if($db->error()){
				$result['success'] = "0";
				$result['message'] = "no_table";
			}else{
				$where = array(
					'email', '=', $data['email']
				);

				$db->getAll();
				$db->where($where);

				if($db->execute()->error()){
					$result['success'] = "0";
					$result['message'] = "query_error";
				}else if(!$db->count()){
					$result['success'] = "0";
					$result['message'] = "auth_error";
				}else{
					$temp = $db->result();

					date_default_timezone_set('Asia/Manila');
					$date = date('m/d/Y h:i:s a', time());
					
					$login['user_id'] = $temp->id;
					$login['last_login'] = strtotime($date);
					$login['IP_address'] = $_SERVER['REMOTE_ADDR'];
					$login['is_now_login'] = 1;

					if(!password_verify($data['password'], $temp->password)){
						$result['success'] = "0";
						$result['message'] = "auth_error";
						$login['is_now_login'] = 0;
					}else{
						$result = (array) $temp;
						array_remove($result, 'password');
						$db->table('login');
						$db->insert($login);
					}	
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
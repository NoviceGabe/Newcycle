<?php
require_once 'core/init.php';

class RestAPI{
	private $db;
	private $result = array();
	private $table;

	public function __construct(){
	 	$this->db = db::getInstance();
	 
	}

	public function db(){
		return $this->db;
	}

	public function table($table){
		$this->table = $table;
	}

	public function insert($data){
		if(!$this->db->isConnected()){
			$this->result['success'] = "0";
			$this->result['message'] = "connect_error";
		}else{
			$this->db->table($this->table);
			// check if table exists
			if($this->db->error()){
				$this->result['success'] = "0";
				$this->result['message'] = "no_table";
			}else{
				if(!count($data)){
					$this->result['success'] = "0";
					$this->result['message'] = "post_error";
				}else if($this->db->insert($data)->error()){
						$this->result['success'] = "0";
						$this->result['message'] = "insert_error";
				}else{
						$this->result['success'] = "1";
						$this->result['message'] = "insert_success";
				}
			}
		}
		return $this->result;
	}

	public function get($query){
		
		if(!$this->db->isConnected()){
			$this->result['success'] = "0";
			$this->result['message'] = "connect_error";
		}else{

			$this->db->table($this->table);
			// check if table exists
			if($this->db->error()){
				$this->result['success'] = "0";
				$this->result['message'] = "no_table";
			}else{
				$query();
				if($this->db->execute()->error()){
					$this->result['success'] = "0";
					$this->result['message'] = "query_error";
				}else if(!$this->db->count()){
					$this->result['success'] = "0";
					$this->result['message'] = "no_data";
				}else{
					$this->result = $this->db->results();
				}
			}
		}
		return $this->result;
	}

	public function delete($data){

	if(!$this->db->isConnected()){
			$this->result['success'] = "0";
			$this->result['message'] = "connect_error";
		}else{
			$this->db->table($this->table);
			// check if table exists
			if($this->db->error()){
				$this->result['success'] = "0";
				$this->result['message'] = "no_table";
			}else{
				if(!count($data)){
					$this->result['success'] = "0";
					$this->result['message'] = "post_error";
				}else{

					$this->db->delete()->where($data);

					if($this->db->execute()->error() || !$this->db->count()){
						$this->result['success'] = "0";
						$this->result['message'] = "delete_error";
					}else{
						$this->result['success'] = "1";
						$this->result['message'] = "delete_success";
					}
				} 
			}
		}
		return $this->result;
	}
}

?>
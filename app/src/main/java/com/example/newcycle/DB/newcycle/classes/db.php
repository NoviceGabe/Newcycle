<?php
if(!defined('APP')) {
  	header( 'HTTP/1.0 403 Forbidden', TRUE, 403 );
  	$page = '';
  	if(file_exists('/404.php')){
		$page = '/404.php';
	}else if(file_exists('../404.php')){
		$page = '../404.php';
	}
	die(header( 'location: '.$page));
}


class db{
	private static $_instance = null;
	private $_pdo;
	private $_query;
	private $_error = false;
	private $_results;
	private $_count = 0;
	private $_table;
	private $_action_param = "*";
	private $_value_param;
	private $_action;
	private $_set;
	private $_where;
	public $_sql;
	private $_connected = false;

	private function __construct(){
		try{
			$this->_pdo = new PDO('mysql:host='.config::get('mysql/host').';dbname='.config::get('mysql/db'), config::get('mysql/username'), config::get('mysql/password'),
				array(
					PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
					PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8"
				));
			$this->_connected = true;
		}catch(PDOException $e){
			//die($e->getMessage());
			$this->_connected = false;
			$this->_error = true;
		}
	}

	public function isConnected(){
		return $this->_connected;
	}

	public static function getInstance(){
		if(!isset(self::$_instance)){
			self::$_instance = new db();
		}
		return self::$_instance;
	}

	public function query($sql, $params = array()){
		//die($sql);
		//die(print_r($params));
		try{
			$this->_error = false;
			if($this->_query = $this->_pdo->prepare($sql)){

				if(isset($params) && count($params)){
					if(is_multi($params)){
						$x = 1;
						foreach ($params as $param) {
							foreach ($param as $value) {
								$this->_query->bindValue($x, $value);
								$x++;
							}
						}
					}else{
						$x = 1;
						foreach ($params as $param) {
							$this->_query->bindValue($x, $param);
							$x++;
						}
					}
				}

				if($this->_query->execute()){
					if($this->_action == "get"){
						$this->_results = $this->_query->fetchAll(PDO::FETCH_OBJ);
					}
					$this->_count = $this->_query->rowCount();
					
				}else{
					$this->_error = true;
				}
			}
		}catch(PDOException $e){
			$this->_error = true;
		}
		return $this;
	}

	public function action($action, $table){
		$this->_sql = "{$action} ";
	
		if(is_array($table)){
			$tb_length = count($table);
			if($tb_length> 1){
				$table = implode(', ', $table);
			}
		}

		$this->_sql .= "{$table} ";

		if(isset($this->_set) && trim($this->_action_param) == 'UPDATE'){
			$this->_sql .= $this->_set;
		}
		return $this;
	}

	public function execute(){
		$this->_error = false;
		if(!isset($this->_sql) && !isset($this->_value_param)){
			$this->_error = true;
		}

		if($this->query($this->_sql, $this->_value_param)->error()){
			$this->_error = true;
		}
		return $this;
	}

	public function get(){
		$this->_action = "get";
		$param = null;
		if(!isset($this->_table) && !isset($this->_action_param)){
			return false;
		}
		if(is_array($this->_action_param)){
			$param = 'SELECT '.implode(', ', $this->_action_param).' FROM';
		}else{
			$param = 'SELECT '.$this->_action_param.' FROM';
		}
		return $this->action($param, $this->_table);
	}

	public function getAll(){
		$this->_action = "get";
		if(!isset($this->_table)){
			return false;
		}
		$this->_action_param = 'SELECT * FROM';
		return $this->action($this->_action_param , $this->_table);
	}

	public function where($where = array()){
		$params = array();
		$values = array();
		$operators = array('=', '!=', '>', '<', '>=', '<=', 'AND', 'OR', ':', 'LIKE');
			
		if(isset($where) && count($where)){
			$this->_sql .= " WHERE ";
			$key = array();
			// get values
			for($i = 0; $i < count($where); $i++){
				if(isset($where[$i]) && is_array($where[$i])){
					$values = $this->getParamValues($where[$i]);
				}
			}

			$key = array_keys($where);
			if(is_array($where[$key[0]])){
				$values = $where[$key[0]];
				if(!(isset($key[0]) && !intval($key[0]) && isset($where[$key[1]]))){
					return false;
				}
				$this->_sql .= $key[0].' in (';
				for($i = 0; $i < count($values); $i++){
					$this->_sql .= '?';
					if( $i < count($values)-1){
						$this->_sql .= ', ';
					}
				}
				$this->_sql .= ')';
				$this->_sql .= ' AND '.$key[1].' = ?';
				array_push($values, $where[$key[1]]);
			}else{
				$values = array_merge($values, $this->getParamValues($where));
				
				//filter where clause
				for($i = 0; $i < count($where); $i++){
					if($i%2==0){
						if($i%4==0){
							if(is_numeric($where[$i])){
								if (($key = array_search($where[$i], $where)) !== false) {
								    unset($where[$key]);
								}
							}
						}
					}else{

						if(!in_array($where[$i], $operators)){
							return false;
						}

						if (($key = array_search(":", $where)) !== false) {
							unset($where[$key]);
						}
					}
					
				}
			
				for($i = 0; $i < count($where); $i++){
					if(isset($where[$i]) && is_array($where[$i])){
						$where[$i] = " (".implode(" ", $where[$i]).") ";
					}
				}
				$this->_sql .= implode(" ", $where);
					
			}
			if(isset($this->_value_param)){
				$this->_value_param = array_merge($this->_value_param, $values);
			}else{
				$this->_value_param = $values;
			}
			return $this;
		}
		return false;
	}

	public function table($table){
		$this->_table = $table;
		$this->_error = false;

		if(!$this->isTableExists()){
			$this->_error = true;
		}
	
		return $this;
	}

	public function param($param){
		$this->_action_param = $param;
		return $this;
	}

	public function join($table, $field, $operator, $value){
		$join = " INNER JOIN $table ON $field $operator $value";
		$this->_sql .= $join;
		return $this;
	}

	public function condition($where, $order, $limit){
		if(count($where)){
			$this->where($where);
		}
		$this->order($order);
		$this->limit($limit);
		return $this;
	}

	public function delete(){
		$this->_action = "delete";
		return $this->action('DELETE FROM ', $this->_table);
	}

	public function insert($fields = array()){
		$this->_error = false;
		$this->_action = "insert";
		if(count($fields) && isset($this->_table)){
			$keys = array();
			$values = null;
			$field_values = array();

			if(isAssoc($fields)){
				$keys = array_keys($fields);
				$x = 1;
				foreach($fields as $field){
					$values .= '?';
					if($x < count($fields)){
						$values .= ', ';
					}
					$x++;
				}
				$values = 'VALUES ('.$values.')';
				$field_values = $fields;
			}else{
				$i = 0;			
				foreach ($fields as $field) {
					$x = 0;
					$values .= '(';
					foreach ($field as $value) {
						$field_values[$i][$x] = $value;
						$values .= '?';
						if($x < count($field)-1){
							$values .= ', ';
						}
						$x++;
					}
					$values .=')';
					if($i < count($fields)-1){
							$values .= ', ';
					}
					$i++;
				}
				$values = 'VALUES '.$values;
				$keys = array_keys($fields[0]);
			}
			
			$sql = "INSERT INTO {$this->_table} (`".implode('`,`', $keys)."`) ".$values;
			
			if($this->query($sql, $field_values)->error()){
				$this->_error = true;
			}
			
		}
		return $this;
	}

	public function update($fields){
		$this->_action = "update";
		if(!isset($this->_table)){
			return false;
		}
		$this->_value_param = array();
		$set = '';
		$x = 1;
		foreach($fields as $name => $value){
			array_push($this->_value_param, $value);
			$set .= "{$name} = ?";
			if($x < count($fields)){
				$set .= ", ";
			}
			$x++;
		}
		$this->_set = " SET {$set} ";
		$this->_action_param = 'UPDATE ';
		return $this->action($this->_action_param , $this->_table);
	}

	public function sum($key, $values){
		
		if(!isset($key)){
			return false;
		}
		if(count($values) < 1 || count($values) > 20){
			return false;
		}

		$range = range('a', 'z');
		$this->_action_param = 'SELECT';
	
		$x = 0;
		foreach($values as $value){
			$this->_action_param .= " SUM($key = $value) AS ".$range[$x];
			if($x < count($values)-1){
				$this->_action_param  .= ', ';
			}
			$x++;
		}
		$this->_action_param .= ' FROM';
		return $this->action($this->_action_param , $this->_table);
	}

	public function isTableExists(){
		if(!is_array($this->_table)){
			if(!isset($this->_table)){
				return false;
			}else if(strlen($this->_table) == 0){
				return false;
			}
		}else{
			if(!count($this->_table)){
				return false;
			}
		}

		if(!isset($this->_action_param)){
			$this->param('1');
		}

		if(!$this->get()->execute()){
			return false;
		}
		return true;
	}

	public function getParamValues(&$where){
		$len = count($where);
		$values = array();
		if($len){
			for($i = 0; $i < $len; $i++){
				if($i % 2 == 0 && !($i % 4 == 0)){
					if(!is_array($where[$i])){
						if (strpos($where[$i], '!:') !== false) {
							$where[$i] = str_replace('!:', '', $where[$i]);
						}else{
							array_push($values, $where[$i]);
							$where[$i] = "?";
						}
					}
				}
			}
		}
		return $values;
	}

	public function limit($num){
		if(isset($num)){
			$this->_sql .= ' LIMIT '.$num;
		}
	}

	public function order($field){
		if(isset($field)){
			$this->_sql .= ' ORDER BY '.$field;
		}
	}

	public function results(){
		return $this->_results;

	}

	public function result(){
		return $this->_results[0];

	}

	public function error(){
		return $this->_error;
	}

	public function count(){
		return $this->_count;
	}

	public function lastInsertId(){
		return $this->_pdo->lastInsertId();
	}
}
?>
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

function json_error(){
	switch(json_last_error()){
		case JSON_ERROR_NONE:
		echo '- No errors';
		break;
		case JSON_ERROR_DEPTH:
		echo '- Maximum stack depth exceeded';
		break;
		case JSON_ERROR_STATE_MISMATCH:
		echo '- Underflow or the modes mismatch';
		break;
		case JSON_ERROR_CTRL_CHAR:
		echo '- Unexpected control character found';
		break;
		case JSON_ERROR_SYNTAX:
		echo '- Syntax error, malformed JSON';
		break;
		case JSON_ERROR_UTF8:
		echo '- Malformed UTTF-8 characters, possibly incorrectly encoded';
		break;
		default:
		echo '- Unknown error';

	}
}

function prepend($string, $chunk) {
     if(!empty($chunk) && isset($chunk)) {
        return $string.$chunk;
     }
     else {
        return $string;
     }
}

function array_insert(&$array, $position, $insert)
{
    if (is_int($position)) {
        array_splice($array, $position, 0, $insert);
    } else {
        $pos   = array_search($position, array_keys($array));
        $array = array_merge(
            array_slice($array, 0, $pos),
            $insert,
            array_slice($array, $pos)
        );
    }
}
/*
function filter_post($raw_data = array()){
	$data = array();
	foreach ($raw_data as $key => $value) {
		if(isset($_POST[$key])){
			$data[$key] = $_POST[$key];
		}
	}
	return $data;
}
*/
function filter($array = array(), $exclude){
	$data = array();
	foreach ($array as $key => $value) {
		if(isset($array[$key])){
			if(is_array($exclude) && count($exclude)){
				if(in_array($key, $exclude)){
					continue;
				}
			}else if($key == $exclude){
				continue;
			}
			$data[$key] = $array[$key];
		}
	}
	return $data;
}


function array_remove(array &$arr, $key) {
    if (array_key_exists($key, $arr)) {
        $val = $arr[$key];
        unset($arr[$key]);

        return $val;
    }

    return null;
}

function map($data){
	$array = array();
	if(count($data)){
		$i = 1;
		foreach ($data as $field => $value) {
			
			if(stripos($field, 'like') !== false){
				array_push($array, str_replace('like-', '', $field));
				array_push($array, 'LIKE');
			}else if(strpos($field, '>')){
				array_push($array, str_replace('>', '', $field));
				array_push($array, '>');
			}else{
				array_push($array, $field);
				array_push($array, '=');
			}
			
			array_push($array, $value);
			if(count($data) > 1 && $i < count($data)){
				array_push($array, 'AND');
			}
			$i++;
		}
	}
	return $array;
}

function isAssoc(array $arr){
	if(array() === $arr) return false;
	return array_keys($arr) != range(0, count($arr)-1);
}


function getId($data = array(), $key){
	$arr[$key] = array();

	foreach ($data as $value) {
		if (!strpos($value, $key)) {
	    		array_push($arr[$key], $value);
		}
	}
	
	return $arr;
}

function generate_order_no(){
	$rand = strtoupper(substr(uniqid(sha1(time())),0, 4));
	$order = strtotime(get_date()).$rand;
	return $order;
}

function get_date(){
	date_default_timezone_set('Asia/Manila');
	return  date('m/d/Y h:i:s a', time());
}

function is_multi($array){
	foreach ($array as $value) {
		if(is_array($value)){
			return true;
		}
	}
	return false;
}
?>
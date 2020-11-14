<?php
session_start();

$GLOBALS['config'] = array(
	'mysql' => array(
		'host' => 'localhost', // host
		'username' => 'root', // username
		'password' => '', // password
		'db' => 'newcycle' // database
	),
	'remember' => array(
		'cookie_name' => 'hash',
		'cookie_expiry' => 604800
	),
	'session' => array(
		'session_name' => 'user'
	)
);


spl_autoload_register(function($class){
	if(file_exists('classes/'.$class.'.php')){
		require_once 'classes/'.$class.'.php';
	}else if('../classes/'.$class.'.php'){
		require_once '../classes/'.$class.'.php';
	}
	
});
///////comment out these functions in production/////////////
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
////////////////////////////////////////////////////////////
define('APP', TRUE);
?>
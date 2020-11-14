<?php
require_once 'core/init.php';
require_once 'functions/function.php';
$db = db::getInstance();


if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$name = trim($_POST['name']);
	$price = trim($_POST['price']);
	$shippingFee = trim($_POST['shipping_fee']);
	$quantity = trim($_POST['quantity']);
	$type = trim($_POST['type']);
	$description = trim($_POST['description']);
	$textarea = null;
	
	if(!($_FILES['image']['size'] > 0)){
		echo '<script>alert("Empty image!")</script>'; 
	}else if(!isset($name)){
		echo '<script>alert("Empty name!")</script>'; 
	}else if(!preg_match('/^[A-Za-z0-9-_( )\s]+$/',$name) && !(strlen($name) > 0)){
		echo '<script>alert("Invalid name!")</script>'; 
	}else if(!isset($price)){
		echo '<script>alert("Empty price!")</script>'; 
	}else if(!preg_match('/^[0-9]+$/', $price) && !(strlen($price) > 0)){
		echo '<script>alert("Invalid price!")</script>'; 
	}else if(!isset($shippingFee)){
		echo '<script>alert("Empty shipping fee!")</script>'; 
	}else if(!preg_match('/^[0-9]+$/', $shippingFee) && !(strlen($shippingFee) > 0)){
		echo '<script>alert("Invalid shipping fee!")</script>'; 
	}else if(!isset($quantity)){
		echo '<script>alert("Empty quantity!")</script>'; 
	}else if(!preg_match('/^[0-9]+$/', $quantity) && !(strlen($quantity) > 0)){
			echo '<script>alert("Invalid quantity!")</script>'; 
	}else if(!isset($type)){
		echo '<script>alert("Empty type!")</script>'; 
	}else if(!preg_match('/^[0-9]+$/', $type) && !(strlen($type) > 0)){
		echo '<script>alert("Invalid type!")</script>'; 
	}else if (!isset($description) && !(strlen($type) > 0)){
		echo '<script>alert("Empty description!")</script>'; 
	}else if(!preg_match('/^[A-Za-z0-9-_,.$+\/( )\s]+$/',$description)){
		echo '<script>alert("Invalid description!")</script>'; 
	}else{
		$upload_path = 'resources/image/';
		$server_ip = 'new-cycle-bike-shop.000webhostapp.com/';
		$upload_url = 'https://'.$server_ip.''.$upload_path;
		$response = array();
		$data = filter($_POST, array('upload'));
		$result = array();
		
		$fileinfo = pathinfo($_FILES['image']['name']);
		$extension = $fileinfo['extension'];
		$file_url = $upload_url.'IMG_'.$_FILES['image']['name'];
		$file_path = $upload_path.'IMG_'.$_FILES['image']['name'];

		try{
			move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
			$data['image'] = $file_url;
			date_default_timezone_set('Asia/Manila');
			$date = date('m/d/Y h:i:s a', time());
			$data['date'] = strtotime($date);
			$db->table('product');
			if($db->insert($data)->error()){
				echo '<script>alert("Unable to insert data!")</script>'; 
			}else{
				echo '<script>alert("Data inserted!")</script>'; 
			}
		}catch(Exception $e){
			echo '<script>alert("Unable to insert data!")</script>'; 
		}
	}
	

}

?>
<!DOCTYPE html>
<html>
<head>
	<title>Upload Product</title>
	<head>
	  <link rel="stylesheet" href="resources/css/style.css">
	</head>
</head>
<body>
	<h1>Upload Product</h1>
	<div class="container">
		<form action="upload.php" method="post" enctype="multipart/form-data">
			<label for="image">Image:</label>
	     	<input type="file" name="image" value="<?php echo isset($_POST['image'])?$_POST['image']:''?>"/></br></br>
	   		
	   		<label for="name">Name:</label>
	   		<input type="text" name="name" value="<?php echo isset($_POST['name'])?$_POST['name']:''?>"></br>

	   		<label for="price">Price:</label>
	    	<input type="number" name="price" value="<?php echo isset($_POST['price'])?$_POST['price']:''?>"></br>

	    	<label for="shipping_fee">Shipping fee:</label>
	    	<input type="number" name="shipping_fee" value="<?php echo isset($_POST['shipping_fee'])?$_POST['shipping_fee']:''?>"></br>

	    	<label for="quantity">Quantity:</label>
	    	<input type="number" name="quantity" value="<?php echo isset($_POST['quantity'])?$_POST['quantity']:''?>"></br>

		    <label for="type">Type:</label>

		    <select name="type" class="type">
			  <option <?php if(isset($_POST['type']) && $_POST['type'] == '1')  echo 'selected= "selected"';
         ?> value="1">1</option>
			  <option  <?php if(isset($_POST['type']) && $_POST['type'] == '2')  echo 'selected= "selected"';
         ?> value="2">2</option>
			  <option   <?php if(isset($_POST['type']) && $_POST['type'] == '3')  echo 'selected= "selected"';
         ?> value="3">3</option>
			</select>

		     <label for="description">Description:</label></br>
		    <textarea name="description" rows="5" cols="50" ></textarea>

	    <button class="upload" name="upload">Upload</button>
		</form>
	</div>

</body>
</html>
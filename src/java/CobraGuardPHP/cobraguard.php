#!/usr/bin/php
<?php
	//print "PHP working directory = " . getcwd() . "\n " . $argv[1];
	//this file needs to be put into the cobraguard directory that was created by the cobraguard program
	//this file needs to be put into the cobraguard directory that was created by the cobraguard program

	//leave the @ sign there
	//$fileLocation = "@/home/kag/phpcode/";
	$fileLocation = "@" . $argv[1] . "/"; //location is being passed in from Java
	$orgId="5266";
	$today = date("Ymd");
	$postData = array();
	$postData['EMFile'] = $fileLocation . $orgId. "_" .$today. "_EM_Refresh.csv";
	$postData['ELFile'] = $fileLocation . $orgId. "_" .$today. "_EL_Refresh.csv";
	$postData['DPFile'] = $fileLocation . $orgId. "_" .$today. "_DP_Refresh.csv";
	$postData['TERMFile'] = $fileLocation . $orgId. "_" .$today. "_TERM_Refresh.csv";
	$postData['AuthString'] = "670a65dac44b049884ab4de61e7cd8d4";
	$postData['OrgID'] = "$orgId";
	$ch = curl_init();
	curl_setopt($ch, CURLOPT_URL, 'https://www.cobraguard.net/api/1.1/loader.html' );
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
	curl_setopt($ch, CURLOPT_POST, 1 );
	curl_setopt($ch, CURLOPT_POSTFIELDS, $postData );
	$response = curl_exec( $ch );
	print "PHP response is " . $response ;

?>
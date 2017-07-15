$(".loginForm button[type=submit]").click(addLogin);

function addLogin(e){
	e.preventDefault();
	console.log("clicked");
	
	var queryString = $(".loginForm").serialize();
	console.log("query : "+queryString);
	
	var url = $(".loginForm").attr("action");
	console.log("url : "+url);
	
	validateEncryptedForm();
	
	

    /*
	$.ajax({
			type     :'post',
			url      : url,
			data     : queryString,
			dataType : 'json',
			error    : onError,
			success   : onSuccess
			});
			*/
}

function onError(){
	
}

function onSuccess(data, status){
	console.log(data);
}



function validateEncryptedForm() {
    var username = document.getElementById("userId").value;
    var password = document.getElementById("password").value;
    if (!username || !password) {
        alert("ID/비밀번호를 입력해주세요.");
        return false;
    }

    try {
        var rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
        var rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;
        submitEncryptedForm(username,password, rsaPublicKeyModulus, rsaPublicKeyExponent);
    } catch(err) {
        alert(err);
    }
    return false;
}

function submitEncryptedForm(username, password, rsaPublicKeyModulus, rsaPpublicKeyExponent) {
    var rsa = new RSAKey();
    rsa.setPublic(rsaPublicKeyModulus, rsaPpublicKeyExponent);

    // 사용자ID와 비밀번호를 RSA로 암호화한다.
    var securedUsername = rsa.encrypt(username);
    var securedPassword = rsa.encrypt(password);
    
    // base64로변환 
    //securedUsername = hex2b64(securedUsername);
    //securedPassword = hex2b64(securedPassword);

    // POST 로그인 폼에 값을 설정하고 발행(submit) 한다.
    //alert(securedUsername);
    //alert(securedPassword);
    //$("#userId").value = securedUsername;
    //$("#password").value = securedPassword;
    
    $("#userId").val(securedUsername);
    $("#password").val(securedPassword);
    
    $(".loginForm").submit();

}
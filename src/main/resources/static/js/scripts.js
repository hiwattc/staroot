$(".reply-write button[type=submit]").click(addReply);

function addReply(e){
	e.preventDefault();
	console.log("clicked");
	
	var queryString = $(".reply-write").serialize();
	console.log("query : "+queryString);
	
	var url = $(".reply-write").attr("action");
	console.log("url : "+url);
	
	$.ajax({
			type     :'post',
			url      : url,
			data     : queryString,
			dataType : 'json',
			error    : onError,
			sucess   : onSuccess
			});
}

function onError(){
	
}

function onSuccess(data, status){
	console.log(data);
}

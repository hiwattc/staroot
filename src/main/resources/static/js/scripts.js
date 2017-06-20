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
			success   : onSuccess
			});
}

function onError(){
	
}

function onSuccess(data, status){
	console.log(data);
	var replyTemplate = $("#replyTemplate").html();
	var template = replyTemplate.format(data.writer.name, data.formattedCreateDate, data.contents);
	$(".reply-list").prepend(template);
	
	$(".reply-write textarea").val("");
	//$("textarea[name=contents]").val("");
	//$(".reply-write textarea").reset();
}

String.prototype.format = function() {
	var args = arguments;
	return this.replace(/{(\d+)}/g, function(match, number){
		return typeof args[number] != 'undefined'
			? args[number]
		    : match
		    ;
	});
};

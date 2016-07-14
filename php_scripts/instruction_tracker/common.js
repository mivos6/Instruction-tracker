function generateAlert(alertContent) {
	$("#alertContainer *").remove();

	var alertMsg = $("<div></div>");
	alertMsg.addClass("alert alert-danger fade in");
	alertMsg.append("<a></a>");
	alertMsg.children("a")
		.html("&times;")
		.attr("href", "#")
		.attr("class","close")
		.attr("data-dismiss", "alert")
		.attr("aria-label", "close");
	alertMsg.append("<strong>Greška: </strong>" + alertContent);
	
	$("#alertContainer").append(alertMsg);
}

function generateSuccess(alertContent) {
	$("#alertContainer *").remove();

	var alertMsg = $("<div></div>");
	alertMsg.addClass("alert alert-success fade in");
	alertMsg.append("<a></a>");
	alertMsg.children("a")
		.html("&times;")
		.attr("href", "#")
		.attr("class","close")
		.attr("data-dismiss", "alert")
		.attr("aria-label", "close");
	alertMsg.append("<strong>Uspjeh: </strong>" + alertContent);
	
	$("#alertContainer").append(alertMsg);
}

function setCookie(cookieName, cookieValue, expireSeconds) {
	var date = new Date();
	date.setTime(date.getTime() + expireSeconds*1000);
	var expires = "expires=" + date.toUTCString();
	document.cookie = cookieName + "=" + cookieValue + "; " + expires;
}

function getCookie(cookieName) {
	var value = null
	var name = cookieName + "="
	var allCookies = document.cookie.split(";");

	allCookies.forEach(function(current) {
		var index = current.indexOf(name);
		if(index != -1) {
			value = current.substring(index + name.length, current.length);
		}
	});

	return value;
}

function removeAlert(id) {
	$(id).parents(".form-group").removeClass("has-error");
	$("#alertContainer *").remove();
}
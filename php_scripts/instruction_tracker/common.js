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

function initializeNavbar() {
	navContent = $("nav.navbar div div ul").first();
	navRight = $("nav.navbar div div ul").last();

	var userString = getCookie("user");
	if(userString != null) {
		var user = eval("(" + userString + ")");
		
		var li = $("<li></li>");
		var a = $("<a></a>")
				.attr("href", "profile.html");
		var span = $("<span></span>")
				.addClass("glyphicon glyphicon-user")

		a.append(span).append(" " + user.username);
		li.append(a);
		navContent.append(li);

		li = $("<li></li>");
		a = $("<a></a>")
				.attr("href", "index.html")
				.click(function() {
					setCookie("user", "", -1);
				});
		span = $("<span></span>")
				.addClass("glyphicon glyphicon-log-out")

		a.append(span).append(" Odjava");
		li.append(a);
		navRight.append(li);
	} else {
		var li = $("<li></li>");
		var a = $("<a></a>")
				.attr("href", "login.html");
		var span = $("<span></span>")
				.addClass("glyphicon glyphicon-log-in")

		a.append(span).append(" Prijava");
		li.append(a);
		navRight.append(li);

		li = $("<li></li>");
		a = $("<a></a>")
				.attr("href", "register.html");
		span = $("<span></span>")
				.addClass("glyphicon glyphicon-user")

		a.append(span).append(" Registracija");
		li.append(a);
		navRight.append(li);
	}
}
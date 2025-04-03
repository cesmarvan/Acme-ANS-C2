/*
 * acme.js
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes.  The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

"use strict"

/* Dealing with redirections ----------------------------------------------- */

function getAbsoluteUrl(url) {
	var result;
	var base, currentUrl, anchor, root, path;
	
	base = document.getElementsByTagName("base");
	if (base.length != 1)
		throw new ReferenceError("Your document does not have a 'base' element!");

	if (/^[\w]+:\/\//.test(url)) {
		result = url;
	} else {
		if (url.charAt(0) == "/") {
			anchor = document.createElement('a');
			anchor.setAttribute("href", base[0].href);
			path = (anchor.pathname == "/" ? "" : (anchor.pathname.endsWith("/") ? anchor.pathname.substring(0, anchor.pathname.length - 1) : anchor.pathname));
			root = anchor.protocol + "//" + anchor.hostname + (anchor.port ? ":" + anchor.port : "") + path;
			result = root + url;
		} else {
			currentUrl = getCurrentUrl();
			anchor = document.createElement("a");
			anchor.setAttribute("href", currentUrl);			
			path = anchor.pathname.substring(0, anchor.pathname.lastIndexOf("/"));
			root = anchor.protocol + "//" + anchor.hostname + (anchor.port ? ":" + anchor.port : "") + path;
			result = root + "/" + url;
		}
	}

	return result;
}

function redirectCurrent() {
	var currentUrl;

	currentUrl = getCurrentUrl();
	redirect(currentUrl);
}

function redirectBackwards() {
	var currentUrl, previousUrl;

	currentUrl = getCurrentUrl();
	previousUrl = popReturnUrl();
	while (currentUrl === previousUrl) {
		previousUrl = popReturnUrl();
	}
	redirect(previousUrl)
}

function redirect(url, target) {
	var absoluteUrl;

	absoluteUrl = getAbsoluteUrl(url);
	if (target)
		window.open(absoluteUrl, target);
	else
		window.location.href = absoluteUrl;
}

/* Dealing with return URLs ------------------------------------------------ */

const returnStackKey = "return-stack", returnStackSeparator = "\n";

function getCurrentUrl() {
	var result;

	result = window.location.href;
	result = getAbsoluteUrl(result);
	if (result.endsWith("/"))
		result = result.substring(0, result.length - 1);

	return result;
}

function getCurrentMethod() {
	var result;

	result = document.querySelector("meta[name='method']").getAttribute("content");
	result = result.toUpperCase();
	
	return result;
}

function clearReturnUrl() {
	sessionStorage.removeItem(returnStackKey);
}

function pushCurrentUrl() {
	var currentMethod, currentUrl, topUrl;

	currentMethod = getCurrentMethod();	
	if (currentMethod == "GET") {
		currentUrl = getCurrentUrl();
		topUrl = peekReturnUrl();
		if (currentUrl !== topUrl)
			pushReturnUrl(currentUrl);
	}
}

function pushReturnUrl(url) {
	var absoluteUrl, currentStack;

	absoluteUrl = getAbsoluteUrl(url)
	currentStack = sessionStorage.getItem(returnStackKey);
	if (currentStack == null) {
		currentStack = absoluteUrl;
	} else {
		currentStack = currentStack.concat(returnStackSeparator, absoluteUrl);
	}

	sessionStorage.setItem(returnStackKey, currentStack);
}

function popReturnUrl() {
	var result;
	var returnStack, position;

	returnStack = sessionStorage.getItem(returnStackKey);
	if (returnStack == null)
		result = getAbsoluteUrl("/");
	else {
		position = returnStack.lastIndexOf(returnStackSeparator);
		if (position == -1) {
			sessionStorage.removeItem(returnStackKey);
			result = returnStack;
		} else {
			result = returnStack.substring(position + returnStackSeparator.length);
			returnStack = returnStack.substring(0, position);
			sessionStorage.setItem(returnStackKey, returnStack);
		}
	}

	return result;
}

function peekReturnUrl() {
	var result;
	var position, returnStack;
	
	result = sessionStorage.getItem(returnStackKey);
	if (result == null)
		result = getAbsoluteUrl("/");
	else {
		returnStack = sessionStorage.getItem(returnStackKey);
		position = returnStack.lastIndexOf(returnStackSeparator);
		if (position == -1) {
			result = returnStack;
		} else {
			result = returnStack.substring(position + returnStackSeparator.length);
		}
	}
	
	return result;
}

function canReturn() {
	var result;
	var returnStack;

	returnStack = sessionStorage.getItem(returnStackKey);
	result = (returnStack !== null && returnStack !== "");
	
	return result;
}

/* Object extensions ------------------------------------------------------- */


if (!String.prototype.format) {
	String.prototype.format = function() {
		var result;
		var args;

		args = arguments;
		result = this.replace(/{(\d+)}/g, function(match, number) {
			return typeof args[number] != "undefined" ? args[number] : match;
		});

		return result;
	};
}

/* Simulating requests ----------------------------------------------------- */

function performRequest(method, path, params) {
	var form, field;
	  
	form = document.createElement("form");
	form.method = method;	
	form.action = path;
	
	if (params != null) {
		for (const key in params) {
			field = document.createElement("input");
			field.type = "text";
			field.name = key;
			field.value = params[key];
			
			form.appendChild(hiddenField);
		}
	}
	
	document.body.appendChild(form);
	form.appendChild(document.createElement("button"));
	form.submit();
}


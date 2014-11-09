function setRad(dis) {
//	console.log("ok");
	document.getElementById('dis').value = dis;
	
	var b = 'title';
	var mi = getURLParameter('min');
	var ma = getURLParameter('max');
	var t = getURLParameter(b);
	
	if (mi != null) {
		$('<input>').attr({
			type : 'hidden',
			value : mi,
			name : 'min',
			id: 'min'
		}).appendTo('#locform');
	}
	if (ma != null) {
		$('<input>').attr({
			type : 'hidden',
			value : ma,
			name : 'max',
			id: 'max'
		}).appendTo('#locform');
	}
	
	if (t != null) {
		$('<input>').attr({
			type : 'hidden',
			value : t,
			name : b,
			id: b
		}).appendTo('#locform');
	}
	
	$('#locform').submit();
}

function popSub() {
	var cat = $("#category").find(":selected").val();
	
//	$("#categoryFilter").val(dog);
	
	var p = "http://www.elovendo.com/bazaar/getsubs/";
	$.getJSON(p+cat, function(json) {
		jQuery.each(json.output, function() {
			var o = document.createElement('option');
			o.value = this.id;
			o.innerHTML = this.name;
			$("#subCategory").append(o);
		})
	})
}

function getURLParameter(name) {
	return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null
}

function getUrlParams() {
	var lat = getURLParameter('lat');
	var lng = getURLParameter('lng');
	var dis = getURLParameter('dis');
	var mi = getURLParameter('min');
	var ma = getURLParameter('max');
	
	var dest = "";
	
	if (lat != null && lng != null)
		if (dis != null)
			dest = "?lat=" + lat + "&lng=" + lng + "&dis=" + dis;
		else
			dest = "?lat=" + lat + "&lng=" + lng;
	
	if (mi != null) dest = dest + "&min=" + mi;
	if (ma != null) dest = dest + "&max=" + ma;
	
	return dest;
}

function die() {
	var itemId = document.getElementById('itemId').value;
	var s = "/bazaar/delete/item";
	var cv = $("meta[name='_csrf']").attr("content");
	var cn = $("meta[name='_csrf_header']").attr("content");
	$.ajax({
		type : 'POST',
		beforeSend : function(request) {
			request.setRequestHeader(cn, cv);
		},
		url : s,
		data : ({
			id: itemId
		}),
		success: dieRmv
	});
}

function dieRmv(val) {
//	console.log("died " + val); 
	$('#did'+val).remove();
	$('#removeModal').modal('hide');
}

//function submitSearchForm() {
//	console.log("submiting prize");
////	form = $("#prizeForm");
////	var params = getUrlParams();
////	console.log("params " + params);
////	
////	form.submit(function(e) {
////		console.log("submitted form " + e);
////	})
//	return true;
////	//disable empty fields so they don't clutter up the url
////	form.find(':input[value=""]').attr('disabled', true);
//}

function removeParam(key, sourceURL) {
	var rtn = sourceURL.split("?")[0], param, params_arr = [], queryString = (sourceURL.indexOf("?") !== -1) ? sourceURL.split("?")[1] : "";
	if (queryString !== "") {
		params_arr = queryString.split("&");
		for (var i = params_arr.length - 1; i >= 0; i -= 1) {
			param = params_arr[i].split("=")[0];
			if (param === key) {
				params_arr.splice(i, 1);
			}
		}
		rtn = rtn + "?" + params_arr.join("&");
	}
	return rtn;
}
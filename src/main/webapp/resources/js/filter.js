//$('#categorySelect').change(function() {
//  // set the window's location property to the value of the option the user has selected
//  window.location = $(this).val();
//});

//$(document).ready( function() {
//	var categorySelect = document.getElementById("categorySelect");
//	var provinceSelect = document.getElementById("categorySelect");
	
//	$.get("/elovendo/messages/getUnread", function(data) {
//		try {
//			console.log("checking for new messages");
//			document.getElementById('messageBadge').innerHTML = data;
//			if (data>0) {
//				console.log("There are message unread");
//				var al = document.createElement('div');
//				al.className = 'falert alert alert-warning';
//				al.id = "favMesDiv";
//				var sp = document.createElement('span');
//				sp.id = "favMes";
//				sp.innerHTML = "New message";
//				al.appendChild(sp);
//				var cont = document.getElementById('container');
//				document.body.insertBefore(al, cont);
//				$('#favMesDiv').delay(100).fadeIn().delay(3000).fadeOut();
//			}
//		} catch(err) {}
//	});
	
//	$('#categorySelect').change( function() {
//		var select = $(this).val();
//		
//		if (select != "")
//			location.href = "/bazaar/" + $(this).val() + getUrlParams();
//	});
	
//	var p = "http://www.elovendo.com/bazaar/";
//	var suf = "getsubs/";
//	$('#categoryFilter').change( function() {
//		var select = $(this).val();
//		
//		if (select != "")
//			location.href = "/bazaar/category/" + $(this).val() + getUrlParams();
//		else
//			location.href = "/bazaar/all" + getUrlParams();
//	});
//});

function setRad(dis) {
//	console.log("ok");
	document.getElementById('dis').value = dis;
	
	var mi = getURLParameter('min');
	var ma = getURLParameter('max');
	
	if (mi != null) {
		console.log("here we are");
//		var o = document.createElement('input');
//		o.value = mi;
//		o.innerHTML = "min";
//		o.type = "hidden";
//		$('#locationForm').append(o);
		$('<input>').attr({
		    type: 'hidden',
		    value: mi,
		    name: 'min'
		}).appendTo('#locationForm');
	}
	if (ma != null) {
		var o = document.createElement('input');
		o.value = ma;
		o.innerHTML = "max";
		o.type = "hidden";
		$('#locationForm').append(o);
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
    var rtn = sourceURL.split("?")[0],
        param,
        params_arr = [],
        queryString = (sourceURL.indexOf("?") !== -1) ? sourceURL.split("?")[1] : "";
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
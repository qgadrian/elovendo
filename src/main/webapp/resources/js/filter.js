//$('#categorySelect').change(function() {
//  // set the window's location property to the value of the option the user has selected
//  window.location = $(this).val();
//});

$(document).ready( function() {
	var categorySelect = document.getElementById("categorySelect");
	var provinceSelect = document.getElementById("categorySelect");
	
	$('#categorySelect').change( function() {
		var select = $(this).val();
		
		if (select != "")
			location.href = "/bazaar/" + $(this).val() + getUrlParams();
	});
	
	$('#subCategory').change( function() {
		console.log('sub ' + $(this).innerHTML);
		var select = $(this).val();
		if (select != "") {
			location.href = "/bazaar/sub/" + $('#subCategory option:selected').text() + getUrlParams();
		}
	});
	
	var p = "http://www.elovendo.com/bazaar/";
	var suf = "getsubs/";
	$('#categoryFilter').change( function() {
		var select = $(this).val();
		
		if (select != "")
			location.href = "/bazaar/category/" + $(this).val() + getUrlParams();
		else
			location.href = "/bazaar/all" + getUrlParams();
	});
});

function setRad(dis) {
	console.log("ok");
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
	var hodor = document.URL;
	var dog;
	if (hodor.indexOf("bazaar/category") > -1) {
		var sa = hodor.lastIndexOf("/")+1;
		var jim = hodor.indexOf("?");
		dog = hodor.substr(sa,jim-sa);
		// FIXME: Work with index
		if (dog.indexOf("ecnolog") > -1) dog = "Tecnología";
	}
	
	$("#categoryFilter").val(dog);
	
	var p = "http://www.elovendo.com/bazaar/";
	var suf = "getsubs/s/";
	$.getJSON(p+suf+dog, function(json) {
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
	var s = "/site/delete/item";
	$.ajax({
        type : 'POST',
        url : s,
        data : ({
            id: itemId
        }),
        success: dieRmv
    });
}

function dieRmv(val) {
	console.log("died " + val); 
	$('#did'+val).remove();
	$('#removeModal').modal('hide');
}

function favClick(toggle) {
	console.log("clicked fav at " + toggle.value);
	var f = toggle.value;
	var e = "/site/item/fav";
	jQuery.post( e,
			{id: f}).done(function(data) {
				console.log("data: " + data);
				if (data) {
					document.getElementById('i'+f).className = "glyphicon glyphicon-heart";
//						$('#f'+f).prop('checked', true);
				}
				else {
					document.getElementById('i'+f).className = "glyphicon glyphicon-heart-empty";
//						$('#f'+f).prop('checked', false);
				}
			}).fail(function() {
			    alert( "error" );
			  })
			  .always(function() {
			    alert( "finished" );
			});
}

function submitSearchForm() {
	quizSearchForm = jQuery("#prizeForm");
	//disable empty fields so they don't clutter up the url
	quizSearchForm.find(':input[value=""]').attr('disabled', true);
	
	quizSearchForm.submit();
}
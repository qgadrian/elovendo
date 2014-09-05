//$('#categorySelect').change(function() {
//  // set the window's location property to the value of the option the user has selected
//  window.location = $(this).val();
//});

$(document).ready( function() {
	var categorySelect = document.getElementById("categorySelect");
	var provinceSelect = document.getElementById("categorySelect");
	
	$('#categorySelect').change( function() {
		var select = $(this).val();
		if (select != "") {
			var lat = getURLParameter('lat');
			var lng = getURLParameter('lng');
			var dis = getURLParameter('dis');
			if (lat != null && lng != null) 
				if (dis != null) location.href = "/bazaar/" + $(this).val() + "?lat=" + lat + "&lng=" + lng + "&dis=" + dis;
				else location.href = "/bazaar/" + $(this).val() + "?lat=" + lat + "&lng=" + lng;
		}
	});
	
	var p = "http://www.elovendo.com/bazaar/";
	var suf = "getsubs/";
	$('#categoryFilter').change( function() {
		var select = $(this).val();
		if (select != "") {
			var lat = getURLParameter('lat');
			var lng = getURLParameter('lng');
			var dis = getURLParameter('dis');
			if (lat != null && lng != null) 
				if (dis != null) location.href = "/bazaar/category/" + $(this).val() + "?lat=" + lat + "&lng=" + lng + "&dis=" + dis;
				else location.href = "/bazaar/category/" + $(this).val() + "?lat=" + lat + "&lng=" + lng;
		}
	});
});

function popSub() {
	var hodor = document.URL;
	var dog;
	if (hodor.indexOf("bazaar/category") > -1) {
		var sa = hodor.lastIndexOf("/")+1;
		var jim = hodor.indexOf("?");
		dog = hodor.substr(sa,jim-sa);
	}
	// FIXME: Work with index
	if (dog.indexOf("Tecnolog") > -1) dog = "Tecnología";
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

function submitSearchForm() {
	quizSearchForm = jQuery("#prizeForm");
	//disable empty fields so they don't clutter up the url
	quizSearchForm.find(':input[value=""]').attr('disabled', true);
	quizSearchForm.submit();
}
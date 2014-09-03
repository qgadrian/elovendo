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
			location.href = "/bazaar/sub/" + $(this).val();
	});
	
	$('#provinceSelect').change( function() {
		var provinceValue = $(this).val();
		var subCatValue = $(categorySelect).val();
		console.log("subcategory is " + subCatValue);
		console.log("provinceis " + provinceValue);
		if (provinceValue != "")
			location.href = window.location.pathname + "?province=" + provinceValue;
		console.log("province selected is " + provinceValue);
	});
   
});

function submitSearchForm() {
	quizSearchForm = jQuery("#prizeForm");
	//disable empty fields so they don't clutter up the url
	quizSearchForm.find(':input[value=""]').attr('disabled', true);
	quizSearchForm.submit();
}
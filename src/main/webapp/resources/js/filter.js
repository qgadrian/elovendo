//$('#categorySelect').change(function() {
//  // set the window's location property to the value of the option the user has selected
//  window.location = $(this).val();
//});

$(document).ready( function() {
	$('#categorySelect').change( function() {
		var select = $(this).val();
		if (select != "") 
			location.href = "/bazaar/sub/" + $(this).val();
	});
	
	$('#provinceSelect').change( function() {
		var select = $(this).val();
		console.log("province selected is " + select);
	});
   
});

function submitSearchForm(){
	  quizSearchForm = jQuery("#prizeForm");
	  //disable empty fields so they don't clutter up the url
	  quizSearchForm.find(':input[value=""]').attr('disabled', true);
	  quizSearchForm.submit();
	}
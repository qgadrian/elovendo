//$('#categorySelect').change(function() {
//  // set the window's location property to the value of the option the user has selected
//  window.location = $(this).val();
//});

$(document).ready( function() {
   $('#categorySelect').change( function() {
      location.href = "/bazaar/sub/" + $(this).val();
   });
});
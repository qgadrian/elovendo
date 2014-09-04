var placeSearch, autocomplete;
		function initAutoComplete() {
		  // Create the autocomplete object, restricting the search
		  // to geographical location types.
		  autocomplete = new google.maps.places.Autocomplete(
		      /** @type {HTMLInputElement} */(document.getElementById('autocomplete')),
		      { types: ['geocode'] });
		  // When the user selects an address from the dropdown,
		  // populate the address fields in the form.
		  google.maps.event.addListener(autocomplete, 'place_changed', function() {
		    geoAddress();
		  });
		}
		
		function geoAddress() {
			var place = autocomplete.getPlace();
// 			console.log("result: " + place.geometry.location.lat());
// 			console.log("result: " + place.geometry.location.lng());
			document.getElementById('lat').value = place.geometry.location.lat();
			document.getElementById('lng').value = place.geometry.location.lng();
			addMarker(place.geometry.location.lat(), place.geometry.location.lng());
		}
		
		// Add a marker to the map and push to the array.
//TODO: search first makes no marker appear in map
			  function addMarker(lat, lng) {
				  var location = new google.maps.LatLng(lat, lng, false); 
				  if (marker == null) {
				    marker = new google.maps.Marker({
				      position: location,
				      map: map
				    });
			    } else marker.setPosition(location);
			    
			    map.setCenter(location);
// 			    markers.push(marker);
			  }


		var loc;
		var marker;
		var map;
		var la = -34.397;
		var lo = 150.644;
		
		function initialize() {
			getLocation();
			var mapOptions = {
				center : new google.maps.LatLng(la, lo),
				zoom : 1,
				maxZoom: 18,
				streetViewControl: false,
				mapTypeId : google.maps.MapTypeId.ROADMAP
			};
			map = new google.maps.Map(
				document.getElementById("map_canvas"), mapOptions);

			// This event listener will call addMarker() when the map is clicked.
			google.maps.event.addListener(map, 'click', function(event) {
				marker.setMap(null); // clear marker in map
				addMarker(event.latLng);
				document.getElementById("latitude").value = event.latLng.lat();
				document.getElementById("longitude").value = event.latLng.lng();
				document.getElementById('createButton').disabled = false;
			});
			
			// Add a marker to the map and push to the array.
			  function addMarker(location) {
			    marker = new google.maps.Marker({
			      position: location,
			      map: map
			    });
// 			    markers.push(marker);
			  }
			
			// To add the marker to the map, use the 'map' property
// 			var marker = new google.maps.Marker({
// 				position: coorLang,
// 				map: map,
// 				title:"Localización"
// 			});
			marker = new google.maps.Marker();
		}
		
		function getLocation() {
			if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition(getPosition);
			}
		}
		
		function getPosition(position) {
			loc = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
			map.setCenter(loc);
			map.setZoom(13);
		}
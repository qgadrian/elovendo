var placeSearch, autocomplete;
function initAutoComplete() {
	// Create the autocomplete object, restricting the search
	// to geographical location types.
	autocomplete = new google.maps.places.Autocomplete(
	/** @type {HTMLInputElement} */
	(document.getElementById('autocomplete')), {
		types : [ 'geocode' ]
	});
	// When the user selects an address from the dropdown,
	// populate the address fields in the form.
	google.maps.event.addListener(autocomplete, 'place_changed', function() {
		geoAddress();
	});
}

function geoAddress() {
	var place = autocomplete.getPlace();
	document.getElementById('lat').value = place.geometry.location.lat();
	document.getElementById('lng').value = place.geometry.location.lng();
	if (map != null)
		addMarker(place.geometry.location.lat(), place.geometry.location.lng());
}

// Add a marker to the map and push to the array.
// TODO: search first makes no marker appear in map
function addMarker(lat, lng) {
	var location = new google.maps.LatLng(lat, lng, false);
	if (marker == null) {
		marker = new google.maps.Marker({
			position : location,
			map : map
		});
	} else
		marker.setPosition(location);
	map.setCenter(location);
	// markers.push(marker);
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
		maxZoom : 18,
		streetViewControl : false,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);

	// This event listener will call addMarker() when the map is clicked.
	google.maps.event.addListener(map, 'click', function(event) {
		marker.setMap(null); // clear marker in map
		addMarker(event.latLng);
		document.getElementById("lat").value = event.latLng.lat();
		document.getElementById("lng").value = event.latLng.lng();
		document.getElementById('createButton').disabled = false;
	});

	// Add a marker to the map and push to the array.
	function addMarker(location) {
		marker = new google.maps.Marker({
			position : location,
			map : map
		});
		// markers.push(marker);
	}

	// To add the marker to the map, use the 'map' property
	// var marker = new google.maps.Marker({
	// position: coorLang,
	// map: map,
	// title:"Localización"
	// });
	marker = new google.maps.Marker();
}

function getLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(getPosition);
	}
}

function getPosition(position) {
	loc = new google.maps.LatLng(position.coords.latitude,
			position.coords.longitude);
	map.setCenter(loc);
	map.setZoom(13);
}

function initLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(getInitPosition);
	}
}

function getInitPosition(position) {
	var lat = document.getElementById("lat").value = position.coords.latitude;
	var lng = document.getElementById("lng").value = position.coords.longitude;

	// initialize geosearch in index
	var arr = [], l = document.links;
	for (var i = 0; i < l.length; i++) {
		var ela = l[i].href;
		if (ela.indexOf("bazaar/category") > -1) {
			l[i].href = ela + "?lat=" + lat + "&lng=" + lng;
		}
	}

}

// Geo for currency
function getCountryCurrency(callback) {
	$.getJSON("http://ipinfo.io/json", function(json) {
		callback(json.country);
	});
}

function setCurrelo(currelo) {
	
	var currecountry = '{"BD": "BDT", "BE": "EUR", "BF": "XOF", "BG": "BGN", "BA": "BAM", "BB": "BBD", "WF": "XPF", "BL": "EUR", "BM": "BMD", "BN": "BND", "BO": "BOB", "BH": "BHD", "BI": "BIF", "BJ": "XOF", "BT": "BTN", "JM": "JMD", "BV": "NOK", "BW": "BWP", "WS": "WST", "BQ": "USD", "BR": "BRL", "BS": "BSD", "JE": "GBP", "BY": "BYR", "BZ": "BZD", "RU": "RUB", "RW": "RWF", "RS": "RSD", "TL": "USD", "RE": "EUR", "TM": "TMT", "TJ": "TJS", "RO": "RON", "TK": "NZD", "GW": "XOF", "GU": "USD", "GT": "GTQ", "GS": "GBP", "GR": "EUR", "GQ": "XAF", "GP": "EUR", "JP": "JPY", "GY": "GYD", "GG": "GBP", "GF": "EUR", "GE": "GEL", "GD": "XCD", "GB": "GBP", "GA": "XAF", "SV": "USD", "GN": "GNF", "GM": "GMD", "GL": "DKK", "GI": "GIP", "GH": "GHS", "OM": "OMR", "TN": "TND", "JO": "JOD", "HR": "HRK", "HT": "HTG", "HU": "HUF", "HK": "HKD", "HN": "HNL", "HM": "AUD", "VE": "VEF", "PR": "USD", "PS": "ILS", "PW": "USD", "PT": "EUR", "SJ": "NOK", "PY": "PYG", "IQ": "IQD", "PA": "PAB", "PF": "XPF", "PG": "PGK", "PE": "PEN", "PK": "PKR", "PH": "PHP", "PN": "NZD", "PL": "PLN", "PM": "EUR", "ZM": "ZMK", "EH": "MAD", "EE": "EUR", "EG": "EGP", "ZA": "ZAR", "EC": "USD", "IT": "EUR", "VN": "VND", "SB": "SBD", "ET": "ETB", "SO": "SOS", "ZW": "ZWL", "SA": "SAR", "ES": "EUR", "ER": "ERN", "ME": "EUR", "MD": "MDL", "MG": "MGA", "MF": "EUR", "MA": "MAD", "MC": "EUR", "UZ": "UZS", "MM": "MMK", "ML": "XOF", "MO": "MOP", "MN": "MNT", "MH": "USD", "MK": "MKD", "MU": "MUR", "MT": "EUR", "MW": "MWK", "MV": "MVR", "MQ": "EUR", "MP": "USD", "MS": "XCD", "MR": "MRO", "IM": "GBP", "UG": "UGX", "TZ": "TZS", "MY": "MYR", "MX": "MXN", "IL": "ILS", "FR": "EUR", "IO": "USD", "SH": "SHP", "FI": "EUR", "FJ": "FJD", "FK": "FKP", "FM": "USD", "FO": "DKK", "NI": "NIO", "NL": "EUR", "NO": "NOK", "NA": "NAD", "VU": "VUV", "NC": "XPF", "NE": "XOF", "NF": "AUD", "NG": "NGN", "NZ": "NZD", "NP": "NPR", "NR": "AUD", "NU": "NZD", "CK": "NZD", "XK": "EUR", "CI": "XOF", "CH": "CHF", "CO": "COP", "CN": "CNY", "CM": "XAF", "CL": "CLP", "CC": "AUD", "CA": "CAD", "CG": "XAF", "CF": "XAF", "CD": "CDF", "CZ": "CZK", "CY": "EUR", "CX": "AUD", "CR": "CRC", "CW": "ANG", "CV": "CVE", "CU": "CUP", "SZ": "SZL", "SY": "SYP", "SX": "ANG", "KG": "KGS", "KE": "KES", "SS": "SSP", "SR": "SRD", "KI": "AUD", "KH": "KHR", "KN": "XCD", "KM": "KMF", "ST": "STD", "SK": "EUR", "KR": "KRW", "SI": "EUR", "KP": "KPW", "KW": "KWD", "SN": "XOF", "SM": "EUR", "SL": "SLL", "SC": "SCR", "KZ": "KZT", "KY": "KYD", "SG": "SGD", "SE": "SEK", "SD": "SDG", "DO": "DOP", "DM": "XCD", "DJ": "DJF", "DK": "DKK", "VG": "USD", "DE": "EUR", "YE": "YER", "DZ": "DZD", "US": "USD", "UY": "UYU", "YT": "EUR", "UM": "USD", "LB": "LBP", "LC": "XCD", "LA": "LAK", "TV": "AUD", "TW": "TWD", "TT": "TTD", "TR": "TRY", "LK": "LKR", "LI": "CHF", "LV": "EUR", "TO": "TOP", "LT": "LTL", "LU": "EUR", "LR": "LRD", "LS": "LSL", "TH": "THB", "TF": "EUR", "TG": "XOF", "TD": "XAF", "TC": "USD", "LY": "LYD", "VA": "EUR", "VC": "XCD", "AE": "AED", "AD": "EUR", "AG": "XCD", "AF": "AFN", "AI": "XCD", "VI": "USD", "IS": "ISK", "IR": "IRR", "AM": "AMD", "AL": "ALL", "AO": "AOA", "AQ": "", "AS": "USD", "AR": "ARS", "AU": "AUD", "AT": "EUR", "AW": "AWG", "IN": "INR", "AX": "EUR", "AZ": "AZN", "IE": "EUR", "ID": "IDR", "UA": "UAH", "QA": "QAR", "MZ": "MZN"}'
	var json = JSON.parse(currecountry);
	var curry = json[currelo];
	
	// If there is no such currency available, just set USD as currency
	if ($('#currelo > option[value="'+ curry +'"]').length > 0)
		$("#currelo").val(curry);
	else 
		$("#currelo").val('USD');
}

function getCurrentCurry(cc) {
	
	var currecountry = '{"BD": "BDT", "BE": "EUR", "BF": "XOF", "BG": "BGN", "BA": "BAM", "BB": "BBD", "WF": "XPF", "BL": "EUR", "BM": "BMD", "BN": "BND", "BO": "BOB", "BH": "BHD", "BI": "BIF", "BJ": "XOF", "BT": "BTN", "JM": "JMD", "BV": "NOK", "BW": "BWP", "WS": "WST", "BQ": "USD", "BR": "BRL", "BS": "BSD", "JE": "GBP", "BY": "BYR", "BZ": "BZD", "RU": "RUB", "RW": "RWF", "RS": "RSD", "TL": "USD", "RE": "EUR", "TM": "TMT", "TJ": "TJS", "RO": "RON", "TK": "NZD", "GW": "XOF", "GU": "USD", "GT": "GTQ", "GS": "GBP", "GR": "EUR", "GQ": "XAF", "GP": "EUR", "JP": "JPY", "GY": "GYD", "GG": "GBP", "GF": "EUR", "GE": "GEL", "GD": "XCD", "GB": "GBP", "GA": "XAF", "SV": "USD", "GN": "GNF", "GM": "GMD", "GL": "DKK", "GI": "GIP", "GH": "GHS", "OM": "OMR", "TN": "TND", "JO": "JOD", "HR": "HRK", "HT": "HTG", "HU": "HUF", "HK": "HKD", "HN": "HNL", "HM": "AUD", "VE": "VEF", "PR": "USD", "PS": "ILS", "PW": "USD", "PT": "EUR", "SJ": "NOK", "PY": "PYG", "IQ": "IQD", "PA": "PAB", "PF": "XPF", "PG": "PGK", "PE": "PEN", "PK": "PKR", "PH": "PHP", "PN": "NZD", "PL": "PLN", "PM": "EUR", "ZM": "ZMK", "EH": "MAD", "EE": "EUR", "EG": "EGP", "ZA": "ZAR", "EC": "USD", "IT": "EUR", "VN": "VND", "SB": "SBD", "ET": "ETB", "SO": "SOS", "ZW": "ZWL", "SA": "SAR", "ES": "EUR", "ER": "ERN", "ME": "EUR", "MD": "MDL", "MG": "MGA", "MF": "EUR", "MA": "MAD", "MC": "EUR", "UZ": "UZS", "MM": "MMK", "ML": "XOF", "MO": "MOP", "MN": "MNT", "MH": "USD", "MK": "MKD", "MU": "MUR", "MT": "EUR", "MW": "MWK", "MV": "MVR", "MQ": "EUR", "MP": "USD", "MS": "XCD", "MR": "MRO", "IM": "GBP", "UG": "UGX", "TZ": "TZS", "MY": "MYR", "MX": "MXN", "IL": "ILS", "FR": "EUR", "IO": "USD", "SH": "SHP", "FI": "EUR", "FJ": "FJD", "FK": "FKP", "FM": "USD", "FO": "DKK", "NI": "NIO", "NL": "EUR", "NO": "NOK", "NA": "NAD", "VU": "VUV", "NC": "XPF", "NE": "XOF", "NF": "AUD", "NG": "NGN", "NZ": "NZD", "NP": "NPR", "NR": "AUD", "NU": "NZD", "CK": "NZD", "XK": "EUR", "CI": "XOF", "CH": "CHF", "CO": "COP", "CN": "CNY", "CM": "XAF", "CL": "CLP", "CC": "AUD", "CA": "CAD", "CG": "XAF", "CF": "XAF", "CD": "CDF", "CZ": "CZK", "CY": "EUR", "CX": "AUD", "CR": "CRC", "CW": "ANG", "CV": "CVE", "CU": "CUP", "SZ": "SZL", "SY": "SYP", "SX": "ANG", "KG": "KGS", "KE": "KES", "SS": "SSP", "SR": "SRD", "KI": "AUD", "KH": "KHR", "KN": "XCD", "KM": "KMF", "ST": "STD", "SK": "EUR", "KR": "KRW", "SI": "EUR", "KP": "KPW", "KW": "KWD", "SN": "XOF", "SM": "EUR", "SL": "SLL", "SC": "SCR", "KZ": "KZT", "KY": "KYD", "SG": "SGD", "SE": "SEK", "SD": "SDG", "DO": "DOP", "DM": "XCD", "DJ": "DJF", "DK": "DKK", "VG": "USD", "DE": "EUR", "YE": "YER", "DZ": "DZD", "US": "USD", "UY": "UYU", "YT": "EUR", "UM": "USD", "LB": "LBP", "LC": "XCD", "LA": "LAK", "TV": "AUD", "TW": "TWD", "TT": "TTD", "TR": "TRY", "LK": "LKR", "LI": "CHF", "LV": "EUR", "TO": "TOP", "LT": "LTL", "LU": "EUR", "LR": "LRD", "LS": "LSL", "TH": "THB", "TF": "EUR", "TG": "XOF", "TD": "XAF", "TC": "USD", "LY": "LYD", "VA": "EUR", "VC": "XCD", "AE": "AED", "AD": "EUR", "AG": "XCD", "AF": "AFN", "AI": "XCD", "VI": "USD", "IS": "ISK", "IR": "IRR", "AM": "AMD", "AL": "ALL", "AO": "AOA", "AQ": "", "AS": "USD", "AR": "ARS", "AU": "AUD", "AT": "EUR", "AW": "AWG", "IN": "INR", "AX": "EUR", "AZ": "AZN", "IE": "EUR", "ID": "IDR", "UA": "UAH", "QA": "QAR", "MZ": "MZN"}'
	var json = JSON.parse(currecountry);
	var curry = json[cc];

	// If there is no such currency available, just set USD as currency
	if (curry != null)
		return curry;
	else 
		return 'USD';
}


function getCurrencySymbol(currency) {
	var currencySymbols = '{ "USD" : "$", "EUR" : "€", "GBP" : "£", "JPY" : "\u00a5" }'
	var json = JSON.parse(currencySymbols);
	
	var curry = json[currency];

	// If there is no such currency available, just set USD as currency
	if (curry != null) return curry;
	else return null;
}

//function formatPrize(country) {
//	var currency = getCurrentCurry(country);
//	
//	// Get all prize elements
//	var elements = document.getElementsByClassName("prizeLabel");
//	jQuery('.prizeLabel').each(function() {
//		var cE = $(this);
//		console.log("prize " + cE.attr('data-prize'));
//		console.log("current currency " + currency);
//		var currCurrency = cE.attr('data-currency');
//		if (currency != currCurrency) {
//			var prize = cE.attr('data-prize');
//			
//			fx.base = "USD";
//	    	fx.rates = {
//	    		"EUR" : 0.745101, // eg. 1 USD === 0.745101 EUR
//	    		"GBP" : 0.647710,
//	    		"HKD" : 7.781919,
//	    		"USD" : 1, // always include the base rate (1:1)
//		    	};
//	    	var p = fx.convert(prize, {from: currCurrency, to: currency}).toFixed(2);
//	    	console.log('prize should be ' + p); // getPrize convert from data-currency to currelo fix(2)
//	    	cE.attr('data-prize', p);
//	    	cE.attr('data-currency', currency);
//	    	var currSym = getCurrencySymbol(currency);
//	    	if (currSym != null) 
//	    		if (currency == 'USD') cE.text(currSym + p);
//	    		else cE.text(p + currSym);
//	    	else cE.text(p + ' ' + currency);
//		}
//	});
//}
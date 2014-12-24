$(document).ready( function() {

	$('#sPend').on('change', function() {
		cVl();
		gv("pending", 0);
		
	});
	
	$('#sPos').on('change', function() {
		cVl();
		gv("positive", 0);
	});
	
	$('#sNeg').on('change', function() {
		cVl();
		gv("negative", 0);
	});

});

function nx(url, p, s) {
	cVl();
	gv(url, p, s);
}

function cVl() {
	var node = document.getElementById('voteList');
	while (last = node.lastChild) node.removeChild(last);
}

function gv(mUrl, pa, s) {
	var url = "current/vote/" + mUrl; 
	$.get(url).done(function( json ) {
		jQuery.each(json.output, function() {
			var aid = this.userid + this.item;
			
			var a = document.createElement('a');
			a.setAttribute("id", aid);
			if (mUrl == "positive") {
				a.className = "list-group-item list-group-item-success text-center";
			}
			else if (mUrl == "negative") {
				a.className = "list-group-item list-group-item-danger text-center";
			}
			else  {
				a.className = "list-group-item text-center";
				a.setAttribute("style", "cursor: pointer;");
				a.setAttribute("onclick", "vstff(" + this.item  + ", " + this.userid + ")");
			}
			
			var h4 = document.createElement('h4');
			h4.className = "list-group-item-heading";
			h4.innerHTML = this.user;
			
			var p = document.createElement('p');
			p.className = "list-group-item-text";
			p.innerHTML = "\"" + this.message + "\"";
			
			a.appendChild(h4);
			a.appendChild(p);
			
			var inp = document.createElement('input');
			inp.setAttribute("type", "hidden");
			inp.setAttribute("id", 'item');
			inp.setAttribute("value", this.item);
			inp.setAttribute("name", "item");
			a.appendChild(inp);
			
			var inpu = document.createElement('input');
			inpu.setAttribute("type", "hidden");
			inpu.setAttribute("id", 'user');
			inpu.setAttribute("value", this.userid);
			inpu.setAttribute("name", "user");
			a.appendChild(inpu);
			
			$("#voteList").append(a);
		})
		
		if (json.page.totalPages > 1) {
			var ul = document.createElement('ul');
			ul.className = "pager";
			
			var li1 = document.createElement('li');
			var a1 = document.createElement('a');
			a1.innerHTML = "Previous";
			var np = pa > 0 ? pa - 1 : 0;
			a1.href = "javascript:nx('" + mUrl + "'," + np + "," + s +");";
			
			var li2 = document.createElement('li');
			var a2 = document.createElement('a');
			a2.innerHTML = "Next";
			var np = pa > json.page.totalPages ? pa + 1 : pa;
			a2.href = "javascript:nx('" + mUrl + "'," + np + "," + s +");";
			
			li1.appendChild(a1);
			li2.appendChild(a2);
			ul.appendChild(li1);
			ul.appendChild(li2);
			$("#voteList").append(ul);
		}
	  });
}
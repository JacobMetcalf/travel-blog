var map;
var centre;
var zoom = 5;
var routes = [];
var locations = {};

function clickClosure(key) {
  // use closure to curry the key
  var anchor = "#"+key;
  return function() {location.href = anchor;}
}
function initMap() {
  map = new google.maps.Map(document.getElementById("map"), {mapTypeId:"hybrid"});
  map.setCenter(centre);
  map.setZoom(zoom);
  routes.forEach(function(route) {new google.maps.Polyline(route).setMap(map);});
  for (var k in locations) {
    new google.maps.Marker({position:{lat:locations[k].lat,lng:locations[k].lng},
      map:map,icon:{path: google.maps.SymbolPath.CIRCLE,scale:5,fillOpacity:1,strokeColor:"#FF0000"},title:k})
      .addListener('click',clickClosure(k));
  }
}
$(document).ready(function() { 
  $('#content').magnificPopup({
     delegate: 'figure',
     callbacks: { elementParse: function(item) {
            var link = item.el.find("a");
            if (link.length!==0) {
                item.src = link.attr("href");
             }
          }},
     gallery:{enabled:true},
     image: { titleSrc: function(item) {                             
                var caption = item.el.find("figcaption");
                if (caption.length!==0) {
                    return caption.html();
                }}},
     type: 'image'
  });
  var mc = new Hammer(document.body);
  mc.on('swipeleft', function(){ $(".mfp-arrow-right").magnificPopup("next"); });
  mc.on('swiperight', function(){ $(".mfp-arrow-left").magnificPopup("prev"); });
});
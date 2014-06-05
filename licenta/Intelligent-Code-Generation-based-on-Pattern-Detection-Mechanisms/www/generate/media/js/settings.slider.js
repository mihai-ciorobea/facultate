jQuery(document).ready(function(){
    jQuery('.caption').show();
    jQuery('#slides').slides({
        "generateNextPrev":true ,
        "generatePagination":false ,
        "slideSpeed":SL_SLIDESPEED ,
        "effect":SL_EFFECT,
        "randomize":SL_RANDOMIZE,
        "hoverPause":SL_PAUSE,
        "play":SL_PLAYSPEED,
        "next":'next_navigation',
        "prev":'prev_navigation',
        "preload":false,

        "pause":2500,
		"animationStart":function(){
            jQuery('.caption').show();
			jQuery('.caption').animate({
				"left":-430
			},100);
		},
		"animationComplete": function(current){
			jQuery('.caption').animate({
				"left":430
			},400);
            
			if (window.console && console.log) {
				console.log(current);
			};
		}
	});
	
    jQuery('.caption').animate({
        "left":430
    } , 400 );
    
    jQuery('.slider_testimonials').slides({
        "generateNextPrev":false ,
        "generatePagination": false ,
        "slideSpeed": 800 ,
        "randomize": false,
        "hoverPause": true,
        "play": 0,
        "autoHeight": true,
        "pause":5000,
        "preload":true
    });
});
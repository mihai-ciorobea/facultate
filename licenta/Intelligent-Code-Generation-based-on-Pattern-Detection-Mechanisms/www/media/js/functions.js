jQuery(function(){
    jQuery('header nav > ul > li ul,header nav div > ul > li ul').hover(function(){
        jQuery( 'div.wrapper div.content' ).fadeTo( 'slow' , 0.4 );
    } , function(){
        jQuery( 'div.wrapper div.content' ).fadeTo( 'fast' , 1 );
    });
});


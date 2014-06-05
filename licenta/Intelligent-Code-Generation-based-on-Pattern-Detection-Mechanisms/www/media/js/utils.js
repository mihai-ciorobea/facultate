var utils = new Object();

utils.feedburner = function( uri ){
    window.open( 'http://feedburner.google.com/fb/a/mailverify?uri=' + uri , 'popupwindow' , 'scrollbars=yes,width=550,height=520' );
    return true;
}

utils.focusEvent = function( obj , text ){
    if( jQuery( obj ).val() == text ) {
        jQuery( obj ).val( '' );
    }
}

utils.blurEvent = function( obj , text ){
    if( jQuery( obj ).val() == '' ) {
        jQuery( obj ).val( text ); 
    }
}

utils.district = function( obj, selector ){
    
    jQuery(function(){
        jQuery( selector ).html( '' );
        jQuery( selector ).parent().children( 'div.district-loading' ).fadeIn( 'fast' );
        jQuery.post( ajaxurl , {
            "action" : 'get_district',
            "location" : jQuery( obj ).val()
        } , function( result ){
            jQuery( selector ).parent().children( 'div.district-loading' ).hide( );
            if( result.length ) {
                jQuery( selector ).html( result );
            }
        } );
    });
}

utils.district2 = function( obj, selector, id ){
    
    jQuery(function(){
        if( jQuery( obj ).is( ':checked') ){
            jQuery.post( ajaxurl , {
                "action" : 'get_district2',
                "location" : id
            } , function( result ){
                jQuery( obj ).parent().parent().children( 'div.' + selector ).html( result );
            } );
        }
        else{
            jQuery( obj ).parent().parent().children( 'div.' + selector ).html( '' );
        }
        
    });
}

utils.keyword = function( slug ){
    jQuery(function(){
        var v = jQuery( 'div.widget_search form input[type="text"]' ).val();
        if( v.length == 0 || v == 'Cauta dupa cuvinte cheie / ID ..' ){
            jQuery( 'div.widget_search form input[type="text"]' ).val( slug );
        }
        else{
            jQuery( 'div.widget_search form input[type="text"]' ).val( v + ',' + slug );
        }
        
        jQuery( 'div.widget_search form input[type="text"]' ).focus();
    });
}

utils.changeForm = function( selector ){
    jQuery(function(){
        jQuery( 'form.panel' ).hide();
        jQuery( 'form.panel' + selector ).show();
    });
}

utils.reset = function( selector ){
    jQuery(function(){
        jQuery( selector + ' select option' ).removeAttr( 'selected' );
        jQuery( selector + ' select' ).val( '' );
        jQuery( selector + ' input[type="checkbox"]' ).removeAttr( 'checked' );
        jQuery( selector + ' input[type="text"]' ).val('');
    });
}
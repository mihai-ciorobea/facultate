
(function($){
    var def = {
        options : {
            map : {
                zoom : 12,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            },
            pos : {
                lat : 45.10845964845801,
                lng : 24.359653502273563
            },
            markers : [],
            selectors : {
                markers  : '.select-markers',
                edit : '.edit-marker-form',
                add : '.add-marker-form'
            },
            listenerHandle : null,
            postID : 0
        },

        init:  function(){
            return this.each(function(){
                var gmap = new google.maps.Map( this , def.options.map );
                gmap.setCenter( new google.maps.LatLng( def.options.pos.lat , def.options.pos.lng ) );
                
                jQuery( 'input#map-zoom' ).val( gmap.getZoom() );
                jQuery( 'input#map-lat' ).val( gmap.getCenter().lat() );
                jQuery( 'input#map-lng' ).val( gmap.getCenter().lng() );
                    
                google.maps.event.addListener( gmap , 'zoom_changed', function() {
                    jQuery( 'input#map-zoom' ).val( gmap.getZoom() );
                    jQuery( 'input#map-lat' ).val( gmap.getCenter().lat() );
                    jQuery( 'input#map-lng' ).val( gmap.getCenter().lng() );
                });
                
                google.maps.event.addListener( gmap , 'bounds_changed', function() {
                    jQuery( 'input#map-zoom' ).val( gmap.getZoom() );
                    jQuery( 'input#map-lat' ).val( gmap.getCenter().lat() );
                    jQuery( 'input#map-lng' ).val( gmap.getCenter().lng() );
                });
                 
                def.addMarkers.apply( gmap );
                def.drawMarker.apply( gmap );
            });
        },
        
        initFrontEnd:  function(){
            return this.each(function(){
                var gmap = new google.maps.Map( this , def.options.map );
                gmap.setCenter( new google.maps.LatLng( def.options.pos.lat , def.options.pos.lng ) );
                def.addMarkersFrontEnd.apply( gmap );

            });
        },

        addMarkers: function(){
            var _self = this;
            var markerOption;
            var i = 0;
            
            if( def.options.markers[ i ].icon != "undefined" ){
                if( def.options.markers[ i ].icon == "standard" ){
                    markerOption = {
                        position: new google.maps.LatLng( def.options.markers[i].lat, def.options.markers[i].lng ),
                        map: _self,
                        draggable: true,
                        visible: true
                    };
                }else{
                    markerOption = {
                        position: new google.maps.LatLng( def.options.markers[i].lat, def.options.markers[i].lng ),
                        map: _self,
                        draggable: true,
                        visible: true,
                        icon: def.options.markers[i].src
                        /*shadow: def.options.markers[i].shadow */
                    };
                }
            }else{
                markerOption = {
                    position: new google.maps.LatLng( def.options.markers[i].lat, def.options.markers[i].lng ),
                    map: _self,
                    draggable: true,
                    visible: true
                };
            }
            
            var marker = new google.maps.Marker( markerOption );

            marker.set( 'id' , i );

            google.maps.event.addListener( marker , 'mouseup' , function( event ){

                map.marker = marker;
                
                jQuery( 'input#my-field-marker-lat' ).val( event.latLng.lat() );
                jQuery( 'input#my-field-marker-lng' ).val( event.latLng.lng() );

                /* crearea formei pentru marker current */
                /* map.vr.init = function( args ){
                    map.r( 'edit' , [ def.options.postID  , marker.get('id') , {lat: event.latLng.lat() , lng : event.latLng.lng()}] );
                }
                field.load(  map , 'editForm' , [ def.options.postID , marker.get('id') ] , '.panel-map-action' , '.marker-editbox' );*/
            });

            /*(function(i, marker) {




                alert( def.options.markers[i].id );


            })(i, marker);*/
        },
        addMarkersFrontEnd: function(){
            var _self = this;
            var markerOption;
            for( var i = 0; i < def.options.markers.length; i++ ){
                if( def.options.markers[i].icon != "undefined" ){
                    if( def.options.markers[i].icon == "standard" ){
                        markerOption = {
                            position: new google.maps.LatLng( def.options.markers[i].lat, def.options.markers[i].lng ),
                            map: _self,
                            draggable: false,
                            visible: true
                        };
                    }else{
                        markerOption = {
                            position: new google.maps.LatLng( def.options.markers[i].lat, def.options.markers[i].lng ),
                            map: _self,
                            draggable: false,
                            visible: true,
                            icon: def.options.markers[i].src,
                            shadow: def.options.markers[i].shadow
                        };
                    }
                }else{
                    markerOption = {
                        position: new google.maps.LatLng( def.options.markers[i].lat, def.options.markers[i].lng ),
                        map: _self,
                        draggable: false,
                        visible: true
                    };
                }
                
                var marker = new google.maps.Marker( markerOption );
                
                

                (function(i, marker) {
                    if( def.options.markers[i].html != null ){
                        google.maps.event.addListener( marker , 'click', function() {
                            var infowindow = new google.maps.InfoWindow({
                                content: def.options.markers[i].html
                            });
                            infowindow.open( _self , marker);
                        });
                    }
                    
                    marker.set( 'id' , def.options.markers[i].id );
                    
                })(i, marker);
            }
        },

        drawMarker: function(){
            var _self = this;
            var marker;
            jQuery( def.options.selectors.markers ).each(function(){
                jQuery( this ).click(function(){
                    if( def.options.listenerHandle != null ){
                        google.maps.event.removeListener( def.options.listenerHandle );
                    }
                    marker = this;    
                    def.options.listenerHandle = google.maps.event.addListener( _self , 'click' , function( event ){
                        
                        /* if( def.options.markers.length == 1 )
                            return;*/
                        
                        def.options.markers[ 0 ] = {
                            lat: event.latLng.lat(), 
                            lng: event.latLng.lng(),
                            html : '',
                            icon: '',
                            src: jQuery( marker ).attr('title'),
                            shadow: '',
                            id: def.options.markers.length
                        };
                        
                        
                        def.addMarkers.apply( _self );
                        
                        /* ajax get id */
                        /* jQuery.post( ajaxurl, {
                                action : map.vr.action,
                                method : 'add',
                                args: [ 
                                    def.options.postID ,
                                    event.latLng.lat() ,
                                    event.latLng.lng() ,
                                    jQuery( marker ).attr('alt'),
                                    jQuery( marker ).attr('src'),
                                    jQuery( def.options.selectors.markers  + '.shadow' ).val()
                                ]
                            } , function( result ){
                                
                                
                            }
                        );*/
                    });
                });
            })
        }
    }

    $.fn.gmap = function( options ){
        if( typeof options.map != "undefined" ){
            def.options.map = $.extend( {} , def.options.map , options.map );
        }
        if( typeof options.pos != "undefined" ){
            def.options.pos = $.extend( {} , def.options.pos , options.pos );
        }

        if( typeof options.markers != "undefined" ){
            def.options.markers = options.markers;
        }

        if( typeof options.selector != "undefined" ){
            def.options.selectors = $.extend( {} , def.options.selectors , options.selectors );
        }
        if( typeof options.postID != "undefined" ){
            if( options.postID > 0 ){
                def.options.postID = options.postID;
            }else{
                def.options.postID = jQuery('#post_ID').val();
            }
        }

        def.init.apply( this );
    }
    
    $.fn.gmapFrontEnd = function( options ){
        if( typeof options.map != "undefined" ){
            def.options.map = $.extend( {} , def.options.map , options.map );
        }
        if( typeof options.pos != "undefined" ){
            def.options.pos = $.extend( {} , def.options.pos , options.pos );
        }

        if( typeof options.markers != "undefined" ){
            def.options.markers = options.markers;
        }

        if( typeof options.selector != "undefined" ){
            def.options.selectors = $.extend( {} , def.options.selectors , options.selectors );
        }
        if( typeof options.postID != "undefined" ){
            if( options.postID > 0 ){
                def.options.postID = options.postID;
            }else{
                def.options.postID = jQuery('#post_ID').val();
            }
        }

        def.initFrontEnd.apply( this );
    }
})(jQuery);
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="recordsUrl" value="/producto/cmd"/>
<c:url var="productoUrl" value="/producto/productos"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Entrada de Productos</title>
        <link rel="icon" href="favicon.ico">
        <link rel="stylesheet" type="text/css" href="static/libs/w2ui/1.5/w2ui.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/styles.css" />
        <link rel="stylesheet" href="static/css/font-awesome.min.css"> 
        <script type="text/javascript" src="static/libs/jquery/js/jquery.min.js"></script>
        <script type="text/javascript" src="static/libs/w2ui/1.5/w2ui.js"></script>
        <script type="text/javascript" src="static/js/main.js"></script>
        
    </head>
    <body>
    
    	<div id="form" class="grid_small"></div>
        <script type="text/javascript">
        
        var conProducto = false; 
        var registroActivo;
        
        $(function () {
        	console.log('0');
        	//new w2field('list', { el: query('input[type=list]')[0], items: people, match: 'contain', markSearch: true });

            $.when(
                $.getJSON('${productoUrl}')
            ).then(function (productosResponse) {
                var productos = productosResponse.map(function(item,index){
                    return {
                        id: item.id, 
                        text: item.codProducto + ' - ' +item.nombre,
                        codProducto: item.codProducto,
                        nombre: item.nombre,
                        existencia: item.existencia,
                        precioCompra: item.precioCompra,
                        precioVenta: item.precioVenta,
                        cveProveedor: item.cveProveedor,
                        entrada: 0
                    };
                });
	
        	
            	$('#form').w2form({
                    name: 'form',
                    url: '${recordsUrl}',
                fields: [
                    {
                        name: 'producto',
                        type: 'list',
                        options:{
                            items: productos
                        },

                        match: 'contain',
                        markSearch: true,
                        required: true,
                        html: {
                            attr: 'size="40" maxlength="50"',
                            caption: 'Producto'
                        }
                    }, {
                        name: 'codProducto',
                        type: 'text',
                        required: false,
                        disabled: true,
                        options:{
                            espacios: true
                        },
                        html: {
                            attr: 'size="40" maxlength="255"',
                            caption: 'Código Producto'
                        }
                    }, {
                        name: 'nombre',
                        type: 'text',
                        required: false,
                        disabled: true,
                        options:{
                            espacios: true
                        },
                        html: {
                            attr: 'size="40" maxlength="255"',
                            caption: 'Nombre'
                        }
                    }, {
                        name: 'existencia',
                        type: 'int',
                        required: false,
                        disabled: true,
                        options:{
                            espacios: true
                        },
                        html: {
                            attr: 'size="40" maxlength="255"',
                            caption: 'Existencia'
                        }
                    }, {
                        name: 'cveProveedor',
                        type: 'text',
                        required: false,
                        disabled: true,
                        options:{
                            espacios: true
                        },
                        html: {
                            attr: 'size="40" maxlength="255"',
                            caption: 'Cve Proveedor'
                        }
                    }, {
                        name: 'entrada',
                        type: 'int',
                        required: true,
                        disabled: true,
                        options:{
                            espacios: false
                        },
                        html: {
                            attr: 'size="40" maxlength="255"',
                            caption: 'Entrada'
                        }
                    }
                ],
                record: {
                    id: (conProducto ? registroActivo.id : null),
                    codProducto: (conProducto ? registroActivo.codProducto : null),
                    nombre: (conProducto ? registroActivo.nombre : null),
                    existencia: (conProducto ? registroActivo.existencia : 0),
                    precioCompra: (conProducto ? registroActivo.precioCompra : 0.0),
                    precioVenta: (conProducto ? registroActivo.precioVenta : 0.0),
                    cveProveedor: (conProducto ? registroActivo.cveProveedor : null),
                    entrada: 0
                },
                onChange: function(event){
                    event.onComplete = function(){
                        if(event.target === "producto"){
                            if($('#producto').data('selected') !== null && typeof $('#producto').data('selected').id !== "undefined"){
                            	registroActivo = $('#producto').data('selected');
                            	conProducto = true;
                            }else{
                            	conProducto = false;
                            }
                            w2ui['form'].record['id'] = registroActivo.id;
                            w2ui['form'].record['codProducto'] = registroActivo.codProducto;
                            w2ui['form'].record['nombre'] = registroActivo.nombre;
                            w2ui['form'].record['existencia'] = registroActivo.existencia;
                            w2ui['form'].record['precioCompra'] = registroActivo.precioCompra;
                            w2ui['form'].record['precioVenta'] = registroActivo.precioVenta;
                            w2ui['form'].record['cveProveedor'] = registroActivo.cveProveedor;
                            w2ui['form'].record['entrada'] = 0;
                            w2ui['form'].set('entrada', {disabled: false});
                            w2ui['form'].refresh();
                        }
                    }
                },
                actions: {
                    save: {
                        caption: ('Guardar'),
                        class: 'w2ui-btn-blue',
                        onClick: function () {
                            
                        	console.log('Antes de Guardar');
                        	console.log(this.record);
                        	
                            if (this.validate(true).length === 0) {

                                this.record.existencia = registroActivo.existencia + this.record.entrada;

                                console.log(this.record);
                                this.save(function (data) {
                                	console.log(data);
                                    w2popup.close();
                                    if(data.status === 'success'){
                                        if(data.records[0] === null){
                                            w2alert('No es posible ingresar dos descripciones para un mismo evento');
                                        }else{
                                            if (true) {
                                                w2alert('Entrada ingresada correctamente');
                                                w2ui['form'].record['entrada'] = 0;
                                                w2ui['form'].refresh();
                                            }
                                        }
                                    }else{
                                        w2alert('No fue posible ingresar el producto. ' + data.message.toUpperCase());
                                    }
                                });//save
                                
                            }else{
                                w2alert('Seleccione un producto y especifique la cantidad de Entrada');
                            }    //if validate
                            }//onClick
                    },
                    reset: {
                        caption: 'Cancelar',
                        onClick: function () {
                            window.close();
                            $().w2destroy('form');
                        }
                    }
                },// Actions
            });// Form

            });
            fitToContent('form');
        });
        </script>
    </body>
</html>
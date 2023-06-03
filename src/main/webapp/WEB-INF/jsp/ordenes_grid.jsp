<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="recordsUrl" value="/orden/cmd"/>
<c:url var="productoOrdenUrl" value="/productoOrden/cmd"/>
<c:url var="clientesUrl" value="/cliente/clientes"/>
<c:url var="maxNumOrdenUrl" value="/orden/max_num_orden"/>
<c:url var="productoUrl" value="/producto/productos"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Productos</title>
        <link rel="icon" href="favicon.ico">
        <link rel="stylesheet" type="text/css" href="static/libs/w2ui/1.5/w2ui.min.css" />
        <link rel="stylesheet" type="text/css" href="static/css/styles.css" />
        <link rel="stylesheet" href="static/css/font-awesome.min.css"> 
        <script type="text/javascript" src="static/libs/jquery/js/jquery.min.js"></script>
        <script type="text/javascript" src="static/libs/w2ui/1.5/w2ui.js"></script>
        <script type="text/javascript" src="static/js/main.js"></script>
        <script type="text/javascript" src="static/libs/moment-with-langs.min.js"></script>
    </head>
    <body>
    	<div id="layout" class="grid_big"></div>
    	<div id="grid" class="grid_medium"></div>
    	
        <script type="text/javascript">
        
        var pstyle = 'border: 1px solid #dfdfdf; padding: 5px;';
        var registroActivo;
        var isEdit = false;                
        var abrirFormulario;
        var registros = new Array();
        var seleccionados = new Array();
        var recordsActivos;
        var recordsTodos;
        var todos = '1';
        var recordsDetalle = new Array();
        var ordenSeleccionada = {};
        var productoOrdenSeleccionada = {};
        var numeroOrden = 0;
        
        $(function () {
        	
        var PEDIDOS = '<div id="grid" class="grid_resize"></div>';
        var DETALLE = '<div id="grid_det" class="grid_resize"></div>';
        	
        $('#layout').w2layout({
            name: 'layout',
            panels: [
                {
                    type: 'bottom',
                    size: '50%',
                    style: pstyle + 'border-top: 0px;',
                    title: '<font size="4">Detalle</font>',
                    content: DETALLE
                },
                {
                    type: 'main',
                    size: '50%',
                    style: pstyle + 'border-top: 0px;',
                    title: '<font size="4">Pedidos</font>',
                    content: PEDIDOS
                }
            ]
        });
        fitToContent('layout');
        
        $('#grid').w2grid({
            name: 'grid',
            header: 'Pedidos',
            recid: 'id',
            url: '${recordsUrl}',
            multiSearch: true,
            show: {
                header: false,
                padding: 0,
                lineNumbers: true,
                toolbar: true,
                footer: true,
                toolbarAdd: true,
                toolbarDelete: false,
                toolbarSave: false,
                toolbarEdit: false,
                toolbarColumns: true,
                toolbarSearch: false,
                toolbarInput: false
            },
            searches: [
                {
                    field: 'numeroOrden',
                    caption: 'Número Orden',
                    type: 'text'
                }, {
                    field: 'cveCliente',
                    caption: 'Cve Cliente',
                    type: 'text'
                }, {
                    field: 'fecha',
                    caption: 'Fecha',
                    type: 'date'
                }, {
                    field: 'subTotal',
                    caption: 'Subtotal',
                    type: 'float'
                }, {
                    field: 'iva',
                    caption: 'iva',
                    size: '11%',
                    sortable: true,
                    resizable: true,
                    type: 'float'
                }, {
                    field: 'total',
                    caption: 'Total',
                    type: 'float',
                }, {
                    field: 'estatus',
                    caption: 'Estatus',
                    type: 'text'
                }
            ],
            
            
            toolbar: {
                items: [
                    {type: 'break'},
                    {type: 'button',
                     id: 'enviar_pedido',
                     disabled: true,
                     caption: 'Enviar Pedido',
                     hint : 'Enviar el pedido seleccionado',
                     icon: 'fa fa-bus'}                      
                ],
                onClick: function (target, data) {
                    if (target === 'enviar_pedido') {
                        mensaje = '¿Estas seguro que desea proceder con el envio del Pedido?';

                        w2confirm({msg: mensaje,  
                            yes_text: 'Si',
                            yes_callBack : function (){
                            	
                                w2ui.grid.lock('Realizando envío...',true);
                                 
								var numeroOrden = ordenSeleccionada.numeroOrden;
								
								console.log('Records Detalle');
								console.log(recordsDetalle);
								
								for(var i = 0; i < recordsDetalle.length; i++){
									var item = recordsDetalle[i];
									if(item.cantidad > item.existencia){
										w2alert('El pedido actual no puede ser surtido por falta de existencia');
										w2ui.grid.unlock();
										return;
									}
								}

				                $.post('/orden/enviarOrden/' + numeroOrden, {
			                        "request": JSON.stringify({"cmd": "save", "selected": ordenSeleccionada, "limit": 100, "offset": 0})
			                        },
			                        function (data) {
			                        	w2ui.grid.unlock();
			                        	refrescaMain(ordenSeleccionada);
			                        	w2alert('Envío realizado de manera exitosa');
			                        }
			                    );
                                
                            },
                            no_text: 'No',
                            height: 230}
                        );
                    }                         
                }
            },
            columns: [
                {
                    field: 'numeroOrden',
                    caption: 'Número Orden',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    type: 'int'
                }, {
                    field: 'cveCliente',
                    caption: 'Cve Cliente',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    type: 'text'
                }, {
                    field: 'fecha',
                    caption: 'Fecha',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    type: 'date',
                    render: function(record){
                        return moment(record.fecha).format('YYYY-MM-DD');
                    }
                }, {
                    field: 'subTotal',
                    caption: 'Subtotal',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    attr: 'align=right',
                    type: 'float',
                    render: 'money'
                }, {
                    field: 'iva',
                    caption: 'Iva',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    attr: 'align=right',
                    type: 'float',
                    render: 'money'
                }, {
                    field: 'total',
                    caption: 'Total',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    attr: 'align=right',
                    type: 'float'
                }, {
                    field: 'estatus',
                    caption: 'Estatus',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    attr: 'align=left',
                    type: 'text'
                }
            ],
            onAdd: function (event) {
                isEdit = false;
                abrirFormulario();
            },
            onEdit: function (event) {
                isEdit = true;
                registroActivo = w2ui.grid.get(event.recid);
                console.log(registroActivo);
                editarRegistro(registroActivo.id);
            },
            onUnselect: function(event) {
            	event.onComplete = function (event) {
            		this.toolbar.disable('enviar_pedido');
            	}
            },
            onSelect: function(event) {
            	event.onComplete = function (event) {
            		var id = w2ui.grid.getSelection();
            		ordenSeleccionada = w2ui.grid.get(id[0]);
            		
                    if (id.length === 1 && ordenSeleccionada.estatus !== 'Enviado') {
                    	this.toolbar.enable('enviar_pedido');
                    } else {
                    	this.toolbar.disable('enviar_pedido');
                    }
            		
            		console.log(ordenSeleccionada);
            		var numeroOrden = ordenSeleccionada.numeroOrden;
            		console.log('Numero Orden : ' + numeroOrden);
                    $.get('/productoOrden/productoOrdenesByOrden/' + numeroOrden, {}, 
                    function (data) {
                        if (data.estatus && data.estatus === 'error') {

                        } else {
                        	console.log('Producto Ordenes DTO');
                            console.log(data);
                            recordsDetalle = data;
                            var cantidadTotal = 0;
                            var subtotalTotal = 0;
                            var ivaTotal = 0;
                            var totalTotal = 0;
                            for(var i = 0; i < data.length; i++){
                            	cantidadTotal = cantidadTotal + data[i].cantidad;
                            	subtotalTotal = subtotalTotal + data[i].subTotal;
                            	ivaTotal = ivaTotal + data[i].iva;
                            	totalTotal = totalTotal + data[i].total;
                            }
                            console.log('11');
                            var total = {
                                    w2ui: {summary: true},
                                    recid: 'S-2',
                                    producto: 'Total',
                                    cantidad: cantidadTotal,
                                    subTotal: subtotalTotal,
                                    iva: ivaTotal,
                                    total: totalTotal
                                };
                            console.log(total);
                            w2ui['grid_det'].clear();
                            w2ui['grid_det'].records = recordsDetalle;
                            w2ui['grid_det'].add(total);
                            w2ui['grid_det'].refresh();
                        }
                    });
            	}
            },
            onDelete: function (event) {
                selected = new Array();
                var id = w2ui.grid.getSelection();
                console.log('Seleccionados: ' + id);
                selected.push(id[0]);
            	event.onComplete = function (event) {
            		event.preventDefault();
                    $.post('/orden/cmd', {
                        "request": JSON.stringify({"cmd": "delete", "selected": selected, "limit": 100, "offset": 0})
                        },
                        function (data) {w2alert( "La Orden fue removido correctamente." );}
                    );
            	}
            },
        });
    	//w2ui.grid.load('${recordsUrl}');
        
    	//fitToContent('grid');
        
        editarRegistro = function (id) {
            seleccionados.splice(0, seleccionados.length);
            isEdit = true;
            $.ajax({
                url: '/orden/' + id,
                type: 'get',
                success: function(data){
                    w2utils.unlock($('#grid'));
                    if(data.status === 'error'){
                        w2alert(data.message);
                    }else{
                        registroActivo = data;
                        abrirFormulario();
                    }
                }
            });
        };
        
        w2ui.grid.toolbar.disable('w2ui-add');
        
            abrirFormulario = function () {
                $.when(
                        $.getJSON('${clientesUrl}'),
                        $.getJSON('${maxNumOrdenUrl}')
                ).then(function (clientesResponse, maxNumOrdenResponse) {
                        var clientes = clientesResponse[0].map(function(item,index){
                            return {
                                id: item.id, 
                                text: item.cveCliente + ' - ' +item.nombre,
                                cveCliente: item.cveCliente
                            };
                        });
                        
                        if(maxNumOrdenResponse[0]){
                        	numeroOrden = maxNumOrdenResponse[0];
                        }else{
                        	numeroOrden = 0;
                        }

                        console.log(clientes);
                        console.log(maxNumOrdenResponse);
                        console.log('numeroOrden');
                        console.log(numeroOrden);
                        
                        var clienteSelecionado = {};
                        
                $().w2destroy('formContent');
                $().w2form({
                    name: 'formContent',
                    url: '${recordsUrl}',
                fields: [
                    {
                        name: 'numeroOrden',
                        type: 'int',
                        required: true,
                        disabled: true,
                        html: {
                            attr: 'size="40" maxlength="50"',
                            caption: 'Número Orden'
                        }
                    }, {
                        name: 'fecha',
                        type: 'date',
                        required: true,
                        options:{
                            espacios: true
                        },
                        html: {
                            attr: 'size="40" maxlength="255"',
                            caption: 'Fecha'
                        },
                        render: function(record){
                            return '<div>' + moment(record.fecha).format('YYYY-MM-DD') +'</div>';
                        }
                    }, {
                        name: 'cliente',
                        type: 'list',
                        options:{
                            items: clientes
                        },
                        match: 'contain',
                        markSearch: true,
                        required: true,
                        html: {
                            attr: 'size="40" maxlength="50"',
                            caption: 'Cliente'
                        }
                    }
                ],
                record: {
                    id: (isEdit ? registroActivo.id : null),
                    numeroOrden: (isEdit ? registroActivo.numeroOrden : numeroOrden + 1)
                },
                onChange: function(event){
                    event.onComplete = function(){
                        if(event.target === "cliente"){
                            if($('#cliente').data('selected') !== null && typeof $('#cliente').data('selected').id !== "undefined"){
                            	clienteSelecionado = $('#cliente').data('selected');
                            }
                        }
                    }
                },
                actions: {
                    save: {
                        caption: (isEdit ?
                                'Guardar' :
                                'Agregar'),
                        class: 'w2ui-btn-blue',
                        onClick: function () {
                            
                        	console.log(this.record);
                        	
                            if (this.validate(true).length === 0) {
                                
                            	this.record.cveCliente = this.record.cliente.cveCliente;
                            	this.record.subTotal = 0;
                            	this.record.iva = 0;
                            	this.record.total = 0;
                            	this.record.estatus = 'Por Enviar';
                            		
                                console.log(this.record);
                                this.save(function (data) {
                                	console.log(data);
                                    w2popup.close();
                                    if(data.status === 'success'){
                                        if(data.records[0] === null){
                                            w2alert('No es posible ingresar dos descripciones para un mismo evento');
                                        }else{
                                            if (isEdit) {
                                                w2alert('Orden modificada correctamente');
                                                w2ui.grid.set(
                                                    data.records[0].id,
                                                    data.records[0]);
                                                    w2ui.grid.reload();
                                            } else {
                                                w2alert('Orden ingresada correctamente');
                                                w2ui.grid.add(data.records[0]);
                                                w2ui.grid.total++;
                                                w2ui.grid.refresh();
                                            }
                                        }
                                    }else{
                                        w2alert('No fue posible ingresar la Orden. ' + data.message.toUpperCase());
                                    }
                                });//save
                                
                            }else{
                                w2alert('Todos los campos tienen que estar llenos. ¡Favor de verificar!');
                            }    //if validate
                            }//onClick
                    },
                    reset: {
                        caption: 'Cancelar',
                        onClick: function () {
                            w2popup.close();
                            $().w2destroy('formContent');
                        }
                    }
                },// Actions
//                 onChange: function (event) {
                
//                     if(event.target === 'descripcion'){
//                         var texto = event.value_new;
//                         if(texto.length > 0){
//                             $( "input[name='descripcion']" ).val(forzaAlfanumerico(texto));
//                         }
//                     }
//                 }
            });// Form
            
            
            $().w2popup('open', { 
                modal: true,
                title: (isEdit ?
                        '<spring:message code="sipsejec.edit.caption"/>' :
                        '<spring:message code="sipsejec.new.caption"/>'),
                body: '<div id="nuevoFormulario" style="width: 468px; height: 266px;"></div>',
                style: 'padding: 0px 0px 0px 0px',
                width: 470,
                height: 300,
                onOpen: function (event) {
                    event.onComplete = function () {
                        $('#w2ui-popup #nuevoFormulario').w2render('formContent');
                    };
                }
            }); // w2popup
            }); // When
        };// abrirFormulario
        
        w2ui.grid.toolbar.enable('w2ui-add');
        
        
        
// GRID de Detalle ********************************************
// ************************************************************
        
        
        $('#grid_det').w2grid({
            name: 'grid_det',
            header: 'Detalle',
            recid: 'id', 
            records: recordsDetalle,
            multiSearch: true,
            show: {
                header: false,
                padding: 0,
                lineNumbers: true,
                toolbar: true,
                footer: true,
                toolbarAdd: true,
                toolbarDelete: true,
                toolbarSave: false,
                toolbarEdit: true,
                toolbarColumns: true,
                toolbarSearch: false,
                toolbarInput: false
            },
            searches: [
                {
                    field: 'producto',
                    caption: 'Producto',
                    type: 'text'
                }, {
                    field: 'cantidad',
                    caption: 'Cantidad',
                    type: 'int'
                }, {
                    field: 'precioUnitario',
                    caption: 'Precio Unidad',
                    type: 'float'
                }, {
                    field: 'subTotal',
                    caption: 'Subtotal',
                    type: 'float'
                }, {
                    field: 'iva',
                    caption: 'iva',
                    type: 'float'
                }, {
                    field: 'total',
                    caption: 'Total',
                    type: 'float'
                }
            ],
            
            
            //toolbar: {
            //    items: [
            //        {type: 'break'},
            //        {type: 'button',
            //         id: 'descargar-csv',
            //         caption: 'XXXX',
            //         hint : 'Un hint',
            //         icon: 'fa fa-download'}                      
            //    ],
            //    onClick: function (target, data) {
            //        if (target === 'descargar-csv') {
            //            alert('Accion del boton');
            //        }                         
            //    }
            //},
            
            columns: [
                {
                    field: 'producto',
                    caption: 'Producto',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    type: 'int'
                }, {
                    field: 'cantidad',
                    caption: 'Cantidad',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    type: 'int'
                }, {
                    field: 'precioUnitario',
                    caption: 'Precio Unitario',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    attr: 'align=right',
                    type: 'float',
                    render: 'money'
                }, {
                    field: 'subTotal',
                    caption: 'Subtotal',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    attr: 'align=right',
                    type: 'float',
                    render: 'money'
                }, {
                    field: 'iva',
                    caption: 'Iva',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    attr: 'align=right',
                    type: 'float',
                    render: 'money'
                }, {
                    field: 'total',
                    caption: 'Total',
                    size: '20%',
                    sortable: false,
                    resizable: true,
                    attr: 'align=right',
                    type: 'float',
                    render: 'money'
                }
            ],
            onAdd: function (event) {
            	console.log('1');
                isEdit = false;
                abrirFormularioDet();
            },
            onEdit: function (event) {
                isEdit = true;
                productoOrdenSeleccionada = w2ui.grid_det.get(event.recid);
                abrirFormularioDet();
            },
            onDelete: function (event) {
                selected = new Array();
                var id = w2ui.grid_det.getSelection();
                console.log('Seleccionados: ' + id);
                selected.push(id[0]);
            	event.onComplete = function (event) {
            		event.preventDefault();
                    $.post('${productoOrdenUrl}', {
                        "request": JSON.stringify({"cmd": "delete", "selected": selected, "limit": 100, "offset": 0})
                        },
                        function (data) {
                        	w2alert( "El Producto fue removido correctamente." );
                        	refrescaMain(ordenSeleccionada);
                        	refrescaDetalle(ordenSeleccionada);
                        }
                    );
            	}
            },
        });
    	//w2ui.grid_det.load('/productoOrden/productoOrdenesByOrden/10');
        
    	//fitToContent('grid');
        
        editarRegistroDet = function (id) {
            seleccionados.splice(0, seleccionados.length);
            isEdit = true;
            $.ajax({
                url: '/producto/' + id,
                type: 'get',
                success: function(data){
                    w2utils.unlock($('#grid'));
                    if(data.status === 'error'){
                        w2alert(data.message);
                    }else{
                        registroActivo = data;
                        abrirFormularioDet();
                    }
                }
            });
        };
        
        w2ui.grid_det.toolbar.disable('w2ui-add');
        
        
        
            abrirFormularioDet = function () {
            	console.log('2');
            	var conProducto = false;
            	var productoSeleccionado = {};
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
                        
                        //recordsDetalle
                        
                        console.log(productos);
                        
                        var productoSeleccionadoArr = jQuery.grep(productos, function( item ) {
                        	  return item.codProducto === productoOrdenSeleccionada.codProducto;
                        	});
                        
                        productoSeleccionado = productoSeleccionadoArr[0];
                        
                $().w2destroy('formContentDet');
                $().w2form({
                    name: 'formContentDet',
                    url: '${productoOrdenUrl}',
                fields: [
                    {
                        name: 'productoSel',
                        type: 'list',
                        options:{
                            items: productos
                        },

                        match: 'contain',
                        markSearch: true,
                        required: !isEdit,
                        hidden: isEdit,
                        html: {
                            attr: 'size="40" maxlength="50"',
                            caption: 'Producto'
                        }
                    }, {
                        name: 'productoSelTxt',
                        type: 'text',
                        required: false,
                        disabled: true,
                        hidden: !isEdit,
                        options:{
                            espacios: true
                        },
                        html: {
                            attr: 'size="40" maxlength="255"',
                            caption: 'Producto'
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
                        name: 'precioVenta',
                        type: 'money',
                        required: false,
                        disabled: true,
                        options:{
                            espacios: true
                        },
                        html: {
                            attr: 'size="40" maxlength="255"',
                            caption: 'Precio Venta'
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
                        name: 'cantidad',
                        type: 'int',
                        required: true,
                        options:{
                            espacios: true
                        },
                        html: {
                            attr: 'size="40" maxlength="255"',
                            caption: 'Cantidad Solicitada'
                        }
                    }
                ],
                record: {
                    id: (isEdit ? productoOrdenSeleccionada.id : null),
                    codProducto: (isEdit ? productoSeleccionado.codProducto : null),
                    productoSelTxt: (isEdit ? productoOrdenSeleccionada.producto : null),
                    existencia: (isEdit ? productoSeleccionado.existencia : 0),
                    precioVenta: (isEdit ? productoSeleccionado.precioVenta : 0.0),
                    cveProveedor: (isEdit ? productoSeleccionado.cveProveedor : null),
                    cantidad: (isEdit ? productoOrdenSeleccionada.cantidad : 0),
                },
                onChange: function(event){
                	console.log('4');
                	
                    event.onComplete = function(){
                    	console.log('5');
                        if(event.target === "productoSel"){
                            if($('#productoSel').data('selected') !== null && typeof $('#productoSel').data('selected').id !== "undefined"){
                            	productoSeleccionado = $('#productoSel').data('selected');
                            	conProducto = true;
                            	console.log(productoSeleccionado);
                            }else{
                            	conProducto = false;
                            }
                            w2ui['formContentDet'].record['id'] = productoSeleccionado.id;
                            w2ui['formContentDet'].record['codProducto'] = productoSeleccionado.codProducto;
                            w2ui['formContentDet'].record['nombre'] = productoSeleccionado.nombre;
                            w2ui['formContentDet'].record['existencia'] = productoSeleccionado.existencia;
                            w2ui['formContentDet'].record['precioCompra'] = productoSeleccionado.precioCompra;
                            w2ui['formContentDet'].record['precioVenta'] = productoSeleccionado.precioVenta;
                            w2ui['formContentDet'].record['cveProveedor'] = productoSeleccionado.cveProveedor;
                            w2ui['formContentDet'].record['cantidad'] = 0;
                            w2ui['formContentDet'].set('entrada', {disabled: false});
                            w2ui['formContentDet'].refresh();
                        }
                    }
                },
                actions: {
                    save: {
                        caption: (isEdit ?
                                'Guardar' :
                                'Agregar'),
                        class: 'w2ui-btn-blue',
                        onClick: function () {
                            
                        	console.log('Antes de Guardar');
                        	console.log(this.record);
                        	console.log(ordenSeleccionada);
                        	
                        	this.record.numeroOrden = ordenSeleccionada.numeroOrden;
                        	
                        	if(!isEdit){
                        		this.record.id = null;
                        	}
                        	
                            if (this.validate(true).length === 0) {
                            	
                                console.log(this.record);
                                this.save(function (data) {
                                	console.log(data);
                                    w2popup.close();
                                    if(data.status === 'success'){
                                        if(data.records[0] === null){
                                            w2alert('No es posible ingresar dos descripciones para un mismo evento');
                                        }else{
                                            if (isEdit) {
                                                w2alert('Producto modificado correctamente');
                                                refrescaDetalle(ordenSeleccionada);
                                                w2ui.grid.reload();
                                                
                                                //w2ui.grid_det.set(
                                                //    data.records[0].id,
                                                //    data.records[0]);
                                                //    w2ui.grid_det.reload();
                                            } else {
                                                w2alert('Producto ingresado correctamente');
                                                refrescaDetalle(ordenSeleccionada);
                                                w2ui.grid.reload();
                                            }
                                        }
                                    }else{
                                        w2alert('No fue posible ingresar el producto. ' + data.message.toUpperCase());
                                    }
                                });//save
                                
                            }else{
                                w2alert('Todos los campos tienen que estar llenos. ¡Favor de verificar!');
                            }    //if validate
                            }//onClick
                    },
                    reset: {
                        caption: 'Cancelar',
                        onClick: function () {
                            w2popup.close();
                            $().w2destroy('formContentDet');
                        }
                    }
                },// Actions
            });// Form
            });//When
            
            $().w2popup('open', { 
                modal: true,
                title: (isEdit ?
                        '<spring:message code="sipsejec.edit.caption"/>' :
                        '<spring:message code="sipsejec.new.caption"/>'),
                body: '<div id="nuevoFormulario1" style="width: 468px; height: 266px;"></div>',
                style: 'padding: 0px 0px 0px 0px',
                width: 470,
                height: 300,
                onOpen: function (event) {
                    event.onComplete = function () {
                        $('#w2ui-popup #nuevoFormulario1').w2render('formContentDet');
                    };
                }
            }); // w2popup
        };// abrirFormulario
        
        w2ui.grid_det.toolbar.enable('w2ui-add');
        
        refrescaMain = function (ordenSeleccionada) {
        	//w2ui['grid'].clear();
        	//w2ui.grid.load('${recordsUrl}');
        	w2ui.grid.reload();
        	//w2ui['grid'].refresh();
        };
        
        refrescaDetalle = function (ordenSeleccionada) {
        	if(ordenSeleccionada){
	       		var numeroOrden = ordenSeleccionada.numeroOrden;
	       		console.log('Numero Orden : ' + numeroOrden);
	               $.get('/productoOrden/productoOrdenesByOrden/' + numeroOrden, {}, 
	               function (data) {
	                   if (data.estatus && data.estatus === 'error') {
	
	                   } else {
	                   	console.log('Producto Ordenes DTO');
	                       console.log(data);
	                       recordsDetalle = data;
	                       var cantidadTotal = 0;
	                       var subtotalTotal = 0;
	                       var ivaTotal = 0;
	                       var totalTotal = 0;
	                       for(var i = 0; i < data.length; i++){
	                       	cantidadTotal = cantidadTotal + data[i].cantidad;
	                       	subtotalTotal = subtotalTotal + data[i].subTotal;
	                       	ivaTotal = ivaTotal + data[i].iva;
	                       	totalTotal = totalTotal + data[i].total;
	                       }
	                       console.log('11');
	                       var total = {
	                               w2ui: {summary: true},
	                               recid: 'S-2',
	                               producto: 'Total',
	                               cantidad: cantidadTotal,
	                               subTotal: subtotalTotal,
	                               iva: ivaTotal,
	                               total: totalTotal
	                           };
	                       console.log(total);
	                       w2ui['grid_det'].clear();
	                       w2ui['grid_det'].records = recordsDetalle;
	                       w2ui['grid_det'].add(total);
	                       w2ui['grid_det'].refresh();
	                   }
	               });
        	}else{
        		w2ui['grid_det'].clear();
        		w2ui['grid_det'].refresh();
        	}
       	};
        
        
        });
        


        </script>
    </body>
</html>
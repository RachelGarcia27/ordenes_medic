<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="recordsUrl" value="/cliente/cmd"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Catálogo de Clientes</title>
        <link rel="icon" href="favicon.ico">
        <link rel="stylesheet" type="text/css" href="/static/libs/w2ui/1.5/w2ui.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/styles.css" />
        <link rel="stylesheet" href="/static/css/font-awesome.min.css"> 
        <script type="text/javascript" src="/static/libs/jquery/js/jquery.min.js"></script>
        <script type="text/javascript" src="/static/libs/w2ui/1.5/w2ui.js"></script>
        <script type="text/javascript" src="/static/js/main.js"></script>
    </head>
    <body>
    	<div id="grid" class="grid_big"></div>
        <script type="text/javascript">
        var registroActivo;
        var isEdit = false;                
        var abrirFormulario;
        var registros = new Array();
        var seleccionados = new Array();
        var recordsActivos;
        var recordsTodos;
        var todos = '1';
        
        $(function () {
            $('#grid').w2grid({
                name: 'grid',
                header: 'Catálogo de Clientes',
                recid: 'id', 
                multiSearch: true,
                show: {
                    header: true,
                    padding: 0,
                    lineNumbers: true,
                    toolbar: true,
                    footer: true,
                    toolbarAdd: true,
                    toolbarDelete: true,
                    toolbarSave: false,
                    toolbarEdit: true,
                    toolbarColumns: true
                },
                searches: [
                    {
                        field: 'cveCliente',
                        caption: 'Cve Cliente',
                        type: 'text'
                    }, {
                        field: 'apellidoPaterno',
                        caption: 'Apellido Paterno',
                        type: 'text'
                    }, {
                        field: 'apellidoMaterno',
                        caption: 'Apellido Materno',
                        type: 'text'
                    }, {
                        field: 'nombre',
                        caption: 'Nombre',
                        size: '11%',
                        sortable: true,
                        resizable: true,
                        type: 'text'
                    }, {
                        field: 'telefono',
                        caption: 'Teléfono',
                        type: 'text'
                    }, {
                        field: 'direccion',
                        caption: 'Dirección',
                        type: 'text'
                    }
                ],
                /*
                toolbar: {
                    items: [
                        {type: 'break'},
                        {type: 'button',
                         id: 'descargar-csv',
                         caption: 'XXXX',
                         hint : 'Un hint',
                         icon: 'fa fa-download'}                      
                    ],
                    onClick: function (target, data) {
                        if (target === 'descargar-csv') {
                            alert('Accion del boton');
                        }                         
                    }
                },
                */
                columns: [
                    {
                        field: 'cveCliente',
                        caption: 'Cve Cliente',
                        size: '10%',
                        sortable: true,
                        resizable: true,
                        type: 'text'
                    }, {
                        field: 'apellidoPaterno',
                        caption: 'Apellido Paterno',
                        size: '10%',
                        sortable: true,
                        resizable: true,
                        type: 'text'
                    }, {
                        field: 'apellidoMaterno',
                        caption: 'Apellido Materno',
                        size: '10%',
                        sortable: true,
                        resizable: true,
                        attr: 'align=left',
                        type: 'int'
                    }, {
                        field: 'nombre',
                        caption: 'Nombre',
                        size: '16%',
                        sortable: true,
                        resizable: true,
                        attr: 'align=left',
                        type: 'float'
                    }, {
                        field: 'telefono',
                        caption: 'Teléfono',
                        size: '8%',
                        sortable: true,
                        resizable: true,
                        attr: 'align=left',
                        type: 'float'
                    }, {
                        field: 'direccion',
                        caption: 'Direccion',
                        size: '30%',
                        sortable: true,
                        resizable: true,
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
                onDelete: function (event) {
                    selected = new Array();
                    var id = w2ui.grid.getSelection();
                    console.log('Seleccionados: ' + id);
                    selected.push(id[0]);
                	event.onComplete = function (event) {
                		event.preventDefault();
                        $.post('${recordsUrl}', {
                            "request": JSON.stringify({"cmd": "delete", "selected": selected, "limit": 100, "offset": 0})
	                        },
	                        function (data) {w2alert( "El Cliente fue removido correctamente." );}
                        );
                	}
                },
            });
        	w2ui.grid.load('${recordsUrl}');
            fitToContent('grid');
            
            editarRegistro = function (id) {
                seleccionados.splice(0, seleccionados.length);
                isEdit = true;
                $.ajax({
                    url: '/cliente/' + id,
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
                    $().w2destroy('formContent');
                    $().w2form({
                        name: 'formContent',
                        url: '${recordsUrl}',
                    fields: [
                        {
                            name: 'cveCliente',
                            type: 'text',
                            required: true,
                            disabled: isEdit,
                            html: {
                                attr: 'size="40" maxlength="50"',
                                caption: 'Cve Cliente'
                            }
                        }, {
                            name: 'apellidoPaterno',
                            type: 'text',
                            required: true,
                            options:{
                                espacios: true
                            },
                            html: {
                                attr: 'size="40" maxlength="255"',
                                caption: 'Apellido Paterno'
                            }
                        }, {
                            name: 'apellidoMaterno',
                            type: 'text',
                            required: true,
                            options:{
                                espacios: true
                            },
                            html: {
                                attr: 'size="40" maxlength="255"',
                                caption: 'Apellido Materno'
                            }
                        }, {
                            name: 'nombre',
                            type: 'text',
                            required: true,
                            options:{
                                espacios: true
                            },
                            html: {
                                attr: 'size="40" maxlength="255"',
                                caption: 'Nombre'
                            }
                        }, {
                            name: 'telefono',
                            type: 'text',
                            required: true,
                            options:{
                                espacios: true
                            },
                            html: {
                                attr: 'size="40" maxlength="255"',
                                caption: 'Teléfono'
                            }
                        }, {
                            name: 'direccion',
                            type: 'text',
                            required: true,
                            options:{
                                espacios: true
                            },
                            html: {
                                attr: 'size="40" maxlength="255"',
                                caption: 'Dirección'
                            }
                        }
                    ],
                    record: {
                        id: (isEdit ? registroActivo.id : null),
                        cveCliente: (isEdit ? registroActivo.cveCliente : null),
                        apellidoPaterno: (isEdit ? registroActivo.apellidoPaterno : null),
                        apellidoMaterno: (isEdit ? registroActivo.apellidoMaterno : null),
                        nombre: (isEdit ? registroActivo.nombre : null),
                        telefono: (isEdit ? registroActivo.telefono : null),
                        direccion: (isEdit ? registroActivo.direccion : null)
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
                                    
                                    console.log(this.record);
                                    this.save(function (data) {
                                    	console.log(data);
                                        w2popup.close();
                                        if(data.status === 'success'){
                                            if(data.records[0] === null){
                                                w2alert('No es posible ingresar dos descripciones para un mismo evento');
                                            }else{
                                                if (isEdit) {
                                                    w2alert('Cliente modificado correctamente');
                                                    w2ui.grid.set(
                                                        data.records[0].id,
                                                        data.records[0]);
                                                        w2ui.grid.reload();
                                                } else {
                                                    w2alert('Cliente ingresado correctamente');
                                                    w2ui.grid.add(data.records[0]);
                                                    w2ui.grid.total++;
                                                    w2ui.grid.refresh();
                                                }
                                            }
                                        }else{
                                            w2alert('No fue posible ingresar el Cliente. ' + data.message.toUpperCase());
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
                    onChange: function (event) {
                    
                        if(event.target === 'descripcion'){
                            var texto = event.value_new;
                            if(texto.length > 0){
                                $( "input[name='descripcion']" ).val(forzaAlfanumerico(texto));
                            }
                        }
                    }
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
            };// abrirFormulario
            
            w2ui.grid.toolbar.enable('w2ui-add');

        });
        </script>
    </body>
</html>
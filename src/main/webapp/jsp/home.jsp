<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="recordsUrl" value="/orden/cmd"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
        <link rel="icon" href="favicon.ico">
        <link rel="stylesheet" type="text/css" href="/static/libs/w2ui/1.5/w2ui.min.css" />
        <link rel="stylesheet" type="text/css" href="/static/css/styles.css" />
        <link rel="stylesheet" href="/static/css/font-awesome.min.css"> 
        <script type="text/javascript" src="/static/libs/jquery/js/jquery.min.js"></script>
        <script type="text/javascript" src="/static/libs/w2ui/1.5/w2ui.js"></script>
        <script type="text/javascript" src="/static/js/main.js"></script>
    </head>
<body>
    <div id="layout" class="grid_resize"></div>
</body>
<script>
$(function () {
    salir = function () {
        window.location.assign('logout');
    };
    
    var HTML_CONTENT = '<img src="static/images/FondoSenda.jpg" style="background-size: 100%;"">';
    
    var HTML_CONTENT_2 = '<div class="container" align="center"> '
            + '    <div class="row top-buffer" style="width:40%; background-image: url("/static/images/mainImage.jpeg");"> '
            + '    <img src="/static/images/mainImage.jpg" width="150%" style="vertical-align:20%;">'
            + '         <div style="margin-top: 50px; "> '
            + '              <a href="f00" target="_blank" class="btn btn-primary btn-huge btn-lg btn-block"><font size="10">Inicio</font> '
            + '                 <span class="glyphicon glyphicon-log-in" style="margin-left:30px; font-size:150%;" aria-hidden="true"></span></a> '
            + '         </div> '
            + '    </div> '
            + '</div>';
    var sidebar = {
        name: 'sidebar',
        topHTML: '<div style="background-color: #eee; padding: 10px 5px; border-bottom: 1px solid silver">Menú</div>',
        bottomHTML: '',

        nodes: [
            {
                id: 'redLogistica', text: 'MEDICAMENTOS', group: true, expanded: false, nodes: [
                    {id: 'stock', text: 'STOCK MEDICAMENTOS', img: 'fa fa-external-link'},
                    {id: 'entradaProd', text: 'ENTRADA MEDICAMENTO', img: 'fa fa-external-link'}
                ]
            }, {
                id: 'unidades', text: 'PEDIDOS', group: true, expanded: false, nodes: [
                    {id: 'orden', text: 'LISTADO', img: 'fa fa-external-link'},
                    {id: 'ordenx', text: 'REGISTRO DE PEDIDOS', img: 'fa fa-external-link'}
                ]
            }, {
                id: 'parametros', text: 'CLIENTES', group: true, expanded: false, nodes: [
                    {id: 'cliente', text: 'LISTADO', img: 'fa fa-external-link'}
                ]
            }
        ],
        onClick: function (event) {
            switch (event.target) {
                case 'orden':
                    abrir('/ordenes');
                    break;
                case 'stock':
                    abrir('/productos');
                    break;
                case 'entradaProd':
                    abrir('/entrada_productos');
                    break;
                case 'cliente':
                    abrir('/clientes');
                    break;
                case 'zb05':
                    abrir('zb05');
                    break;
                case 'zb06':
                    abrir('zb06');
                    break;
                case 'zb07':
                    abrir('zb07');
                    break;
                case 'zb08':
                    abrir('zb08');
                    break;
                case 'zc01':
                    abrir('zc01');
                    break;
                case 'zc02':
                    abrir('zc02');
                    break;
                case 'zc03':
                    abrir('zc03');
                    break;
                case 'zc04':
                    abrir('zc04');
                    break;
                case 'zc06':
                    abrir('zc06');
                    break;
                case 'zd01':
                    abrir('zd01');
                    break;
                case 'zd02':
                    abrir('zd02');
                    break;
                case 'zd04':
                    abrir('zd04');
                    break;
                case 'zd05':
                    abrir('zd05');
                    break;
                case 'zd06':
                    abrir('zd06');
                    break;    
                case 'zd07':
                    abrir('zd07');
                    break;
                case 'ze01':
                    abrir('ze01');
                    break;
                case 'ze02':
                    abrir('ze02');
                    break;
                case 'zf01':
                    abrir('zf01');
                    break;
                case 'zc05':
                    abrir('zc05');
                    break;
                case 'algoritmo':
                    console.log(event);
                    $.getJSON('${ObtenerUrl}', function (data) {});
                    break;
                case 'factibilidad':
                    abrir('fac01');
                    break;
                case 'disponibilidad':
                    abrir('disp01');
                    break;
                case 'z04':
                    abrir('z04');
                    break;
            }
        }
    };
    var pstyle = 'border: 1px solid #dfdfdf; padding: 5px;';

    $('#layout').w2layout({
        name: 'layout',
        panels: [
            {
                type: 'left',
                size: '15%',
                style: pstyle + 'border-top: 0px;'
            },
            {
                type: 'main',
                size: '40%',
                style: pstyle + 'border-top: 0px;',
                title: '<font size="4">Sistema de Medicamentos</font>',
                content: HTML_CONTENT
            }
        ]
    });
    w2ui.layout.html('left', $().w2sidebar(sidebar));
});
</script>
</html>
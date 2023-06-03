/* global w2utils,w2popup*/
w2utils.locale('static/libs/w2ui/1.5/locale/es-mx.json');

    var cveEscenario;
    var cveRol;
    var numeroOpcion;
    var cambioColor;
    var sumAcumKms = 0;
    var sumAcumLts = 0;
    var acumKmsArray = [];
    var colorHexArray = [];
    var acumLtsArray = [];
    
    showViewEncadenamientoPorDia = function(mapa, funcion){
        callback = funcion;
        cveEscenario = mapa.cveEscenario;
        cveRol = mapa.cveRol;
        numeroOpcion = mapa.numeroOpcion;

        var inc = 0;
        restUrl = "rest/f07/" + cveEscenario + "/" + cveRol + "/" + numeroOpcion;
        $.getJSON(restUrl,function (response) {
            var data = response.map(function(item,index){
                    return {
                        cveEscenario: item.cveEscenario,
                        cveRol: item.cveRol,
                        cveRolNumeroOpcionStr: item.cveRolNumeroOpcionStr,
                        destino: item.destino,
                        horarioLlegada: item.horarioLlegada,
                        horarioLlegadaStr: item.horarioLlegadaStr,
                        horarioSalida: item.horarioSalida,
                        horarioSalidaStr: item.horarioSalidaStr,
                        id: item.id,
                        kilometros: item.kilometros,
                        litrosCombustible: item.litrosCombustible,
                        numCorrida: item.numCorrida,
                        numViaje: item.numViaje,
                        numeroCamion: item.numeroCamion,
                        numeroOpcion: item.numeroOpcion,
                        numeroServicio: item.numeroServicio,
                        numeroTramoProximo: item.numeroTramoProximo,
                        origen: item.origen,
                        recargarDieselEfectivo: item.recargarDieselEfectivo,
                        acumKmsOculto: item.kilometros,
                        acumLtsOculto: item.litrosCombustible
                    };
                });
            abrirGrid(data);
        });
        abrirGrid = function(data) {
            $().w2destroy('grid');
            $('#grid').w2grid({
                name: 'grid',
                recid: 'id',
//                url: restUrl,
                records: data,
                multiSelect : false,
    //            selectType: 'cell',  
    //            selectionmode: 'multiplecellsextended',
                show: {
                    padding: 0,
                    lineNumbers: true,
                    toolbar: true,
                    footer: true,
                    toolbarColumns: false,
                    toolbarSearch: false,
                    toolbarInput: false
                },
                toolbar: {
                    name: 'toolbar',
                    items: [
                        {type: 'html',  id: 'escenario',
                            html: '<div style="padding: 3px 5px;">'+
                                  ' Esc: '+
                                  '    <input id="inputCveEscenario" size="10" style="padding: 3px; border-radius: 2px; border: 1px solid silver" disabled/>'+
                                  '</div>' 
                        },
                        {type: 'break'},
                        {type: 'button',
                         id: 'descargar-csv',
                         caption: 'CSV',
                         hint : 'Generar CSV',
                         icon: 'fa fa-download'}
                    ],
                    onClick: function (target, info) {
                        if (target === 'descargar-csv') {
                            if (w2ui.grid.records.length > 0) {
                                var data = [["Exportacion a CSV"], ["Tabla de Encadenamiento por dia"]];
                                var titulos = ['Dia Trabajo', 'RolOp', 'Servicio', 'Viaje', 'Corrida', 'Horario Sale', 'Origen', 'Horario Llegada', 'Destino', 'Kms', 'Lts Diesel','Acum Kms','Acum Lts'];
                                data.push(titulos);
                                for (var i=0; i < w2ui.grid.records.length; i++) {
                                    var record = w2ui.grid.records[i];
                                    var row = [];
                                    row.push(record.numeroCamion);
                                    row.push(record.cveRol + ' - ' + record.numeroOpcion);
                                    row.push(record.numeroServicio);
                                    row.push(record.numViaje);
                                    row.push(record.numCorrida);
                                    row.push(formatDate2Time(record.horarioSalida));
                                    row.push(record.origen);
                                    row.push(formatDate2Time(record.horarioLlegada));
                                    row.push(record.destino);
                                    row.push((record.kilometros !== null ? record.kilometros : 0 ));
                                    row.push((record.litrosCombustible !== null ? record.litrosCombustible.formatMoney(2, '.', '') : 0.00 ));
                                    sumAcumKms = sumAcumKms + record.kilometros;
                                    imprimirSumAcumLts = sumAcumKms;
                                    if(record.recargarDieselEfectivo === 1) {
                                        sumAcumKms = 0;
                                    }                                
                                    row.push(record.acumKmsOculto);
                                    if(record.litrosCombustible !== null){
                                        sumAcumLts = sumAcumLts + record.litrosCombustible;
                                    }
                                    imprimirSumAcumLts = sumAcumLts;
                                    if(record.recargarDieselEfectivo === 1) {
                                        sumAcumLts = 0;
                                    }
                                    row.push(record.acumLtsOculto);
                                    data.push(row);
                                }
                            }
                            exportCSV(data, "EncadenamientoPorDiaRolOpcion.csv");                           
                        }
                    }
                },
                searches: [
                    {
                        field: 'numeroCamion',
                        caption: '<spring:message code="sipdr.f00.epd.numeroCamion.caption"/>',
                        type: 'int'
                    },{
                        field: 'rolOp',
                        caption: '<spring:message code="sipdr.f00.epd.rolOp.caption"/>',
                        type: 'text'
                    },{
                        field: 'numeroServicio',
                        caption: '<spring:message code="sipdr.f00.epd.servicio.caption"/>',
                        type: 'text'
                    }, {
                        field: 'numViaje',
                        caption: '<spring:message code="sipdr.f00.epd.numViaje.caption"/>',
                        type: 'int'
                    },{
                        field: 'numCorrida',
                        caption: '<spring:message code="sipdr.f00.epd.numCorrida.caption"/>',
                        type: 'text'
                    },{
                        field: 'horarioSalida',
                        caption: '<spring:message code="sipdr.f00.epd.horarioSalida.caption"/>',
                        type: 'text'
                    },{
                        field: 'origen',
                        caption: '<spring:message code="sipdr.f00.epd.origen.caption"/>',
                        type: 'text'
                    },{
                        field: 'horarioLlegada',
                        caption: '<spring:message code="sipdr.f00.epd.horarioLlegada.caption"/>',
                        type: 'text'
                    },{
                        field: 'destino',
                        caption: '<spring:message code="sipdr.f00.epd.destino.caption"/>',
                        type: 'text'
                    },{
                        field: 'kilometros',
                        caption: '<spring:message code="sipdr.f00.epd.kilometros.caption"/>',
                        type: 'text'
                    },{
                        field: 'litrosCombustible',
                        caption: '<spring:message code="sipdr.f00.epd.litrosCombustible.caption"/>',
                        type: 'text'
                    },{
                        field: 'acumKms',
                        caption: '<spring:message code="sipdr.f00.epd.acumKms.caption"/>',
                        type: 'text'
                    },{
                        field: 'acumLts',
                        caption: '<spring:message code="sipdr.f00.epd.acumLts.caption"/>',
                        type: 'float'
                    }        
                ],  
                columns: [
                    {
                        field: 'numeroCamion',
                        caption: 'Día Trabajo',
                        size: '9%',
                        sortable: true,
                        attr: 'align=center',
                        resizable: true,
                        style: 'background-color: #FFFFCC',
                        render: function (record) {
                            (record.numeroCamion % 2 === 0) ? 
                            (cambioColor = true) :
                            (cambioColor = false);
                            (cambioColor) ? (colorHex = "#9E7961") : (colorHex = "#FFFFCC");
                            inputCveEscenario.value = record.cveEscenario;
                            return '<div style = "background-color: '+ colorHex +'; border: \n\
                            3px solid '+ colorHex +';">' + record.numeroCamion + '</div>';
                        }
                    },{
                        field: 'rolOp',
                        caption: 'RolOp',
                        size: '8%',
                        sortable: true,
                        resizable: true,
                        attr: 'align=center',
                        style: 'background-color: #FFFFCC',
                        render: function (record) {
                            (record.numeroCamion % 2 === 0) ? 
                            (cambioColor = true) :
                            (cambioColor = false);
                            (cambioColor) ? (colorHex = "#9E7961") : (colorHex = "#FFFFCC");
                            return '<div style = "background-color: '+ colorHex +'; border: \n\
                            3px solid '+ colorHex +';">' + record.cveRol + ' - ' + record.numeroOpcion + '</div>';
                        }
                    },{
                        field: 'numeroServicio',
                        caption: 'Servicio',
                        size: '8%',
                        sortable: true,
                        attr: 'align=center',
                        resizable: true,
                        style: 'background-color: #FDF0F0',
                        render: function (record) {
                            (record.numeroCamion % 2 === 0) ? 
                            (cambioColor = true) :
                            (cambioColor = false);
                            (cambioColor) ? (colorHex = "#FCDAC4") : (colorHex = "#FDF0F0");
                            return '<div style = "background-color: '+ colorHex +'; border: \n\
                            3px solid '+ colorHex +';">' + record.numeroServicio + '</div>';
                        }
                    },{
                        field: 'numViaje',
                        caption: 'Viaje',
                        size: '8%',
                        sortable: true,
                        attr: 'align=center',
                        style: 'background-color: #FDF0F0',
                        resizable: true,
                        render: function (record) {
                            (record.numeroCamion % 2 === 0) ? 
                            (cambioColor = true) :
                            (cambioColor = false);
                            (cambioColor) ? (colorHex = "#FCDAC4") : (colorHex = "#FDF0F0");
                            return '<div style = "background-color: '+ colorHex +'; border: \n\
                            3px solid '+ colorHex +';">' + record.numViaje + '</div>';
                        }
                    },{
                        field: 'numCorrida',
                        caption: 'Corrida',
                        size: '8%',
                        sortable: true,
                        attr: 'align=center',
                        style: 'background-color: #FDF0F0',
                        resizable: true,
                        render: function (record) {
                            (record.numeroCamion % 2 === 0) ? 
                            (cambioColor = true) :
                            (cambioColor = false);
                            (cambioColor) ? (colorHex = "#FCDAC4") : (colorHex = "#FDF0F0");
                            return '<div style = "background-color: '+ colorHex +'; border: \n\
                            3px solid '+ colorHex +';">' + record.numCorrida + '</div>';
                        }
                    },{
                        field: 'horarioSalida',
                        caption: 'Horario Sale',
                        size: '100px',
                        sortable: true,
                        resizable: true,
                        attr: 'align=center',
                        style: 'background-color: #FDF0F0',
                        render: function (record) {
                            (record.numeroCamion % 2 === 0) ? 
                            (cambioColor = true) :
                            (cambioColor = false);
                            (cambioColor) ? (colorHex = "#FCDAC4") : (colorHex = "#FDF0F0");
                            return '<div style = "background-color: '+ colorHex +'; border: \n\
                            3px solid '+ colorHex +';">' + formatDate2Time(record.horarioSalida) + '</div>';
                        }
                    },{
                        field: 'origen',
                        caption: 'Origen',
                        size: '8%',
                        sortable: true,
                        style: 'background-color: #FDF0F0',
                        resizable: true,
                        render: function (record) {
                            (record.numeroCamion % 2 === 0) ? 
                            (cambioColor = true) :
                            (cambioColor = false);
                            (cambioColor) ? (colorHex = "#FCDAC4") : (colorHex = "#FDF0F0");
                            return '<div style = "background-color: '+ colorHex +'; border: \n\
                            3px solid '+ colorHex +';">' + record.origen + '</div>';
                        }
                    },{
                        field: 'horarioLlegada',
                        caption: 'Horario Llegada',
                        size: '110px',
                        sortable: true,
                        resizable: true,
                        attr: 'align=center',
                        style: 'background-color: #FDF0F0',
                        render: function (record) {
                            (record.numeroCamion % 2 === 0) ? 
                            (cambioColor = true) :
                            (cambioColor = false);
                            (cambioColor) ? (colorHex = "#FCDAC4") : (colorHex = "#FDF0F0");
                            return '<div style = "background-color: '+ colorHex +'; border: \n\
                            3px solid '+ colorHex +';">' + formatDate2Time(record.horarioLlegada) + '</div>';
                        }
                    },{
                        field: 'destino',
                        caption: 'Destino',
                        size: '8%',
                        sortable: true,
                        style: 'background-color: #FDF0F0',
                        resizable: true,
                        render: function (record) {
                            (record.numeroCamion % 2 === 0) ? 
                            (cambioColor = true) :
                            (cambioColor = false);
                            (cambioColor) ? (colorHex = "#FCDAC4") : (colorHex = "#FDF0F0");
                            return '<div style = "background-color: '+ colorHex +'; border: \n\
                            3px solid '+ colorHex +';">' + record.destino + '</div>';
                        }
                    },{
                        field: 'kilometros',
                        caption: 'kms',
                        size: '8%',
                        sortable: true,
                        attr: 'align=right',
                        style: 'background-color: #FDF0F0',
                        resizable: true,
                        render: function (record) {
                            (record.numeroCamion % 2 === 0) ? 
                            (cambioColor = true) :
                            (cambioColor = false);
                            (cambioColor) ? (colorHex = "#FCDAC4") : (colorHex = "#FDF0F0");
                            return '<div style = "background-color: '+ colorHex +'; border: \n\
                            3px solid '+ colorHex +';">' + (record.kilometros !== null ? record.kilometros.formatMoney(0, '.', ',') : 0 ) + '</div>';
                        }
                    },{
                        field: 'litrosCombustible',
                        caption: 'Lts Diesel',
                        size: '8%',
                        sortable: true,
                        attr: 'align=right',
                        style: 'background-color: #FDF0F0',
                        resizable: true,
                        render: function (record) {
                            (record.numeroCamion % 2 === 0) ? 
                            (cambioColor = true) :
                            (cambioColor = false);
                            (cambioColor) ? (colorHex = "#FCDAC4") : (colorHex = "#FDF0F0");

                            return '<div style = "background-color: '+ colorHex +'; border: \n\
                                3px solid '+ colorHex +';">' + (record.litrosCombustible !== null ? 
                                record.litrosCombustible.formatMoney(2, '.', ',') : 0.00 )+ '</div>';
                        }
                    },{
                        field: 'acumKms',
                        caption: 'Acum Kms',
                        size: '9%',
                        sortable: true,
                        resizable: true,
                        attr: 'align=right',
                        style: 'background-color: #FDF0F0',
                        render: function (record) {
                            if(typeof record.render === "undefined") {
                                (record.numeroCamion % 2 === 0) ? 
                                (cambioColor = true) :
                                (cambioColor = false);
                                (cambioColor) ? (colorHex = "#FCDAC4") : (colorHex = "#FDF0F0");
                                sumAcumKms = sumAcumKms + record.kilometros;
                                imprimirSumAcumLts = sumAcumKms;
                                if(record.recargarDieselEfectivo === 1) {
                                    (colorHex = "#06893E");
                                    sumAcumKms = 0;
                                }
                                acumKmsArray.push(imprimirSumAcumLts.formatMoney(0, '.', ','));
                                record.acumKmsOculto = imprimirSumAcumLts;
                                colorHexArray.push(colorHex);
                                return '<div style = "background-color: '+ colorHex +'; border: \n\
                                3px solid '+ colorHex +';">' + imprimirSumAcumLts.formatMoney(0, '.', ',')  + '</div>';
                            }else {
                                return '<div style = "background-color: '+ colorHexArray[record.inc] +'; border: \n\
                                3px solid '+ colorHexArray[record.inc] +';">' + acumKmsArray[record.inc]  + '</div>';
                            }
                        }
                    },{
                        field: 'acumLts',
                        caption: 'Acum Lts',
                        size: '8%',
                        sortable: true,
                        resizable: true,
                        attr: 'align=right',
                        style: 'background-color: #FDF0F0',
                        render: function (record) {
                            if(typeof record.render === "undefined") {
                                (record.numeroCamion % 2 === 0) ? 
                                (cambioColor = true) :
                                (cambioColor = false);
                                (cambioColor) ? (colorHex = "#FCDAC4") : (colorHex = "#FDF0F0");
                                if(record.litrosCombustible !== null){
                                    sumAcumLts = sumAcumLts + record.litrosCombustible;
                                }
                                imprimirSumAcumLts = sumAcumLts;
                                if(record.recargarDieselEfectivo === 1) {
                                    (colorHex = "#06893E");
                                    sumAcumLts = 0;
                                }
                                record.render = true;
                                record.inc = inc;
                                inc = inc +1;
                                acumLtsArray.push(imprimirSumAcumLts.formatMoney(2, '.', ','));
                                record.acumLtsOculto = imprimirSumAcumLts;
                                return '<div style = "background-color: '+ colorHex +'; border: \n\
                                3px solid '+ colorHex +';">' + imprimirSumAcumLts.formatMoney(2, '.', ',') + '</div>';
                            }else {
                                return '<div style = "background-color: '+ colorHexArray[record.inc] +'; border: \n\
                                3px solid '+ colorHexArray[record.inc] +';">' + acumLtsArray[record.inc]  + '</div>';
                            }    
                        }
                    },{
                        field: 'acumKmsOculto',
                        caption: 'Lts Diesel',
                        size: '8%',
                        hidden: true,
                        sortable: true,
                        attr: 'align=right',
                        style: 'background-color: #FDF0F0',
                        resizable: true
                    },{
                        field: 'acumLtsOculto',
                        caption: 'Lts Diesel',
                        size: '8%',
                        hidden: true,
                        sortable: true,
                        attr: 'align=right',
                        style: 'background-color: #FDF0F0',
                        resizable: true
                    }
                ],
                onLoad: function (event) {
                    event.onComplete = function () {
                        sumAcumKms = 0;
                        sumAcumLts = 0;
                    };
                },
                onSort: function (event) {
                    if(event.field === "acumKms") {
                        event.preventDefault();
                        w2ui.grid.sort('acumKmsOculto');
                    }
                    if(event.field === "acumLts") {
                        event.preventDefault();
                        w2ui.grid.sort('acumLtsOculto');
                    }
                }
            });
            $().w2popup('open', {
                title: 'Encadenamiento por día',
                body: '<div id="nuevoFormulario" style="width: 100%; height: 100%;"></div>',
                style: 'padding: 0px 0px 0px 0px',
                width: 1024,
                height: 768,
                modal: true,
                onOpen: function (event) {
                    event.onComplete = function () {
                        $('#w2ui-popup #nuevoFormulario').w2render('grid');
                    };
                },
                onClose: function (event) {
                    acumKmsArray = [];
                    colorHexArray = [];
                    acumLtsArray = [];
                    sumAcumLts = 0;
                    sumAcumKms = 0;
                }
            });
        };
    };


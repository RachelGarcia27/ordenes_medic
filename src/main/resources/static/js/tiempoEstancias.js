/* global w2utils,w2popup*/
w2utils.locale('static/libs/w2ui/1.5/locale/es-mx.json');

    var cveEscenario;
    var cveRol;
    var numeroOpcion;

    showViewTiemposEstancia = function(mapa, funcion){
        console.log(mapa);
        callback = funcion;
        cveEscenario = mapa.cveEscenario;
        cveRol = mapa.cveRol;
        numeroOpcion = mapa.numeroOpcion;
        restUrl = "rest/f06/tiempoEstancia";
        generaTiemposEstancia(cveEscenario,cveRol,numeroOpcion,restUrl);
    };
    
    generaTiemposEstancia = function (cveEscenario,cveRol,numeroOpcion,restUrl){
        console.log('en la funcion principal');
        $().w2destroy('gridEstancia');
        $().w2destroy('layout');
        $('#gridEstancia').w2grid({
            name: 'gridEstancia',
            recid: 'id',
            selectType: 'cell',
            show: {
                padding: 0,
                lineNumbers: true,
                toolbar: true,
                footer: false,
                toolbarSearch: false,
                toolbarInput: false,                
                toolbarColumns: false
            },
            toolbar: {
                name: 'toolbar',
                items: [
                    {type: 'button',
                     id: 'descargar-csv',
                     caption: 'CSV',
                     hint : 'Generar CSV',
                     icon: 'fa fa-download'}
                ],
                onClick: function (target, info) {
                    if (target === 'descargar-csv') {
                        if (w2ui.gridEstancia.records.length > 0) {
                            var data = [["Exportacion a CSV"], ["Tabla de Tiempos de Reserva/estancias"]];
                            var titulos = ['Id Nodo', 'Nodo', 'Estancia'];
                            data.push(titulos);
                            for (var i=0; i < w2ui.gridEstancia.records.length; i++) {
                                var record = w2ui.gridEstancia.records[i];
                                var row = [];
                                row.push(record.idNodo);
                                row.push(record.nodo);
                                row.push(record.estancia);
                                data.push(row);
                            }
                        }
                        exportCSV(data, "F07TiemposReserva.csv");                           
                    }
                }
            },
            searches: [
                {
                    field: 'idNodo',
                    caption: 'Id Nodo',
                    type: 'text'
                },{
                    field: 'nodo',
                    caption: 'Nodo',
                    type: 'text'
                },{
                    field: 'estancia',
                    caption: 'Estancia',
                    type: 'text'
                }
            ],
            columns: [
                {
                    field: 'idNodo',
                    caption: 'Id Nodo',
                    size: '30%',
                    sortable: true,
                    attr: 'align=center',
                    resizable: true
                },{
                    field: 'nodo',
                    caption: 'Nodo',
                    size: '30%',
                    sortable: true,
                    attr: 'align=center',
                    resizable: true
                },{
                    field: 'estancia',
                    caption: 'Estancia',
                    size: '30%',
                    sortable: true,
                    attr: 'align=center',
                    resizable: true
                }
            ],
            onRender: function () {
                w2popup.lock('Cargando...',true);
                var myData = {
                    'cveEscenario': cveEscenario,
                    'cveRol': cveRol,
                    'numeroOpcion': numeroOpcion
                };
                $.post(restUrl,myData,function (data){
                    if (data.status === 'error') {
                            w2alert(data.message);
                    }else {
                        w2ui.gridEstancia.clear();
                        for(var i=0; i < data.length; i++){
                            var horaText = function () {
                                var mm = data[i].minutosEstancia;
                                var hh = mm/60;
                                if(hh < 1){
                                    if(mm < 10){ 
                                        return "00:0" + mm;
                                    }else{
                                        return "00:" + mm;
                                    }    
                                }else{
                                    var horas = Math.floor(hh);
                                    var minutos = Math.round((hh - horas)*60);
                                    if(minutos <10){
                                        return horas + ":0" + minutos;
                                    }else{
                                        return horas + ":" + minutos;
                                    }
                                }
                            };
                            w2ui.gridEstancia.add({
                                id: data[i].id,
                                idNodo: data[i].cveNodo,
                                nodo: data[i].nombreNodo,
                                estancia: horaText()
                            });
                            w2popup.unlock();
                        }    
                    }
                });
            }
        });
        
            $('#popup1').w2popup('open', {
                    title: 'Tiempos de reserva / estancia',
                    body    : '<div id="tiempoEstancia" style="position: absolute; left: 5px; top: 5px; right: 5px; bottom: 5px;"></div>',
                    style: 'padding: 15px 0px 0px 0px',
                    width: 400,
                    height: 450,
                    onOpen  : function (event) {
                        event.onComplete = function () {
                            $('#w2ui-popup #tiempoEstancia').w2render('gridEstancia');
                        };
                    }
                });
        
    };





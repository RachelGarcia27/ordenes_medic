/* global w2utils,w2popup*/
w2utils.locale('static/libs/w2ui/1.5/locale/es-mx.json');

    var validaTiempoRecorridoUrl = 'rest/f07/validaTiempoRecorrido';
    var guardaRutaDerivadaUrl = 'rest/f00/guardaRutaDerivadaGantt';
    var cuentaEventosUrl = 'rest/f00/cuentaEventos';
    var listaHorariosXRuta =  new Array();
    var MILISEGUNDOS_X_HORA = 60 * 60 * 1000;
    var MILISEGUNDOS_X_MINUTO = 60 * 1000;
    var cveEscenario;
    var cveRol;
    var numeroOpcion;
    var gridHorarios;
    var tipoMov = 0;

    showViewModificaHorario = function(servicioActivo, funcion){
        callback = funcion;
        cveEscenario = servicioActivo.cveEscenario;
        cveRol = servicioActivo.cveRol;
        numeroOpcion = servicioActivo.numeroOpcion;
        tipoMov = 0;
        generaHorariosXRuta(servicioActivo);
    };
    
    generaHorariosXRuta = function (barra){
//        w2ui.layout2.lock('main', 'Cargando...', true);
        w2utils.lock(layout, '<br><br>Cargando...', true);
        //Limpiamos el arraglo
        listaHorariosXRuta.splice(0, listaHorariosXRuta.length);
        //Leemos los horarios de la ruta
        $.ajax({
            url: "rest/f00/tramosHorarioGantt"
            +"/"+barra.id,
            type: 'POST',
            success: function (data) {
                var horaSale;
                var horaLlega;
                for(i = 0; i < data.length; i++){
                    horaSale = (data[i].hhInicio < 10 ? '0' : '') 
                            + data[i].hhInicio+':'
                            +(data[i].mmInicio < 10 ? '0' : '')
                            +data[i].mmInicio;
                    horaLlega = (data[i].hhTermino < 10 ? '0' : '') 
                            + data[i].hhTermino+':'
                            +(data[i].mmTermino < 10 ? '0' : '')
                            +data[i].mmTermino;

                    listaHorariosXRuta.push({
                        id: data[i].id,
                        origen: data[i].cveNodoOrigen+' - '+data[i].nombreNodoOrigen,
                        destino: data[i].cveNodoDestino+' - '+data[i].nombreNodoDestino,
                        horaSale: horaSale,
                        horaLlega: horaLlega,
                        kms: data[i].kilometros,
                        idNodoHacia: data[i].idNodoDestino,
                        idNodoDesde: data[i].idNodoOrigen,       
                        ultimo: i === data.length - 1 ? 1 : 0
                    });
                }
                modificarRutaSalida(barra);
            }
        });
    };
                
                
    //**** Presenta la pantalla de MODIFICACION DE HORARIOS ****//
    modificarRutaSalida = function(recordPadre){
        $().w2destroy('gridModHorarios');
        w2utils.unlock(layout);
        gridHorarios = $('#gridModHorarios').w2grid({
            name: 'gridModHorarios',
            header: recordPadre.idRuta +' '+recordPadre.nombreRuta,
            recid: 'id',
            records: listaHorariosXRuta,
            show: {
                header: true,
                padding: 0,
                lineNumbers: true,
                toolbar: true,
                footer: true,
                toolbarReload: false,
                toolbarColumns: false,
                toolbarSearch: false,
                toolbarInput: false                                  
            },
            columns: [
                {
                    field: 'id',
                    hidden: true
                }, {
                    field: 'origen',
                    caption: 'Origen',
                    size: '238px',
                    sortable: false,
                    resizable: true,
                    render: function (record) {
                        return ('<div class="sec_tramos_beige" >'+ record.origen+ '</div>');
                    }
                }, {
                    field: 'destino',
                    caption: 'Destino',
                    size: '238px',
                    sortable: false,
                    resizable: true,
                    render: function (record) {
                        return ('<div class="sec_tramos_beige" >'+ record.destino+ '</div>');
                    }
                }, {
                    field: 'horaSale',
                    caption: 'Hr. Sale',
                    size: '60px',
                    sortable: false,
                    resizable: true,
                    editable: { type: 'tiempo', options: {max: '23:59'}},
                    render: function (record) {
                        return (
                            '<div class="sec_tramos_salida">' 
                            + record.horaSale
                            + '</div>');
                    }
                }, {
                    field: 'horaLlega',
                    caption: 'hr. Llega',
                    size: '65px',
                    sortable: false,
                    resizable: true,
                    editable: { type: 'tiempo', options: {max: '23:59'}},
                    render: function (record) {
                        return (
                            '<div class="'
                            + (record.ultimo === 1 ? 'sec_tramos_salida' : 'sec_tramos_beige')
                            + '">' 
                            + record.horaLlega
                            + '</div>');
                    }
                }, {
                    field: 'kms',
                    caption: 'Kms',
                    size: '60px',
                    sortable: false,
                    resizable: true,
                    render: function (record) {
                        return ('<div class="sec_tramos_beige" >'
                            + (record.kms === null ? '&nbsp;' : record.kms) 
                            + '</div>');
                    }
                }
            ],
            onDblClick: function (event) {
                if(event.column === 4){
                    var rec = gridHorarios.get(event.recid);
                    if(rec.ultimo !== 1){
                        event.preventDefault();
                    }
                }
            },
            onChange: function (event){
                event.onComplete = function () {
                    gridHorarios.mergeChanges();
                    //Identificamos el registro modificado
                    var idx = gridHorarios.get(event.recid, true);
                    var rec = gridHorarios.records[idx];
                    //Identificamos el registro anterior para poner la misma hora
                    if(idx > 0 && event.column === 3){
                        rec = gridHorarios.records[idx-1];
                        rec.horaLlega = event.value_new;
                    }
                    gridHorarios.refresh();
                };
            },
            toolbar: {
                items: [
                    {type: 'button',
                     caption: 'Aceptar',
                     id: 'aceptar'},
                    {type: 'button',
                     caption: 'Cancelar',
                     id: 'cancelar'},
                    { type: 'break', id: 'break1' },
                    { type: 'radio',  id: 'diaAnterior',  group: '1', caption: 'Día Anterior', icon: 'fa fa-angle-double-left' },
                    { type: 'radio',  id: 'diaActual',  group: '1', caption: 'Día Actual', icon: 'fa fa-dot-circle-o' , checked: true},
                    { type: 'radio',  id: 'diaSiguiente',  group: '1', caption: 'Día Siguiente', icon: 'fa fa-angle-double-right' }
                ],
                onClick: function (target, data) {
                    if (target === 'cancelar'){
                        w2popup.close();
                    }
                    if (target === 'diaAnterior'){
                        tipoMov = 1;
                    }
                    if (target === 'diaActual'){
                        tipoMov = 0;
                    }
                    if (target === 'diaSiguiente'){
                        tipoMov = 2;
                    }
                    if (target === 'aceptar'){
                        w2popup.lock('Guardando horarios', true);
                        //Validar que no sea mayor a 24 horas
                        if(!validaNoExceda24Horas())
                            return;
                        //Validar que no exceda tiempo maximo
                        var registros = gridHorarios.records;
                        var regs = registros.length;
                        var horaSale = w2utils.formatTime(recordPadre.horarioSalida, 'hhh:mi');
                        var horaLlega = w2utils.formatTime(recordPadre.horarioLlegada, 'hhh:mi');
                        var horaSaleNueva = registros[0].horaSale;
                        var horaLlegaNueva = registros[regs - 1].horaLlega;
                        $.ajax({
                            url: validaTiempoRecorridoUrl
                                    +'/'+horaSale
                                    +'/'+horaLlega
                                    +'/'+horaSaleNueva
                                    +'/'+horaLlegaNueva,
                            type: 'POST',
                            success: function (data) {
                                if(data[0] !== ''){
                                    var mensaje = 'La variación de tiempo respecto a la Ruta<br>'
                                        +'Original excede los '+data[0] +' mins establecidos<br>'
                                        +'como máxima variación.<br><br>'
                                        +'Duración Recorrido Original: '+data[1]+'<br>'
                                        +'Duración Recorrido Solicitado:'+data[2]+'<br><br>'
                                        +'¿Desea continuar?';
                                    w2popup.unlock();
                                    w2confirm({msg: mensaje,  
                                        yes_text: 'Si',
                                        yes_callBack : function (){
                                            w2popup.lock('Guardando horarios', true);
                                            guardaCambiosEnHorarios(recordPadre, registros);
                                        },
                                        no_text: 'No',
                                        height: 230}
                                    );
                                }else{
                                    guardaCambiosEnHorarios(recordPadre, registros);
                                }
                            }
                        });
                    }
                }
            }
        });
                    
        guardaCambiosEnHorarios = function(recordPadre, registros){
            gridHorarios.mergeChanges();
            //Ejecutar proceso de guardado
            //Convertir registros a objetos C03bRutaTramoSimpleModHorariosDTO
            var detallesRutaDerivada = new Array();
            var regs = registros.length;
            var idPropios = new Array();
            
            //Calculamos los minutos de recorrido por Ruta
            var saleRuta = new Date('01/01/1970 '+registros[0].horaSale+':00');
            var llegaRuta = new Date('01/01/1970 '+registros[regs - 1].horaLlega+':00');
            var elapsedRuta = llegaRuta - saleRuta;
            var minutosRuta = Math.floor(elapsedRuta / MILISEGUNDOS_X_MINUTO);
            
            for(i = 0; i < registros.length; i++){
                var rec = registros[i];
                idPropios.push(rec.id);
                
                //Calculamos las horas y minutos de recorrido por tramo
                var sale = new Date('01/01/1970 '+rec.horaSale+':00');
                var llega = new Date('01/01/1970 '+rec.horaLlega+':00');
                
                var elapsed = llega.time2Minutes() - sale.time2Minutes();
                if(sale > llega){//Al dia siguiente
                    elapsed = sale.minutes4Midnight() + llega.time2Minutes();
                }
                var horas = Math.floor(elapsed / 60);
                if(horas > 0){
                    elapsed = elapsed % 60;
                }
                var minutos = Math.floor(elapsed);
                //Generamos las lista de objetos
                detallesRutaDerivada.push({
                    id: recordPadre.id,
                    idRuta: recordPadre.idRuta,
                    idRutaDerivada: recordPadre.idRutaDerivada,
                    idNodoHacia: rec.idNodoHacia,
                    idNodoDesde: rec.idNodoDesde,
                    tiempoRecorridoHh: horas,
                    tiempoRecorridoMm: minutos
                });
            }
            //Ejecutar Ajax
            $.ajax({
                url: guardaRutaDerivadaUrl
                        +'/'+registros[0].horaSale
                        +'/'+minutosRuta
                        +'/'+tipoMov
                        +'/'+cveEscenario
                        +'/'+cveRol
                        +'/'+numeroOpcion
                        +'/'+idPropios.toString(),
                data: JSON.stringify(detallesRutaDerivada),
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                success: function (data) {
                    tipoMov = 0;
                    w2popup.unlock();
                    if(data.status === 'error'){
                        w2alert(data.message);
                    }else{
                        w2popup.close();
                        callback('success');
                    }
                },
                error: function (data){
                    tipoMov = 0;
                    w2popup.unlock();
                    console.log(data);
                    w2alert(data);
//                    w2popup.close();
//                    callback('error');
                }
            });
        };
                    
        validaNoExceda24Horas = function(){
            var regs = gridHorarios.records.length;
            //Verificamos los cambios
            var errorEncontrado = false;
            var idxError;
            var minutosAcumulados = 0;
            for(i = 0; i < regs; i++){
                var rec = gridHorarios.records[i];
                //Separa hora y minuto de horaSale y horaLlega y validamos
                var sale = new Date('01/01/1970 '+rec.horaSale+':00');
                var llega = new Date('01/01/1970 '+rec.horaLlega+':00');
                //En el mismo día
                if(llega.getTime() >= sale.getTime()){
                    minutosAcumulados += llega.time2Minutes() 
                            - sale.time2Minutes();
                }else{//Al otro día
                    minutosAcumulados += sale.minutes4Midnight() 
                            + llega.time2Minutes();
                }
                var horas = minutosAcumulados / 60;
                if(horas >= 24){
                    errorEncontrado = true;
                    idxError = i;
                    break;
                }
            }
            //Si encontró error mostrar mensaje y marcamos el rojo
            if(errorEncontrado){
                var mensaje = 'La ruta supera las 24 horas de duración. Favor de validar';
                $('#grid_gridModHorarios_data_'+idxError+'_4').children()
                        .css("background-color","red").css("color", "white");
                w2alert(mensaje);

                return false;
            }

            return true;
        };

        $().w2popup('open', {
                title: 'Modificar Horarios de Ruta – Esc: ' + cveEscenario
                    +' Rol Op.: '+cveRol+'-'+numeroOpcion,
                body: '<div id="nuevoFormulario" style="width: 100%; height: 100%;"></div>',
                style: 'padding: 0px 0px 0px 0px',
                width: 700,
                height: 500,
                modal: true,
                onOpen: function (event) {
                    event.onComplete = function () {
                        $('#w2ui-popup #nuevoFormulario').w2render('gridModHorarios');
                    };
                }
            });
    };


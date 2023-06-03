/* global w2utils,w2popup*/
w2utils.locale('static/libs/w2ui/1.5/locale/es-mx.json');

    var cveEscenario;
    var cveRol;
    var numeroOpcion;
    var formUrl = "static/html/f00_form_cndg.html";
    var descansoUrl = "rest/f00/diasDescanso";
    var guardarUrl = "rest/f05/guardarDiaSugerido";

    showViewConfNumGuardias = function(mapa, funcion){
        callback = funcion;
        console.log(callback);
        cveEscenario = mapa.cveEscenario;
        cveRol = mapa.cveRol;
        numeroOpcion = mapa.numeroOpcion;
        restUrl = "rest/f05/" + cveEscenario + "/" + cveRol + "/" + numeroOpcion;
        generaConfNumGuardias(restUrl);
    };
    
    generaConfNumGuardias = function (restUrl){
        w2utils.lock(layout, '<br><br>Cargando...', true);
        $.getJSON(restUrl, function (guardiasResponse) {
            var data = {
                diasTrabajo: guardiasResponse.diasTrabajo
            };
            $.post(descansoUrl,data, function (descansoResponse) {
                var descanso = new Array();
                for(var i=1; i < descansoResponse.length; i++) {
                    if(i===1){
                        descanso.push({
                            id: 0,
                            text: '0'
                        }); 
                    }
                    descanso.push({
                        id: descansoResponse[i],
                        text: descansoResponse[i]
                    });
                    
                }
                guardarUnidadesGuardias = function (myData) {
                    w2popup.lock('Guardando...',true);
                    $.post(guardarUrl,myData,function (data){
                        if (data.status === 'error') {
                            w2alert(data.message);
                        }
                        w2popup.unlock();
                    });
                };
                w2utils.unlock(layout);
                $().w2destroy('formConfNumGuardias');
                $('#form').w2form({
                    name: 'formConfNumGuardias',
                    formURL: formUrl,
                    fields: [
                        {
                            name: 'inputRol',
                            type: 'text',
                            required: true,
                            disabled: true,
                            html: {
                                attr: 'maxlength="5"',
                                caption: 'inputRol'
                            }
                        },{
                            name: 'inputOpcion',
                            type: 'text',
                            required: true,
                            disabled: true,
                            html: {
                                attr: 'maxlength="5"',
                                caption: 'inputOpcion'
                            }
                        },{
                            name: 'satisfactoriamente',
                            type: 'text',
                            required: true,
                            disabled: true,
                            html: {
                                attr: 'maxlength="5"',
                                caption: 'satisfactoriamente'
                            }
                        },{
                            name: 'inputDiasDeTrabajo',
                            type: 'text',
                            required: true,
                            disabled: true,
                            html: {
                                attr: 'maxlength="5"',
                                caption: 'inputDiasDeTrabajo'
                            }
                        },{
                            name: 'inputUnidadesGuardiasActual',
                            type: 'text',
                            required: true,
                            disabled: true,
                            html: {
                                attr: 'maxlength="5"',
                                caption: 'inputUnidadesGuardiasActual'
                            }
                        },{
                            name: 'inputUnidadesGuardiasSugerido',
                            type: 'list',
                            required: true,
                            options: {
                                items: descanso
                            },
                            html: {
                                attr: 'maxlength="5"',
                                caption: 'inputUnidadesGuardiasSugerido'
                            }
                        }
                    ],
                    record: {
                        inputRol: guardiasResponse.cveRol,
                        inputOpcion: guardiasResponse.numeroOpcion,
                        satisfactoriamente: 'Se generó Satisfactoriamente!!',
                        inputDiasDeTrabajo: guardiasResponse.diasTrabajo,
                        inputUnidadesGuardiasActual: guardiasResponse.numeroUnidadesGuardias,
                        inputUnidadesGuardiasSugerido: descansoResponse[0]
                    },
                    actions: {
                        save: {
                            onClick: function () {
                                myData = {
                                    id: guardiasResponse.id,
                                    numeroUnidadesGuardias: w2ui.formConfNumGuardias.
                                            record.inputUnidadesGuardiasSugerido.text
                                };
                                guardarUnidadesGuardias(myData);
                                w2popup.close();
                            }
                        },
                        reset: {
                            caption: '<spring:message code="sipdr.boton.cancelar"/>',
                            onClick: function () {
                                w2popup.close();
                            }
                        }
                    }
                });
                $().w2popup('open', {
                    title: 'Configurar núm. de guardias',
                    body: '<div id="nuevoFormulario" style="width: 100%; height: 100%;"></div>',
                    style: 'padding: 0px 0px 0px 0px',
                    width: 370,
                    height: 315,
                    showClose: false, 
                    modal: true,
                    onOpen: function (event) {
                        event.onComplete = function () {
                            $('#w2ui-popup #nuevoFormulario').w2render('formConfNumGuardias');
                        };
                    }
                });
            });    
        }); 
    };


/* global w2utils,w2popup,w2ui*/

w2utils.locale('static/libs/w2ui/1.5/locale/es-mx.json');

/**
 * Función para darle formato a los números.
 * 
 * @param {type} c Cantidad de decimales a escribir
 * @param {type} d Caracter usar para separar decimales
 * @param {type} t Caracter usar para separar miles
 * @returns {String} texto formateado
 */
Number.prototype.formatMoney = function (c, d, t) {
    var n = this,
            c = isNaN(c = Math.abs(c)) ? 2 : c,
            d = d === undefined ? "." : d,
            t = t === undefined ? "," : t,
            s = n < 0 ? "-" : "",
            i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "",
            j = (j = i.length) > 3 ? j % 3 : 0;
    return s + (j ? i.substr(0, j) + t : "")
            + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t)
            + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
};

/**
 * Funcion de redondeo de numeros.
 * 
 * @returns {Number}
 */
Number.prototype.roundMax = function () {
    var s = this.toExponential();
    return (s.substr(0, 1) * 1 + 1) * Math.pow(10, s.substr(-1, 1) * 1);
};

/**
 *  Función que extrae el tiempo de una fecha en minutos.
 *  
 * @returns {Number|Date.prototype@call;getMinutes|Date.prototype@call;getHours}
 */
Date.prototype.time2Minutes = function () {
    return this.getHours() * 60 + this.getMinutes();
};

/**
 *  Función calcula los minutos que le faltan al tiempo de una fecha para la media noche..
 *  
 * @returns {Number|Date.prototype@call;getMinutes|Date.prototype@call;getHours}
 */
Date.prototype.minutes4Midnight = function () {
    return 24 * 60 - (this.getHours() * 60 + this.getMinutes());
};

/**
 * Función que permite abrir una ventana emergente sin barra de url.
 * 
 * @param {type} url URL de la ventana a abrir.
 * @returns {undefined}
 */
function abrir(url) {
    var width = screen.availWidth;
    var height = screen.availHeight;
    window.open(url, '_blank', 'location=no, status=no, toolbar=no, titlebar=no,' +
            'menubar=no, scrollbars=1,modal=yes,width=' + width + ',height=' + height);
}

/**
 * Función que defuelve un texto de tiempo a partir de la hora y minutos recibidos.
 * 
 * @param {type} hr Horas a considerar
 * @param {type} min Minutos a considerar.
 * @returns {String} El tiempo formateado.
 */
function formatTime(hr, min) {    
    return ("0" + hr).slice(-2) + ':' + ("0" + min).slice(-2);
}

/**
 * Función que Calcula los minutos a partir de una hora en texto.
 * 
 * @param {type} timeStr Texto recibido
 * @returns {Number} Equivalencia en Minutos 
 */
timeStr2Minutes = function (timeStr) {
    var time = timeStr.split(':');
    var min = (time.length < 2 ? 0 : parseInt((time[1] + "0").slice(0, 2)));
    if (time[0] === "") {
        time[0] = "0";
    }
    var hours = Math.floor(min/60) + parseInt(time[0]);
    var minutes = min % 60;
    return hours * 60 + minutes;
};

/**
 * Función que Calcula los minutos a partir de una hora en texto.
 * 
 * @param {type} totalMinutes Texto recibido
 * @returns {Number} Equivalencia en Minutos 
 */
minutes2TimeStr = function (totalMinutes) {
    var hours = Math.floor(totalMinutes/60);
    var minutes = totalMinutes % 60;
    return formatTime(hours, minutes);
};

/**
 * Función que defuelve un texto de tiempo a partir de la suma de dos horas 
 * recibidos como parametros y dos minutos recibidos.
 * 
 * @param {type} hr1 Horas a considerar
 * @param {type} min1 Horas a considerar
 * @param {type} hr2 Horas a considerar
 * @param {type} min2 Minutos a considerar.
 * @returns {type} La suma resultante de las horas y los minutos
 */
function acumulaTime(hr1, min1, hr2, min2) {
    var hr, min, mm = 0;
    hr = hr1 + hr2;
    mm = min1 + min2;
    if (mm >= 60) {
        min = mm - 60;
        hr = hr + 1;
    } else {
        min = mm;
    }
    return [hr, min];
}

/**
 * Función que formatea una fecha a horas y minutos.
 * 
 * @param {type} theDate La fecha a forrmatear.
 * @returns {String}  La fecha ya formateada
 */
function formatDate2Time(theDate) {
    theDate = (theDate ? new Date(theDate) : new Date(0, 0, 0, 0, 0, 0, 0));
    hour = "" + theDate.getHours();
    minute = "" + theDate.getMinutes();
    return formatTime(hour, minute);
}

/**
 * Funcion que ajusta el contorno de la ventanaa a un grid.
 * 
 * @param {type} contentId Identificador del objeto a usar para ajustar la ventana.
 * @param {type} centered Indica si se debe centrar la ventana.
 * @returns {undefined}
 */
fitToContent = function (contentId, centered) {
    var w = $(window), d = $(document), b = $('#' + contentId);
    var theWidth = 3 + (b.width() - w.width()) || (d.width() - w.width());
    var theHeight = 3 + (b.height() - w.height()) || (d.height() - w.height());
    window.resizeBy(theWidth + 0, theHeight + 3);
    if (centered) {
        window.moveTo((w.availWidth - theWidth) / 2, 0);
    }
};

/**
 *  Función que abre una ventana enviando datos mediante post.
 *  
 * @param {type} url  direccion a abrir
 * @param {type} data datos que se pasan a la página
 * @param {type} target url opcional (nombre, _self o _blank) Default: "_blank"
 * @param {type} options características de la ventana a abrir si target es _blank.
 * @returns {undefined}
 */
function abrirPOST(url, data, target, options) {
    var windowoption;
    if($.isArray(options) && options.length === 2) {
        windowoption = 'resizable=yes,height='+options[0]+',width='+options[1]+',location=0,menubar=0,scrollbars=1';
    } else if (typeof options === 'string') {
        windowoption = options;
    } else {
        windowoption = 'resizable=yes,location=0,menubar=0,scrollbars=1,width='+screen.availWidth+',height='+screen.availHeight;
    }
    var form = document.createElement("form");
    form.style.display = 'none';
    form.method = "POST";
    form.action = url;
    form.target = target || "_self";
    if (data) {
        for (var key in data) {
            if (data.hasOwnProperty(key)) {
                var input = document.createElement('input');
                input.type = 'hidden';
                input.name = key;
                input.value = (typeof data[key] === "object" && !$.isArray(data[key])) ?
                                    JSON.stringify(data[key]) : data[key];
                form.appendChild(input);
            }
        }              
    }
    document.body.appendChild(form);
    if (form.target === '_blank') {
        form.target = 'newWindow';
        window.open('', form.target, windowoption);
    }
    form.submit();                 
    document.body.removeChild(form);           
}

/**
 * Alias para la ejecución de una petición ajax tipo POST con datos en formato json.
 * 
 * @param {type} url Destino de la petición.
 * @param {type} data Datos a enviar.
 * @returns {jqXHR} Petición realizada.
 */
postJSON = function (url, data) {
    return $.ajax({url: url, data: JSON.stringify(data), type: 'POST', contentType: 'application/json'});
};

/**
 *  Función que abre una ventana enviando datos mediante post.
 *  
 * @param {type} url  direccion a abrir
 * @returns {undefined}
 */
abrirSELF = function (url) {
    var form = document.createElement("form");
    form.action = url;
    form.method = 'GET';
    form.target = "_self";
    form.style.display = 'none';
    document.body.appendChild(form);
    form.submit();
};

/**
 * Funcion para generar un mensaje HTML para un mensaje de confirmacion con 
 * area de texto.
 * 
 * @param {type} title Titulo a desplegar
 * @param {type} body  Cuerpo del textarea
 * @param {type} columns Número de columnas del textarea
 * @returns {String} Menaje HTML formateado.
 */
function msgTextArea(title, body, columns) {
    var cols = columns || 35;
    return '<div>' + title + '</div>' +
            '<textarea rows="6" cols="' + cols + '">' + body + '</textarea>';
}

/**
 * Funcion que despliega un mensaje para avisar que se esta cargando el 
 * formulario, durante dos segundos y se cierra.
 * 
 * @returns {undefined}
 */
function avisoCargando() {
    w2popup.open({
        modal:true,
        width: 500,
        height: 300,
        title: 'Un momento',
        body: '<div class="w2ui-centered">Cargando el formulario.<br>Intente en un instante nuevamente.</div>'
    });
    w2popup.lock('', true);
    setTimeout(function () {
        w2popup.unlock();
        w2popup.close();
    }, 2000);
}

/**
 * Funcion para capitalizar palabras. Se agrega a prototipe para que todos los objetos la tenga.
 * 
 * @returns Texto con la primera letra capitalizada
 */
String.prototype.capitalizeFirstLetter = function () {
    return this.charAt(0).toUpperCase() + this.slice(1);
};

/**
 *  Complementa un texto al largo deseado con espacios.
 *  
 * @param {type} n Largo deseado
 * @returns {String.prototype}
 */
String.prototype.fillTo = function (n) {
    var ch = " ";
    n = n - this.length;
    result = this;
    while (n-- > 0) {
        result += ch;
    }
    return result;
};

/**
 * Función para remplazar el comportamiento por defecto de la baja de un grid w2ui.
 * Se llama dentro del metodo onDelete del grid. Se debe llamar con call:
 * onDelete: function (event) {
 *     replaceDelete.call(this, event, callBack);
 * }
 * 
 * @param {type} mainEvent Recibe el evento del OnDelete
 * @param {type} callBack Recibe una función o texto con el nombre de la 
 * funcion a ejecutar. Debe ser declarada a nivel global.
 * @returns {undefined}
 */
function replaceDelete(mainEvent, callBack) {
    var obj = this;
    var funName = (typeof callBack === 'function' ? callBack.name : callBack);
    mainEvent.preventDefault();
    options = {
        width: 350,
        height: 170,
        body: '<div class="w2ui-centered">' + w2utils.lang(obj.msgDelete) + '</div>',
        buttons: '<button class="w2ui-btn w2ui-btn-red" onclick="w2ui[\'' + this.name + '\'].message();' + funName + '()">' + w2utils.lang('Yes') + '</button>' +
                '<button class="w2ui-btn" onclick="w2ui[\'' + this.name + '\'].message()">' + w2utils.lang('No') + '</button>',
        onOpen: function (event) {
            var inputs = $(this.box).find('input, textarea, select, button');
            inputs.off('.message')
                    .on('blur.message', function (evt) {
                        // last input
                        if (inputs.index(evt.target) + 1 === inputs.length) {
                            inputs.get(0).focus();
                            evt.preventDefault();
                        }
                    })
                    .on('keydown.message', function (evt) {
                        if (evt.keyCode === 27)
                            obj.message(); // esc
                    });
            setTimeout(function () {
                $(this.box).find('.w2ui-btn:last-child').focus();
                clearTimeout(obj.last.kbd_timer);
            }, 25);
        }
    };
    w2utils.message.call(obj, {
        box: obj.box,
        path: 'w2ui.' + obj.name,
        title: '.w2ui-grid-header:visible',
        body: '.w2ui-grid-box'
    }, options);
}

/**
 * Despliega el verión simple del menu flotante para los escenarios.
 * 
 * @param {type} gridName
 * @param {type} opciones
 * @returns {String}
 */
function infoMenuSimple(gridName, opciones) {
    var id, i, len = opciones.length, opcStr = '';
    for (i = 0; i < len; i++) {
        id = gridName + '_menu_opc_' + (i + 1);
        if (opciones[i].data) {
            opcStr += '<tr><td id="' + id + '"><a href="#" onclick="' + (opciones[i].self ? 'abrirSELF' : 'abrir') + '(\'' + opciones[i].data + '\'); $(\'.w2ui-info-bubble\').hide(); return false;">' + opciones[i].text + '</a></td></tr>';
        } else {
            opcStr += '<tr><td id="' + id + '"><a href="#">' + opciones[i].text + '</a></td></tr>';
        }
    }
    return  '<table>' + opcStr + '</table>';
}

/**
 * Despliega el menu flotante para los escenarios.
 * 
 * @param {type} gridName
 * @param {type} opciones
 * @returns {String}
 */
function infoMenu(gridName, opciones) {
    var id, i, len = opciones.length, opcStr = '';
    for (i = 0; i < len; i++) {
        id = gridName + '_menu_opc_' + (i + 1);
        if (opciones[i].data) {
            if (opciones[i].process) {
                opcStr += '<tr><td id="' + id + '"><a href="#" onclick="$(\'.w2ui-info-bubble\').hide();\n\
                    w2confirm(\''+opciones[i].mensajeConfirmacion+'\')\n\
                        .yes(function () { \n\
                            w2utils.lock('+gridName+', \''+opciones[i].mensajeProcess+'\', true); \n\
                            $.ajax({ url:\'' + opciones[i].data + '\',\n\
                                    success: function(data) { console.log(data);\n\
                                    if (data.status === \'success\'){\n\
                                        validaAvance(0,{\'code\':\'' + opciones[i].job +'\'}); \n\
                                        }else{\n\
                                             w2alert(data.message);\n\
                                             w2utils.unlock('+gridName+');\n\
                                            }\n\
                            }}); \n\
                        }).no(function () { w2popup.close(); });">'+ opciones[i].text + '</a></td></tr>';
            } else {
                opcStr += '<tr><td id="' + id + '"><a href="#" onclick="' + (opciones[i].self ? 'abrirSELF' : 'abrir') + '(\'' + opciones[i].data + '\'); $(\'.w2ui-info-bubble\').hide(); return false;">' + opciones[i].text + '</a></td></tr>';
            }
        } else {
            opcStr += '<tr><td id="' + id + '"><a href="#">' + opciones[i].text + '</a></td></tr>';
        }
    }
    return  '<table>' + opcStr + '</table>';
    }

     var validaAvance = function (count,code) {        
        setTimeout(function () {                       
            $.post('rest/jobs/check', code, function (data) {                    
                if (count > 30000) {
                    w2alert('Se excedió el tiempo de espera!');
                } else {
                    if (data.status === 'running') {                    
                        validaAvance(count+1,code);
                    } else {
                        w2utils.unlock($('#grid'));
                        if (data.status === 'finished_ok') {                    
                            w2alert('Actualización de Roles concluido exitosamente.');
                        } else if (data.status === 'finished_error') {                    
                            w2alert('Actualización de Roles concluido con error.');
                        } else if (data.status === 'success') {                    
                            w2alert('Actualización de Roles no se está ejecutando.');
                        }
                    }
                }
            });    
        }, 5000);
    };

/**
 * Rutina que regresa el URL base de la página.
 * 
 * @returns {String} El url Base.
 */
var getBaseURL = function () {
    var l = window.location;
    var pathArray = l.pathname.split( '/' );
    var newPathname = "";
    for ( i = 0; i < pathArray.length-1; i++ ) {
      newPathname += pathArray[i];
      newPathname += "/";
    }
    return l.protocol + "//" + l.host + newPathname;
};

/**
 * Acción para la descarga la información de un grid en un archivos CSV, 
 * enviando via post la información de filtrado activa. Utiliza GET.
 * @param {type} url del que e desea descargar el CSV.
 * @returns {undefined}
 */
var downloadCSV = function (url) {
    var link = document.createElement("a");
    link.setAttribute("href", getBaseURL() + url);
    document.body.appendChild(link);
    link.click();
    delete link;
};

/**
 * Acción para la descarga de un archivos CSV. Utiliza POST.
 * @param {type} url del archivo que se desea descargar el CSV.
 * @param {type} searchData criterios de búsqueda de datos.
 * @returns {undefined}
 */
var downloadGridToView = function (url, searchData) {
    var form = document.createElement("form");
        form.style.display = 'none';
        form.method = "POST";
        form.action = url;
    var input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'request';
        input.value = JSON.stringify(searchData);
    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();                 
    document.body.removeChild(form);
};

var exportCSV = function (data, fileName) {
    var csvContent = "data:text/csv;charset=utf-8,";
    data.forEach(function(infoArray, index){
       dataString = infoArray.join(",");
       csvContent += index < data.length ? dataString+ "\n" : dataString;
    });            
    var encodedUri = encodeURI(csvContent);
    var link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", fileName);
    document.body.appendChild(link);
    link.click();
    delete link;    
};

/**
 * Acción para la abrir de un archivos PDF.
 * @param {type} url del archivo que se desea mostrar.
 * @param {type} searchData criterios de búsqueda de datos.
 * @returns {undefined}
 */
var abrirPDF = function (url, searchData) {
    var windowoption = 'resizable=yes,location=0,menubar=0,scrollbars=1,width='+screen.availWidth+',height='+screen.availHeight;
    var form = document.createElement("form");
        form.style.display = 'none';
        form.method = "POST";
        form.action = url;
    var input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'request';
        input.value = JSON.stringify(searchData);
    form.appendChild(input);
    document.body.appendChild(form);
    form.target = 'newWindow';
    window.open('', form.target, windowoption);
    form.submit();                 
    document.body.removeChild(form);
};

/**
 * Tipo de datos específico para manejo del tiempo, con captura de separador de 
 * minutos mediante comas y punto.
 * 
 * @param {type} función de adición de tipo
 * @param {type} nombre del tiepo de dato
 * @param {type} opciones de configuración.
 */
$().w2field('addType', 'tiempo', function (options) {
    $(this.el).on('keypress', function (event) {
        if (event.metaKey || event.ctrlKey || event.altKey || (event.charCode !== event.keyCode && event.keyCode > 0)) return;
        var ch = String.fromCharCode(event.charCode);
        if ('.:,'.indexOf(ch) > -1) {
            if (this.value.indexOf('.') > -1 || this.value.indexOf(':') > -1 || this.value.indexOf(',') > -1) {
                if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;
                return false;                                
}
        } else  if ('0123456789'.indexOf(ch) === -1) {
            if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;
            return false;
        }
    });
    $(this.el).on('blur', function (event)  { // keyCode & charCode differ in FireFox
        var max = (options.options && options.options.max ? timeStr2Minutes(options.options.max): undefined);
        this.value = this.value.replace(/[,.]/g, ":");
        var minutes = timeStr2Minutes(this.value);
        this.value = (minutes > max ? options.selected : minutes2TimeStr(minutes));
    });
});

/**
 * Tipo de campo para captura de datos alfabéticos.
 * Opciones: espacios: true | false
 * 
 * @param {type} función de adición de tipo
 * @param {type} nombre del tipo de dato
 * @param {type} opciones de configuración.
 */
$().w2field('addType', 'alfabetico', function (options) {
    $(this.el).on('keypress', function (event) {
        if (event.metaKey || event.ctrlKey || event.altKey || (event.charCode !== event.keyCode && event.keyCode > 0)) return;
        var ch = String.fromCharCode(event.charCode);
        if((' '.indexOf(ch) > -1 && options.espacios)){
        }else{
            if ('ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz'.indexOf(ch) === -1) {//Si no lo encuentra
                    if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;
                    return false;                                
            }
        }
    });
    $(this.el).on('blur', function (event)  { // keyCode & charCode differ in FireFox. Al salir del campo
    });
});

/**
 * Tipo de campo para captura de datos alfanuméricos. 
 * Opciones: espacios: true | false
 * 
 * @param {type} función de adición de tipo
 * @param {nombre} nombre del tipo de dato
 * @param {options} opciones de configuración.
 */
$().w2field('addType', 'alfanumerico', function (options) {
    $(this.el).on('keypress', function (event) {
        if (event.metaKey || event.ctrlKey || event.altKey || (event.charCode !== event.keyCode && event.keyCode > 0)) return;
        var ch = String.fromCharCode(event.charCode);
        if((' '.indexOf(ch) > -1 && options.espacios)){
        }else{
            if ('ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz0123456789'.indexOf(ch) === -1) {//Si no lo encuentra
                    if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;
                    return false;                                
            }
        }
    });
    $(this.el).on('blur', function (event)  { // keyCode & charCode differ in FireFox. Al salir del campo
    });
});

/**
 * Tipo de campo para captura de datos numéricos. 
 * Opciones: espacios: true | false
 * 
 * @param {type} función de adición de tipo
 * @param {type} nombre del tipo de dato
 * @param {type} opciones de configuración.
 */
$().w2field('addType', 'numerico', function (options) {
    $(this.el).on('keypress', function (event) {
        if (event.metaKey || event.ctrlKey || event.altKey || (event.charCode !== event.keyCode && event.keyCode > 0)) return;
        var ch = String.fromCharCode(event.charCode);
        if((' '.indexOf(ch) > -1 && options.espacios)){
        }else{
            if ('0123456789'.indexOf(ch) === -1) {//Si no lo encuentra
                    if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;
                    return false;                                
            }
        }
    });
    $(this.el).on('blur', function (event)  { // keyCode & charCode differ in FireFox. Al salir del campo
    });
});

/**
 * Tipo de campo para captura de datos para la inclusión
 * de un conjunto de caracteres particulares. 
 * Opciones: espacios: true | false
 *           caracteres: caracteres aceptados, ejemplo: 'abcABC' o '0123456789 .'
 * 
 * @param {type} función de adición de tipo
 * @param {type} nombre del tipo de dato
 * @param {type} opciones de configuración.
 */
$().w2field('addType', 'especial', function (options) {
    $(this.el).on('keypress', function (event) {
        if (event.metaKey || event.ctrlKey || event.altKey || (event.charCode !== event.keyCode && event.keyCode > 0)) return;
        var ch = String.fromCharCode(event.charCode);
        if((' '.indexOf(ch) > -1 && options.espacios)){
        }else{
            if (options.caracteres.indexOf(ch) === -1) {//Si no lo encuentra
                    if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;
                    return false;                                
            }
        }
    });
    $(this.el).on('blur', function (event)  { // keyCode & charCode differ in FireFox. Al salir del campo
    });
});

/**
 * Tipo de campo para captura de datos numéricos con decimales de . 
 * Opciones: espacios: true | false
 * 
 * @param {type} función de adición de tipo
 * @param {type} nombre del tipo de dato
 * @param {type} opciones de configuración.
 */
$().w2field('addType', 'decimalPositivos', function (options) {
    $(this.el).on('keypress', function (event) {
        if (event.metaKey || event.ctrlKey || event.altKey || (event.charCode !== event.keyCode && event.keyCode > 0)) return;
        var ch = String.fromCharCode(event.charCode);
        if((' '.indexOf(ch) > -1 && options.espacios)){
        }else{
            if ('0123456789.'.indexOf(ch) === -1) {//Si no lo encuentra
                    if (event.stopPropagation) event.stopPropagation(); else event.cancelBubble = true;
                    return false;                                
            }
        }
    });
    $(this.el).on('blur', function (event)  { // keyCode & charCode differ in FireFox. Al salir del campo
        var split = this.value.split('.');
        var split_first_length = split[0].length;
        var dig = 5;
        this.value = split_first_length > dig ? "" : (typeof split[1] !== "undefined" ? split[0] + '.' + split[1] : split[0]);
    });
});

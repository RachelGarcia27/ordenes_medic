/* global w2utils,w2popup,w2ui*/

w2utils.locale('static/libs/w2ui/1.5/locale/es-mx.json');
var idx, idxActual;
var rolHabilitado;
var servicioActivo = null;
var gServicios;
var rowHeight = 22;
var originX = 10;
var srvHeight = rowHeight - 4;
var originY = 30;
var posLineaBloque = 5;
var posLineaDisel = 8;
var posLineaSubeBaja = 12;
var posLineaTraslado = 16;
var PREF_SRV = '#srv-';
var tramosGridIsExpanded = false;
var srvSVG;
var CON_VALIDACION = true;
var SIN_VALIDACION = false;
var prevDia = 0;
var posDia = [];
var countDia = [];
var numDias = 0;
var listaServicios = [];
var records = [];
var origenSegmento;
var fechaInicial, fecha_x, fechaExtendida;
var selectInicio = null;
var selectFinal = null;
var daysweek = [{id:1,day:'L'},{id:2,day:'M'},{id:3,day:'X'},{id:4,day:'J'}
    ,{id:5,day:'V'},{id:6,day:'S'},{id:0,day:'D'}];
var paramGANTT = {cuadros: 720, minutesPerPixel: 2, colWidth: Math.floor(60/2), type: 'dia'};

function drawLabels(svg) {
    var labelY = originY;
    var labelWidth = originX + 90;
    var numRec = records.length;
    var boxHeight = (labelY + rowHeight * (numRec + 1) + 10);
    var i, y, clase;
    var name = origenSegmento === 1 ? 'Unidades' : 'Operadores';
    svg.configure({'width': (labelWidth + "px"),
        'height': (boxHeight + "px"),
        'class': "a5-gantt-labels",
        'viewBox': '0 0 ' + labelWidth + ' ' + boxHeight}, true);
    var g = svg.group({'class': 'a5-grids'});
    gLbl = svg.group();
    // Lineas Verticales
    svg.line(g, originX, labelY, originX, labelY + rowHeight * (numRec + 1), {class: 'a5-vertical'});
    svg.line(g, labelWidth, labelY, labelWidth, labelY + rowHeight * (numRec + 1), {class: 'a5-vertical'});
    // Lineas Horizontales
    var nameGantt = name === 'Operadores' ? 'Op -' : 'Un -';
    for (i = 0; i <= numRec + 1; i++) {
        y = labelY + i * rowHeight;
        clase = (i === 0 || i === 1 || i === (numRec + 1) ? "a5-horizontal" : "a5-day-line");
        svg.line(g, originX, y, labelWidth, y, {class: clase});
        if (1 < i && i <= numRec + 1) {
            svg.text(gLbl, originX + 5,
                    (labelY - 5) + (rowHeight * (i)),
                    nameGantt + (i - 1));
        } else if (i === 1) {
            svg.text(gLbl, originX + 5,
                    (labelY - 5) + (rowHeight * (i)),
                    name);
        }
    }
}

function dibujaServicio(srvData, clicked) {
    var horaInicio = moment(srvData.horaSalida); 
    var horaFin = moment(srvData.horaLlegada); 
    var diaini = moment(srvData.horaSalida).startOf('day'); 
    var diafin = moment(srvData.horaLlegada).startOf('day');
    if(srvData.tipoSegmento === 2){
        var pre = listaServicios.find(x => x.numeroSecuenciaDesarrollo === (srvData.numeroSecuenciaDesarrollo - 1));
        var post = listaServicios.find(x => x.numeroSecuenciaDesarrollo === (srvData.numeroSecuenciaDesarrollo + 1));
        if(typeof pre === 'undefined' &&  typeof post !== 'undefined'){
        horaInicio = moment(srvData.horaSalida);
        horaFin = moment(post.horaLlegada); 
        diaini = moment(srvData.horaSalida).startOf('day'); 
        diafin = moment(post.horaLlegada).startOf('day'); 
        }
        if(typeof post === 'undefined' && typeof pre !== 'undefined'){
          horaInicio = moment(pre.horaSalida);
        horaFin = moment(srvData.horaLlegada); 
        diaini = moment(pre.horaSalida).startOf('day'); 
        diafin = moment(srvData.horaLlegada).startOf('day');  
        }
        if(typeof post === 'undefined' && typeof pre === 'undefined'){
        horaInicio = moment(srvData.horaSalida);
        horaFin = moment(srvData.horaLlegada); 
        diaini = moment(srvData.horaSalida).startOf('day'); 
        diafin = moment(srvData.horaLlegada).startOf('day');
        }
        if(typeof post !== 'undefined' && typeof pre !== 'undefined'){
        horaInicio = moment(pre.horaSalida);
        horaFin = moment(post.horaLlegada); 
        diaini = moment(pre.horaSalida).startOf('day'); 
        diafin = moment(post.horaLlegada).startOf('day');
        }
    }
    var inicio = horaInicio.hour() * paramGANTT.colWidth + Math.floor(horaInicio.minutes() / paramGANTT.minutesPerPixel);
    var id = origenSegmento === 1 ? 'idUnidad' : 'idOperador';
    var minutos, adjY = originY + (srvData[id] - 1)* rowHeight;
    var fx = moment({ year: 2010, month: 1, day: 1, hour: horaInicio.hour(), minute:horaInicio.minute()});
    var fy = moment({ year: 2010, month: 1, day: 1 + diafin.diff(diaini,'days'), hour: horaFin.hour(), minute:horaFin.minute()});
    minutos = fy.diff(fx,'minutes');
    minutos = Math.floor(minutos / paramGANTT.minutesPerPixel);
    var diffDaysTomin = horaInicio.diff(fechaInicial,'days') * paramGANTT.cuadros;
    var gBus = srvSVG.group(gServicios, {
        'id': 'srv-' + srvData.numeroSecuenciaDesarrollo,
        'idOp': srvData.id,
        'data-id': srvData.numeroSecuenciaDesarrollo,
        'class': (colorTipoSegmento(srvData.tipoSegmento,srvData.esSencillo)) + (clicked ? ' clicked' : '')});
    srvSVG.rect(gBus, originX + inicio + diffDaysTomin, adjY + rowHeight + 2, minutos, srvHeight);
    srvSVG.title(gBus, 'idOp: ' + srvData.idOperador+ ', sec: '+ srvData.numeroSecuenciaDesarrollo+', id: '+ srvData.id); 
        //evento   click
    $(PREF_SRV + srvData.numeroSecuenciaDesarrollo).click(function (event) {
        if (event.shiftKey) {
            //selectShift(this);
        } else {
            if(srvData.tipoSegmento === 1 || srvData.tipoSegmento === 3) {
                switch(srvData.tipoSegmento){
                    case 1: 
                        selectNoShift($(PREF_SRV + (srvData.numeroSecuenciaDesarrollo + 1))[0]);
                        break;
                    case 3: 
                        selectNoShift($(PREF_SRV + (srvData.numeroSecuenciaDesarrollo - 1))[0]);
                        break;
                }
            }else if(srvData.tipoSegmento !== 12){
                selectNoShift(this);
            }
        }
    });
}
                
function drawGrid(svg) {
    srvSVG = svg;
    var numRec = records.length;
    var boxHeight = (originY + rowHeight * (numRec + 1) + 10);
    var gridWidth = originX + hours * paramGANTT.colWidth + 10;
    var clase;
    svg.configure({width: (gridWidth + "px"),
        height: (boxHeight + "px"),
        class: "a5-gantt-labels",
        viewBox: '0 0 ' + gridWidth + ' ' + boxHeight}, true);
    var g = svg.group({'class': 'a5-gantt-chart'});
    gServicios = svg.group({'id': 'node', 'class': 'a5-menu-container'});
    var gTxt = svg.group({'class': 'a5-gantt-hours'});
    var dateTxt = svg.group({'class': 'a5-gantt-date-txt'});
    var j, x, y;
    // Lineas Verticales
    for (i = 0; i <= hours; i++) {
        x = originX + paramGANTT.colWidth * i;
        clase = (i === 0 || i === hours ? "a5-vertical" : (i%24 === 0 ? "a5-midnight" : "a5-hour-line"));
        svg.line(g, x, originY, x, originY + rowHeight * (numRec + 1), {class: clase});
        var dayweek = daysweek.find(x => x.id === parseInt(fecha_x.format('d'))).day;
        var fecha_txt = dayweek.concat(' ' + fecha_x.format('DD-MMM'));
        // Textos horas
        if (i < hours  && paramGANTT.type === 'dia') {
            svg.text(gTxt, x + 2, originY + 15, (i % 24) + ':00');
        }
        // Texto dia
        if( i%24 === 0  && paramGANTT.type === 'dia'){
            svg.text(dateTxt, x + 300, originY/3 + 15, fecha_txt);
            fecha_x.add(1,'days');
        }
        if((i !== 192) && (i%24 === 0) && paramGANTT.type === 'semanal' ){
            svg.text(gTxt, x + 34, originY + 15, fecha_txt);
            fecha_x.add(1,'days');
        }
    }
    // Lineas Horizontales
    for (i = 0; i <= numRec + 1; i++) {
        y = originY + i * rowHeight;
        clase = (i === 0 || i === 1 || i === numRec + 1 ? "a5-horizontal" : "a5-day-line");
        svg.line(g, originX, y, gridWidth - 10, y, {class: clase});
        // Viajes
        if (i < numRec) {
            var id = origenSegmento === 1 ? 'idUnidad' : 'idOperador';
            var services = $.grep(listaServicios,function(e) { return e[id] === (i+1) && e.tipoSegmento !== 2;});
            var servicesTipoSegmento2 = $.grep(listaServicios,function(e) { return e[id] === (i+1) && e.tipoSegmento === 2;});
            servicesTipoSegmento2.forEach(function(e){dibujaServicio(e);});
            services.forEach(function(e){dibujaServicio(e);});
        }
    }
}

function colorTipoSegmento(tipoSegmento,EsSensillo) {
    switch(tipoSegmento){
        case 1:
            return 'tipo-segmento-1';
        case 2:
            return EsSensillo === 1 ? 'tipo-segmento-2-op' : 'tipo-segmento-2-op-es-doble';
        case 3:
            return 'tipo-segmento-3';
        case 4:
            return 'tipo-segmento-4';
        case 5:
            return 'tipo-segmento-5';
        case 12:
            return 'tipo-segmento-12';
            
    }
}

function selectNoShift(obj, menuTarget) {
    var recarga = true;
    if (selectInicio === selectFinal) {
        if (selectInicio && selectFinal) {
            if (selectInicio === obj.id) {
                if (obj.id !== menuTarget) {
                    selectInicio = null;
                    selectFinal = null;
                    obj.classList.remove("clicked");
                }
                return;
            } else {
                $(PREF_SRV + selectInicio)[0].classList.remove("clicked");
            }
        }
    } else {
        idxTarget = (typeof menuTarget !== 'undefined' ? parseInt(menuTarget.substring(4)) : null);
        recarga = !(idxTarget !== null && (selectInicio <= idxTarget &&  idxTarget <= selectFinal));
        if (recarga) {
            for (idx = selectInicio; idx <= selectFinal; idx++) {
                $(PREF_SRV + idx)[0].classList.remove("clicked");
            }                            
        }
    }
    if (recarga) {
        selectInicio = parseInt(obj.getAttribute("data-id"));
        selectFinal = parseInt(obj.getAttribute("data-id"));
        idGantt = parseInt(obj.getAttribute("idOp"));
        obj.classList.add("clicked");
        activaServicio(idGantt);
    }
};

function selectShift(obj) {
    if (selectInicio && selectFinal) {
        if (selectInicio === parseInt(obj.getAttribute("data-id")) && selectFinal ===  parseInt(obj.getAttribute("data-id"))) {
            selectInicio = null;
            selectFinal = null;
            obj.classList.remove("clicked");
        } else {
            idxActual = parseInt(obj.getAttribute("data-id"));
            if (idxActual < selectInicio) {
                for (idx = idxActual; idx < selectInicio; idx++) {
                    $(PREF_SRV + idx)[0].classList.add("clicked");
                }
                selectInicio = idxActual;
            } else if (selectFinal < idxActual) {
                for (idx = selectFinal + 1; idx <= idxActual; idx++) {
                    $(PREF_SRV + idx)[0].classList.add("clicked");
                }
                selectFinal = idxActual;
            } else {
                for (idx = idxActual + 1; idx <= selectFinal; idx++) {
                    $(PREF_SRV + idx)[0].classList.remove("clicked");
                }
                selectFinal = idxActual;
            }
        }
    } else {
        selectInicio = parseInt(obj.getAttribute("data-id"));
        selectFinal = parseInt(obj.getAttribute("data-id"));
        obj.classList.add("clicked");
    }
};
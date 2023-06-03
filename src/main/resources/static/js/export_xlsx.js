
/**
 * Acción para lexportar archivos en formato XLSX
 * 
 * @param {array} fileName información a exportar.
 * @param {string} searchIds archivo a generar.
 * @param {array} grid_records w2ui grid records a generar.
 * @returns {undefined}
 */
var exportarXLSX = function (fileName, searchIds, grid_records) {
    var posicion, rec;
    var records = [];
    var recsLen = searchIds.length;
    if (recsLen > 0) {
        for (var i = 0; i < recsLen; i++) {
            posicion = searchIds[i];
            rec = jQuery.extend({}, grid_records[posicion]);
            delete rec.Id;
            delete rec.recid;
            delete rec.IdNegocio;        
            delete rec.w2ui;
            records.push(rec);
        }
    } else {
        recsLen = grid_records.length;
    for (var i = 0; i < recsLen; i++) {
            rec = jQuery.extend({}, grid_records[i]);
            delete rec.Id;
            delete rec.recid;
            delete rec.IdNegocio;       
            delete rec.w2ui;
            records.push(rec);
        }
    }
    alasql('SELECT * INTO XLSX("' + fileName + '.xlsx",{headers:true}) FROM ?', [records]);
};

var exportarMultipleSheetsXLSX = function(fileName, sheets, grid_records) {
    var data1 = [{a:1,b:10},{a:2,b:20}];
    var data2 = [{a:100,b:10},{a:200,b:20}];
    var opts = [{sheetid:'One',header:true},{sheetid:'Two',header:false}];
    alasql('SELECT INTO XLSX("' + fileName + '.xlsx",?) FROM ?',[sheets,grid_records]);
};
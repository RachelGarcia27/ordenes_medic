/*global SockJS, window, w2alert, w2confirm, sock */
/* 
    Utilería para control de conexiones simultaneas a Gantt.
    Author     : rpacheco
*/
"use strict";

var new_status = function(status) {
    console.log('SockJS status: '+status);
};

(function (window) {

    var TYPE_SESSION_START = 0;
    var TYPE_SESSION_FORCE = 1;
    var TYPE_SESSION_CLOSE = 2;
    var TYPE_MESSAGE = 3;
    
    var bind = function(fn, me){ 
        return function(){ 
            return fn.apply(me, arguments); 
        }; 
    };

    function GanttMonitor(options, reconnect, status_cb, cli_onmessage, cli_onopen, cli_onclose) {
        this.status_cb = status_cb;
        this.cli_onmessage = cli_onmessage || null;
        this.cli_onopen = cli_onopen || null;
        this.cli_onclose = cli_onclose || null;
        this.on_close = bind(this.on_close, this);
        this.on_open = bind(this.on_open, this);
        this.on_message = bind(this.on_message, this);
        this.send = bind(this.send, this);
        this.reconnect_try = bind(this.reconnect_try, this);
        this.reconnect_reset = bind(this.reconnect_reset, this);
        this.register = bind(this.register, this);
        this.generaWSMessage = bind(this.generaWSMessage, this);
        this.setGanttId = bind(this.setGanttId, this);
        this.unregister = bind(this.unregister, this);
        this.connect = bind(this.connect, this);
        this.update_status = bind(this.update_status, this);
        this.sessionId = null;
        this.ganttId = null; 
      
        this.generaWSMessage = function (tipo) {
            if (this.ganttId && this.ganttId !== null) {
                 var msgObject = {
                    type: tipo,
                    text: this.ganttId
                };
                return JSON.stringify(msgObject);
            }
        };    

        this.setGanttId = function (escenario, rol, opcion) {
            this.ganttId = escenario+'|'+rol+'|'+opcion;
        };
        
        $.extend(this.options, options);
        $.extend(this.reconnect, reconnect);
        
        return this;
    }
    
    GanttMonitor.prototype.reconnect = {
      reconnecting: false,
      do_not_reconnect: false,
      reload_after_n: true,
      max_retries: 30,
      reset_mult: 6,
      retry_timeout_ms: 1500 + Math.floor(Math.random() * 60),
      retry_multiplier: 2,
      retry_curr_multiplier: 0,
      retry_curr_timeout: 0,
      retry_count: 0
    };

    GanttMonitor.prototype.options = {
        context_path      : '/sipdr',
        ws_handler        : '/ws_message',
        register_on_open  : true,
        single_messages   : false,
        messages: {
            desea_cerrar  : '¿Desea cerrar el otro Gantt?',
            gantt_abierto : 'El mismo Gantt ha sido abierto por:',
            se_cerrara    : 'Ésta sesión del Gantt se cerrará',
            otro_gantt    : 'Se ha activado otro Gantt'
        }
    };    
  
    GanttMonitor.prototype.conn = null;

    GanttMonitor.prototype.update_status = function() {
      if (this.reconnect.reconnecting) {
        if (this.status_cb !== null) {
          return this.status_cb('reconnecting');
        }
      } else if (this.conn === null || this.conn.readyState !== SockJS.OPEN) {
        if (this.status_cb !== null) {
          return this.status_cb('disconnected');
        }
      } else {
        if (this.status_cb !== null) {
          return this.status_cb('connected');
        }
      }
    };

    GanttMonitor.prototype.connect = function() {
      if (this.conn !== null) {
        this.conn.close();
        this.conn = null;
      }
      this.conn = new SockJS(this.options.context_path+this.options.ws_handler);
      if (this.status_cb !== null) {
        this.status_cb('connecting');
      }
      this.conn.onopen = this.on_open;
      this.conn.onclose = this.on_close;
      return this.conn.onmessage = this.on_message;
    };

    GanttMonitor.prototype.reconnect_reset = function() {
      this.reconnect.reconnecting = false;
      this.reconnect.retry_curr_timeout = 0;
      this.reconnect.retry_curr_multipler = 0;
      return this.reconnect.retry_count = 0;
    };

    GanttMonitor.prototype.reconnect_try = function(connfunc) {
      var callback;
      if (this.reconnect.retry_count === this.reconnect.max_retries) {
        this.reconnect.reconnecting = false;
        if (this.reconnect.reload_after_n) {
          window.location.reload(true);
        }
        return;
      }
      if (!this.reconnect.reconnecting) {
        this.reconnect.reconnecting = true;
        this.reconnect.retry_curr_timeout = this.reconnect.retry_timeout_ms;
        this.reconnect.retry_curr_multipler = 1;
        this.reconnect.retry_count = 1;
        return connfunc();
      } else {
        this.reconnect.retry_count += 1;
        callback = (function(_this) {
          return function() {
            _this.reconnect.retry_curr_timeout *= _this.reconnect.retry_multiplier;
            _this.reconnect.retry_curr_multipler += 1;
            if (_this.reconnect.retry_curr_multipler === _this.reconnect.reset_mult) {
              _this.reconnect.retry_curr_timeout = _this.reconnect.retry_timeout_ms;
              _this.reconnect.retry_curr_multipler = 1;
            }
            return connfunc();
          };
        })(this);
        return setTimeout(callback, this.reconnect.retry_curr_timeout);
      }
    };

    GanttMonitor.prototype.send = function(data) {
      return this.conn.send(data);
    };

    GanttMonitor.prototype.on_open = function() {
        this.reconnect_reset();
        this.update_status();
        if (this.cli_onopen !== null) {
            return this.cli_onopen();
        }
        if (this.options.register_on_open) {
            this.setGanttId(
                sessionStorage.cveEscenario, 
                sessionStorage.cveRol, 
                sessionStorage.numeroOpcion); 
            var msg = this.generaWSMessage(TYPE_SESSION_START);
            if (msg) {
                this.send(msg);
            }               
        }        
    };

    GanttMonitor.prototype.on_close = function() {
        this.unregister();
        this.conn = null;
        this.update_status();
        if (this.cli_onclose !== null) {
            this.cli_onclose();
        }
        if (this.reconnect.do_not_reconnect) {
            return;
        }
        return this.reconnect_try(this.connect);
    };
    
    GanttMonitor.prototype.on_message = function (e) {
        var that = this;
        var msgObj = JSON.parse(e.data);
        var response, msg, title, caption;
        if (msgObj.type === TYPE_MESSAGE) {
            response = JSON.parse(msgObj.text);
            if (response.status === 'error') {  
                if (this.options.single_messages) {
                    title = this.options.messages.gantt_abierto + '<br>' 
                          +' '+ response.user + '<br>'
                          + this.options.messages.desea_cerrar; 
                    caption = '';                        
                } else {
                    title = this.options.messages.desea_cerrar;
                    caption = this.options.messages.gantt_abierto +' '+ response.user;                        
                }
                w2confirm(title, caption, function btn(answer) {
                    if (answer === 'No') {
                        window.close();
                    } else {
                        msg = that.generaWSMessage(TYPE_SESSION_FORCE);
                        if (msg) {
                            that.send(msg);
                        }                                    
                    }
                });
            } else if (response.status === 'exit') {
                this.unregister();
                if (this.options.single_messages) {
                    title = this.options.messages.otro_gantt + '<br>' 
                          + this.options.messages.se_cerrara;
                    caption = '';                        
                } else {
                    title = this.options.messages.se_cerrara;
                    caption = this.options.messages.otro_gantt;                        
                }                    
                w2alert(title, caption, function (data) {
                    window.close();
                }); 
            } else if (response.status === 'success') {
                this.sessionId = response.sessionId;
            }
        }
        if (this.cli_onmessage !== null) {
            this.cli_onmessage();
        }
    };    
  
    GanttMonitor.prototype.register = function (escenario, rol, opcion) {
        this.setGanttId(escenario, rol, opcion);
        var msg = this.generaWSMessage(TYPE_SESSION_START);
        if (msg) {
            this.send(msg);
        }
        console.log('register');
    };
   
    GanttMonitor.prototype.unregister = function () {
        var msg = this.generaWSMessage(TYPE_SESSION_CLOSE);
        if (msg) {
            this.send(msg);
            console.log('unregister');
        }
    };
      
    GanttMonitor.prototype.generaWSMessage = function (tipo) {
        if (this.ganttId && this.ganttId !== null) {
             var msgObject = {
                type: tipo,
                text: this.ganttId
            };
            return JSON.stringify(msgObject);
        }
    };    

    GanttMonitor.prototype.setGanttId = function (escenario, rol, opcion) {
        this.ganttId = escenario+'|'+rol+'|'+opcion;
    };    

    // Expongo desktop al contexto a5.
    if (window['a5'] === undefined) {
        window.a5 = function () {
        };
    }
    
    window.a5.GanttMonitor = GanttMonitor;

})(window);
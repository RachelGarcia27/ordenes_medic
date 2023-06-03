document.addEventListener('DOMContentLoaded', function () {
    var w = $(window), d = $(document), b = $('#mainFrame');
    var theWidth = 3 + (b.width() - w.width()) || (d.width() - w.width());
    var theHeight = 3 + (b.height() - w.height()) || (d.height() - w.height());
    window.resizeBy(theWidth + 0, theHeight + 3);
    window.moveTo((w.availWidth - theWidth) / 2, 0);
}, false);


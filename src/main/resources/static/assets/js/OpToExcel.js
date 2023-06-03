
function fetchData() {
  // Array para almacenar los datos de la tabla
  var csv = [];

  // Seleccionar todas las filas de la tabla
  var rows = document.getElementById('TableOperarios').querySelectorAll("table tr");

  // Agregar una fila de títulos para usuario y contraseña
  // Iterar sobre cada fila
  for (var i = 0; i < rows.length; i++) {
    // Array para almacenar los datos de la fila actual
    var row = [];
    // Seleccionar todas las celdas de la fila actual (td)
    var cols = rows[i].querySelectorAll("td, th");

    // Iterar sobre cada celda de la fila actual
    for (var j = 0; j < cols.length; j++) {
      if (cols[j].id === "IgnoreForExcel") {
        continue;
      }
      // Si la celda contiene un input, tomar el valor del input
      if (cols[j].querySelector('input') !== null) {
        row.push(cols[j].querySelector('input').value);
      }
      // Si no tiene input, tomar el texto interno de la celda
      else {
        row.push(cols[j].innerText);
      }
    }

    // Agregar la fila actual al array csv
    csv.push(row.join("\t"));
  }

  // Crear un enlace de descarga
  var downloadLink;
  // Tipo de archivo (en este caso, Excel)
  var dataType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
  // Convertir el array csv a una tabla HTML
  var tableHTML = '<table>';
  for(var i=0; i<csv.length; i++){
    tableHTML += '<tr>';
    var cells = csv[i].split("\t");
    for(var j=0; j<cells.length; j++){
      tableHTML += '<td>' + cells[j] + '</td>';
    }
    tableHTML += '</tr>';
  }
  tableHTML += '</table>';

  // Especificar el nombre del archivo de descarga
  filename = 'Operarios.xls';

  // Crear un enlace de descarga
  downloadLink = document.createElement("a");
  // Agregar el enlace de descarga al cuerpo del documento
  document.body.appendChild(downloadLink);


  // Si el navegador es Internet Explorer o Edge, utilizar la función msSaveOrOpenBlob para descargar el archivo
  if(navigator.msSaveOrOpenBlob){
    var blob = new Blob(['\ufeff', tableHTML], {
      type: dataType
    });
    navigator.msSaveOrOpenBlob(blob, filename);
  }
  // Si no es Internet Explorer o Edge, crear un enlace de descarga normal y hacer clic en él para descargar el archivo
  else {
    // Especificar la URL del archivo
    downloadLink.href = 'data:' + dataType + ', ' + tableHTML;
    // Especificar el nombre del archivo de descarga
    downloadLink.download = filename;
    // Hacer clic en el enlace de descarga
    downloadLink.click();
  }
}

function password_show_hide(id) {

  var x = document.getElementById("pass" + id);
  var show_eye = document.getElementById("show" + id);
  var hide_eye = document.getElementById("hide" + id);
  hide_eye.classList.remove("d-none");
  if (x.type === "password") {
    x.type = "text";
    show_eye.style.display = "none";
    hide_eye.style.display = "block";
  } else {
    x.type = "password";
    show_eye.style.display = "block";
    hide_eye.style.display = "none";
  }
}

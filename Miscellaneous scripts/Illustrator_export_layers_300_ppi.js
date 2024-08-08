// Save this script as 'export_layers.js' and run it in Adobe Illustrator
// Especially useful for architecure and urban landscapping projects 

// Prompt user for export options
var exportFolder = Folder.selectDialog('Select the folder to export to:');
if (exportFolder == null) {
    // If no folder selected, exit the script
    alert('No folder selected. Script will exit.');
    exit();
}

// Function to export layers
function exportLayers() {
    var doc = app.activeDocument;
    var layers = doc.layers;

    for (var i = 0; i < layers.length; i++) {
        var layer = layers[i];

        // Hide all other layers
        for (var j = 0; j < layers.length; j++) {
            layers[j].visible = false;
        }

        // Make the current layer visible
        layer.visible = true;

        // Define the file name and path
        var fileName = layer.name + '.png';
        var file = new File(exportFolder.fsName + '/' + fileName);

        exportPNG(file);
    }

    // Make all layers visible again
    for (var k = 0; k < layers.length; k++) {
        layers[k].visible = true;
    }

    alert('Layers exported successfully!');
}

// Function to export as PNG
function exportPNG(file) {
    var exportOptions = new ExportOptionsPNG24();
    exportOptions.transparency = true;
    exportOptions.artBoardClipping = true;
    exportOptions.horizontalScale = 300.0 / 72.0 * 100; // Set the resolution to 300 ppi
    exportOptions.verticalScale = 300.0 / 72.0 * 100;   // Set the resolution to 300 ppi
    app.activeDocument.exportFile(file, ExportType.PNG24, exportOptions);
}

// Run the export layers function
exportLayers();
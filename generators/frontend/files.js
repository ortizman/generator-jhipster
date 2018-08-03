module.exports = {
    writeFiles
};

function writeFiles() {
    return {
        writeCommonFiles() {
            // Copio todo el frontend custom al webapp del backend generado
            // this.directory('frontend/', 'frontend/');

            this.fs.copy(
                this.templatePath('frontend/'),
                this.destinationPath('frontend/')
            );
        }
    };
}

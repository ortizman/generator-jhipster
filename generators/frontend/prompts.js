'use strict';

module.exports = {
    askForModuleName,
    askForClientSideOpts,
    askFori18n
};

function askForModuleName() {

    if (this.baseName) return;

    this.askModuleName(this);
}

function askForClientSideOpts() {
    if (this.existingProject) return;

    var done = this.async();
    var getNumberedQuestion = this.getNumberedQuestion.bind(this);

    this.prompt({
        type: 'list',
        name: 'layoutType',
        message: function (response) {
            return getNumberedQuestion('Which *type* of Layout would you like to use?', true);
        },
        choices: [
            {
                value: 'default',
                name: 'Default'
            },
            {
                value: 'top',
                name: 'Layout Top'
            },
            {
                value: 'left',
                name: 'Layout left'
            },
            {
                value: 'right',
                name: 'Layout Right'
            }
        ],
        default: 'default'
    }).then(function (prompt) {
        this.layoutType = this.configOptions.layoutType = prompt.layoutType;
        done();
    }.bind(this));
}

function askFori18n() {
    if (this.existingProject || this.configOptions.skipI18nQuestion) return;

    this.aski18n(this);
}

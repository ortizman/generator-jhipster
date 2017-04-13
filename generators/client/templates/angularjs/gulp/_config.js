<%#
 Copyright 2013-2017 the original author or authors.

 This file is part of the JHipster project, see https://jhipster.github.io/
 for more information.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-%>
'use strict';

module.exports = {
    app: '<%= MAIN_SRC_DIR %>',
    dist: '<%= DIST_DIR %>',
    swaggerDist: '<%= DIST_DIR %>swagger-ui/',
    test: '<%= TEST_SRC_DIR %>'<% if(useSass) { %>,
    scss: '<%= MAIN_SRC_DIR %>scss/',
    sassSrc: '<%= MAIN_SRC_DIR %>scss/**/*.{scss,sass}',
    sassVendor: 'src/main/webapp/scss/vendor.scss',
    cssDir: '<%= MAIN_SRC_DIR %>content/css'<% } %>,
    bower: '<%= MAIN_SRC_DIR %>bower_components/',
    tmp: '<%= BUILD_DIR %>tmp',
    revManifest: '<%= BUILD_DIR %>tmp/rev-manifest.json',
    port: 9000,
    apiPort: <%= serverPort %>,
    liveReloadPort: 35729,
    uri: 'http://localhost:',
    constantTemplate:
        '(function () {\n' +
        '    \'use strict\';\n' +
        '    // DO NOT EDIT THIS FILE, EDIT THE GULP TASK NGCONSTANT SETTINGS INSTEAD WHICH GENERATES THIS FILE\n' +
        '    angular\n' +
        '        .module(\'<%%- moduleName %>\')\n' +
        '<%% constants.forEach(function(constant) { %>        .constant(\'<%%- constant.name %>\', <%%= constant.value %>)\n<%% }) %>;\n' +
        '})();\n'
};
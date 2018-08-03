const chalk = require('chalk');
const _ = require('lodash');
const BaseGenerator = require('../generator-base');
const prompts = require('./prompts');
const writeFiles = require('./files').writeFiles;
const shelljs = require('shelljs');
const packagejs = require('../../package.json');


/* Constants use throughout */
const constants = require('../generator-constants');

module.exports = class extends BaseGenerator {
    constructor(args, opts) {
        super(args, opts);

        this.configOptions = this.options.configOptions || {};

        // This adds support for a `--auth` flag
        this.option('auth', {
            desc: 'Provide authentication type for the application',
            type: String
        });

        // This adds support for a `--skip-user-management` flag
        this.option('skip-user-management', {
            desc: 'Skip the user management module during app generation',
            type: Boolean,
            defaults: false
        });


        this.skipServer = this.configOptions.skipServer || this.config.get('skipServer');
        this.skipUserManagement = this.configOptions.skipUserManagement || this.options['skip-user-management'] || this.config.get('skipUserManagement');
        this.authenticationType = this.options.auth;
        this.buildTool = this.options.build;
        this.websocket = this.options.websocket;
        this.devDatabaseType = this.options['dev-db'];
        this.databaseType = this.options.db;
        this.enableSocialSignIn = this.options.social;
        this.searchEngine = this.options['search-engine'];
        this.hibernateCache = this.options['hb-cache'];
        this.otherModules = this.configOptions.otherModules || [];
        this.jhiPrefix = this.configOptions.jhiPrefix || this.config.get('jhiPrefix') || this.options['jhi-prefix'];
        this.jhiPrefixCapitalized = _.upperFirst(this.jhiPrefix);
        this.testFrameworks = [];
        this.options.protractor && this.testFrameworks.push('protractor');
        this.currentQuestion = this.configOptions.lastQuestion ? this.configOptions.lastQuestion : 0;
        this.totalQuestions = this.configOptions.totalQuestions ? this.configOptions.totalQuestions : constants.QUESTIONS;
        this.baseName = this.configOptions.baseName;
        this.logo = this.configOptions.logo;
        this.yarnInstall = this.configOptions.yarnInstall = this.configOptions.yarnInstall || this.options.yarn || this.config.get('yarn');
    }

    get initializing() {
        return {
            setupClientVars() {
                // Make constants available in templates
                this.MAIN_SRC_DIR = constants.CLIENT_MAIN_SRC_DIR;
                this.TEST_SRC_DIR = constants.CLIENT_TEST_SRC_DIR;
                this.CLIENT_MAIN_SRC_DIR = constants.CLIENT_MAIN_SRC_DIR;
            }
        };
    }

    get prompting() {
        return {
            askForModuleName: prompts.askForModuleName,

            askForClientSideOpts: prompts.askForClientSideOpts,

            askFori18n: prompts.askFori18n,

            setSharedConfigOptions() {
                this.configOptions.lastQuestion = this.currentQuestion;
                this.configOptions.totalQuestions = this.totalQuestions;
                this.configOptions.useSass = this.useSass;
            }
        };
    }

    get writing() {
        return writeFiles();
    }

    get install() {
        return {
            installing() {
                try {
                    this.log(chalk.green.bold('\n Execute npm install ...\n'));
                    this.spawnCommandSync('cd', ['frontend']);
                    this.spawnCommandSync('npm', ['install'], { cwd: 'frontend' });
                } catch (err) {
                    this.log(`\n ${chalk.bold.red('npm install fail!. No modification done to the generated app.')}`);
                }
            }
        };
    }

    get end() {
        return {
            afterRunHook() {
                this.log(chalk.green.bold('\nFrontend app generated successfully.\n'));
            }
        };
    }

};

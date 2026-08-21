pipeline {

    agent any
    options {
        durabilityHint 'MAX_SURVIVABILITY'
    }
    stages {
        stage('Checkout') {
            steps {
                sh 'rm -rf hr_admin; git clone -b v0.0.7 https://github.com/cherepakhin/hr_admin'
            }
        }

        stage('Unit tests') {
            steps {
                sh 'pwd;cd hr_admin;./mvnw clean test -DexcludedGroups="integration"'
            }
        }

        stage('Build bootJar') {
            steps {
                sh 'pwd;cd hr_admin;./mvnw -Dmaven.test.skip=true package'
            }
        }

        stage('Publish to Nexus') {
            environment {
                NEXUS_CRED = credentials('vasi')
            }
            steps {
                sh 'export NEXUS_CI_USER=admin; export NEXUS_CI_PASS=pass;echo $NEXUS_CI_USER;cd hr_admin;ls;./mvnw -Dmaven.test.skip=true deploy'
            }
        }
        stage('Copy Files') {
            steps {
                sh 'ls hr_admin; ls hr_admin/target; cp hr_admin/target/hr-admin*.jar /home/vasi/temp'
            }
        }
    }
}
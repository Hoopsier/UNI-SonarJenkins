pipeline {
    agent any

    tools {
        maven 'Maven3'   // Must match the name configured in Jenkins Global Tool Configuration
    }

    environment {
        // these are the environment variables (Amir's machine) that will be used in the pipeline

        SONARQUBE_SERVER = 'SonarQubeServer'
        // SONAR_TOKEN = credentials('SONAR_TOKEN')
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub-credentials'
        DOCKERHUB_REPO = 'renanhoruz/bmi_3013_renan'
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Hoopsier/UNI-SonarJenkins'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh '''
                    if command -v xvfb-run >/dev/null 2>&1; then
                        xvfb-run -a mvn test
                    else
                        mvn test
                    fi
                '''
            }
        }
// Check the SonarQube analysis stage for the correct project key and name
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${env.SONARQUBE_SERVER}") {
                  script{
                      def scannerHome = tool 'sonarscanner'
                      sh "${scannerHome}/bin/sonar-scanner " +
                      "-Dsonar.projectKey=OTP-SONAR " +
                      "-Dsonar.projectName=OTP-SONAR " +
                      "-Dsonar.sources=src " +
                      "-Dsonar.java.binaries=target/classes " +
                      "-Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml " +
                      "-Dsonar.junit.reportPaths=target/surefire-reports " +
                      "-Dsonar.java.source=21 " +
                      "-Dsonar.scanner.skipJreProvisioning=true"
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build \
                        -t ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG} \
                        .
                '''
            }
        }

        stage('Login to Docker Hub') {
          steps {
              withCredentials([
                  usernamePassword(
                      credentialsId: 'dockerhub-credentials',
                      usernameVariable: 'DOCKER_USER',
                      passwordVariable: 'DOCKER_PASS'
                  )
              ]) {
                  sh '''
                      echo "$DOCKER_PASS" | docker login \
                        -u "$DOCKER_USER" \
                        --password-stdin
                  '''
              }
          }
        }

        stage('Push Docker Image') {
            steps {
                sh '''
                    docker push ${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}
                    docker push ${DOCKERHUB_REPO}:latest
                   '''
            }
        }
    }
}

pipeline 
{

    agent any

    tools {
        maven 'Maven'
        
        
    }

    environment {

        IMAGE1       = "kmarket"
        
        DOCKERHUB_USER = "lakshvar96"
        GIT_REPO = "https://github.com/Lakshmanan1996/SuperMarketApp.git"
    }
    
   /* =====================================================   
   CHECKOUT
    ===================================================== */

    stages {

        stage('Checkout Code') {
            
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: 'main']],
                    userRemoteConfigs: [[url: "${GIT_REPO}"]]
                ])
            }
        }


        stage('Stash Source') {
            
            steps {
                stash includes: '**/*', name: 'source-code'
            }
        }


        /* ===================== Build Maven Stage ===================== */
        stage('Build') {
            
            
                    
            steps {
                unstash 'source-code'
                sh 'mvn clean package -DskipTests'
            }
        }

        /* =====================================================
           SONARQUBE ANALYSIS
        ===================================================== */

        stage('SonarQube Analysis') {
            
            steps {
                unstash 'source-code'
                script {
                    def scannerHome = tool 'SonarQubeScanner'
                    
                    withSonarQubeEnv('sonarqube') {
                        
                        sh """
                             mvn clean verify sonar:sonar \
                             -DskipTests \
                             -Dsonar.projectKey=kmarket \
                             -Dsonar.projectName=kmarket \
                        """
                    }
                        
                }
            }
        }
        

        /* =====================================================
           QUALITY GATE
        ===================================================== */

        stage('Quality Gate') {
            
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }


        /* =====================================================
           DOCKER BUILD
        ===================================================== */

        stage('Docker Build') {
            
            steps {
                unstash 'source-code'
                
                echo "Build a image for kmarket"
                
                
                sh """
                docker build -t ${DOCKERHUB_USER}/${IMAGE1}:${BUILD_NUMBER} .
                docker tag ${DOCKERHUB_USER}/${IMAGE1}:${BUILD_NUMBER} ${DOCKERHUB_USER}/${IMAGE1}:latest 
                """
            }
                
                
        }
        

      
         /* =====================================================
           DOCKER PUSH
        ===================================================== */

        stage('Push Image') {
            
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                }

                sh """
                docker push ${DOCKERHUB_USER}/${IMAGE1}:${BUILD_NUMBER}
                docker push ${DOCKERHUB_USER}/${IMAGE1}:latest
                """

                 
                     
            }
        }

    }    
    post {
        success {
            echo "✅ kmarket CI Pipeline SUCCESS"
        }
        failure {
            echo "❌ kmarket CI Pipeline FAILED"
        }
    }
}


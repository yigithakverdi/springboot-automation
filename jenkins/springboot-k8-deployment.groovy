pipeline {
    agent any

    environment {
        GIT_CREDENTIALS = credentials('github-credentials')
        DOCKER_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKER_IMAGE = "yigithak/springboot-automation"
        DOCKER_TAG = "latest"
        K8S_DEPLOYMENT = "springboot-deployment"
        KUBECONFIG = credentials('kubeconfig')
    }

    stages {
        stage('Sanity Check') {
            steps {
                sh"""
                kubectl config current-context
                docker --version
                java --version
                """
            }    
        }
    
        
        stage('Checkout') {
            steps {
                git credentialsId: 'github-credentials', url: 'https://github.com/yigithakverdi/springboot-automation.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    dir('src'){
                        sh 'mvn clean package'       
                    }
                }
            }
        }
        
        stage('Deploy to Nexus') {
            steps {
                // Deploy the artifact to Nexus
                sh "${MAVEN_HOME}/bin/mvn deploy -s /path/to/your/settings.xml"
            }
        }

        stage('Dockerize') {
            steps {
                script {
                    sh """
                        docker build -t springboot-automation:latest .
                        docker login -u $DOCKER_CREDENTIALS_USR -p $DOCKER_CREDENTIALS_PSW
                        docker tag springboot-automation:latest yigithak/springboot-automation:latest
                        docker push yigithak/springboot-automation:latest
                    """
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    dir('kubernetes') {
                        sh """
                        kubectl apply -f test-namespace.yaml
                        kubectl apply -f deployment.yaml
                        kubectl apply -f service.yaml
                        """
                    }
                }

            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}

pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds')
        IMAGE_NAME = "bluedio/boot-login-app"
        IMAGE_TAG = "v${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/bue-dio/springboot-microservice.git'
            }
        }

        stage('Build with Maven') {
            steps {
                echo "🧱 Building the application with Maven..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "🐳 Building Docker image..."
                sh 'docker build -t $IMAGE_NAME:$IMAGE_TAG .'
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                echo "🚀 Pushing image to Docker Hub..."
                sh '''
                    echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                    docker push $IMAGE_NAME:$IMAGE_TAG
                '''
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo "☸️ Deploying app to Kubernetes cluster..."
                sh '''
                    kubectl set image deployment/springboot-microservice-deployment \
                    springboot-microservice=$IMAGE_NAME:$IMAGE_TAG --record || \
                    kubectl apply -f k8s/
                '''
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful! App deployed with image tag: $IMAGE_TAG"
        }
        failure {
            echo "❌ Build or deployment failed. Check Jenkins logs for details."
        }
    }
}

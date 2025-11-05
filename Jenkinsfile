pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds')
        IMAGE_NAME = "bluedio/boot-login-app"
        IMAGE_TAG = "v1.0.${BUILD_NUMBER}"
        KUBECONFIG = "/var/jenkins_home/.kube/config"
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "📦 Checking out source code..."
                git branch: 'main', url: 'https://github.com/bue-dio/springboot-microservice.git'
            }
        }

        stage('Build with Maven') {
            steps {
                echo "🧱 Building the application..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "🐳 Building Docker image..."
                sh """
                   docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                """
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                echo "📤 Pushing image to Docker Hub..."
                sh """
                   echo "${DOCKERHUB_CREDENTIALS_PSW}" | docker login -u "${DOCKERHUB_CREDENTIALS_USR}" --password-stdin
                   docker push ${IMAGE_NAME}:${IMAGE_TAG}
                """
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo "🚀 Deploying to Kubernetes..."
                sh """
                   sed -i 's|<YOUR_REGISTRY>/springboot-microservice:<TAG>|${IMAGE_NAME}:${IMAGE_TAG}|g' k8s/deployment.yaml
                   kubectl apply -f k8s/deployment.yaml
                   kubectl apply -f k8s/service.yaml
                """
            }
        }

        stage('Verify Deployment') {
            steps {
                echo "🔍 Verifying pods and services..."
                sh """
                   kubectl get pods -o wide
                   kubectl get svc
                """
            }
        }
    }

    post {
        success {
            echo "✅ Deployment Successful!"
        }
        failure {
            echo "❌ Build or Deployment Failed. Check logs for details."
        }
    }
}

pipeline {
  
  agent {
        docker {
            image 'maven:3.9.0-eclipse-temurin-11' 
            args '-v /root/.m2:/root/.m2' 
        }
  }
  
  stages {
    
    stage("build") {
      steps {
        echo 'building the application...'
        sh 'mvn -B -DskipTests clean package'
        echo 'done with build'
      }
    }
    stage("test") {
      steps {
        echo 'Invoking integration tests for the application...'
        echo 'done with Testing'
      }
    }
    
    stage("deploy") {
      steps {
        echo 'deploying the application...'
        echo 'done with Deployment'
      }
    }
    
  }
  
  
}

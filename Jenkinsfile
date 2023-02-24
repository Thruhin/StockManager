pipeline {
  
  agent any
  
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
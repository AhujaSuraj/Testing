import jenkins.model.Jenkins
import java.text.SimpleDateFormat

String pattern = "dd-MM-yyyy";
def builddate
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
String currentdate = simpleDateFormat.format(new Date());
println currentdate;
pipeline {
	agent {
		label 'DETerminal'
	}
	stages {
		stage('Build-AR-HBASE') {
		 	steps {
				echo 'test'
			}
		}
		stage('HBASE-DWHSRC') {
			steps {
				echo 'test1'
			}
		}
		stage ('Lastsuccess') {
			steps{
				
				echo 'ecdfdfd'
			}
		}
		stage('DWHJOBS'){
			steps {
				echo 'test3'
			}
		}
	}
}

import jenkins.model.Jenkins
import java.text.SimpleDateFormat

String pattern = "dd-MM-yyyy";
def builddate
def  ff
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
		stage('DWHJOBS'){
			steps {
				echo 'test3'
			}
		}
	}
}

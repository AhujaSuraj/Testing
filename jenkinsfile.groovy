import jenkins.model.Jenkins
import java.text.SimpleDateFormat

String pattern = "dd-MM-yyyy";
def builddate
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
String currentdate = simpleDateFormat.format(new Date());
println currentdate;
def item = Jenkins.instance.getItemByFullName("/Team/Suraj/mainjob-pipline/job1");
if (item.getLastBuild()) {
	println 'getiing buil date';
	ff=item.getLastSuccessfulBuild();
	builddate=ff.getTime().format("dd-MM-yyyy");
}
else {
	println 'system date';
	builddate=currentdate;
}
println  'build date';
println builddate;
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

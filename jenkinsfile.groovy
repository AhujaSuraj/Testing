import java.text.SimpleDateFormat
import jenkins.model.Jenkins


String pattern = "dd-MM-yyyy";
String builddate='sds'
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
String currentdate = simpleDateFormat.format(new Date());
println currentdate;

def BUDATE(){
	def item = Jenkins.instance.getItemByFullName("/Team/Suraj/mainjob-pipline/job1");
	if (item.getLastBuild()) {
		def ff=item.getLastSuccessfulBuild();
		builddate=ff.getTime().format("dd-MM-yyyy");
	}
	else {
		builddate=currentdate;
	}
	println builddate;
}

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
				script {
					BUDATE()
				}
				println builddate
			}
		}
		stage('DWHJOBS'){
			steps {
				println builddate
			}
		}
	}
}

import java.text.SimpleDateFormat
import jenkins.model.Jenkins


String pattern = "dd-MM-yyyy";
builddate='sds'
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
String currentdate = simpleDateFormat.format(new Date());

def BUDATE(){
	def item = Jenkins.instance.getItemByFullName("/Team/Suraj/mainjob-pipline/job1");
	if (item.getLastBuild()) {
		def ff=item.getLastSuccessfulBuild();
		builddate=ff.getTime().format("dd-MM-yyyy");
	}
	else {
		builddate=currentdate;
	}
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
		stage ('Lastsuccessbuild of job1') {
			steps{
				script {
					BUDATE()
				}
			}
		}
		stage('DWHJOBS'){
			when {builddate==currentdate}
			steps {
				println builddate
				println currentdate
			}
		}
	}
}

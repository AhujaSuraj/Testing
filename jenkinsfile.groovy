import java.text.SimpleDateFormat
import jenkins.model.Jenkins


String pattern = "dd-MM-yyyy";
def builddate
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

BUDATE()

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
				script{
					BUDATE()
				}
				echo $[builddate]
				echo 'completed'
				echo 0
			}
		}
		stage('DWHJOBS'){
			steps {
				echo 'test3'
			}
		}
	}
}

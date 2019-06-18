import jenkins.model.Jenkins
import java.text.SimpleDateFormat

String pattern = "dd-MM-yyyy";
builddate='sds'
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
String currentdate = simpleDateFormat.format(new Date());

def BUDATE(){
	def item = Jenkins.instance.getItemByFullName("/Team/Suraj/mainjob-pipline/job5");
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
				build '/Team/Suraj/mainjob-pipline/job1'
			}
		}
		stage('HBASE-DWHSRC') {
			parallel {
				stage('HBASE-DWHSRC-A') {
					steps {
						build '/Team/Suraj/mainjob-pipline/job2'
					}
				}
				stage('HBASE-DWHSRC-B') {
					steps {
						build '/Team/Suraj/mainjob-pipline/job3'
					}
				}
				stage('HBASE-DWHSRC-C') {
					steps {
						build '/Team/Suraj/mainjob-pipline/job4'
					}
				}
			}
		}
		stage ('Lastsuccessjob5') {
			steps{
				script {
					BUDATE()
				}
			}
		}
		stage('DWHJOBS'){
			steps {
				echo builddate
				build '/Team/Suraj/mainjob-pipline/job5'
			}
		}
	}
}
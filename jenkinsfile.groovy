import jenkins.model.Jenkins
import java.text.SimpleDateFormat

String pattern = "dd-MM-yyyy";
builddate='sds'
buildrunning='false'
SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
String currentdate = simpleDateFormat.format(new Date());
println currentdate;
def LASTSUCCESSFULBUILD(){
	def item = Jenkins.instance.getItemByFullName("/Team/Suraj/mainjob-pipline/job5");
	if (item.getLastBuild()) {
		def ff=item.getLastSuccessfulBuild();
		builddate=ff.getTime().format("dd-MM-yyyy");
	}
	else {
		builddate=currentdate;
	}
	println builddate;
}

def LASTBUILDRUNNING () {
	def item = Jenkins.instance.getItemByFullName("/Team/Suraj/mainjob-pipline/job5");
	if (item.lastBuild.building) {
		buildrunning='true';
	}	
	else {
		buildrunning='false';
	}
	println buildrunning;
}

pipeline {
	agent {
		label 'DETerminal'
	}
	stages {
		stage ('Lastbuildstatus') {
			steps{
				script {
					LASTSUCCESSFULBUILD()
					LASTBUILDRUNNING ()
				}
			}
		}
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
		stage('DWHJOBS'){
			when {
				allOf {
					expression {
						return builddate!= currentdate;
					}
					expression {
						return buildrunning=='false'
					}
				}	
			}	
			steps {
				build '/Team/Suraj/mainjob-pipline/job5'
			}
		}
	}
}
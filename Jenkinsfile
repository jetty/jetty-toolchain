#!groovy

// if removing jdk look at the test made for deploying
def jdks = ["jdk8", "jdk9", "jdk10", "jdk11"]
def oss = ["linux"] //windows?  ,"linux-docker"
def builds = [:]
for (def os in oss) {
  for (def jdk in jdks) {
    builds[os+"_"+jdk] = getFullBuild( jdk, os )
  }
}

parallel builds

def getFullBuild(jdk, os) {
  return {
    node(os) {
      // System Dependent Locations
      def mvntool = tool name: 'maven3.5', type: 'hudson.tasks.Maven$MavenInstallation'
      def jdktool = tool name: "$jdk", type: 'hudson.model.JDK'
      def mvnName = 'maven3.5'
      def localRepo = ".repository" // "${env.JENKINS_HOME}/${env.EXECUTOR_NUMBER}"

      // Environment
      List mvnEnv = ["PATH+MVN=${mvntool}/bin", "PATH+JDK=${jdktool}/bin", "JAVA_HOME=${jdktool}/", "MAVEN_HOME=${mvntool}"]
      mvnEnv.add("MAVEN_OPTS=-Xms256m -Xmx1024m -Djava.awt.headless=true")

      try
      {
        stage('Checkout') {
          checkout scm
        }
      } catch (Exception e) {
        throw e
      }

      try
      {
        stage('Test') {
          withEnv(mvnEnv) {
            timeout(time: 90, unit: 'MINUTES') {
              // Run test phase / ignore test failures
              withMaven(
                      maven: mvnName,
                      jdk: "$jdk",
                      publisherStrategy: 'EXPLICIT',
                      globalMavenSettingsConfig: 'oss-settings.xml',
                      mavenLocalRepo: localRepo) {
                //
                sh "mvn -V -B clean install -DfailIfNoTests=false -Dmaven.test.failure.ignore=true -T3 -e "
              }
              // withMaven doesn't label..
              // Report failures in the jenkins UI
              junit testResults:'**/target/surefire-reports/TEST-*.xml'
              // Collect up the jacoco execution results
              def jacocoExcludes =
                      // build tools
                      "**/org/eclipse/jetty/ant/**" + ",**/org/eclipse/jetty/maven/**" +
                              ",**/org/eclipse/jetty/jspc/**" +
                              // example code / documentation
                              ",**/org/eclipse/jetty/embedded/**" + ",**/org/eclipse/jetty/asyncrest/**" +
                              ",**/org/eclipse/jetty/demo/**" +
                              // special environments / late integrations
                              ",**/org/eclipse/jetty/gcloud/**" + ",**/org/eclipse/jetty/infinispan/**" +
                              ",**/org/eclipse/jetty/osgi/**" + ",**/org/eclipse/jetty/spring/**" +
                              ",**/org/eclipse/jetty/http/spi/**" +
                              // test classes
                              ",**/org/eclipse/jetty/tests/**" + ",**/org/eclipse/jetty/test/**";
              step( [$class          : 'JacocoPublisher',
                     inclusionPattern: '**/org/eclipse/jetty/**/*.class',
                     exclusionPattern: jacocoExcludes,
                     execPattern     : '**/target/jacoco.exec',
                     classPattern    : '**/target/classes',
                     sourcePattern   : '**/src/main/java'] )
              // Report on Maven and Javadoc warnings
              step( [$class        : 'WarningsPublisher',
                     consoleParsers: [[parserName: 'Maven'],
                                      [parserName: 'JavaDoc'],
                                      [parserName: 'JavaC']]] )
            }
          }
        }
      } catch(Exception e) {
        throw e
      }
      // deploy only master branch for jdk8
      if (isActiveBranch() && jdk == "jdk8") {
        try
        {
          stage('Deploy') {
            withEnv(mvnEnv) {
              timeout(time: 90, unit: 'MINUTES') {
                // Run test phase / ignore test failures
                withMaven(
                        maven: mvnName,
                        jdk: "$jdk",
                        publisherStrategy: 'EXPLICIT',
                        globalMavenSettingsConfig: 'oss-settings.xml',
                        mavenLocalRepo: localRepo) {
                  //
                  sh "mvn -V -B deploy -DfailIfNoTests=false -Dmaven.test.failure.ignore=true -T3 -e"
                }
              }
            }
          }
        } catch(Exception e) {
          throw e
        }
      }
    }
  }
}


// True if this build is part of the "active" branches
// for Jetty.
def isActiveBranch()
{
  def branchName = "${env.BRANCH_NAME}"
  return ( branchName == "master" );
}

// Test if the Jenkins Pipeline or Step has marked the
// current build as unstable
def isUnstable()
{
  return currentBuild.result == "UNSTABLE"
}


// vim: et:ts=2:sw=2:ft=groovy
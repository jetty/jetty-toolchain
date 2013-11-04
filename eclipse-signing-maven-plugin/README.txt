Example usage:

  <profiles>
    <profile>
      <id>build-server</id>
      <build>
        <plugins>
          <plugin>
            <groupId>org.eclipse.dash.m4e</groupId>
            <artifactId>eclipse-signing-maven-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
            <executions>
              <!-- 
              Pack the p2 repository.
               -->
              <execution>
                <id>pack</id>
                <phase>package</phase>
                <goals>
                  <goal>pack</goal>
                </goals>
              </execution>
              <!-- 
              Sign the p2 repository
              -->
              <execution>
                <id>sign</id>
                <configuration>
                  <signerInputDirectory>/home/data/httpd/download-staging.priv/rt/PROJECT</signerInputDirectory>
                </configuration>
                <phase>package</phase>
                <goals>
                  <goal>sign</goal>
                </goals>
              </execution>
              <!-- 
              Repack the p2 repository
              -->
              <execution>
                <id>repack</id>
                <configuration>
                  <inputFile>${project.build.directory}/signed/site_assembly.zip</inputFile> <!-- this is output from signer mojo -->
                </configuration>
                <phase>package</phase>
                <goals>
                  <goal>pack</goal>
                </goals>
              </execution>
              <!-- 
              Signing and packing alters checksums so fix them
              -->
              <execution>
                <id>fixCheckSums</id>
                <phase>package</phase>
                <goals>
                  <goal>fixCheckSums</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
          <!--
          This is what I use to deploy a p2 repository someplace to test from before manually making active.        
          -->
          <plugin>
              <artifactId>maven-antrun-plugin</artifactId>
              <executions>
                <execution>
                  <id>deploy</id>
                  <phase>install</phase>
                  <goals>
                    <goal>run</goal>
                  </goals>
                  <configuration>
                    <tasks>
                      <delete includeemptydirs="false">
                        <fileset
                          dir="/home/data/httpd/download.eclipse.org/jetty/updates/jetty-wtp/development">
                          <include name="**" />
                        </fileset>
                      </delete>
                      <copy includeemptydirs="false"
                        todir="/home/data/httpd/download.eclipse.org/jetty/updates/jetty-wtp/development">
                        <fileset dir="target/checksumFix">
                          <include name="**" />
                        </fileset>
                      </copy>
                    </tasks>
                  </configuration>
                </execution>
              </executions>
            </plugin>
          </plugins>
      </build>
    </profile>
  </profiles>

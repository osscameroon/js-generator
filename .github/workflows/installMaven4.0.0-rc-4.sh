curl -sL https://archive.apache.org/dist/maven/maven-4/4.0.0-rc-4/binaries/apache-maven-4.0.0-rc-4-bin.tar.gz | tar xz
sudo mv apache-maven-4.0.0-rc-4 /opt/maven
echo "M2_HOME=/opt/maven" >> $GITHUB_ENV
echo "PATH=/opt/maven/bin:$PATH" >> $GITHUB_ENV

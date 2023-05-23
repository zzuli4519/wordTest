# 设置基础镜像
FROM openjdk:8-jre-alpine

# 暴露应用程序端口
EXPOSE 8999

# 将应用程序的 JAR 文件复制到容器中
ADD target/WordTest-0.0.1-SNAPSHOT.jar word-test.jar

# 运行应用程序
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/word-test.jar"]
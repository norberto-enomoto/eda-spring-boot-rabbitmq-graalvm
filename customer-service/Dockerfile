# Estágio de compilação
FROM eclipse-temurin:21-jdk AS builder

# Instalar GraalVM Native Image
RUN apt-get update && apt-get install -y gcc zlib1g-dev libz-dev
RUN curl -L https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-21.0.2/graalvm-community-jdk-21.0.2_linux-x64_bin.tar.gz | tar -xz -C /opt
ENV JAVA_HOME=/opt/graalvm-community-openjdk-21.0.2+13.1
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Instalar Maven
RUN apt-get install -y maven

WORKDIR /home/app

# Copiar o código-fonte e o pom.xml
COPY src /home/app/src
COPY pom.xml /home/app

# Compilar o aplicativo
RUN mvn -Pnative native:compile -DskipTests

# Estágio final
FROM ubuntu:22.04

WORKDIR /app

# Copiar o binário compilado do estágio de compilação
COPY --from=builder /home/app/target/customer-service /app/customer-service

# Tornar o binário executável
RUN chmod +x /app/customer-service

# Expor a porta que o aplicativo usa
EXPOSE 8080

# Comando para executar o aplicativo
ENTRYPOINT ["./customer-service"]
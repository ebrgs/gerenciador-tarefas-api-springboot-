# 1. Base: Começamos com um Linux levezinho que já tem o Java 17 instalado
FROM eclipse-temurin:21-jdk-alpine

# 2. Pasta de Trabalho: Criamos uma pasta dentro do container para organizar
WORKDIR /app

# 3. Cópia: Pegamos o JAR que o Maven gerou no teu PC e jogamos para dentro do container
# (Nota: O nome do arquivo pode variar, vamos confirmar isso já já)
COPY target/*.jar app.jar

# 4. Porta: Avisamos que este container "escuta" na porta 8080
EXPOSE 8080

# 5. Execução: O comando que roda quando o container liga
ENTRYPOINT ["java", "-jar", "app.jar"]
FROM openjdk:11-slim as build
WORKDIR /workspace/app

# Copy the Maven Wrapper files
COPY mvnw .
COPY .mvn .mvn

# Make the mvnw file executable
RUN chmod +x mvnw

# Copy the pom.xml file
COPY docker-pom pom.xml

# Copy the source files
COPY src src

# The Maven install lifecycle phase installs packages into a local repository, for use as a dependency in other projects locally
# The -DskipTests option skips all tests
RUN ./mvnw install -DskipTests

# Create a folder to extract the Jar file into
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# -----------------------------------------------------------------------------

FROM openjdk:11-slim
VOLUME /tmp

# Configure an argument for the path to the folder that contains the extracted Jar files
ARG DEPENDENCY=/workspace/app/target/dependency

# Copy the necessary files from the intermediate build image
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Start the application using the java command
# The -cp option sets the class path
# The last argument is the fully qualified namd of the entry point class
ENTRYPOINT ["java","-cp","app:app/lib/*","com.dev10.userapi.App"]
FROM openjdk:13
WORKDIR /app/
COPY ./* ./
RUN ls
RUN javac Analyser.java
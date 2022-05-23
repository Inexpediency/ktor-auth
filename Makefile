run:
	./ktor-auth/gradlew -p ktor-auth clean shadowJar && docker-compose up --build
